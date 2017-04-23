import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import java.awt.event.WindowEvent; //for CloseListener()
import java.awt.event.WindowAdapter; //for CloseListener()
import java.lang.Integer; //int from Model is passed as an Integer
import java.util.Observable; //for update();
import java.awt.event.ActionListener; //for addController()
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

		// frame in constructor and not an attribute as doesn't need to be
		// visible to whole class
		JFrame frame = new JFrame(frameName);
		frame.add("North", table);

		// panel in constructor and not an attribute as doesn't need to be
		// visible to whole class
		Panel mainPanel = new Panel();
		mainPanel.add(newMailBtn);
		frame.add("Center", mainPanel);

		Panel subPanel = new Panel();
		subPanel.add(readMailBtn);
		subPanel.add(forwardMailBtn);
		subPanel.add(deleteMailBtn);
		frame.add("South", subPanel);

		// frame.addWindowListener(new CloseListener());
		frame.setSize(800, 200);
		frame.setLocation(100, 100);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	} // View()

	// Called from the Model
	public void update(Observable obs, Object obj) {

		// who called us and what did they send?
		// System.out.println ("View : Observable is " + obs.getClass() + ",
		// object passed is " + obj.getClass());

		// model Pull
		// ignore obj and ask model for value,
		// to do this, the view has to know about the model (which I decided I
		// didn't want to do)
		// uncomment next line to do Model Pull
		// myTextField.setText("" + model.getValue());

		// model Push
		// parse obj
		// myTextField.setText("" + ((Integer)obj).intValue()); //obj is an
		// Object, need to cast to an Integer

	} // update()

	public void addController(ActionListener controller, MouseListener e) {
		System.out.println("View      : adding controller");
		newMailBtn.addActionListener(controller); // need instance of controller
													// before can add it as a
													// listener
		readMailBtn.addActionListener(controller);
		table.addMouseListener(e);
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

	public void createMailFrame() {
		Font headerFont = new Font("Futura", Font.BOLD, 18);
		Font receiverFontFocus = new Font("Futura", Font.BOLD, 18);
		Font receiverFontNoFocus = new Font("Futura", Font.ITALIC, 18);

		JFrame newFrame = new JFrame();

		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		mainPanel.setOpaque(true);
		mainPanel.setBackground(new Color(66, 66, 66));

		JLabel headerLbl = new JLabel("New Message");
		headerLbl.setForeground(Color.WHITE);
		headerLbl.setFont(headerFont);

		JTextArea receiverTextArea = new JTextArea();

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

		JTextArea subjectTextArea = new JTextArea();
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

		JButton sendBtn = new JButton("Send");
		sendBtn.setBackground(new Color(59, 89, 182));
		sendBtn.setForeground(Color.WHITE);
		sendBtn.setFocusPainted(false);
		sendBtn.setFont(new Font("Tahoma", Font.BOLD, 12));

		Box box = new Box(BoxLayout.Y_AXIS);

		JTextArea messageTextArea = new JTextArea();
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

		newFrame.add("North", mainPanel);
		newFrame.add("Center", box);
		newFrame.add("South", btnPanel);
		newFrame.setSize(x, y);
		newFrame.setLocation(100, 100);
		newFrame.setVisible(true);
		messageTextArea.grabFocus();
		jScrollPane1.setMinimumSize(new Dimension(x, 200));
		jScrollPane1.setMaximumSize(new Dimension(x, 200));
		jScrollPane2.setMinimumSize(new Dimension(x, 200));
		jScrollPane2.setMaximumSize(new Dimension(x, 200));
		newFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ClientController.isMailEditorOpen = false;
				newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
	}

	public JTable getTable() {
		return table;
	}

} // View