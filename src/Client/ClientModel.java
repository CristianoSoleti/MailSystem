package Client;
import java.util.ArrayList;
import MailSystemUtilities.Email;


public class ClientModel extends java.util.Observable {	
	
	String[] columnNames = {"Sender", "Receiver", "Subject", "Text" };

	public ArrayList<Email> mailList = new ArrayList<Email>();
	Object[][] table ;

	public ClientModel(){

	}

	
	public void setValue(ArrayList<Email> newMailList)
	{
		mailList = newMailList;
		table = new Object[mailList.size()][];
		for (int i = 0; i < mailList.size(); i++) {
		    table[i] = mailList.get(i).toObjectArray();
		}
		setChanged();
		notifyObservers(mailList);
	}

}