package UserInterface;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException; 
import java.rmi.NotBoundException;

public interface UiUtenteInterfaccia extends Remote
{
	//RF02
	void avvioCreaUtente(boolean cliente) throws RemoteException, NotBoundException;
	void avvioBloccaSbloccaUtente() throws RemoteException;
	void avvioAggiornaDomicilio() throws RemoteException;
}

