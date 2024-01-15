package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.HashMap;

public interface UiNotificaInterfaccia extends Remote
{
	void avvioVisualizzaNotifiche(String tipoUtente) throws RemoteException;
	//metodo per avviare la visualizzazione delle notifiche

	void avvioGeneraNotifica(String tipoNotifica, HashMap<String, Object> oggetto) throws RemoteException;
	//metodo per avviare la generazione della notifica

	void avvioRicercaNotifiche() throws RemoteException;
	//metodo per avviare la  ricerca delle notifiche
}

