package Remote;

import java.awt.HeadlessException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import MailSystemUtilities.Email;

public interface RequestsInterface extends Remote {
	public String getName() throws RemoteException;
	public void send(Email msg) throws RemoteException;
	public void delete(ClientImpl c,int index) throws RemoteException;

	public void setClient(ClientImpl c)throws RemoteException;
	public void clientConnectionWelcome(ClientImpl c) throws RemoteException;
	public ArrayList<Email> requestUserMailList(ClientImpl c) throws RemoteException;
	public void destroyClient(ClientImpl c) throws RemoteException;
}
