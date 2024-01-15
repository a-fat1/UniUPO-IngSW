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
import javax.swing.table.DefaultTableModel;

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
	
	// RF21: RicercaNotifiche (Caviggia, Colombo)
	private String dataPubblicazioneRF21;
	private String dataScadenzaRF21;
	private String tipoUtente = "";
	private ArrayList<HashMap<String, Object>> notifica;
	private String esitoControllo;
	private int sceltaRF21;


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
	
	// RF21: RicercaNotifiche (Caviggia, Colombo)
	private JLabel titoloLabel;
	private JLabel dataPubblicazioneLabel;
	private JLabel dataScadenzaLabel;
	private JTextField dataPubblicazioneField;
	private JTextField dataScadenzaField;
	private JLabel tipoUtenteLabel;
	private JButton cercaButton;
	private JPanel mostraFormRicercaNotifichePanel;
	private JCheckBox[] check;
	private JScrollPane scrollTable;


	public UiNotifica(String hostGestore) throws RemoteException, NotBoundException
	{

		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

		gestoreNotifiche = (GestoreNotificheInterfaccia) registryGestore.lookup("gestoreNotifiche");

		inizializzaUIRF04();
		
		// RF21: RicercaNotifiche (Caviggia, Colombo)
		inizializzaUIRF21();
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

		//pannello di modifica notifica
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

	/**
	 * RF04: Avvia la generazione di una notifica.
	 *
	 * @author Linda Monfermoso, Gabriele Magenta Biasina
	 * @param tipoNotifica il tipo di notifica da generare (avviso, nuovo utente, nuovo prodotto, nuovo ordine)
	 * @param oggetto l'oggetto (utente, prodotto, ordine) per generare la notifica
	 * @throws RemoteException
	 */
	public void avvioGeneraNotifica(String tipoNotifica, HashMap<String, Object> oggetto) throws RemoteException {
		int scelta;

		switch (tipoNotifica) {
            case "nuovo prodotto": //nuovo prodotto
                testoNotifica = gestoreNotifiche.generaTestoNotificaProdotto(oggetto);
				do {
					do {
						scelta = this.mostraFormNotifica(this.testoNotifica);
						if(scelta == -1)
							mostraErrore("errore uscita");
					} while (scelta != 0);
					this.testoNotifica = testoField.getText();
					esitoVerifica = gestoreNotifiche.verificaCorrettezzaDati(this.dataScadenza.get("data"), this.dataScadenza.get("ora"), this.testoNotifica);
					if (esitoVerifica.contains("errore")) {
						mostraErrore(esitoVerifica);
					}
				} while (!Objects.equals(esitoVerifica, "ok"));
                gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), dataScadenza, testoField.getText(), "cliente");
				// se la notifica è stata inserita correttamente nel database, mostra messaggio di conferma
				this.showMessageDialog(null,"Notifica inserita correttamente nel database!", "Avviso", this.INFORMATION_MESSAGE);
	    	break;
            
            case "avviso"://nuovo avviso
                testoNotifica = gestoreNotifiche.generaTestoNotificaAvviso();
				do {
					scelta = this.mostraFormNotifica(this.testoNotifica);
					if(scelta == -1)
						return;
					this.testoNotifica = testoField.getText();
					esitoVerifica = gestoreNotifiche.verificaCorrettezzaDati(this.dataScadenza.get("data"), this.dataScadenza.get("ora"), this.testoNotifica);
					if (esitoVerifica.contains("errore")) {
						mostraErrore(esitoVerifica);
					}
				} while (!Objects.equals(esitoVerifica, "ok"));
                gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), dataScadenza, testoField.getText(), "tutti");
				// se la notifica è stata inserita correttamente nel database, mostra messaggio di conferma
				this.showMessageDialog(null,"Notifica inserita correttamente nel database!", "Avviso", this.INFORMATION_MESSAGE);
            break;
            case "nuovo ordine"://nuovo ordine creato
                testoNotifica = gestoreNotifiche.generaTestoNotificaOrdine(oggetto);
                gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), setDataScadenzaDefault(), testoNotifica, "staff");
            break;
            case "nuovo utente"://nuova utenza
                testoNotifica = gestoreNotifiche.generaTestoNotificaUtente(oggetto);
                gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), setDataScadenzaDefault(), testoNotifica, "amministratore");
            break;
        }
	}

	/**
	 * RF04: Mostra il form delle notifiche, contenente i campi "data", "ora" e "testo notifica".
	 *
	 * @author Linda Monfermoso, Gabriele Magenta Biasina
	 */
	private int mostraFormNotifica(String testoNotifica) {// metodo che moostra il form della notifica
		testoField.setText(testoNotifica);

		int scelta = this.showConfirmDialog(null, modificaNotificaPanel, "Modifica notifica", this.DEFAULT_OPTION, this.QUESTION_MESSAGE, null);

		dataField.setBackground(Color.WHITE);
		oraField.setBackground(Color.WHITE);
		testoField.setBackground(Color.WHITE);

		dataScadenza.put("data", dataField.getText());
		dataScadenza.put("ora", oraField.getText());

		return scelta;
	}

	/**
	 * RF04: Mostra una schermata di errore contenente informazioni riguardo all'errore riscontrato durante la generazione di una notifica.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	private void mostraErrore(String tipoErrore) {
		String messaggio = "";
		switch (tipoErrore) {
            case "errore formato data"://formato data errato
                messaggio = "La data fornita non e' in formato YYYY-MM-DD.\n(clicca OK o X per continuare)";
                dataField.setBackground(Color.YELLOW);
            	break;
            case "errore formato ora"://formato ora errato
                messaggio = "L'ora fornita non e' in formato HH:mm:ss.\n(clicca OK o X per continuare)";
                oraField.setBackground(Color.YELLOW);
            	break;
            case "errore data"://data non compatibile con la data di pubblicazione
                messaggio = "La data fornita non e' compatibile con la data di pubblicazione.\n(clicca OK o X per continuare)";
                dataField.setBackground(Color.YELLOW);
            	break;
            case "errore testo notifica"://testo notifica vuoto
                messaggio = "Il testo della notifica non può essere vuoto.\n(clicca OK o X per continuare)";
                testoField.setBackground(Color.RED);
            	break;
			case "errore uscita"://quando viene premuto X e il tipo della notifica è prodotto
				messaggio = "Non è possibile annullare la generazione di una notifica per un nuovo prodotto.\n(clicca OK o X per continuare)";
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
	
	
	public void inizializzaUIRF21(){
		// RF21: RicercaNotifiche (Caviggia, Colombo)

		titoloLabel = new JLabel("Inserire data di pubblicazione, data di scadenza e tipo utente");
		dataPubblicazioneLabel = new JLabel("Data di pubblicazione (aaaa-mm-gg): ");
		dataScadenzaLabel = new JLabel("Data di scadenza (aaaa-mm-gg): ");
		dataPubblicazioneField = new JTextField("" ,10);
		dataScadenzaField = new JTextField("", 10);
		tipoUtenteLabel = new JLabel("Tipo utente: ");
		cercaButton = new JButton("Cerca");
		check = new JCheckBox[4];
		check[0] = new JCheckBox("tutti");
		check[1] = new JCheckBox("amministratore");
		check[2] = new JCheckBox("cliente");
		check[3] = new JCheckBox("staff");
		mostraFormRicercaNotifichePanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		GridBagConstraints constraintsForField = new GridBagConstraints();

		constraints.anchor = GridBagConstraints.WEST;
		constraintsForField.anchor = GridBagConstraints.EAST;
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.insets = new Insets(0, 0, 10, 0);
		mostraFormRicercaNotifichePanel.add(titoloLabel, constraints);
		constraints.gridy = 2;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, 0, 0);
		mostraFormRicercaNotifichePanel.add(dataPubblicazioneLabel, constraints);
		constraintsForField.gridy = 2;
		constraintsForField.gridx = 1;
		constraints.insets = new Insets(0, 0, 0, 0);
		mostraFormRicercaNotifichePanel.add(dataPubblicazioneField, constraintsForField);
		constraints.gridy = 3;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, 10, 0);
		mostraFormRicercaNotifichePanel.add(dataScadenzaLabel, constraints);
		constraintsForField.gridy = 3;
		constraintsForField.gridx = 1;
		constraints.insets = new Insets(0, 0, 10, 0);
		mostraFormRicercaNotifichePanel.add(dataScadenzaField, constraintsForField);
		constraints.gridy = 5;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, 0, 0);
		mostraFormRicercaNotifichePanel.add(tipoUtenteLabel, constraints);
		constraints.gridy = 6;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, 0, 0);
		mostraFormRicercaNotifichePanel.add(check[0], constraints);
		constraints.gridy = 7;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, 0, 0);
		mostraFormRicercaNotifichePanel.add(check[1], constraints);
		constraints.gridy = 8;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, 0, 0);
		mostraFormRicercaNotifichePanel.add(check[2], constraints);
		constraints.gridy = 9;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, 0, 0);
		mostraFormRicercaNotifichePanel.add(check[3], constraints);
	}
	
	private void mostraFormRicercaNotifiche() throws RemoteException {
		// RF21: RicercaNotifiche (Caviggia, Colombo)
		sceltaRF21 = JOptionPane.OK_OPTION;
		dataPubblicazioneRF21 = "";
		dataScadenzaRF21 = "";
		tipoUtente = "";
		sceltaRF21 = showConfirmDialog(null, mostraFormRicercaNotifichePanel, "Ricerca notifiche", JOptionPane.OK_CANCEL_OPTION);
		dataPubblicazioneRF21 = dataPubblicazioneField.getText();
		dataScadenzaRF21 = dataScadenzaField.getText();


		for(int i = 0; i < 4; i++){
			if(check[i].isSelected()){
				tipoUtente = check[i].getText();
			}
		}

		if(sceltaRF21 == CANCEL_OPTION)
			return;

		if(tipoUtente.isEmpty())
			mostraErroreRF21("Selezionare un tipo utente!");
	
	}
	
	public void avvioRicercaNotifiche() throws RemoteException
	{
		// RF21: RicercaNotifiche (Caviggia, Colombo)
		do {
			mostraFormRicercaNotifiche();
			if(sceltaRF21 == CANCEL_OPTION || sceltaRF21 == CLOSED_OPTION)
				break;
			esitoControllo = gestoreNotifiche.controlloParametri(dataPubblicazioneRF21, dataScadenzaRF21);
			if(esitoControllo.equals("Date non valide!") || esitoControllo.equals("Formato data non valido!") || esitoControllo.equals("Manca una data!"))
				this.mostraErroreRF21(esitoControllo);
			else{
				notifica = gestoreNotifiche.cercaNotifiche(dataPubblicazioneRF21, dataScadenzaRF21, tipoUtente);
				System.out.println(notifica);
				if(notifica.isEmpty()){
					this.mostraErroreRF21("Nessuna notifica trovata!");
				}
				else
					this.mostraNotificheRF21(notifica);
			}
		} while(sceltaRF21 == OK_OPTION && ( esitoControllo.equals("Date non valide!") || esitoControllo.equals("Formato data non valido!") || esitoControllo.equals("Manca una data!") || notifica.isEmpty()));
	
	}
	
	private void mostraErroreRF21(String esitoControllo) {
		// RF21: RicercaNotifiche (Caviggia, Colombo)
		String messaggio = "";
		switch (esitoControllo) {
			case "Date non valide!":
				messaggio = "La data di pubblicazione non può essere successiva alla data di scadenza!";
				break;
			case "Formato data non valido!":
				messaggio = "Formato data non valido!";
				break;
			case "Manca una data!":
				messaggio = "Manca una data!";
				break;
			case "Nessuna notifica trovata!":
				messaggio = "Nessuna notifica trovata!";
				break;
			case "Selezionare un tipo utente!":
				messaggio = "Selezionare un tipo utente!";
				break;
		}

		showMessageDialog(null, messaggio, "Errore", JOptionPane.ERROR_MESSAGE, null);
	}

	public void mostraNotificheRF21(ArrayList<HashMap<String, Object>> notifica){
        	// RF21: RicercaNotifiche (Caviggia, Colombo)
        	int dim = notifica.size();
        	String[] columnNames = {"Data pubblicazione", "Data scadenza", "Tipo utente", "Testo"};
        	DefaultTableModel model = new DefaultTableModel(dim, columnNames.length);
        	model.setColumnIdentifiers(columnNames);

        	for (int i = 0; i < dim; i++) {
            		String dataPubblicazione = (String) notifica.get(i).get("dataPubblicazione");
            		String dataScadenza = (String) notifica.get(i).get("dataScadenza");
            		String tipoUtente = (String) notifica.get(i).get("tipoUtente");
           		 String testo = (String) notifica.get(i).get("testo");

           		 model.setValueAt(dataPubblicazione, i, 0);
           		 model.setValueAt(dataScadenza, i, 1);
            		model.setValueAt(tipoUtente, i, 2);
           		 model.setValueAt(testo, i, 3);
        	}

      	  	JTable tabellaRF21 = new JTable(model);
       	 	tabellaRF21.getColumnModel().getColumn(0).setPreferredWidth(200);
        	tabellaRF21.getColumnModel().getColumn(1).setPreferredWidth(200);
        	tabellaRF21.getColumnModel().getColumn(2).setPreferredWidth(100);
        	tabellaRF21.getColumnModel().getColumn(3).setPreferredWidth(300);
        	tabellaRF21.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        	scrollTable = new JScrollPane(tabellaRF21);
        	scrollTable.setPreferredSize(new Dimension(800, 200));
        	showMessageDialog(null, scrollTable, "Notifiche trovate", JOptionPane.INFORMATION_MESSAGE, null);
    }



}
