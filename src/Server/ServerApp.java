package Server;

import java.rmi.RemoteException;

public class ServerApp {

	public ServerApp() throws RemoteException{
		
		ServerModel myServerModel = new ServerModel();
		ServerView myServerView = new ServerView();

		myServerModel.addObserver( myServerView );

		ServerController myServerController = new ServerController();
		myServerController.addModel( myServerModel );
		myServerController.addView( myServerView );
		myServerView.addController( myServerController );
		
	}
	public static void main( String[] args ) throws RemoteException{
		
		ServerApp myserver = new ServerApp();
		
	}
	
}
