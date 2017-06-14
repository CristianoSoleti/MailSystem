package Remote;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;

import MailSystemUtilities.Email;
import MailSystemUtilities.MailAccount;
import MailSystemUtilities.MailAccountDatabase;
import Server.ServerController;

public class Requests extends UnicastRemoteObject implements RequestsInterface {

	private static final long serialVersionUID = 1L;
	public String name;
	public static final ArrayList<String> clientList = new ArrayList<String>();
	public static MailAccountDatabase db = MailAccountDatabase.getInstance();

	public Requests(String n) throws RemoteException {
		this.name = n;
	}

	public void setClient(Client c) throws RemoteException {
		clientList.add(c.getClientName());
		refreshServerList();
	}

	void refreshServerList() {
		// Server.connectedClients = getClients();
		ServerController.refreshTable(getClients(), Calendar.getInstance().getTime());
	}

	public synchronized void delete(Client c, int index) throws RemoteException {

		String sender = c.getClientName();

		MailAccount senderAccount = null;

		for (MailAccount ml : db.getAccountList()) {
			if (ml.getMailAccount().equals(sender)) {
				senderAccount = ml;
			}

		}

		senderAccount.getMessageList().remove(index);
		ServerController.logActions(sender + " deleted a message");

	}

	public synchronized void send(ArrayList<Email> m, ClientImpl c) throws RemoteException {
		sendMail(m, c);
	}

	void sendMail(ArrayList<Email> list, ClientImpl c) {
		for (Email m : list) {
			String sender = m.getSender();
			String receiver = m.getReceiver();

			MailAccount senderAccount = null;
			MailAccount receiverAccount = null;

			for (MailAccount ml : db.getAccountList()) {
				if (ml.getMailAccount().equals(sender)) {
					senderAccount = ml;
				}
				if (ml.getMailAccount().equals(receiver)) {
					receiverAccount = ml;
				}
			}
			ServerController.logActions(sender + " sent a message to " + receiver);

			senderAccount.getMessageList().add(m);
			// If you forward a mail to yourself it may occur twice in your
			// inbox
			if (sender.equals(receiver)) {
				return;
			}
			receiverAccount.getMessageList().add(m);
			ServerController.logActions(receiver + " received a message from " + sender);

		}

	}

	public ArrayList<String> getClients() {
		return clientList;
	}

	public void clientConnectionWelcome(Client c) throws RemoteException {
		ServerController.logActions(c.getClientName() + "Just logged in\n");
	}

	public ArrayList<Email> requestUserMailList(Client c) throws HeadlessException, RemoteException {
		MailAccount currentAccount = null;
		for (MailAccount ml : db.getAccountList()) {
			if (ml.getMailAccount().equals(c.getClientName())) {
				currentAccount = ml;
			}
		}
		return currentAccount.getMessageList();
	}

	public void destroyClient(Client c) throws RemoteException {
		ServerController.logActions(c.getClientName() + "Just disconnected");

		clientList.remove(c.getClientName());
		refreshServerList();
	}

	@Override
	public boolean checkError(String input) throws RemoteException {

		boolean found = true;
		for (MailAccount ml : db.getAccountList()) {
			if (ml.getMailAccount().equals(input)) {
				found = true;
				break;
			} else {
				found = false;
			}
		}
		if (!found) {
			ServerController.logActions("Someone just tried to send to an illegal mail");
		}
		return !found;
	}
}
