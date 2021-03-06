package Client;

import java.io.IOException;
import java.net.UnknownHostException;


public class ClientApp {

	/*
	 * The order of instantiating the objects below will be important for some
	 * pairs of commands. I haven't explored this in any detail, beyond that the
	 * order below works.
	 */

	public ClientApp() throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException {

		ClientModel myClientModel = new ClientModel();
		ClientView myClientView = new ClientView();

		myClientModel.addObserver(myClientView);

		ClientController myClientController = new ClientController();
		myClientController.addModel(myClientModel);
		myClientController.addView(myClientView);
		myClientView.addController(myClientController);
		myClientController.connectToServer();
	}

	public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException, InterruptedException {
		@SuppressWarnings("unused")
		ClientApp mainRunMVC = new ClientApp();
	
	}

}
