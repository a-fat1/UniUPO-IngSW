package UserInterface;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UiLoginInterfaccia extends Remote
{
	 void avvioLogin() throws RemoteException, NotBoundException;
	 void avvioAggiornaUsername(String vecchioUsername) throws RemoteException;
	 String avvioAggiornaPassword(Boolean loggato, String username, String Password) throws RemoteException;
}

