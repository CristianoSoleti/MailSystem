//RunMVC.java
//(C) Joseph Mack 2011, jmack (at) wm7d (dot) net, released under GPL v3 (or any later version)

public class GlueCode {

	//The order of instantiating the objects below will be important for some pairs of commands.
	//I haven't explored this in any detail, beyond that the order below works.

	private int start_value = 10;	//initialise ClientModel, which in turn initialises ClientView

	public GlueCode() {

		//create ClientModel and ClientView
		ClientModel myClientModel 	= new ClientModel();
		ClientView myClientView 	= new ClientView("cristiano.soleti@edu.unito.it");

		//tell ClientModel about ClientView. 
		myClientModel.addObserver(myClientView);
		/*	
			init ClientModel after ClientView is instantiated and can show the status of the ClientModel
			(I later decided that only the ClientController should talk to the ClientModel
			and moved initialisation to the ClientController (see below).)
		*/
		//uncomment to directly initialise ClientModel
		//myClientModel.setValue(start_value);	

		//create ClientController. tell it about ClientModel and ClientView, initialise ClientModel
		ClientController myClientController = new ClientController();
		myClientController.addModel(myClientModel);
		myClientController.addView(myClientView);
		myClientController.refreshViewTableData();
		//myClientController.initModel(start_value);

		//tell ClientView about ClientController 
		//myClientView.addController(myClientController);
		//and ClientModel, 
		//this was only needed when the ClientView inits the ClientModel
		//myClientView.addClientModel(myClientModel);

	} //RunMVC()

	public static void main(String[] args){

		GlueCode mainRunMVC = new GlueCode();

	} //main()	
	
} //RunMVC
