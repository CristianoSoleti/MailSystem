import java.io.IOException;
import java.net.UnknownHostException;

public class GlueCode {

	/*
	 * The order of instantiating the objects below will be important for some
	 * pairs of commands. I haven't explored this in any detail, beyond that the
	 * order below works.
	 */

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
		myClientView.addController(myClientController);
		
	}


	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException, InterruptedException {
		
		GlueCode mainRunMVC = new GlueCode();
		
	} 

} // RunMVC
