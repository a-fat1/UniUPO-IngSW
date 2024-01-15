package UserInterface;

/*RF09*/
import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;

import UserInterface.*;
import Elaborazione.*;

public class UiCarrello extends JOptionPane implements UiCarrelloInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private UiNotificaInterfaccia uiNotifica;
	private GestoreCarrelliInterfaccia gestoreCarrelli;

	// attributi
	// RF07: rimuovi prodotto dal carrello
	private int scelta;
	
	// elementi grafici
	// RF07: rimuovi prodotto dal carrello
	private JPanel confermaSvuotamento;
	private JPanel confermaRimozione;
	private JPanel messaggioConferma;
	private String pulsantiConferma[];
	private String pulsanteNotifica[];


	//RF09 : Aggiunta al carrello
	//attributi
	private int sceltaQuantita; 
	private int quantita; 

	//elementi grafici
	private JLabel quantitaLabel;	/*RF09*/
	private JTextField quantitaField; /*RF09*/
	private JPanel formPanel;		/*RF09*/
	private String[] pulsanteOK;	/*RF09*/

	// RF05: visualizza carrello
	// autori: Bossola Fancesco, Oppezzo Raul

	//attributi
	private ArrayList<HashMap<String, Object>> listaProdottiCarrello;
	private HashMap<String, Object> prodottoSelezionato;
	private int richiestaCarrello;
	private int richiestaQuantita;
	private String nuovaQuantita;
	private int esitoQuantita;
	private boolean richiestaRimozioneEffettuata;
	private boolean richiestaOrdineEffettuata;
	
	// elementi grafici
	private JTable carrelloTable;
	private JScrollPane carrelloPanel;
	private String[] pulsantiCarrello;
	private JTextField formQuantitaField;
	private JLabel formQuantitaLabel;
	private JPanel formQuantitaPanel;
	
	
	//attributi
	//RF06
	private int sceltaEffettuaOrdine;
    private int sceltaMostraFormCarta;
    private int sceltaErrore;
    private float prezzoTotale;
    private boolean procedereConOrdine;
    private String numeroCarta;
    private String tipoCarta;
    private String messaggioErrore;
    
    //elemmentin grafici
	//RF06
    private JLabel labelPrezzoTotale;
    private JTextField fieldPrezzoTotale;
    private JLabel labelProcediOrdine;
    private JTextField fieldProcediOrdine;
    private JLabel labelNumeroCarta;
    private JTextField fieldNumeroCarta;
    private JLabel labelTipoCarta;
    private JPanel effettuaOrdinePanel;
    private JPanel mostraFormCartaPanel;
    private JPanel errorePanel;

	public UiCarrello(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		gestoreCarrelli = (GestoreCarrelliInterfaccia) registryGestore.lookup("gestoreCarrelli");
		
		
		pulsantiConferma = new String[2];
		pulsantiConferma[0] = "Si";
		pulsantiConferma[1] = "No";
		
		pulsanteNotifica = new String[1];
		pulsanteNotifica[0] = "Ok";
		
		confermaSvuotamento = new JPanel();
		JLabel testoConfermaSvuotamento = new JLabel("Sei sicuro di voler svuotare il carrello?");
        confermaSvuotamento.add(testoConfermaSvuotamento);
        
        confermaRimozione = new JPanel();
        
        messaggioConferma = new JPanel();


	//RF09 : aggiunta al carrello 
	quantitaLabel = new JLabel("Quantita'");		//RF09
	quantitaField = new JTextField("", 10); 		//RF09

	formPanel = new JPanel(new GridLayout(1,2));	//RF09
	formPanel.add(quantitaLabel);  					//RF09
	formPanel.add(quantitaField); 					//RF09
	
	pulsanteOK = new String[1]; 					//RF09
		pulsanteOK[0] = "Ok"; 						//RF09


		// RF05: Visualizza carrello
		// autori: Bossola Francesco, Oppezzo Raul
		pulsantiCarrello = new String[] {"Svuota carrello",
				"Rimuovi prodotto",
				"Modifica quantità",
				"Procedi all'ordine"};

		formQuantitaField = new JTextField("", 5);
		formQuantitaLabel = new JLabel("Nuova quantità: ");
		formQuantitaPanel = new JPanel();
		formQuantitaPanel.add(formQuantitaLabel);
		formQuantitaPanel.add(formQuantitaField);

	}

	public void avvioVisualizzaCarrello(String username) throws RemoteException {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		richiestaRimozioneEffettuata = false;
		richiestaOrdineEffettuata = false;

		do {
			listaProdottiCarrello = gestoreCarrelli.cercaProdottiCarrello(username);
			/* if (richiestaOrdineEffettuata || richiestaRimozioneEffettuata) {
				listaProdottiCarrello.clear();
			} */
			if (listaProdottiCarrello.isEmpty() && !(richiestaRimozioneEffettuata || richiestaOrdineEffettuata)) { // carrello vuoto
				this.mostraErroreCarrello(0);
			}
			if (!listaProdottiCarrello.isEmpty()) {
				this.visualizzaListaProdottiCarrello();
				if (richiestaCarrello == 0) { // richiesta svuotamento
					richiestaRimozioneEffettuata = true;
					this.avvioRimuoviProdottiDalCarrello(false, username, listaProdottiCarrello, prodottoSelezionato);
				}
				if (richiestaCarrello == 1 && prodottoSelezionato != null) { // richiesta rimozione
					richiestaRimozioneEffettuata = true;
					this.avvioRimuoviProdottiDalCarrello(true, username, listaProdottiCarrello, prodottoSelezionato);
				}
				if (richiestaCarrello == 2 && prodottoSelezionato != null) { // richiesta modifica quantità
					do {
						this.mostraFormQuantita();
						if (richiestaQuantita == 0) { // richiesta continua
							esitoQuantita = gestoreCarrelli.verificaQuantita(nuovaQuantita, prodottoSelezionato);
							if (esitoQuantita == 0) { // nuovaQuantita valida
								gestoreCarrelli.modificaQuantita(nuovaQuantita, prodottoSelezionato, username);
							} else {
								this.mostraErroreCarrello(esitoQuantita);
							}
						}
					} while (richiestaQuantita != CLOSED_OPTION && esitoQuantita != 0);
				}
				if (richiestaCarrello == 3) { // richiesta ordine
					richiestaOrdineEffettuata = true;
					this.avvioEffettuaOrdine(listaProdottiCarrello);
				}
				if ((richiestaCarrello == 1 || richiestaCarrello == 2) && prodottoSelezionato == null) {
					this.mostraErroreCarrello(3);
				}
			}
		} while (richiestaCarrello != CLOSED_OPTION && !listaProdottiCarrello.isEmpty());
	}

	public void avvioEffettuaOrdine(ArrayList<HashMap<String, Object>> carrello) throws RemoteException
	{	// RF06 Luini-Mengaptche
		prezzoTotale=mostraPrezzo();

		if(sceltaEffettuaOrdine==0){
			mostraFormCarta();
			if(sceltaMostraFormCarta==0){
				mostraErrore();
			}
		}
	

	}
	
	public float mostraPrezzo() throws RemoteException {
	    // RF06 Luini-Mengaptche
		effettuaOrdinePanel = new JPanel();

	    labelPrezzoTotale = new JLabel("Il prezzo totale");
	    //fieldPrezzoTotale = new JTextField("", 15);
	    labelProcediOrdine = new JLabel("Vuoi procedere con l'ordine?");

	    effettuaOrdinePanel.add(labelPrezzoTotale);
	    effettuaOrdinePanel.add(fieldPrezzoTotale);
	    effettuaOrdinePanel.add(labelProcediOrdine);

	    sceltaEffettuaOrdine = showConfirmDialog(
	        	null,
	    		effettuaOrdinePanel,
	        	"Effettua Ordine",
	        	YES_NO_OPTION);

	    // Conversione del  valore del campo testo  in un float
	    try {
	    	prezzoTotale = Float.parseFloat(fieldPrezzoTotale.getText());
	    } catch (NumberFormatException e) {
	        prezzoTotale = 0.0f; //Valore predefinito in caso di errore di conversione
	    }

	    // È possibile controllare il valore di sceltaEffettuaOrdine anche per altre azioni
	    procedereConOrdine = (sceltaEffettuaOrdine == YES_OPTION);
		System.out.println("Valore: " + sceltaEffettuaOrdine);

	    return prezzoTotale;
	}
	 
	public void mostraFormCarta() throws RemoteException {
		// RF06 Luini-Mengaptche
	    mostraFormCartaPanel = new JPanel();

	    labelNumeroCarta = new JLabel("Numero Carta");
	    fieldNumeroCarta = new JTextField("", 20);
	    labelTipoCarta = new JLabel("Tipo Carta");
	        
	        /*sceltaMostraFormCarta = this.showConfirmDialog(null, mostraFormCartaPanel, "mostra form carta", this.VISA_MASTERCARD_AMEX_OK_OPTION);
	        numeroCarta = this.showInputDialog(null, "numero carta", "mostra form carta".this.QUESTION_MESSAGE);
	        String[] scelta= {"Visa", "Mastercard", "AmEx"};*/
	       
	    labelNumeroCarta = new JLabel("numero carta");
	    fieldNumeroCarta = new JTextField("", 20);
	    labelTipoCarta = new JLabel("tipo carta");

	    mostraFormCartaPanel.add(labelNumeroCarta);
	    mostraFormCartaPanel.add(fieldNumeroCarta);
	    mostraFormCartaPanel.add(labelTipoCarta);

	    String[] scelte = {"OK"};

	    sceltaMostraFormCarta = showConfirmDialog(
	        	null,
	        	mostraFormCartaPanel,
	        	"Mostra Form Carta",
	        	OK_OPTION);

	    // Recupero dei valori dei campi di testo
	    numeroCarta = fieldNumeroCarta.getText();
	    tipoCarta = labelTipoCarta.getText();
	}

	 
	public void mostraErrore() throws RemoteException {
		// RF06 Luini-Mengaptche
	    errorePanel = new JPanel();

	    labelNumeroCarta = new JLabel("Dati carta errati");

	    errorePanel.add(labelNumeroCarta);

	    String[] scelte = {"OK"};

	    sceltaErrore = showConfirmDialog(
	        	null,
	        	errorePanel,
	        	"Errore",
	        	OK_OPTION);

	    // Recupero del messaggio di errore 
	    messaggioErrore = labelNumeroCarta.getText();
	}

	
	public void avvioRimuoviProdottiDalCarrello(boolean tipoDiRimozione, String username, ArrayList<HashMap<String, Object>> carrello, HashMap<String, Object> prodotto) throws RemoteException{
		// RF07: rimuovi prodotto dal carrello
		// autori: Simone Aldo Borsa, Andrea Padoan
		
		if(!tipoDiRimozione) {
			mostraConfermaSvuotamento();
			if(scelta == 0) {
				gestoreCarrelli.svuotaCarrello(carrello, username);
				mostraMessaggio(tipoDiRimozione);
			}
		}
		else {
			mostraConfermaRimozione((String) prodotto.get("titolo"));
			if(scelta == 0) {
				gestoreCarrelli.rimozioneProdottoDalCarrello(carrello, prodotto, username);
				mostraMessaggio(tipoDiRimozione);
			}
		}
		
	}
	
	private void mostraConfermaSvuotamento() {
		// RF07: rimuovi prodotto dal carrello
		// autori: Simone Aldo Borsa, Andrea Padoan
		
		scelta = showOptionDialog(
				null,
				confermaSvuotamento,
				"Conferma svuotamento",
				DEFAULT_OPTION,
				PLAIN_MESSAGE,
				null,
				pulsantiConferma,
				"Si");
	}
	
	private void mostraMessaggio(boolean tipoDiRimozione) {
		// RF07: rimuovi prodotto dal carrello
		// autori: Simone Aldo Borsa, Andrea Padoan
		
		String messaggio;
		String titolo;
		
		if(!tipoDiRimozione) {
			messaggio = "Il carrello è stato svuotato correttamente";
			titolo = "Svuotamento";
		}
		else {
			messaggio = "Il prodotto è stato rimosso correttamente";
			titolo = "Rimozione";
		}
		
        JLabel testoConferma = new JLabel(messaggio);
        messaggioConferma.add(testoConferma);
        
		showOptionDialog(
				null,
				messaggioConferma,
				titolo,
				DEFAULT_OPTION,
				PLAIN_MESSAGE,
				null,
				pulsanteNotifica,
				"Ok");
	}
	
	private void mostraConfermaRimozione(String titolo) {
		// RF07: rimuovi prodotto dal carrello
		// autori: Simone Aldo Borsa, Andrea Padoan
		
		JLabel testoConfermaRimozione = new JLabel("Sei sicuro di rimuovere " + titolo + " dal carrello?");
		confermaRimozione.add(testoConfermaRimozione);
		
		scelta = showOptionDialog(
				null,
				confermaRimozione,
				"Rimozione prodotto",
				DEFAULT_OPTION,
				PLAIN_MESSAGE,
				null,
				pulsantiConferma,
				"Si");
	}

	
	public void avvioAggiungiAlCarrello(String username, int codiceProdotto) throws RemoteException
	{	// RF09	: Aggiunta al carrello
		// Autori: Fasano Lorenzo, Iacobucci Luca;


		//prendo la quantita' inserita dall'utente
		mostraForm();

		//controllo che il prodotto selezionato dall'utente sia disponibile in database
		if(gestoreCarrelli.controlloDisponibilita(codiceProdotto)){
			System.out.println("1)Controllo disponibilita': OK");
		//mostra un messaggio di errore in caso di indisponibilita' del prodotto
		}else{
			mostraErrore(1);
		}
	
		//controllo che la quantita' richiesta dall'utente sia compresa tra 1 e 3
		if(gestoreCarrelli.controlloLimiteQuantita(quantita)){
			System.out.println("2)Controllo limite quantita': OK");
		//mostra errore in qualsiasi altro caso
		}else{
			mostraErrore(2);
		}

		//aggiorno la quantita del prodotto in database
		gestoreCarrelli.aggiornamentoQuantita(quantita,
		codiceProdotto,username);
		
		this.showMessageDialog(null, "Il carrello e' stato aggiornato", "Carrello aggiornato", this.PLAIN_MESSAGE, null);

	}
	
	public void mostraForm() throws RemoteException
	{	// RF09	: Aggiunta al carrello
		// Autori: Fasano Lorenzo, Iacobucci Luca;

		sceltaQuantita = this.showOptionDialog(null, formPanel, "Inserire quantita'", 		DEFAULT_OPTION, QUESTION_MESSAGE, null, pulsanteOK, "OK");

		//utente preme X
		if(sceltaQuantita == -1){
			System.out.println("Scelta annullata.");
			mostraErrore(sceltaQuantita);
		}
			//utente preme OK
		if(sceltaQuantita == 0){
			//aggiungere numberFormatException
			quantita = Integer.parseInt(quantitaField.getText());
			//controllo quantita inserita x vedere numero
			System.out.println("Quantita' inserita:" + quantita);
		}
	}
		//RF09 : Aggiunta al carrello
	public void mostraErrore(int numberError)throws RemoteException{
	// RF09	: Aggiunta al carrello
	// Autori: Fasano Lorenzo, Iacobucci Luca;
		switch (numberError) {
			case 1:
				this.showMessageDialog(null, "Prodotto non disponibile", "Errore disponibilita' prodotto", this.PLAIN_MESSAGE, null);
				break;
			
			case 2:
				this.showMessageDialog(null, "La quantita' deve essere compresa tra 1 e 3", "Errore quantita'", this.PLAIN_MESSAGE, null);
				break;
				
				default:
					System.out.println("Scelta annullata.");
					break;
			}
		}

	private void visualizzaListaProdottiCarrello() {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		carrelloTable = new JTable(listaProdottiCarrello.size(), 7) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
				return false;
			}
		};

		carrelloTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		carrelloTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		carrelloTable.getColumnModel().getColumn(0).setHeaderValue("autore");
		carrelloTable.getColumnModel().getColumn(1).setHeaderValue("titolo");
		carrelloTable.getColumnModel().getColumn(2).setHeaderValue("editore");
		carrelloTable.getColumnModel().getColumn(3).setHeaderValue("tipo");
		carrelloTable.getColumnModel().getColumn(4).setHeaderValue("anno");
		carrelloTable.getColumnModel().getColumn(5).setHeaderValue("prezzo");
		carrelloTable.getColumnModel().getColumn(6).setHeaderValue("quantità");

		carrelloTable.getColumnModel().getColumn(0).setMinWidth(150);
		carrelloTable.getColumnModel().getColumn(1).setMinWidth(200);
		carrelloTable.getColumnModel().getColumn(2).setMinWidth(150);

		for (int i = 0; i < listaProdottiCarrello.size(); i++) {
			int j = 0;
			carrelloTable.setValueAt(listaProdottiCarrello.get(i).get("autore"), i, j++);
			carrelloTable.setValueAt(listaProdottiCarrello.get(i).get("titolo"), i, j++);
			carrelloTable.setValueAt(listaProdottiCarrello.get(i).get("editore"), i, j++);
			carrelloTable.setValueAt(listaProdottiCarrello.get(i).get("tipo"), i, j++);
			carrelloTable.setValueAt(listaProdottiCarrello.get(i).get("anno").toString(), i, j++);
			carrelloTable.setValueAt(listaProdottiCarrello.get(i).get("prezzo").toString(), i, j++);
			carrelloTable.setValueAt(listaProdottiCarrello.get(i).get("quantitaProdotto").toString(), i, j);
		}

		carrelloPanel = new JScrollPane(carrelloTable);
		carrelloPanel.setPreferredSize(new Dimension(250, 250));

		richiestaCarrello = showOptionDialog(null, carrelloPanel, "Carrello (clicca X per uscire)",
				DEFAULT_OPTION, PLAIN_MESSAGE, null, pulsantiCarrello, null);

		prodottoSelezionato = carrelloTable.getSelectedRow() >= 0 ?
				listaProdottiCarrello.get(carrelloTable.getSelectedRow()) : null;
	}

	private void mostraFormQuantita() {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		richiestaQuantita = showOptionDialog(null, formQuantitaPanel, "modifica quantità (clicca X per uscire)",
				DEFAULT_OPTION, QUESTION_MESSAGE, null, new String[]{"OK"}, null);

		nuovaQuantita = formQuantitaField.getText();
	}

	private void mostraErroreCarrello(int errore) {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		if (errore == 0) {
			showMessageDialog(null, "Il carrello è vuoto.\n(clicca OK o X per uscire)", "Errore", ERROR_MESSAGE);
		}
		if (errore == 1) {
			showMessageDialog(null, "Quantità non valida.\n(clicca OK o X per uscire)", "Errore", ERROR_MESSAGE);
		}
		if (errore == 2) {
			showMessageDialog(null, "Quantità non disponibile.\n(clicca OK o X per uscire)", "Errore", ERROR_MESSAGE);
		}
		if (errore == 3) {
			showMessageDialog(null, "Nessun prodotto selezionato.\n(clicca OK o X per uscire)", "Errore", ERROR_MESSAGE);
		}
	}

}
