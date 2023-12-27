package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface UiUtenteInterfaccia extends Remote
{
	//RF02
	void avvioCreaUtente(boolean cliente) throws RemoteException;
	void avvioBloccaSbloccaUtente() throws RemoteException;
	void avvioAggiornaDomicilio() throws RemoteException;
}

