package Client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

import java.awt.event.WindowEvent; //for CloseListener()
import java.awt.event.WindowListener;
import java.io.Serializable;
import java.awt.event.WindowAdapter; //for CloseListener()
import java.util.Observable; //for update();
import java.awt.event.ActionListener; //for addController()
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.*;

import MailSystemUtilities.Email;
import MailSystemUtilities.SYSTEM_CONSTANTS;

class ClientView implements java.util.Observer, Serializable {

	Font inBoxMailFont = new Font("Arial", Font.PLAIN, 14);

	Font headerFont = new Font("Futura", Font.BOLD, 18);
	Font receiverFontFocus = new Font("Futura", Font.BOLD, 18);
	Font receiverFontNoFocus = new Font("Futura", Font.ITALIC, 18);

	// Base Client GUI
	public JFrame frame = new JFrame();
	private JTable table = new JTable();
	private JButton newMailBtn = new JButton("Create");
	//private JButton forwardMailBtn;
	//private JButton deleteMailBtn;

	// Create Mail GUI
	private JFrame newMailFrame;
	private JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
	private JLabel headerLbl = new JLabel("New Message");
	public JTextArea receiverTextArea = new JTextArea();
	private JTextArea subjectTextArea = new JTextArea();
	private JTextArea messageTextArea = new JTextArea();
	private JButton sendBtn = new JButton("Send");

	// Read Mail GUI
	public JFrame readMailFrame = new JFrame();
	//public JButton replyBtn;

	public JTable getTable() {
		return table;
	}

	String emailAccount = "";
	ActionListener controller;

	ClientView() {
		System.out.println("Client View Created Successfully");

		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane, BorderLayout.CENTER);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

		restyleButton(newMailBtn, Color.WHITE, Color.decode("#E44C41"), Color.BLACK);
		newMailBtn.setFont(new Font("Arial", Font.BOLD, 14));
		newMailBtn.setActionCommand(SYSTEM_CONSTANTS.CREATE_ACTION);
		mainPanel.add(newMailBtn);
		// deleteMailBtn = createBtnWithImage("deleteMail.png",
		// SYSTEM_CONSTANTS.DELETE_ACTION);
		// forwardMailBtn = createBtnWithImage("forwardMail.png",
		// SYSTEM_CONSTANTS.FORWARD_ACTION);
		// mainPanel.add(deleteMailBtn);

		// mainPanel.add(forwardMailBtn);
		styleTable(table);
		frame.add(mainPanel, BorderLayout.SOUTH);

		setFrame(frame);

	}

	private void setFrame(JFrame frame) {
		frame.setSize(800, 200);
		frame.setLocation(100, 100);
		frame.setVisible(true);
	}

	private void styleTable(JTable table) {
		JTableHeader anHeader = table.getTableHeader();
		table.setFont(inBoxMailFont);
		anHeader.setForeground(Color.black);
		anHeader.setFont(headerFont);
	}

	private JButton createBtnWithImage(String imageIcon, String actionCommand) {
		Icon icon = new ImageIcon(imageIcon);
		JButton button = new JButton(icon);
		button.setActionCommand(actionCommand);
		restyleButton(button, Color.WHITE, Color.decode("#ecf0f1"), Color.BLACK);
		return button;
	}

	private void restyleButton(JButton button, Color foreground, Color background, Color border) {
		button.setForeground(foreground);
		button.setBackground(background);
		Border line = new LineBorder(border);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		button.setBorder(compound);
		button.setPreferredSize(new Dimension(80, 30));

	}

	public void update(Observable obs, Object obj) {
		System.out.println("View : Observable is " + obs.getClass() + ",object passed is " + obj + "");
		// String emailAccount = ((String)obj).toString(); //obj is an Object,
		// need to cast to an Integer
		if (!emailAccount.equals("")) {
			return;
		}
		emailAccount = obj + "";

		// System.out.println(emailAccount);
	}

	public void addController(ActionListener controller) {
		System.out.println("View: adding controller");

		this.controller = controller;
		newMailBtn.addActionListener(controller);
		sendBtn.addActionListener(controller);
		table.addMouseListener((MouseListener) controller);
		frame.addWindowListener((WindowListener) controller);

	}

	public void createReadMailGUI(String sender, String messageText) {

		JLabel dialogueLbl = new JLabel(sender);
		JScrollPane scrollPane = new JScrollPane(dialogueLbl);

		JTextArea messageTextArea = new JTextArea(messageText);
		JScrollPane scrollPane1 = new JScrollPane(messageTextArea);

		JPanel mainPanel = new JPanel();
		JButton replyBtn = createBtnWithImage("replyMail.png", SYSTEM_CONSTANTS.REPLY_ACTION);

		dialogueLbl.setFont(new Font("Tahoma", Font.BOLD, 20));
		messageTextArea.setFont(new Font("Tahoma", Font.PLAIN, 18));

		mainPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		JButton deleteMailBtn = createBtnWithImage("deleteMail.png", SYSTEM_CONSTANTS.DELETE_ACTION);
		JButton forwardMailBtn = createBtnWithImage("forwardMail.png", SYSTEM_CONSTANTS.FORWARD_ACTION);
		
		//Son qua, perchè qua vengono inizializzati (potevo farlo nel costruttore però c'era un bug con la renderizzazione
		//delle immagini
		deleteMailBtn.addActionListener(controller);
		forwardMailBtn.addActionListener(controller);
		replyBtn.addActionListener(controller);
		
		mainPanel.add(deleteMailBtn);
		mainPanel.add(replyBtn);
		mainPanel.add(forwardMailBtn);

		messageTextArea.setEditable(false);

		readMailFrame.add(scrollPane, BorderLayout.NORTH);
		readMailFrame.add(scrollPane1, BorderLayout.CENTER);
		readMailFrame.add(mainPanel, BorderLayout.SOUTH);

		readMailFrame.setVisible(true);
		readMailFrame.setSize(800, 200);
		readMailFrame.setLocation(100, 100);
		readMailFrame.setVisible(true);
		readMailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	public void createMailGUI() {

		newMailFrame = new JFrame();

		mainPanel.setOpaque(true);
		mainPanel.setBackground(new Color(66, 66, 66));

		headerLbl.setForeground(Color.WHITE);
		headerLbl.setFont(headerFont);

		settingReceiverTextArea();
		settingSubjectTextArea();

		sendBtn.setBackground(new Color(59, 89, 182));
		sendBtn.setForeground(Color.WHITE);
		sendBtn.setFocusPainted(false);
		sendBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		sendBtn.setActionCommand(SYSTEM_CONSTANTS.SEND_ACTION);
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
		receiverTextArea.grabFocus();
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

	public void resetTextBox(JTextArea txtArea, String value) {
		txtArea.setText("");
		txtArea.setText(value);
		txtArea.setFont(receiverFontNoFocus);
		txtArea.setForeground(new Color(144, 164, 174));
	}

	public void addKeyListener(JTextArea txtArea, String value) {
		txtArea.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB) {

					if (txtArea.getText().equals("")) {
						resetTextBox(txtArea, value);
					}
					txtArea.transferFocus();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

		});
	}

	public void addFocusListener(JTextArea txtArea, String value) {
		txtArea.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				if (txtArea.getText().equals(value) || txtArea.getText().equals(value + "\t")) {
					txtArea.setText("");
					txtArea.setFont(receiverFontFocus);
					txtArea.setForeground(Color.BLACK);

				}

			}

			public void focusLost(FocusEvent e) {
				if (txtArea.getText().equals("")) {
					resetTextBox(txtArea, value);

				}

			}

		});

	}

	public void settingReceiverTextArea() {
		String startingValue = "Add Receiver";
		resetTextBox(receiverTextArea, startingValue);
		addKeyListener(receiverTextArea, startingValue);
		addFocusListener(receiverTextArea, startingValue);
	}

	public void settingSubjectTextArea() {
		String startingValue = "Add Subject";
		resetTextBox(subjectTextArea, startingValue);
		addKeyListener(subjectTextArea, startingValue);
		addFocusListener(subjectTextArea, startingValue);
	}

	public void setFrameTitle(String title) {
		frame.setTitle(title);
	}

	public Email createMailFromGUI() {

		if (receiverTextArea.getText().equals("") || receiverTextArea.getText().equals("Add Receiver")) {
			return null;
		}
		if (subjectTextArea.getText().equals("") || subjectTextArea.getText().equals("Add Subject")) {
			return null;
		}

		Email newMail = new Email(emailAccount, receiverTextArea.getText(), subjectTextArea.getText(),
				messageTextArea.getText());
		return newMail;
	}
}