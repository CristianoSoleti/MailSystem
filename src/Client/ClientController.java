package Client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

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

	/*
	 * sends mail request to server if mail isn't null.
	 */
	public void sendMailRequest() throws IOException {
		Email newMail = view.createMailFromGUI();
		if (newMail == null) {
			return;
		}
		System.out.println("Controller: Sending" + newMail.getSender());
		System.out.println("Controller >sender is" +newMail.getSender().toString());

		server.send(newMail);
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

			while (true) {
				Thread.sleep(1000);
				
				ArrayList<Email> serverMailList = server.requestUserMailList(client);
				System.out.println("Server list size"+serverMailList.size()+"Local size"+model.getMailListSize());

				if (serverMailList.size() > model.getMailListSize()) {
					String sender = serverMailList.get(serverMailList.size()-1).getSender();
					String title = serverMailList.get(serverMailList.size()-1).getEmailObject();
					// [CRITICAL]mostarre mittente e titolo
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
			view.setFrameTitle(emailAccount);

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
		view.readMailFrame(model.table[selectedRow][1].toString(), model.table[selectedRow][2].toString());
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