package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.HashMap;

public interface UiNotificaInterfaccia extends Remote
{
	void avvioVisualizzaNotifiche(String tipoUtente) throws RemoteException;

	void avvioGeneraNotifica(String tipoNotifica, HashMap<String, Object> oggetto) throws RemoteException;

	void avvioRicercaNotifiche() throws RemoteException;
}

