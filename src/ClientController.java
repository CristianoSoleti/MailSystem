import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * Controller extends action listener cause he needs to perform actions on events like click.
 */
class ClientController implements ActionListener, MouseListener {

	
	static boolean isMailEditorOpen = false;
	PrintWriter out;
	private Socket socket;
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
			createMail();
			break;

		case "Read":
			System.out.println("Controller: Reading Mail" +socket.isClosed());
			try {
				tryThing();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;

		case "Send":
			System.out.println("Controller: Send Mail");
			try {
				sendMailRequest();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;

		default:
			break;
		}

	}

	public void sendMailRequest() throws IOException {

		model.setValue(view.getReceiver(), view.getSubject(), view.getMessage());
		out.flush();
		out.write("Send");
		out.println();
		
	}

	public void tryThing() throws IOException
	{
		out.flush();
		out.write("Read");
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

	public void connectToServer() throws UnknownHostException, IOException {
		socket = new Socket("127.0.0.1", 9898);
		out = new PrintWriter(socket.getOutputStream(), true);

		System.out.println("connected" + socket.getPort() + "local port" + socket.getLocalPort());
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