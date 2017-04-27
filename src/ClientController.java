import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/*
 * Controller extends action listener cause he needs to perform actions on events like click.
 */
class ClientController implements ActionListener, MouseListener {

	static boolean isMailEditorOpen = false;
	PrintWriter out;
	ObjectInputStream in;
	private Socket socket;

	ClientModel model;
	ClientView view;

	ClientController() {
		System.out.println("ClientController created");
	}

	// invoked when a button is pressed
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

	public void sendMailRequest() throws IOException {

		out.flush();
		out.write(SYSTEM_CONSTANTS.SEND_ACTION);
		out.println();

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
		if (!Server.requestConnection(emailAccount)) {
			return;
		}
		view.setFrameTitle(emailAccount);
		socket = new Socket("127.0.0.1", 9898);
		in = new ObjectInputStream(socket.getInputStream());

		out = new PrintWriter(socket.getOutputStream(), true);
		out.flush();
		out.write(SYSTEM_CONSTANTS.LOAD_ACTION);
		out.println();

		loadMailIntoTable(emailAccount);

	}

	public void loadMailIntoTable(String emailAccount) throws ClassNotFoundException, IOException {
		out.flush();
		out.write(emailAccount);
		out.println();
		@SuppressWarnings("unchecked")
		ArrayList<Email> messageList = (ArrayList<Email>) in.readObject();
		if (messageList == null) {return;}
		
		model.setValue(messageList);
		refreshViewTableData();
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
	private void refreshViewTableData() {

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

}