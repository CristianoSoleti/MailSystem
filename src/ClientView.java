import javax.swing.*;

import java.awt.event.WindowEvent; //for CloseListener()
import java.awt.event.WindowAdapter; //for CloseListener()
import java.util.Observable; //for update();
import java.awt.event.ActionListener; //for addController()
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.awt.*;

class ClientView implements java.util.Observer {

	// Base Client GUI
	private JFrame frame = new JFrame();
	private JTable table = new JTable();
	private JButton newMailBtn = new JButton("Create");
	private JButton readMailBtn = new JButton("Read");
	private JButton forwardMailBtn = new JButton("Forward");
	private JButton deleteMailBtn = new JButton("Delete");

	// Create Mail GUI
	private JFrame newMailFrame;
	private JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
	private JLabel headerLbl = new JLabel("New Message");
	public JTextArea receiverTextArea = new JTextArea();
	private JTextArea subjectTextArea = new JTextArea();
	private JTextArea messageTextArea = new JTextArea();
	private JButton sendBtn = new JButton("Send");

	public String getMessage() {
		return messageTextArea.getText();
	}

	public String getReceiver() {
		return receiverTextArea.getText();
	}

	public String getSubject() {
		return subjectTextArea.getText();
	}
	public JTable getTable() {
		return table;
	}
	ClientView(String frameName) {

		System.out.println("ClientView Created");

		frame.setName(frameName);
		frame.add("North", table);
		
		Panel mainPanel = new Panel();
		mainPanel.add(newMailBtn);
		
		frame.add("Center", mainPanel);

		Panel subPanel = new Panel();
		subPanel.add(readMailBtn);
		subPanel.add(forwardMailBtn);
		subPanel.add(deleteMailBtn);
		frame.add("South", subPanel);

		frame.setSize(800, 200);
		frame.setLocation(100, 100);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	} // View()

	public void update(Observable obs, Object obj) {
		System.out.println("View : Observable is " + obs.getClass() + ",object passed is " + obj + "");
	}

	public void addController(ActionListener controller) {
		System.out.println("View: adding controller");
		newMailBtn.addActionListener(controller); 													
		readMailBtn.addActionListener(controller);
		sendBtn.addActionListener(controller);
		table.addMouseListener((MouseListener) controller);
	}

	public void readMailFrame(String sender, String messageText) {

		JFrame newFrame = new JFrame();
		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		;
		JLabel dialogueLbl = new JLabel(sender);
		JTextArea messageTextArea = new JTextArea(messageText);
		messageTextArea.setEditable(false);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		newFrame.add("Center", mainPanel);
		mainPanel.add(dialogueLbl, mainPanel.BOTTOM_ALIGNMENT);
		mainPanel.add(messageTextArea, mainPanel.BOTTOM_ALIGNMENT);
		newFrame.setVisible(true);
		newFrame.setSize(800, 200);
		newFrame.setLocation(100, 100);
		newFrame.setVisible(true);
		newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void createMailGUI() {
		Font headerFont = new Font("Futura", Font.BOLD, 18);
		Font receiverFontFocus = new Font("Futura", Font.BOLD, 18);
		Font receiverFontNoFocus = new Font("Futura", Font.ITALIC, 18);

		newMailFrame = new JFrame();

		mainPanel.setOpaque(true);
		mainPanel.setBackground(new Color(66, 66, 66));

		headerLbl.setForeground(Color.WHITE);
		headerLbl.setFont(headerFont);

		receiverTextArea.setText("Add Receiver");
		receiverTextArea.setFont(receiverFontNoFocus);
		receiverTextArea.setForeground(new Color(144, 164, 174));

		receiverTextArea.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				if (receiverTextArea.getText().equals("Add Receiver")) {
					receiverTextArea.setText("");
					receiverTextArea.setFont(receiverFontFocus);
					receiverTextArea.setForeground(Color.BLACK);

				}

			}

			public void focusLost(FocusEvent e) {
				if (receiverTextArea.getText().equals("")) {
					receiverTextArea.setText("Add Receiver");
					receiverTextArea.setFont(receiverFontNoFocus);
					receiverTextArea.setForeground(new Color(144, 164, 174));
				}

			}

		});

		subjectTextArea.setText("Add Subject");
		subjectTextArea.setFont(receiverFontNoFocus);
		subjectTextArea.setForeground(new Color(144, 164, 174));
		subjectTextArea.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				if (subjectTextArea.getText().equals("Add Subject")) {
					subjectTextArea.setText("");
					subjectTextArea.setFont(receiverFontFocus);
					subjectTextArea.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (subjectTextArea.getText().equals("")) {
					subjectTextArea.setText("Add Subject");
					subjectTextArea.setFont(receiverFontNoFocus);
					subjectTextArea.setForeground(new Color(144, 164, 174));

				}
			}

		});

		sendBtn.setBackground(new Color(59, 89, 182));
		sendBtn.setForeground(Color.WHITE);
		sendBtn.setFocusPainted(false);
		sendBtn.setFont(new Font("Tahoma", Font.BOLD, 12));

		Box box = new Box(BoxLayout.Y_AXIS);

		messageTextArea.setFont(new Font("Serif", Font.ITALIC, 32));
		messageTextArea.setLineWrap(true);
		messageTextArea.setWrapStyleWord(true);

		JScrollPane jScrollPane1 = new JScrollPane(receiverTextArea);
		JScrollPane jScrollPane2 = new JScrollPane(subjectTextArea);
		JScrollPane jScrollPane = new JScrollPane(messageTextArea);

		box.add(jScrollPane1);
		box.add(jScrollPane2);
		box.add("Center", jScrollPane);
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		btnPanel.add(sendBtn);

		int x = 800;
		int y = 400;
		mainPanel.add(headerLbl);

		newMailFrame.add("North", mainPanel);
		newMailFrame.add("Center", box);
		newMailFrame.add("South", btnPanel);
		newMailFrame.setSize(x, y);
		newMailFrame.setLocation(100, 100);
		newMailFrame.setVisible(true);
		messageTextArea.grabFocus();
		jScrollPane1.setMinimumSize(new Dimension(x, 200));
		jScrollPane1.setMaximumSize(new Dimension(x, 200));
		jScrollPane2.setMinimumSize(new Dimension(x, 200));
		jScrollPane2.setMaximumSize(new Dimension(x, 200));
		newMailFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ClientController.isMailEditorOpen = false;
				newMailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
	}

	
	

}