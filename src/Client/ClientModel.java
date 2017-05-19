package Client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import MailSystemUtilities.Email;

public class ClientModel extends Observable implements Serializable {

	String[] columnNames = { "Sender", "Receiver", "Subject", "Text" };

	public ArrayList<Email> mailList = new ArrayList<Email>();
	Object[][] table;

	public String userEmailAccount = "";

	public ClientModel() {

	}

	public void setUserAccountValue(String account) {
		System.out.println("Model is changing mail name to" + account);
		userEmailAccount = account;
		setChanged();
		notifyObservers(account);
	}

	public void setValue(ArrayList<Email> newMailList) {
		mailList = newMailList;
		table = new Object[mailList.size()][];
		for (int i = 0; i < mailList.size(); i++) {
			table[i] = mailList.get(i).toObjectArray();
		}
		setChanged();
		notifyObservers(mailList);
	}

	public int getMailListSize() {
		return mailList.size();
	}

	public ArrayList<Email> getMailList() {
		return mailList;
	}
}