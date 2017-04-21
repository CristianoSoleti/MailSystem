import java.util.ArrayList;

public class MailAccountDatabase {

	ArrayList<MailAccount> accountList = new ArrayList<MailAccount>();

	public ArrayList<MailAccount> getAccountList()
	{
		return accountList;
	}
	
	public MailAccountDatabase()
	{
		MailAccount a1 = new MailAccount("cristiano.soleti@edu.unito.it");
		MailAccount a2 = new MailAccount("davide.brunetti@edu.unito.it");
		MailAccount a3 = new MailAccount("elio.cometto@edu.unito.it");
		accountList.add(a1);
		accountList.add(a2);
		accountList.add(a3);
	}
	
	
}
