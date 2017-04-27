import java.util.ArrayList;
import java.util.Calendar;

public class MailAccount {

	private String mailAccount;
	ArrayList<Email> messageList;
	public Object[][] data = {
			
			{"Sent", "Cristiano Soleti", "Send Nudes please", Calendar.getInstance().getTime()},
			{"Received", "Davide Brunetti", "Wrong destination my friend", Calendar.getInstance().getTime()}
			
	};
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
	public void getData()
	{
		
	}
}
