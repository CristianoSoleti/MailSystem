import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.WindowEvent;	//for CloseListener()
import java.awt.event.WindowAdapter;	//for CloseListener()
import java.lang.Integer;		//int from Model is passed as an Integer
import java.util.Observable;		//for update();
import java.awt.event.ActionListener;	//for addController()
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;


class ClientView implements java.util.Observer {

	private JTable table = new JTable();
	private JButton newMailBtn = new JButton("Create");
	private JButton readMailBtn = new JButton("Read");
	private JButton forwardMailBtn = new JButton("Forward");
	private JButton deleteMailBtn = new JButton("Delete");
	
	ClientView(String frameName) {
		
		System.out.println("View()");	
		
		//frame in constructor and not an attribute as doesn't need to be visible to whole class
		JFrame frame = new JFrame(frameName);
		frame.add("North", table);


		//panel in constructor and not an attribute as doesn't need to be visible to whole class
		Panel mainPanel 		= new Panel();
		mainPanel.add(newMailBtn);
		frame.add("Center", mainPanel);
		
		Panel subPanel 		= new Panel();
		subPanel.add(readMailBtn);
		subPanel.add(forwardMailBtn);
		subPanel.add(deleteMailBtn);
		frame.add("South", subPanel);	

		//frame.addWindowListener(new CloseListener());	
		frame.setSize(800,200);
		frame.setLocation(100,100);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		
		
	} //View()

	// Called from the Model
    	public void update(Observable obs, Object obj) {

		//who called us and what did they send?
		//System.out.println ("View      : Observable is " + obs.getClass() + ", object passed is " + obj.getClass());

		//model Pull 
		//ignore obj and ask model for value, 
		//to do this, the view has to know about the model (which I decided I didn't want to do)
		//uncomment next line to do Model Pull
    		//myTextField.setText("" + model.getValue());

		//model Push 
		//parse obj
		//myTextField.setText("" + ((Integer)obj).intValue());	//obj is an Object, need to cast to an Integer

    	} //update()

	
    	
	public void addController(ActionListener controller,MouseListener e){
		System.out.println("View      : adding controller");
		newMailBtn.addActionListener(controller);	//need instance of controller before can add it as a listener 
		readMailBtn.addActionListener(controller);
		table.addMouseListener(e);
	} 
	

	
	public JTable getTable()
	{
		return table;
	}
} //View