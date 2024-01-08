package UserInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public interface UiUtenteInterfaccia extends Remote
{
	void avvioCreaUtente(boolean cliente) throws RemoteException, NotBoundException;
	void avvioBloccaSbloccaUtente(String username, boolean attivo) throws RemoteException;
	void avvioAggiornaDomicilio() throws RemoteException;
}
