package Server;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import MailSystemUtilities.MailAccountDatabase;
import MailSystemUtilities.SYSTEM_CONSTANTS;
import Remote.Requests;

public class Server {

	public static MailAccountDatabase db = MailAccountDatabase.getInstance();
	private static Server instance = null;

	static String[] columnNames = { "Client #", "Account", "Date" };
	public static Object[][] data = new Object[5][3];

	public JLabel informationLbL = new JLabel("Connected clients");
	private static JTable table = new JTable();
	static public JTextArea logArea = new JTextArea(20, 10);

	Server() throws RemoteException {

		JFrame frame = new JFrame("MailServer");
		JScrollPane scrollPane = new JScrollPane(table);
		JScrollPane scrollPane1 = new JScrollPane(logArea);
		logArea.setFont(new Font("Serif", Font.ITALIC, 18));
		logArea.setLineWrap(true);
		logArea.setWrapStyleWord(true);

		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(scrollPane1, BorderLayout.SOUTH);

		frame.setSize(800, 800);
		frame.setLocation(100, 100);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public static Server getInstance() throws RemoteException {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	/**
	 * Application method to run the server runs in an infinite loop listening
	 * on port 9898. When a connection is requested, it spawns a new thread to
	 * do the servicing and immediately returns to listening. The server keeps a
	 * unique client number for each client that connects just to show
	 * interesting logging messages. It is certainly not necessary to do this.
	 */
	public static void main(String[] args) throws Exception {
		runRMIRegistry();
		try {

			System.out.println("Creating server..");
			Requests server = new Requests(SYSTEM_CONSTANTS.SERVER);

			Naming.rebind("rmi://localhost/Server", server);

			System.out.println("[System] Server Remote Object is ready:");

			getInstance();

		} catch (Exception e) {
			System.out.println("[System] Server failed: " + e);
		}
	}

	public static void runRMIRegistry() {
		try {
			LocateRegistry.createRegistry(1099);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}
	}

	@SuppressWarnings("serial")
	public static void refreshTable(ArrayList<String> connectedClients, Date date) {
		for (int i = 0; i < data.length; i++) {
			data[i][0] = "";
			data[i][1] = "";
			data[i][2] = "";
		}
		for (int k = 0; k < connectedClients.size(); k++) {
			data[k][0] = k;
			data[k][1] = connectedClients.get(k);
			data[k][2] = date;
		}
		table.setModel(new DefaultTableModel(data, columnNames) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}
}