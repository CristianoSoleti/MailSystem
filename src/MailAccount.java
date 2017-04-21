import java.util.ArrayList;

public class MailAccount {

	private String mailAccount;
	ArrayList<Email> messageList;
	
	public MailAccount(String account)
	{
		mailAccount = account;
		messageList =  new ArrayList<Email>();
	}
	
	public String getMailAccount()
	{
		return mailAccount;
	}
	
	public ArrayList<Email> getMessageList()
	{
		return messageList;
	}
}
