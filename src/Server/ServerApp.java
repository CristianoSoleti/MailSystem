package Server;

import java.rmi.RemoteException;

public class ServerApp {
	private static ServerApp instance = null;

	private ServerApp() throws RemoteException {

		ServerModel myServerModel = new ServerModel();
		ServerView myServerView = new ServerView();

		myServerModel.addObserver(myServerView);

		ServerController myServerController = new ServerController();
		myServerController.addModel(myServerModel);
		myServerController.addView(myServerView);
		myServerView.addController(myServerController);

	}

	public static ServerApp getInstance() throws RemoteException {
		if (instance == null) {
			instance = new ServerApp();
		}
		return instance;
	}

	public static void main(String[] args) throws RemoteException {

		getInstance();

	}

}
