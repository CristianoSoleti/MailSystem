package Server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import MailSystemUtilities.MailAccountDatabase;

public class ServerModel extends java.util.Observable{

	public static MailAccountDatabase db = MailAccountDatabase.getInstance();
	private Server instance = null;

	static String[] columnNames = { "Client #", "Account", "Date" };
	public static Object[][] data = new Object[5][3];

	public JLabel informationLbL = new JLabel("Connected clients");
	private static JTable table = new JTable();
	static public JTextArea logArea = new JTextArea(20, 10);

	public Server getInstance() throws RemoteException {

		if (instance == null) {

			instance = new Server();

		}

		return instance;

	}

}
