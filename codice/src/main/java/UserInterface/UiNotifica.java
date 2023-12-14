package UserInterface;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;

import UserInterface.*;
import Elaborazione.*;

public class UiNotifica extends JOptionPane implements UiNotificaInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private GestoreNotificheInterfaccia gestoreNotifiche;

	// attributi
	// RF04: genera notifica (Monfermoso, Magenta Biasina)
	private HashMap<String, Object> utente;
	private HashMap<String, Object> prodotto;
	private HashMap<String, Object> ordine;
	private HashMap<String, Object> dataScadenza;
	private HashMap<String, Object> dataPubblicazione;
	private String esitoVerifica;

	// elementi grafici
	//RF04: genera notifica (Monfermoso, Magenta Biasina)
	private JLabel dataLabel;
	private JLabel testoLabel;
	private JTextField dataField;
	private JTextField testoField;
	private JPanel modificaNotificaPanel;
	
	public UiNotifica(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		gestoreNotifiche = (GestoreNotificheInterfaccia) registryGestore.lookup("gestoreNotifiche"); 
	}
	
	public void avvioVisualizzaNotifiche() throws RemoteException
	{ 	// RF01
	}

	/**
	 * RF04: Avvia la generazione di una notifica
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	public void avvioGeneraNotifica(String tipoNotifica, HashMap<String, Object> prodotto, HashMap<String, Object> ordine, HashMap<String, Object> utente) throws RemoteException
	{
		String testoNotifica = "";

		switch (tipoNotifica) {
			case "nuovo prodotto":
				testoNotifica = gestoreNotifiche.generaTestoNotificaProdotto(prodotto);
				mostraFormNotifica(testoNotifica);
				break;
			case "nuovo ordine":
				testoNotifica = gestoreNotifiche.generaTestoNotificaOrdine(ordine);
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), dataScadenza, testoNotifica, utente.get("tipoUtente").toString());
				break;
			case "nuovo utente":
				testoNotifica = gestoreNotifiche.generaTestoNotificaUtente(utente);
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), dataScadenza, testoNotifica, utente.get("tipoUtente").toString());
				break;
			case "avviso":
				testoNotifica = gestoreNotifiche.generaTestoNotificaAvviso();
				mostraFormNotifica(testoNotifica);
				break;
		}
	}

	/**
	 * RF04: Mostra il form delle notifiche.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	private void mostraFormNotifica(String testoNotifica) {

	}

	public void avvioRicercaNotifiche() throws RemoteException
	{ 	// RF21	
	}

	/**
	 * RF04: Mostra un errore.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	private void mostraErrore(String tipoErrore) {
		String messaggio = "";

		switch(tipoErrore) {
			case "errore formato data":
				messaggio = "La data fornita non è in formato YYYY-MM-DD.";
				break;
			case "errore formato ora":
				messaggio = "La ora fornita non è in formato HH:mm.";
				break;
			case "errore data":
				messaggio = "La data fornita non è compatibile con la data di pubblicazione.";
				break;
			case "errore testo notifica":
				messaggio = "Il testo della notifica non può essere vuoto.";
				break;
		}
	}

	private HashMap<String, Object> setDataPubblicazione() {
		dataPubblicazione.put("data", LocalDate.now());
		dataPubblicazione.put("ora", LocalTime.now());

		return dataPubblicazione;
	}

}
