import java.awt.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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

	private static Server instance = null;

	static ArrayList<Socket> connectedClient = new ArrayList<Socket>();
	static ArrayList<Integer> numberOfClients = new ArrayList<Integer>();

	static int i = 0;
	static String[] columnNames = { "Client #", "Port", "Date" };
	public static Object[][] data = new Object[5][3];
	DefaultTableModel tableModel;

	public JLabel informationLbL = new JLabel("Connected clients");
	private static JTable table = new JTable();

	protected Server() {

		JFrame frame = new JFrame("MailServer");
		Panel mainPanel = new Panel();
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
		frame.setSize(800, 200);
		frame.setLocation(100, 100);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static Server getInstance() {
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
		Server.getInstance();
		System.out.println("Mail Server is running.");
		int clientNumber = 0;
		ServerSocket listener = new ServerSocket(9898);
		try {
			while (true) {
				new MailThread(listener.accept(), clientNumber++).start();
			}
		} finally {
			listener.close();
		}
	}

	/**
	 * A private thread to handle capitalization requests on a particular
	 * socket. The client terminates the dialogue by sending a single line
	 * containing only a period.
	 */
	private static class MailThread extends Thread {
		private Socket socket;
		private int clientNumber;

		public MailThread(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			log("New connection with client# " + clientNumber + " at " + socket);
		}

		/**
		 * Services this thread's client by first sending the client a welcome
		 * message then repeatedly reading strings and sending back the
		 * capitalized version of the string.
		 */
		public void run() {
			try {
				connectedClient.add(socket);
				System.out.println("Aggiunto il socket" + socket);

				System.out.println("Number of connected clients" + connectedClient.size() + "");

				// Decorate the streams so we can send characters
				// and not just bytes. Ensure output is flushed
				// after every newline.
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// Send a welcome message to the client.
				refreshTable();

				while (true) {
					switch (in.readLine()) {
					case "Send":
						JOptionPane.showMessageDialog(null, "Server Authority : Mail Sent");
						break;

					case "Read":
						JOptionPane.showMessageDialog(null, "Read");
						break;
					}

				}
			} catch (IOException e) {
				log("Error handling client# " + clientNumber + ": " + e);
				connectedClient.remove(socket);
				refreshTable();
			} finally {
				try {
					socket.close();
					connectedClient.remove(socket);
					refreshTable();
				} catch (IOException e) {
					log("Couldn't close a socket, what's going on?");
				}
				log("Connection with client# " + clientNumber + " closed");
			}
		}

		/**
		 * Logs a simple message. In this case we just write the message to the
		 * server applications standard output.
		 */
		private void log(String message) {
			System.out.println(message);
		}
	}

	public static boolean requestConnection() throws InterruptedException {
		System.out.println("Connecting...");
		Thread.sleep(3000);
		System.out.println("Connection established");
		return true;
	}

	@SuppressWarnings("serial")
	public static void refreshTable() {
		for (int i = 0; i < data.length; i++) {
			data[i][0] = "";
			data[i][1] = "";
			data[i][2] = "";
		}
		for (int k = 0; k < connectedClient.size(); k++) {
			data[k][0] = k;
			data[k][1] = connectedClient.get(k).getPort();
			data[k][2] = Calendar.getInstance().getTime();;
		}
		table.setModel(new DefaultTableModel(data, columnNames) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}
}