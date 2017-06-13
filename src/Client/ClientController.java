package Client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

import MailSystemUtilities.Email;
import MailSystemUtilities.SYSTEM_CONSTANTS;
import Remote.*;

/*
 * Controller extends action listener cause he needs to perform actions on events like click.
 */
public class ClientController implements ActionListener, MouseListener, Serializable, WindowListener {

	static boolean isMailEditorOpen = false;
	int currentMailIndexOpened;
	
	ClientModel model;
	ClientView view;

	public RequestsInterface server;

	public ClientImpl client;

	
	
	ClientController() {
		System.out.println("ClientController created");

	}

	public ClientModel getModel() {
		return model;
	}

	// invoked when a button from view is pressed
	public void actionPerformed(ActionEvent e) {

		System.out.println("Controller: The " + e.getActionCommand() + " button is clicked");
		switch (e.getActionCommand()) {
		case SYSTEM_CONSTANTS.CREATE_ACTION:
			createMail();
			break;
		case SYSTEM_CONSTANTS.REPLY_ACTION:
			replyMail();
			break;
		case SYSTEM_CONSTANTS.FORWARD_ACTION:
			forwardMail();
			break;
		case SYSTEM_CONSTANTS.DELETE_ACTION:
			try {
				deleteMail();
			} catch (RemoteException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;
		case SYSTEM_CONSTANTS.SEND_ACTION:
			System.out.println("Controller: Send Mail");
			try {
				sendMailRequest();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		}

	}

	public void forwardMail()
	{
		if (isMailEditorOpen) {
			return;
		}
		System.out.println("Controller: -FORWARD- Open Mail Editor");
		view.createMailGUI();
		isMailEditorOpen = true;
		setForwardMailInfo();
		
	}
	
	public void setForwardMailInfo()
	{
		view.resetTextBox(view.receiverTextArea,model.getMailList().get(currentMailIndexOpened).getSender());
		view.resetTextBox(view.subjectTextArea,model.getMailList().get(currentMailIndexOpened).getEmailObject());
		view.receiverTextArea.setForeground(Color.BLACK);
		view.subjectTextArea.setForeground(Color.BLACK);
	}
	
	
	public void replyMail()
	{
		if (isMailEditorOpen) {
			return;
		}
		System.out.println("Controller: -REPLY- Open Mail Editor");
		view.createMailGUI();
		isMailEditorOpen = true;
		setReplyMailInfo();
		

	}
	
	public void setReplyMailInfo()
	{
		view.resetTextBox(view.receiverTextArea,model.getMailList().get(currentMailIndexOpened).getSender());
		view.resetTextBox(view.subjectTextArea,"RE: "+model.getMailList().get(currentMailIndexOpened).getEmailObject());
		view.receiverTextArea.setForeground(Color.BLACK);
		view.subjectTextArea.setForeground(Color.BLACK);
	}
	
	public void deleteMail() throws RemoteException
	{
		System.out.println("Removing mail at index"+currentMailIndexOpened);
		server.delete(client, currentMailIndexOpened);
		model.getMailList().remove(currentMailIndexOpened);
		refreshTableData(model.userEmailAccount);
		view.readMailFrame.setVisible(false);
	}
	
	
	/*
	 * sends mail request to server if mail isn't null.
	 */
	public void sendMailRequest() throws IOException {
		view.getListOfReceiver();
		
		ArrayList<Email> newMailList = view.createMailFromGUI();
		if (newMailList == null) {
			return;
		}
		/*if(!server.checkError(view.getListOfReceiver()))
		{
			System.out.println("Ciaomamma");
		}*/
		server.send(newMailList,client);
		refreshTableData(model.userEmailAccount);

	}

	public void createMail() {
		if (isMailEditorOpen) {
			return;
		}
		System.out.println("Controller: Open Mail Editor");
		view.createMailGUI();
		isMailEditorOpen = true;
	}

	public void connectToServer()
			throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {

		String emailAccount = JOptionPane.showInputDialog(null, "Enter your email Account", "Account login.",
				JOptionPane.QUESTION_MESSAGE);

		try {
			client = new ClientImpl(emailAccount);
			server = (RequestsInterface) Naming.lookup("rmi://localhost/Server");

			System.out.println(
					"[System] Client created and ready to communicate with server." + "Nome account " + emailAccount);
			server.setClient(client);
			server.clientConnectionWelcome(client);
			refreshTableData(emailAccount);
			model.setUserAccountValue(emailAccount);
			view.setFrameTitle(emailAccount);


			while (true) {
				Thread.sleep(1000);
				
				ArrayList<Email> serverMailList = server.requestUserMailList(client);
				//System.out.println("Server list size"+serverMailList.size()+"Local size"+model.getMailListSize());

				if (serverMailList.size() > model.getMailListSize()) {
					String sender = serverMailList.get(serverMailList.size()-1).getSender();
					String title = serverMailList.get(serverMailList.size()-1).getEmailObject();
					System.out.println("new mail arrived");
					client.showNewMessagePopUp(sender,title);
					refreshTableData(emailAccount);
				}

			}

		} catch (Exception e) {
			System.out.println("Server failed to find an account - Disconnecting " + e);
			server.destroyClient(client);
			System.exit(0);
		}
	}

	public void refreshTableData(String emailAccount) throws RemoteException {
		ArrayList<Email> userMailList = server.requestUserMailList(client);

		if (userMailList == null) {
			return;
		} else {
			model.setValue(userMailList);
			updateViewTableData();
		}
	}

	/*
	 * mouse listener for clicking events
	 */
	public void addMouseListener(MouseEvent e) {

	}

	public void addModel(ClientModel m) {
		System.out.println("Controller: adding model");
		this.model = m;
	}

	public void addView(ClientView v) {
		System.out.println("Controller: adding view");
		this.view = v;
	}

	@SuppressWarnings("serial")
	public void updateViewTableData() {

		view.getTable().setModel(new DefaultTableModel(model.table, model.columnNames) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int selectedRow = view.getTable().getSelectedRow();
		System.out.println("Controller: Opening mail at row " + view.getTable().getSelectedRow());
		view.createReadMailGUI(model.table[selectedRow][0].toString(), model.table[selectedRow][2].toString());
		currentMailIndexOpened = selectedRow;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			server.destroyClient(client);
			System.exit(0);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}