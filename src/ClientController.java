import javax.swing.table.DefaultTableModel;

class ClientController implements java.awt.event.ActionListener {

	//Joe: Controller has Model and View hardwired in
	ClientModel model;
	ClientView view;

	ClientController() {	
		System.out.println ("Controller()");
	} //Controller()

	//invoked when a button is pressed
	public void actionPerformed(java.awt.event.ActionEvent e){
		//uncomment to see what action happened at view
		/*
		System.out.println ("Controller: The " + e.getActionCommand() 
			+ " button is clicked at " + new java.util.Date(e.getWhen())
			+ " with e.paramString " + e.paramString() );
		*/
		System.out.println("Controller: acting on Model");
		//model.incrementValue();
	} //actionPerformed()

	//Joe I should be able to add any model/view with the correct API
	//but here I can only add Model/View
	public void addModel(ClientModel m){
		System.out.println("Controller: adding model");
		this.model = m;
	} //addModel()

	public void addView(ClientView v){
		System.out.println("Controller: adding view");
		this.view = v;
	} //addView()

	public DefaultTableModel initData(){
		
		String[] columnNames = {"Type", "Source", "Subject", "Date" };
		Object[][] data = {
				
				{"Received", "Cristiano Soleti", "Porn Pics", new Date(2017, 4, 21).getDate()},
				{"Received", "Cristiano Soleti", "Porn Pics", "2017/4/21"}
				
		};
		
		DefaultTableModel model;
		model = new DefaultTableModel(data, columnNames);
		return model;
		
	}
	
	public void refreshViewTableData(){
		
		view.getTable().setModel(initData());
		
	}

} //Controller