package Client;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
			openNewMailEditor();
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

	public void forwardMail() {
		if (isMailEditorOpen) {
			return;
		}
		System.out.println("Controller: -FORWARD- Open Mail Editor");
		view.createMailGUI();
		isMailEditorOpen = true;
		setForwardMailInfo();

	}

	public void setForwardMailInfo() {
		view.resetTextBox(view.getReceiverField(), model.getMailList().get(currentMailIndexOpened).getSender());
		view.resetTextBox(view.getSubjectField(), model.getMailList().get(currentMailIndexOpened).getEmailObject());
		view.getReceiverField().setForeground(Color.BLACK);
		view.getSubjectField().setForeground(Color.BLACK);
	}

	public void replyMail() {
		if (isMailEditorOpen) {
			return;
		}
		System.out.println("Controller: -REPLY- Open Mail Editor");
		view.createMailGUI();
		isMailEditorOpen = true;
		setReplyMailInfo();

	}

	/*
	 * Set reply information in GUI N.B If receiver is just one , it will reply
	 * to him , if receivers are more than 1 , it will reply to all
	 */
	public void setReplyMailInfo() {
		String[] mailReceivers = model.getMailList().get(currentMailIndexOpened).getAllReceivers();

		String splittedReceivers = "";
		int i = 1;

		for (String s : mailReceivers) {
			if (mailReceivers.length == 1) {
				splittedReceivers += s;
			} else {
				if (i++ == mailReceivers.length) {
					splittedReceivers += s;
				} else {
					splittedReceivers += s + ",";

				}

			}

		}
		view.resetTextBox(view.getReceiverField(), splittedReceivers);
		view.resetTextBox(view.getSubjectField(),
				"RE: " + model.getMailList().get(currentMailIndexOpened).getEmailObject());
		view.getReceiverField().setForeground(Color.BLACK);
		view.getSubjectField().setForeground(Color.BLACK);
	}

	public void deleteMail() throws RemoteException {
		System.out.println("Removing mail at index" + currentMailIndexOpened);
		server.delete(client, currentMailIndexOpened);
		model.getMailList().remove(currentMailIndexOpened);
		refreshTableData(model.userEmailAccount);
		view.getReadMailFrame().setVisible(false);
	}

	/*
	 * sends mail request to server if mail isn't null.
	 */
	public void sendMailRequest() throws IOException {
		ArrayList<Email> newMailList = createMail();
		if (newMailList == null) {
			return;
		}
		for (Email m : newMailList)
			if (server.checkError(m.getReceiver())) {
				client.showErrorMessage("Please , send to an existing mail account");
				view.getReceiverField().grabFocus();
				return;
			}

		server.send(newMailList, client);
		refreshTableData(model.userEmailAccount);
		// Close frame after succesfully sending a message
		view.getNewMailFrameView()
				.dispatchEvent(new WindowEvent(view.getNewMailFrameView(), WindowEvent.WINDOW_CLOSING));
	}

	public ArrayList<Email> createMail() {
		if (view.getReceiverData().equals("") || view.getReceiverData().equals("Add Receiver")) {
			return null;

		}
		if (view.getSubjectData().equals("") || view.getSubjectData().equals("Add Subject")) {
			JOptionPane.showMessageDialog(null, "Please submit a subject");
			view.getSubjectField().grabFocus();
			return null;
		}

		String[] listOfReceiver = getListOfReceiver();
		ArrayList<Email> newMailList = new ArrayList<Email>();
		for (int i = 0; i < listOfReceiver.length; i++) {
			Email newMail = new Email(model.userEmailAccount, listOfReceiver[i], view.getSubjectData(),
					view.getMessageData(), listOfReceiver);
			newMailList.add(newMail);
		}
		return newMailList;
	}

	public String[] getListOfReceiver() {
		if (view.getReceiverData().equals("") || view.getReceiverData().equals("Add Receiver")) {
			return null;
		}
		String receiversString = view.getReceiverData();
		String[] array = receiversString.split(",");
		for (int i = 0; i < array.length; i++) {
			System.out.println("Destinatario numero" + i + array[i]);
		}
		return array;
	}

	public void openNewMailEditor() {
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
				// System.out.println("Server list
				// size"+serverMailList.size()+"Local
				// size"+model.getMailListSize());

				if (serverMailList.size() > model.getMailListSize()) {
					String sender = serverMailList.get(serverMailList.size() - 1).getSender();
					String title = serverMailList.get(serverMailList.size() - 1).getEmailObject();
					System.out.println("new mail arrived");
					client.showNewMessagePopUp(sender, title);
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