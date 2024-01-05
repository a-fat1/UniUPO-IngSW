package UserInterface;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException; 

public interface UiLoginInterfaccia extends Remote
{
	 void avvioLogin() throws RemoteException, NotBoundException;
	 void avvioAggiornaUsername() throws RemoteException;
	 void avvioAggiornaPassword(Boolean loggato, String username, String Password) throws RemoteException;
}

