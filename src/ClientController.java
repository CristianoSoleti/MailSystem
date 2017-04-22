import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

/*
 * Controller extends action listener cause he needs to perform actions on events like click.
 */
class ClientController implements ActionListener, MouseListener {

	public String[] columnNames = { "Type", "Source", "Subject", "Date" };
	public Object[][] data;
	/*
	 * reference to client model
	 */
	ClientModel model;
	/*
	 * reference to client view
	 */
	ClientView view;

	ClientController() {
		System.out.println("Controller()");
	}

	// invoked when a button is pressed
	public void actionPerformed(ActionEvent e) {

		System.out.println("Controller: The " + e.getActionCommand() + " button is clicked");
		switch(e.getActionCommand())
		{
		case "Create":
			System.out.println("Controller: Open Mail Editor");
			break;
			
		case "Read":
			System.out.println("Controller: Reading Mail");
			break;
			
		default:
			break;
		}
		// model.incrementValue();

	}

	/*
	 * mouse listener for clicking events
	 */
	public void addMouseListener(MouseEvent e) {
	}

	public void addModel(ClientModel m) {
		System.out.println("Controller: adding model");
		this.model = m;
	}

	public void addView(ClientView v) {
		System.out.println("Controller: adding view");
		this.view = v;
	}

	/*
	 * setting datas from method
	 */
	public void setData(Object a[][]) {
		data = a;
	}

	/*
	 * making table not editable.(If you click on the table GUI it won't let you
	 * change anything)
	 */
	@SuppressWarnings("serial")
	public void refreshViewTableData() {

		view.getTable().setModel(new DefaultTableModel(data, columnNames) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Controller: Opening mail at row " + view.getTable().getSelectedRow());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}