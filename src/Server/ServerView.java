package Server;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class ServerView implements java.util.Observer {
	
	private ServerController controller;

	public JLabel informationLbL = new JLabel( "Connected clients" );
	public JTable table = new JTable();
	static public JTextArea logArea = new JTextArea( 20, 10 );
	
	public ServerView() throws RemoteException {

		JFrame frame = new JFrame( "MailServer" );
		JScrollPane scrollPane = new JScrollPane( table );
		JScrollPane scrollPane1 = new JScrollPane( logArea );
		logArea.setFont( new Font( "Serif", Font.ITALIC, 18 ) );
		logArea.setLineWrap( true );
		logArea.setWrapStyleWord( true );

		frame.add( scrollPane, BorderLayout.CENTER );
		frame.add( scrollPane1, BorderLayout.SOUTH );

		frame.setSize( 800, 800 );
		frame.setLocation( 100, 100 );
		frame.setVisible( true );
		frame.addWindowListener( new WindowAdapter() {
			
			public void windowClosing( WindowEvent e ) {
			
				System.exit( 0 );
		
			}
		
		} );
		
	}
	
	public void addController( ServerController c ){
		
		System.out.print( "Server: adding controller to view" );
		this.controller = c;
		
	}
	
	public void update( java.util.Observable obs, Object obj ){
		
		System.out.println( "Server: updating view" );
		
	}
	
}
