package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.HashMap;

public interface UiNotificaInterfaccia extends Remote
{
	void avvioVisualizzaNotifiche(String tipoUtente) throws RemoteException;
	void avvioGeneraNotifica() throws RemoteException;
	void avvioVisualizzaNotifiche() throws RemoteException;
	void avvioGeneraNotifica(String tipoNotifica, HashMap<String, Object> prodotto, HashMap<String, Object> ordine, HashMap<String, Object> utente) throws RemoteException;
	void avvioRicercaNotifiche() throws RemoteException;
}

