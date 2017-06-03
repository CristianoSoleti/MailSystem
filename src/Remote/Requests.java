package Remote;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.text.html.HTML;

import MailSystemUtilities.Email;
import MailSystemUtilities.MailAccount;
import MailSystemUtilities.MailAccountDatabase;
import MailSystemUtilities.SYSTEM_CONSTANTS;
import Server.Server;

public class Requests extends UnicastRemoteObject implements RequestsInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public static final ArrayList<String> clientList = new ArrayList<String>();
	public static MailAccountDatabase db = MailAccountDatabase.getInstance();
	public Requests(String n) throws RemoteException {
		this.name = n;

	}

	public String getName() throws RemoteException {
		return this.name;
	}

	public void setClient(ClientImpl c) throws RemoteException {
		clientList.add(c.getClientName());
		refreshServerList();

	}

	void refreshServerList() {
		Server.connectedClients = getClients();
		Server.refreshTable(Calendar.getInstance().getTime());
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
		Server.logArea.append(sender + " deleted a message\n");

	}

	public synchronized void send(ArrayList<Email> m) throws RemoteException {
		if (name.equals(SYSTEM_CONSTANTS.SERVER)) {

			sendMail(m);
			System.out.println("Server authority sending mail");
		} else {
			System.out.println("No server authority");

		}
	}

	void sendMail(ArrayList<Email> list) {
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
			Server.logArea.append(sender + " sent a message to " + receiver + "\n");

			senderAccount.getMessageList().add(m);
			//If you forward a mail to yourself it may occur twice in your inbox
			if (sender.equals(receiver)) {
				return;
			}
			receiverAccount.getMessageList().add(m);
		}

	}


	public boolean notifyChanges() {
		return true;
	}

	public ArrayList<String> getClients() {
		return clientList;
	}

	public void clientConnectionWelcome(ClientImpl c) throws RemoteException {
		System.out.println(name + " ha accettato il client di " + c.getClientName());
	}

	public ArrayList<Email> requestUserMailList(ClientImpl c) throws HeadlessException, RemoteException {
		MailAccount currentAccount = null;
		for (MailAccount ml : db.getAccountList()) {
			if (ml.getMailAccount().equals(c.getClientName())) {
				currentAccount = ml;
			}
		}
		return currentAccount.getMessageList();
	}

	public void destroyClient(ClientImpl c) throws RemoteException {
		System.out.println("Oggetto passato " + c.getClientName() + "la lista ha :" + clientList.size() + "oggetti");

		clientList.remove(c.getClientName());

		refreshServerList();
	}

}
