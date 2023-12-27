package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException; 
import java.rmi.NotBoundException;

public interface UiLoginInterfaccia extends Remote
{
	 void avvioLogin() throws RemoteException, NotBoundException;
	 void avvioAggiornaUsername() throws RemoteException;
	 void avvioAggiornaPassword(Boolean loggato, String username, String Password) throws RemoteException;
}

