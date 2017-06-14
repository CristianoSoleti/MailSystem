package Server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.DefaultTableModel;

import MailSystemUtilities.SYSTEM_CONSTANTS;
import Remote.Requests;

public class ServerController {

	static ServerModel model;
	static ServerView view;
	private static ServerController instance = null;

	protected ServerController() {
		setUpRMI();
	}

	public static ServerController getInstance() throws RemoteException {
		if (instance == null) {
			instance = new ServerController();
		}
		return instance;
	}
	public void setUpRMI() {
		runRMIRegistry();
		try {

			System.out.println("Creating server..");
			Requests server = new Requests(SYSTEM_CONSTANTS.SERVER);
			Naming.rebind("rmi://localhost/Server", server);
			System.out.println("[System] Server Remote Object is ready:");

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

	public void addModel(ServerModel m) {

		System.out.println("Server: adding model");
		this.model = m;

	}

	public void addView(ServerView v) {

		System.out.println("Server: adding view");
		this.view = v;

	}

	@SuppressWarnings("serial")
	public static void refreshTable(ArrayList<String> connectedClients, Date date) {

		for (int i = 0; i < model.getData().length; i++) {

			model.getData()[i][0] = "";
			model.getData()[i][1] = "";
			model.getData()[i][2] = "";

		}
		for (int k = 0; k < connectedClients.size(); k++) {

			model.getData()[k][0] = k;
			model.getData()[k][1] = connectedClients.get(k);
			model.getData()[k][2] = date;

		}
		view.table.setModel(new DefaultTableModel(model.getData(), model.getColumnNames()) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		});

	}

	public static void logActions(String s) {
		view.getLogArea().append(s + "\n");
	}

}
