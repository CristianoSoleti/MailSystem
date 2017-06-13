package Remote;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import MailSystemUtilities.Email;

public interface RequestsInterface extends Remote {
	public void send(ArrayList<Email> msg,ClientImpl client) throws RemoteException;
	public void delete(Client c,int index) throws RemoteException;
	public void setClient(Client c)throws RemoteException;
	public void clientConnectionWelcome(Client c) throws RemoteException;
	public ArrayList<Email> requestUserMailList(Client c) throws RemoteException;
	public void destroyClient(Client c) throws RemoteException;
}
