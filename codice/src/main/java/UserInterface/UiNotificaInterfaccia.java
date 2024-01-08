package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.HashMap;

public interface UiNotificaInterfaccia extends Remote
{
	void avvioVisualizzaNotifiche(String tipoUtente) throws RemoteException;

	/**
	 * RF04: Avvia la generazione di una notifica.
	 *
	 * @author Linda Monfermoso, Gabriele Magenta Biasina
	 * @param tipoNotifica il tipo di notifica da generare (avviso, nuovo utente, nuovo prodotto, nuovo ordine)
	 * @param oggetto l'oggetto (utente, prodotto, ordine) per generare la notifica
	 * @throws RemoteException
	 */
	void avvioGeneraNotifica(String tipoNotifica, HashMap<String, Object> oggetto) throws RemoteException;
	void avvioRicercaNotifiche() throws RemoteException;
}

