package UserInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UiUtenteInterfaccia extends Remote
{
	void avvioCreaUtente() throws RemoteException;
	void avvioBloccaSbloccaUtente(String username, boolean attivo) throws RemoteException;
	void avvioAggiornaDomicilio() throws RemoteException;
	
}
