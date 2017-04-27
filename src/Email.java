import java.sql.Date;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class Email {

	private static final AtomicInteger count = new AtomicInteger(0); 
	private final int ID;
	private String sender;
	private String receiver;
	private String emailObject;
	private String emailText;
	private java.util.Date sendingDate;
	

	/**
	 * 
	 * @param sender
	 * @param receiver
	 * @param messageObject
	 * @param messageText
	 * @param sendingDate
	 */
	public Email(String sender , String receiver , String messageObject , String messageText)
	{
		
		ID = count.incrementAndGet();
		this.sender = sender;
		this.receiver = receiver;
		emailObject = messageObject;
		emailText = messageText;
		sendingDate = Calendar.getInstance().getTime();
	}
	
	public int getID()
	{
		return ID;
	}
	
	public String getSender ()
	{
		return sender;
	}
	
	public String getReceiver()
	{
		return receiver;
	}
	
	public String getEmailObject()
	{
		return emailObject;
	}
	
	public String getEmailText()
	{
		return emailText;
	}
	
	public java.util.Date getSendingDate()
	{
		return sendingDate;
	}
}
