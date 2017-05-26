package MailSystemUtilities;
import java.util.ArrayList;

public class MailAccountDatabase {

	ArrayList<MailAccount> accountList = new ArrayList<MailAccount>();

	public ArrayList<MailAccount> getAccountList()
	{
		return accountList;
	}
	private static MailAccountDatabase instance = null;
	
	public static MailAccountDatabase getInstance() {
		if (instance == null) {
			instance = new MailAccountDatabase();
		}
		return instance;
	}

	protected MailAccountDatabase()
	{
		MailAccount a1 = new MailAccount("cristiano.soleti@edu.unito.it");
		MailAccount a2 = new MailAccount("davide.brunetti@edu.unito.it");
		MailAccount a3 = new MailAccount("elio.cometto@edu.unito.it");
		MailAccount a4 = new MailAccount("1");
		MailAccount a5 = new MailAccount("2");

		a4.messageList.add(new Email("cristiano.soleti@edu.unito.it","davide.brunetti@edu.unito.it","Give me food","Please I need food."));
		a4.messageList.add(new Email("davide.brunetti@edu.unito.it","cristiano.soleti@edu.unito.it","Wrong receiver","I'm not your ma."));
		a4.messageList.add(new Email("cristiano.soleti@edu.unito.it","elio.cometto@edu.unito.it","I love you","Please send me some food"));
		a4.messageList.add(new Email("elio.cometto@edu.unito.it","cristiano.soleti@edu.unito.it","Stop boring","You are annoying"));

		accountList.add(a1);
		accountList.add(a2);
		accountList.add(a3);
		accountList.add(a4);
		accountList.add(a5);


	}
	
	
}
