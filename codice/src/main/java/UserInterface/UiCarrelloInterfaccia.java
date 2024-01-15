package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap; 

public interface UiCarrelloInterfaccia extends Remote
{
	// RF05: Visualizza Carrello
	// autori: Bossola Francesco, Oppezzo Raul
	void avvioVisualizzaCarrello(String username) throws RemoteException;

	void avvioEffettuaOrdine() throws RemoteException;
	
	
    // RF07: rimuovi prodotto dal carrello
 	// autori: Simone Aldo Borsa, Andrea Padoan
	void avvioRimuoviProdottiDalCarrello(boolean tipoDiRimozione, String username, ArrayList<HashMap<String, Object>> carrello, HashMap<String, Object> prodotto) throws RemoteException;
	
	
	// RF09 : aggiunta al carrello
	// Autori: Fasano Lorenzo, Iacobucci Luca
	public void avvioAggiungiAlCarrello(String username, int codiceProdotto) throws RemoteException;
	public void mostraErr(int inputEr) throws RemoteException;

}

