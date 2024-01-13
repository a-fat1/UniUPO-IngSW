package UserInterface;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.util.EventObject;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import java.time.LocalTime;

import javax.swing.*;

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
	private HashMap<String, String> dataScadenza;
	private HashMap<String, String> dataPubblicazione;
	private String esitoVerifica;
	private String testoNotifica;


	// elementi grafici
	//RF04: genera notifica (Monfermoso, Magenta Biasina)
	private JLabel dataLabel;
	private JLabel oraLabel;
	private JLabel testoLabel;
	private JTextField dataField;
	private JTextField oraField;
	private JTextArea testoField;
	private JPanel modificaNotificaPanel;
	// RF01 Galletti-Calcaterra
	JPanel pannello = new JPanel(); // Nuova finestra
	JScrollPane pannello_tabella;


	public UiNotifica(String hostGestore) throws RemoteException, NotBoundException
	{

		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

		gestoreNotifiche = (GestoreNotificheInterfaccia) registryGestore.lookup("gestoreNotifiche");

		inizializzaUIRF04();
	}

	public void avvioVisualizzaNotifiche(String tipoUtente) throws RemoteException
	{ // RF01 Galletti-Calcaterra
		int id;
		String testo, tipodiUtente, dataPubblicazione, dataScadenza;
		ArrayList<HashMap<String, Object>> lista = null;
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// Formattare la data utilizzando il formato personalizzato
		String formattedDate = myDateObj.format(formatter);
		lista = gestoreNotifiche.ricercaNotifiche(tipoUtente, formattedDate);
		int dim = lista.size();
		if (dim == 0) {
			JLabel messaggio = new JLabel("Nessuna notifica");
			pannello.add(messaggio);
			this.showMessageDialog(pannello, messaggio);
		} else {
			JTable tabella = creaTabella(dim);
			tabella=inserisciDatiTabella(tabella, lista, dim);
			pannello_tabella = new JScrollPane(tabella);
			pannello_tabella.setPreferredSize(new Dimension(800, 250));
			this.showMessageDialog(null, pannello_tabella);
		}
	}
	private JTable creaTabella(int dim) { // RF01 Galletti-Calcaterra
		JTable tabella = new JTable(dim, 4){
			@Override
			public boolean editCellAt(int row, int column, EventObject e) {
				return false;
			}
		};
		tabella.getColumnModel().getColumn(0).setHeaderValue("Data");
		tabella.getColumnModel().getColumn(0).setPreferredWidth(150);
		tabella.getColumnModel().getColumn(1).setHeaderValue("Contenuto");
		tabella.getColumnModel().getColumn(1).setPreferredWidth(400);
		tabella.getColumnModel().getColumn(2).setHeaderValue("Destinatario");
		tabella.getColumnModel().getColumn(2).setPreferredWidth(100);
		tabella.getColumnModel().getColumn(3).setHeaderValue("Scadenza");
		tabella.getColumnModel().getColumn(3).setPreferredWidth(150);
		tabella.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return tabella;
	}

	// RF01 Galletti-Calcaterra
	private JTable inserisciDatiTabella(JTable tabella, ArrayList<HashMap<String, Object>> lista, int dim){
		for (int i = 0; i < dim; i++) {
			String testo = (String) lista.get(i).get("testo");
			String dataPubblicazione = (String) lista.get(i).get("dataPubblicazione");
			String dataScadenza = (String) lista.get(i).get("dataScadenza");
			String tipodiUtente = (String) lista.get(i).get("tipoUtente");

			tabella.setValueAt(dataPubblicazione, i, 0);
			tabella.setValueAt(testo, i, 1);
			tabella.setValueAt(tipodiUtente, i, 2);
			tabella.setValueAt(dataScadenza, i, 3);
		}
		return tabella;
	}

	/**
	 * RF04: Inizializza gli attributi per generare la finestra di dialogo di avvioGeneraNotifica().
	 *
	 * @author Linda Monfermoso, Gabriele Magenta Biasina
	 */
	public void inizializzaUIRF04() {
		dataPubblicazione = new HashMap<>();
		dataScadenza = new HashMap<>();

		dataField = new JTextField("", 10);
		dataLabel = new JLabel("Inserire data scadenza: ");
		dataField.setToolTipText("formato YYYY-MM-DD");

		oraField = new JTextField("", 10);
		oraLabel = new JLabel("Inserire ora scadenza: ");
		oraField.setToolTipText("formato HH:mm:ss");

		testoField = new JTextArea(5, 30);
		testoField.setLineWrap(true);
		testoLabel = new JLabel("Inserire testo notifica:");
		testoField.setToolTipText("testo");

		modificaNotificaPanel = new JPanel();

		modificaNotificaPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;
		modificaNotificaPanel.add(dataLabel, gbc);
		gbc.gridx++;
		modificaNotificaPanel.add(dataField, gbc);
		gbc.gridx++;
		modificaNotificaPanel.add(oraLabel, gbc);
		gbc.gridx++;
		modificaNotificaPanel.add(oraField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		modificaNotificaPanel.add(testoLabel, gbc);

		gbc.gridx++;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		modificaNotificaPanel.add(testoField, gbc);

		(testoField).setBorder(new JTextField().getBorder());
	}

	// RF04
	public void avvioGeneraNotifica(String tipoNotifica, HashMap<String, Object> oggetto) throws RemoteException {
		switch (tipoNotifica) {
			case "nuovo prodotto":
				testoNotifica = gestoreNotifiche.generaTestoNotificaProdotto(oggetto);
				loopVerificaDatiNotifica(tipoNotifica);
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), dataScadenza, testoField.getText(), "cliente");
				break;
			case "avviso":
				testoNotifica = gestoreNotifiche.generaTestoNotificaAvviso();
				loopVerificaDatiNotifica(tipoNotifica);
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), dataScadenza, testoField.getText(), "tutti");
				break;
			case "nuovo ordine":
				testoNotifica = gestoreNotifiche.generaTestoNotificaOrdine(oggetto);
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), setDataScadenzaDefault(), testoNotifica, "staff");
				break;
			case "nuovo utente":
				testoNotifica = gestoreNotifiche.generaTestoNotificaUtente(oggetto);
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), setDataScadenzaDefault(), testoNotifica, "amministratore");
				break;
			default:
				throw new IllegalStateException("Valore inatteso: " + tipoNotifica);
		}
	}

	/**
	 * RF04: Metodo per evitare duplicazione del codice.
	 *
	 * @author Linda Monfermoso, Gabriele Magenta Biasina
	 * @throws RemoteException
	 */
	private void loopVerificaDatiNotifica(String tipoNotifica) throws RemoteException {
		do {
			this.mostraFormNotifica(this.testoNotifica);
			this.testoNotifica = testoField.getText();
			esitoVerifica = gestoreNotifiche.verificaCorrettezzaDati(this.dataScadenza.get("data"), this.dataScadenza.get("ora"), this.testoNotifica);
			if (esitoVerifica.contains("errore")) {
				mostraErrore(esitoVerifica);
			}
			if(tipoNotifica.contains("prodotto")){
				mostraErrore(tipoNotifica);
			}

		} while (!Objects.equals(esitoVerifica, "ok"));
	}

	/**
	 * RF04: Mostra il form delle notifiche, contenente i campi "data", "ora" e "testo notifica".
	 *
	 * @author Linda Monfermoso, Gabriele Magenta Biasina
	 */
	private void mostraFormNotifica(String testoNotifica) {
		testoField.setText(testoNotifica);

		this.showMessageDialog(null, modificaNotificaPanel, "Modifica notifica", this.QUESTION_MESSAGE);

		dataField.setBackground(Color.WHITE);
		oraField.setBackground(Color.WHITE);
		testoField.setBackground(Color.WHITE);

		dataScadenza.put("data", dataField.getText());
		dataScadenza.put("ora", oraField.getText());
	}

	public void avvioRicercaNotifiche() throws RemoteException
	{ 	// RF21	
	}

	/**
	 * RF04: Mostra una schermata di errore contenente informazioni riguardo all'errore riscontrato durante la generazione di una notifica.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	private void mostraErrore(String tipoErrore) {
		String messaggio = "";
	// YELLOW per quanto riguarda errori di inserimento/sintassi
	// RED per quanto riguarda il mancato inserimento
		switch(tipoErrore) {
			case "errore prodotto":
				messaggio="non è possibile annullare l'inserimento, e' necessario compilare tutti i campi!";
				dataField.setBackground(Color.RED);
				oraField.setBackground(Color.RED);
				testoField.setBackground(Color.RED);
				break;
			case "errore formato data":
				messaggio = "La data fornita non e' in formato YYYY-MM-DD.\n(clicca ok o X per continuare)";
				dataField.setBackground(Color.YELLOW);
				break;
			case "errore formato ora":
				messaggio = "L'ora fornita non e' in formato HH:mm:ss.\n(clicca ok o X per continuare)";
				oraField.setBackground(Color.YELLOW);
				break;
			case "errore data":
				messaggio = "La data fornita non e' compatibile con la data di pubblicazione.\n(clicca ok o X per continuare)";
				dataField.setBackground(Color.YELLOW);
				break;
			case "errore testo notifica":
				messaggio = "Il testo della notifica non può essere vuoto.\n(clicca ok o X per continuare)";
				testoField.setBackground(Color.RED);
				break;

		}
		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE, null);
	}

	/**
	 * RF04: Imposta la data di pubblicazione di una notifica.
	 *
	 * @return la data entro la quale verrà pubblicata la notifica (la data corrente)
	 */
	private HashMap<String, String> setDataPubblicazione() {
		dataPubblicazione.put("data", LocalDate.now().toString());
		dataPubblicazione.put("ora", LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());

		return dataPubblicazione;
	}

	/**
	 * RF04: Imposta la data di scadenza di una notifica, richiesto dai metodi generaTestoNotificaAvviso e generaTestoNotificaUtente.
	 *
	 * @return la data entro la quale la notifica scadrà (un giorno dalla data di pubblicazione)
	 */
	private HashMap<String, String> setDataScadenzaDefault() {
		dataScadenza.put("data", LocalDate.now().plusDays(1).toString());
		dataScadenza.put("ora", LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());

		return dataScadenza;
	}

}