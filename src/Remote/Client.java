package Remote;

/** 
 * Client interface which will be serializable.
 * 
 */

public interface Client {

	String getClientName();
	
	public void showNewMessagePopUp(String sender,String title);
	public void showErrorMessage(String error);
}
