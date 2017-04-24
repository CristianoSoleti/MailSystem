import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * Controller extends action listener cause he needs to perform actions on events like click.
 */
class ClientController implements ActionListener, MouseListener {
	private BufferedReader in;
	private PrintWriter out;
	static boolean isMailEditorOpen = false;
	/*
	 * reference to client model
	 */
	ClientModel model;
	/*
	 * reference to client view
	 */
	ClientView view;

	ClientController() {
		System.out.println("ClientController created");
	}

	// invoked when a button is pressed
	public void actionPerformed(ActionEvent e) {

		System.out.println("Controller: The " + e.getActionCommand() + " button is clicked");
		switch (e.getActionCommand()) {
		case "Create":
			System.out.println(view == null);

			createMail();
			break;

		case "Read":
			System.out.println("Controller: Reading Mail");
			break;

		case "Send":
			System.out.println("Controller: Send Mail");
			sendMailRequest();
			break;

		default:
			break;
		}
		// model.incrementValue();

	}

	public void sendMailRequest() {
		// System.out.println(view.receiverTextArea.getName());
		model.setValue(view.getReceiver(), view.getSubject(), view.getMessage());
		
	}

	public void createMail() {
		if (isMailEditorOpen) {
			return;
		}
		System.out.println("Controller: Open Mail Editor");
		view.createMailGUI();
		isMailEditorOpen = true;
	}

	public void connectToServer() throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1", 9898);

		System.out.println("connected");
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

	/*
	 * setting datas from method
	 */
	public void setData(Object a[][]) {
		model.data = a;
	}

	/*
	 * making table not editable.(If you click on the table GUI it won't let you
	 * change anything)
	 */

	@SuppressWarnings("serial")
	public void refreshViewTableData() {

		view.getTable().setModel(new DefaultTableModel(model.data, model.columnNames) {

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
		view.readMailFrame(model.data[selectedRow][1].toString(), model.data[selectedRow][2].toString());

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