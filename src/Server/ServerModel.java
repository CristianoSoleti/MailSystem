package Server;
import java.util.Observable;


public class ServerModel extends Observable{

	private String[] columnNames = { "Client #", "Account", "Date" };
	private Object[][] data = new Object[5][3];

	public void setValue(Object[][] newData)
	{
		data = newData;
	}
	
	public Object[][] getData()
	{
		return data;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

}
