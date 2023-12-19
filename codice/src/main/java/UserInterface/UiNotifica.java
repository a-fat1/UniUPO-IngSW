package UserInterface;

import java.awt.*;
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
	private JLabel oraLabel;
	private JTextField dataField;
	private JTextField oraField;
	private JTextField testoField;
	private JPanel modificaNotificaPanel;
	
	public UiNotifica(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		gestoreNotifiche = (GestoreNotificheInterfaccia) registryGestore.lookup("gestoreNotifiche");


		dataField = new JTextField("", 20);
		dataLabel = new JLabel("Inserisci data di scadenza: ");
		dataField.setToolTipText("Data");

		oraField = new JTextField("", 20);
		oraLabel = new JLabel("Inserisci l'ora di scadenza: ");
		oraField.setToolTipText("Ora");

		testoField = new JTextField("", 50);
		testoLabel = new JLabel("Inserire testo notifica:");
		testoField.setToolTipText("Testo");


		modificaNotificaPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constrains = new GridBagConstraints();

		constrains.gridy=0;
		constrains.gridx=0;
		modificaNotificaPanel.add(dataLabel,constrains);

		constrains.gridy=0;
		constrains.gridx=2;
		constrains.gridwidth=1;
		modificaNotificaPanel.add(dataField,constrains);

		constrains.gridy=1;
		constrains.gridx=0;
		modificaNotificaPanel.add(oraLabel,constrains);

		constrains.gridy=1;
		constrains.gridx=2;
		constrains.gridwidth=1;
		modificaNotificaPanel.add(oraField,constrains);

		constrains.gridy=3;
		constrains.gridx=0;
		modificaNotificaPanel.add(testoLabel,constrains);

		constrains.gridy=3;
		constrains.gridx=2;
		constrains.gridwidth=5;
		constrains.ipady = 100;
		modificaNotificaPanel.add(testoField,constrains);
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
				this.mostraFormNotifica(testoNotifica);
				//if(gestoreNotifiche.verificaCorrettezzaDati())
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
				this.mostraFormNotifica(testoNotifica);
				break;
		}
	}

	/**
	 * RF04: Mostra il form delle notifiche.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	private void mostraFormNotifica(String testoNotifica) {

		this.showMessageDialog(null, modificaNotificaPanel,"Modifica notifica",this.OK_CANCEL_OPTION);
		dataField.setText("");
		oraField.setText("");
		testoField.setText("");
		dataField.setBackground(Color.WHITE);
		oraField.setBackground(Color.WHITE);
		testoField.setBackground(Color.WHITE);
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
				dataField.setBackground(Color.RED);
				oraField.setBackground(Color.WHITE);
				testoField.setBackground(Color.WHITE);
				messaggio = "La data fornita non è in formato YYYY-MM-DD.";
				this.showMessageDialog(null,messaggio, "errore",this.ERROR_MESSAGE);
				break;
			case "errore formato ora":
				dataField.setBackground(Color.WHITE);
				oraField.setBackground(Color.RED);
				testoField.setBackground(Color.WHITE);
				messaggio = "La ora fornita non è in formato HH:mm.";
				this.showMessageDialog(null,messaggio, "errore",this.ERROR_MESSAGE);
				break;
			case "errore data":
				dataField.setBackground(Color.RED);
				oraField.setBackground(Color.WHITE);
				testoField.setBackground(Color.WHITE);
				messaggio = "La data fornita non è compatibile con la data di pubblicazione.";
				this.showMessageDialog(null,messaggio, "errore",this.ERROR_MESSAGE);
				break;
			case "errore testo notifica":
				dataField.setBackground(Color.WHITE);
				oraField.setBackground(Color.WHITE);
				testoField.setBackground(Color.RED);
				messaggio = "Il testo della notifica non può essere vuoto.";
				this.showMessageDialog(null,messaggio, "errore",this.ERROR_MESSAGE);
				break;
		}
	}

	private HashMap<String, Object> setDataPubblicazione() {
		dataPubblicazione.put("data", LocalDate.now());
		dataPubblicazione.put("ora", LocalTime.now());

		return dataPubblicazione;
	}

}