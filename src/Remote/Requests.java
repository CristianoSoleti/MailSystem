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
	public static final ArrayList<ClientImpl> clientsList = new ArrayList<ClientImpl>();

	public Requests(String n) throws RemoteException {
		this.name = n;
		
		
	}


	public String getName() throws RemoteException {
		return this.name;
	}

	public void setClient(ClientImpl c) throws RemoteException {
		clientList.add(c.getClientName());
		clientsList.add(c);

		refreshServerList();

	}

	void refreshServerList() {
		Server.connectedClients = getClients();
		Server.refreshTable(Calendar.getInstance().getTime());
	}

	public void delete(ClientImpl c,int index) throws RemoteException
	{
		
		String sender = c.getClientName();

		MailAccount senderAccount = null;

		for (MailAccount ml : db.getAccountList()) {
			if (ml.getMailAccount().equals(sender)) {
				senderAccount = ml;
			}

		}
		
		senderAccount.getMessageList().remove(index);
		Server.logArea.append(sender+" deleted a message\n");

	}
	
	public void send(Email s) throws RemoteException {
		if (name.equals(SYSTEM_CONSTANTS.SERVER)) {

			sendMail(s);
			System.out.println("Server authority sending mail");
		} else {
			System.out.println("No server authority");

		}
	}

	void sendMail(Email s) {
		String sender = s.getSender();
		String receiver = s.getReceiver();

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
		
		Server.logArea.append(sender+" sent a message to "+receiver+"\n");

		senderAccount.getMessageList().add(s);

		receiverAccount.getMessageList().add(s);

	}


	ClientImpl findClient(String clientName)
	{
		for(ClientImpl c : clientsList)
		{
			if(c.getClientName().equals(clientName))
			{
				return c;
			}
		}
		return null;
	}
	public boolean notifyChanges()
	{
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
				// JOptionPane.showMessageDialog(null, "Trovata lista di
				// messaggi");
				System.err.println("loaded");
				currentAccount = ml;
			}
		}
		return currentAccount.getMessageList();
	}

	public void destroyClient(ClientImpl c) throws RemoteException {
		System.out.println("Oggetto passato "+ c.getClientName()+"la lista ha :"+clientList.size()+"oggetti");

		clientList.remove(c.getClientName());

		refreshServerList();
	}




}
