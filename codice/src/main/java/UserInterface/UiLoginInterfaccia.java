package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface UiLoginInterfaccia extends Remote
{
	 void avvioLogin() throws RemoteException;
	 void avvioAggiornaUsername() throws RemoteException;
	 void avvioAggiornaPassword(Boolean loggato, String username, String Password) throws RemoteException;
}

