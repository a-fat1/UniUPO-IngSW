package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface UiCarrelloInterfaccia extends Remote
{
	void avvioVisualizzaCarrello() throws RemoteException;
	void avvioEffettuaOrdine() throws RemoteException;
	void avvioRimuoviProdottiDalCarrello() throws RemoteException;
	void avvioAggiungiAlCarrello() throws RemoteException;
}

