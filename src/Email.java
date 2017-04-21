
public class Email {

	private int ID;
	private int personalID = 0;
	private String sender;
	private String receiver;
	private String emailObject;
	private String emailText;
	private Date sendingDate;
	

	/**
	 * 
	 * @param ID 
	 * @param sender
	 * @param receiver
	 * @param messageObject
	 * @param messageText
	 * @param sendingDate
	 */
	public Email(int ID , String sender , String receiver , String messageObject , String messageText , Date sendingDate)
	{
		
		personalID = ID;
		ID++;
		this.sender = sender;
		this.receiver = receiver;
		emailObject = messageObject;
		emailText = messageText;
		this.sendingDate = sendingDate;
	}
	
	public int getID()
	{
		return personalID;
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
	
	public Date getSendingDate()
	{
		return sendingDate;
	}
}
