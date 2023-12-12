package Elaborazione;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import DataBase.*;

public class GestoreNotifiche implements GestoreNotificheInterfaccia
{
	private Registry registry;
	private DbNotificheInterfaccia dbNotifiche;

	public GestoreNotifiche(String host) throws RemoteException, NotBoundException
	{
		registry = LocateRegistry.getRegistry(host, 1098); 
		dbNotifiche = (DbNotificheInterfaccia) registry.lookup("dbNotifiche");
	}

	public GestoreNotifiche(DbNotifiche d1) // per testing
	{
		dbNotifiche = d1;
	}

	/**
	 * RF04: Genera il testo della notifica per un nuovo prodotto.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	public String generaTestoNotificaProdotto(HashMap<String, Object> object) {
		String testoNotifica = "";


		return testoNotifica;
	}

	/**
	 * RF04: Genera il testo della notifica per un nuovo ordine.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	public String generaTestoNotificaOrdine(HashMap<String, Object> object) {
		String testoNotifica = "";

		return testoNotifica;
	}

	/**
	 * RF04: Genera il testo della notifica per un avviso.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	public String generaTestoNotificaAvviso(HashMap<String, Object> object) {
		String testoNotifica = "";

		return testoNotifica;
	}

	/**
	 * RF04: Genera il testo della notifica per un nuovo utente.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	public String generaTestoNotificaUtente(HashMap<String, Object> object) {
		String testoNotifica = "";

		return testoNotifica;
	}

	/**
	 * RF04: Inserisce una notifica nel database.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	public void inserimentoNotifica() {

	}
}
