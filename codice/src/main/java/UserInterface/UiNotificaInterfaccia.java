package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface UiNotificaInterfaccia extends Remote
{
	void avvioVisualizzaNotifiche(String tipoUtente) throws RemoteException;
	void avvioGeneraNotifica() throws RemoteException;
	void avvioRicercaNotifiche() throws RemoteException;
}

