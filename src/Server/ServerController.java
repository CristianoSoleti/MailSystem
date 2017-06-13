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

	public ServerController() {
		
		setUpRMI();
		
	}

	public void setUpRMI() {
		
		runRMIRegistry();

		try {

			System.out.println("Creating server..");
			Requests server = new Requests(SYSTEM_CONSTANTS.SERVER);

			Naming.rebind("rmi://localhost/Server", server);

			System.out.println("[System] Server Remote Object is ready:");

			model.getInstance();

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

	public void addModel( ServerModel m ) {

		System.out.println( "Server: adding model" );
		this.model = m;

	}
	
	public void addView( ServerView v ){
		
		System.out.println( "Server: adding view" );
		this.view = v;
		
	}

	@SuppressWarnings("serial")
	public static void refreshTable(ArrayList<String> connectedClients, Date date) {

		for (int i = 0; i < model.data.length; i++) {

			model.data[i][0] = "";
			model.data[i][1] = "";
			model.data[i][2] = "";

		}
		for (int k = 0; k < connectedClients.size(); k++) {

			model.data[k][0] = k;
			model.data[k][1] = connectedClients.get(k);
			model.data[k][2] = date;

		}
		view.table.setModel( new DefaultTableModel( model.data, model.columnNames ) {

			@Override
			public boolean isCellEditable( int row, int column ) {

				return false;

			}

		});

	}
	
	public static void logActions( String s ){
		
		view.logArea.append( s + " \n " );
		
	}

}
