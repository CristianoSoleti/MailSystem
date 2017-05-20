package Remote;

import java.io.Serializable;

import javax.swing.JOptionPane;

public class ClientImpl implements Client, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String clientName;

	@Override
	public String getClientName() {
		return clientName;
	}

	public ClientImpl(String name) {
		clientName = name;
	}


	@Override
	public void showNewMessagePopUp(String sender,String title) {
		 JOptionPane.showMessageDialog(null, "Nuovo messaggio da :"+sender+"\n"+title);
		
	}
	
}
