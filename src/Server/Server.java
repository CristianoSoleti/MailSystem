package Server;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import MailSystemUtilities.MailAccount;
import MailSystemUtilities.MailAccountDatabase;
import MailSystemUtilities.SYSTEM_CONSTANTS;
import Remote.Client;
import Remote.ClientImpl;
import Remote.Requests;
import Remote.RequestsInterface;

/**
 * A server program which accepts requests from clients to capitalize strings.
 * When clients connect, a new thread is started to handle an interactive dialog
 * in which the client sends in a string and the server thread sends back the
 * capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform dependent.
 * If you ran it from a console window with the "java" interpreter, Ctrl+C
 * generally will shut it down.
 */
public class Server {

	public static MailAccountDatabase db = MailAccountDatabase.getInstance();
	private static Server instance = null;

	static String[] columnNames = { "Client #", "Account", "Date" };
	public static Object[][] data = new Object[5][3];

	public JLabel informationLbL = new JLabel("Connected clients");
	private static JTable table = new JTable();
	public static ArrayList<String> connectedClients = new ArrayList<String>();

	Server() throws RemoteException {
		super();

		JFrame frame = new JFrame("MailServer");
		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane);
		frame.setSize(800, 200);
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

	public static boolean requestConnection(String mail) throws InterruptedException {
		for (MailAccount ml : db.getAccountList()) {
			if (ml.getMailAccount().equals(mail)) {
				System.out.println("Connecting...");
				Thread.sleep(3000);
				System.out.println("Connection established");
				return true;
			}
		}

		System.out.println("Connection Failed");
		System.out.println("Closing application");
		Thread.sleep(1000);
		System.exit(0);

		return false;

	}

	public static void runRMIRegistry() {
		try { // special exception handler for registry creation
			LocateRegistry.createRegistry(1099);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			// do nothing, error means registry already exists
			System.out.println("java RMI registry already exists.");
		}
	}

	@SuppressWarnings("serial")
	public static void refreshTable(Date date) {
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