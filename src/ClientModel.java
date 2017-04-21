import javax.swing.table.DefaultTableModel;

public class ClientModel extends java.util.Observable {	
	
	String[] columnNames = {"Type", "Source", "Subject", "Date" };
	Object[][] data = {};
	DefaultTableModel tableModel;
	public ClientModel(){

		System.out.println("Model()");

		/**
		Problem initialising both model and view:

		On a car you set the speedometer (view) to 0 when the car (model) is stationary.
		In some circles, this is called calibrating the readout instrument.
		In this MVC example, you would need two separate pieces of initialisation code,
			in the model and in the view. If you changed the initialisation value in one
			you'd have to remember (or know) to change the initialisation value in the other.
			A recipe for disaster.

		Alternately, when instantiating model, you could run  

		setValue(0);

		as part of the constructor, sending a message to the view. 
		This requires the view to be instantiated before the model,
		otherwise the message will be send to null (the unitialised value for view).
		This isn't a particularly onerous requirement, and is possibly a reasonable approach.

		Alternately, have RunMVC tell the view to intialise the model.
		The requires the view to have a reference to the model.
		This seemed an unneccesary complication.

		I decided instead in RunMVC, to instantiate model, view and controller, make all the connections,
		then since the Controller already has a reference to the model (which it uses to alter the status of the model),
		to initialise the model from the controller and have the model automatically update the view.
		*/

	} //Model()

	public void updateData()
	{
		
	}
	public void updateModel()
	{
		
	}
	
	//uncomment this if View is using Model Pull to get the counter
	//not needed if getting counter from notifyObservers()
	//public int getValue(){return counter;}

	//notifyObservers()
	//model sends notification to view because of RunMVC: myModel.addObserver(myView)
	//myView then runs update()
	//
	//model Push - send counter as part of the message
	
	
} //Model