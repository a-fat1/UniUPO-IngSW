package UserInterface;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException; 
import java.rmi.NotBoundException;

public interface UiUtenteInterfaccia extends Remote
{
<<<<<<< HEAD
=======
	//RF02
>>>>>>> c5c81cbeb1a340c9b454f92c7fb0e16de334b2dc
	void avvioCreaUtente(boolean cliente) throws RemoteException, NotBoundException;
	void avvioBloccaSbloccaUtente() throws RemoteException;
	void avvioAggiornaDomicilio() throws RemoteException;
}

