import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class GlueCode {

	/*
	 * The order of instantiating the objects below will be important for some
	 * pairs of commands. I haven't explored this in any detail, beyond that the
	 * order below works.
	 */

    private JTextArea messageArea = new JTextArea(8, 60);

    private JFrame frame = new JFrame("Email Login System - Client");

	public GlueCode() throws UnknownHostException, IOException, InterruptedException {

		ClientModel myClientModel = new ClientModel();
		ClientView myClientView = new ClientView("cristiano.soleti@edu.unito.it");

		myClientModel.addObserver(myClientView);

		ClientController myClientController = new ClientController();
		if(!Server.requestConnection()){return;}
		myClientController.connectToServer();

		myClientController.addModel(myClientModel);
		myClientController.addView(myClientView);
		myClientController.refreshViewTableData();
		myClientView.addController(myClientController, myClientController);
		
	}


	public static void main(String[] args) throws IOException, InterruptedException {
		GlueCode mainRunMVC = new GlueCode();
		
	} 

} // RunMVC
