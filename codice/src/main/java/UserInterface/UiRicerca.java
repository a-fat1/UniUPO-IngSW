package UserInterface;

import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.util.Objects;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import UserInterface.*;
import Elaborazione.*;

public class UiRicerca extends JOptionPane implements UiRicercaInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private UiListaInterfaccia uiLista;
	private UiUtenteInterfaccia uiUtente;
	private UiCarrelloInterfaccia uiCarrello;
	private UiProdottoInterfaccia uiProdotto;
	private GestoreRicercheInterfaccia gestoreRicerche;

	// attributi

	// RF19 - Ricerca Utente
	// elementi grafici
	private JPanel searchPanel;
	private JLabel labelUsername;
	private JLabel labelNome;
	private JLabel labelCognome;
	private JComboBox<String> comboMenu;
	private JTextField fieldUsername;
	private JTextField fieldNome;
	private JTextField fieldCognome;

	// RF19 - Ricerca Utente
	// variabili
	private int sceltaUtente; // variabile per determinare l'uscita o meno dall'interfaccia di Ricerca utente
	private String nome;
	private String cognome;
	private String username;
	private String sceltaRicerca;
	private int esitoControllo;

	private ArrayList<HashMap<String, Object>> elencoUtenti;

	// RF19 - Ricerca Utente
	// Array di String per i pulsanti di ricerca e le colonne per la tabella dei dati estratti dal database
	private final String[] pulsanteRicerca;
	private final String[] pulsanteElencoUtentiStaff;
	private final String[] pulsanteElencoUtentiAdmin;
	private final String[] colonneStaff;
	private final String[] colonneAmministratore;
	// RF18
	private int sceltaGiacenza, esitoGiacenza = -1;
	private String giacenza;
	ArrayList<HashMap<String, Object>> listaProdotti;
	//RF08
	private static final String _CD_ = "CD"; 
	private static final String _DVD_ = "DVD";
	private static final String _LIBRO_ = "Libro";
	private static final String _NUOVA_FORNITURA = "Nuova Fornitura";
	private static final String _AGGIORNA_PREZZO = "Aggiorna il Prezzo";
	private static final String _RIMUOVI_RIPRISTINA = "Rimuovi/Ripristina";
	private static final String _LISTA_FORNITURE = "Lista Forniture";
	private static final String _LISTA_ORDINI = "Lista Ordini";
	private static final String _AGGIUNGI_AL_CARRELLO = "Aggiungi al Carrello";
	
	// elementi grafici
	// RF18
	private JPanel pannelloGiacenza;
	private JTextField campoGiacenza;
	private JTable tabellaProdotti;
	private JScrollPane pannelloGiacenzaScorrevole;
	private String giacenzaPulsanti[] = new String[2];
	//RF08
	JPanel ricercaPanel;
	JPanel mainPanel;
	private JTextField autoreField; 	
	private JTextField titoloField; 	
	private JTextField editoreField; 
	private JTextField annoField; 	

    private JCheckBox libroCheckbox;
    private JCheckBox cdCheckbox;
    private JCheckBox dvdCheckbox;
    private JCheckBox tuttiCheckbox;

	public UiRicerca(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiLista = (UiListaInterfaccia) registryUI.lookup("uiLista");
		uiUtente = (UiUtenteInterfaccia) registryUI.lookup("uiUtente");
		uiCarrello = (UiCarrelloInterfaccia) registryUI.lookup("uiCarrello");
		uiProdotto = (UiProdottoInterfaccia) registryUI.lookup("uiProdotto");
		gestoreRicerche = (GestoreRicercheInterfaccia) registryGestore.lookup("gestoreRicerche");

		// RF19 - Ricerca Utente

		// campi username, nome e cognome con i relativi field
		labelUsername = new JLabel("Inserisci uno username");
		labelNome = new JLabel("Inserisci un nome");
		labelCognome = new JLabel("Inserisci un cognome");
		fieldUsername = new JTextField("", 20);
		fieldNome = new JTextField("", 20);
		fieldCognome = new JTextField("", 20);

		// RF19 - Ricerca Utente
		// stringhe per il menu' per la ricerca
		String[] scelteMenuRicercaUtente = new String[2];
		scelteMenuRicercaUtente[0] = "Ricerca per nome-cognome";
		scelteMenuRicercaUtente[1] = "Ricerca per username";
		comboMenu = new JComboBox<>(scelteMenuRicercaUtente);

		// RF19 - Ricerca Utente
		// creo il panel per l'interfaccia di inserimento nome, cognome o username
		searchPanel = new JPanel(new GridLayout(7, 1));
		searchPanel.add(comboMenu);
		searchPanel.add(labelUsername);
		searchPanel.add(fieldUsername);
		searchPanel.add(labelNome);
		searchPanel.add(fieldNome);
		searchPanel.add(labelCognome);
		searchPanel.add(fieldCognome);
		pulsanteRicerca = new String[1];
		pulsanteRicerca[0] = "Invia";

		// RF19 - Ricerca Utente
		// colonne per staff e amministratore, servono per mostrare i campi della ricerca nel database
		colonneStaff = new String[]{"Nome", "Cognome", "Username", "Tipo utente"};
		colonneAmministratore = new String[]{"Nome", "Cognome", "Username", "Tipo utente", "Attivo"};

		// RF19 - Ricerca Utente
		// pulsanti necessari per la parte in cui si decide cosa fare con un utente selezionato tra quelli
		// trovati nel database
		pulsanteElencoUtentiStaff = new String[2];
		pulsanteElencoUtentiStaff[0] = "Lista pagamenti";
		pulsanteElencoUtentiStaff[1] = "Lista ordini";
		pulsanteElencoUtentiAdmin = new String[1];
		pulsanteElencoUtentiAdmin[0] = "Blocca\\Sblocca utente";

		// ArrayList per elenco degli utenti
		elencoUtenti = new ArrayList<>();

		//RF08
		//if(ricercaPanel==null)
		ricercaPanel = new JPanel();
        // Etichetta in cima al corpo della finestra
        JLabel labelText = new JLabel("Inserire titolo e/o autore e premere OK");
        labelText.setBounds(20, 10, 300, 20); // Posizione e dimensioni
        ricercaPanel.add(labelText);

        // Etichetta "premere X per uscire"
        JLabel exitLabel = new JLabel("(premere X per uscire)");
        exitLabel.setBounds(20, 35, 200, 20); // Posizione e dimensioni
        ricercaPanel.add(exitLabel);

        // Form con campi e checkbox
       
        ricercaPanel.setBounds(20, 70, 420, 250); // Posizione e dimensioni

        // Campi Autore, Titolo, Editore, Anno
        autoreField =addFormField(ricercaPanel, "Autore:", 35, 80);
        titoloField =addFormField(ricercaPanel, "Titolo:", 35, 110);
        editoreField =addFormField(ricercaPanel, "Editore:", 35, 140);
        annoField =addFormField(ricercaPanel, "Anno:", 35, 170);
        
        JLabel tipoLabel = new JLabel("Tipo:");
        tipoLabel.setBounds(315, 70, 200, 20); // Posizione e dimensioni
        ricercaPanel.add(tipoLabel);

        // Checkbox con etichetta "Tipo"
        libroCheckbox = new JCheckBox("Libro");
        libroCheckbox.setBounds(315, 100, 100, 20);
        cdCheckbox = new JCheckBox("CD");
        cdCheckbox.setBounds(315, 125, 100, 20);
        dvdCheckbox = new JCheckBox("DVD");
        dvdCheckbox.setBounds(315, 150, 100, 20);
        tuttiCheckbox = new JCheckBox("Tutti");
        tuttiCheckbox.setBounds(315, 175, 100, 20);
     	// Aggiungi ItemListener a tuttiCheckbox
        tuttiCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                libroCheckbox.setSelected(selected);
                cdCheckbox.setSelected(selected);
                dvdCheckbox.setSelected(selected);
            }
        });

        ricercaPanel.add(libroCheckbox);
        ricercaPanel.add(cdCheckbox);
        ricercaPanel.add(dvdCheckbox);
        ricercaPanel.add(tuttiCheckbox);
	}
	
	//RF08 Raffaele Camera e Vanessa Tafarella
	private int mostraBarraRicerca(String tipo_utente)
	{
		//mostra ricercaPanel
		System.out.println("dentro al metodo: avvioRicercaProdotto");

		//rinizializza i campi
		libroCheckbox.setSelected(false);
		dvdCheckbox.setSelected(false);
		tuttiCheckbox.setSelected(false);
		cdCheckbox.setSelected(false);
		autoreField.setText("");
		editoreField.setText("");
		titoloField.setText("");
		annoField.setText("");


		ricercaPanel.setPreferredSize(new Dimension(430, 260));
		ricercaPanel.setLayout(null);
		ricercaPanel.setVisible(true);

		String pulsanti[] = {"CERCA"};

		return showOptionDialog(
			null, 
			ricercaPanel, 
			"Ricerca Prodotto", 
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.PLAIN_MESSAGE, 
			null, pulsanti,"Cerca");
	}

	//RF08 Raffaele Camera e Vanessa Tafarella
	private void mostraElenco(ArrayList<HashMap<String, Object>> risultato, String username, String tipoUtente) throws RemoteException{
		//nel caso ho 0 risultati mostro dialog
		if(risultato!=null && risultato.size()==0){					
			mostraErrore("errorNoResult");
			return ;
		}
		boolean isCliente = (tipoUtente!=null && tipoUtente.equalsIgnoreCase("cliente")) ? true : false;						
		// Creazione del modello della tabella con le colonne specificate nell'ordine desiderato
		DefaultTableModel tableModel = new DefaultTableModel(
			new Object[][]{},  // Nessuna riga iniziale
			isCliente ? new String[]{"autore", "titolo", "editore", "anno", "tipo", "prezzo", "quantità"}
			: new String[]{"autore", "titolo", "editore", "anno", "tipo", "prezzo", "quantità", "disponibilità"} // Ordine delle colonne
		);
		// Popolare la tabella con i dati
		for (HashMap<String, Object> riga : risultato) {
			Object[] rowData = {
					riga.get("autore"),
					riga.get("titolo"),
					riga.get("editore"),
					riga.get("anno"),
					riga.get("tipo"),
					riga.get("prezzo"),
					riga.get("quantita"),
					isCliente ? "" : riga.get("disponibile")  // Nasconde la colonna "Disponibilità" se cliente è true
			};
			tableModel.addRow(rowData);
		}

		// Creazione della tabella
		JTable table = new JTable(tableModel);
		// Creazione dello scroll pane per la tabella
		JScrollPane scrollPane = new JScrollPane(table);
		String pulsanti [] ;

		if (!isCliente) {
			pulsanti = new String[]{
				_NUOVA_FORNITURA,
				_AGGIORNA_PREZZO,
				_RIMUOVI_RIPRISTINA,
				_LISTA_FORNITURE,
				_LISTA_ORDINI
			};
		} else {
			pulsanti = new String[]{
				_AGGIUNGI_AL_CARRELLO
			};

		}
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		// mainPanel.add(buttonPanel);

		//mostra risultatiPanel
		System.out.println("dentro al metodo: avvioRisultatiRicerca");
		mainPanel.setPreferredSize(new Dimension(830, 260));
		//risultatiPanel.setLayout(null);
		mainPanel.setVisible(true);

		int res = showOptionDialog(
			null, 
			mainPanel, 
			"Prodotti trovati", 
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.PLAIN_MESSAGE, 
			null, pulsanti, null);
		//controllo se è stato cliccato su X
		if(res==-1)
			return;
		String pulsantePremuto = pulsanti[res];
		//System.out.println("Hai cliccato su: " + pulsantePremuto);
		int selectedRow = table.getSelectedRow();
		if(selectedRow<0){
			mostraErrore("noRowSelected");
			return;
		} 

		System.out.println("selectedRow = "+selectedRow);
		HashMap<String,Object> prodotto = risultato.get(selectedRow);
		System.out.println("oggetto selezionato: "+prodotto);
		int codiceProdotto = (int) prodotto.get("codice");
		Integer disponibile = (Integer) prodotto.get("disponibile");
		System.out.println("codice: "+codiceProdotto);
		System.out.println("username: "+username);
		System.out.println("disponibile: "+disponibile);

		switch (pulsantePremuto) {
			case _NUOVA_FORNITURA:
				//RF15	
				
				uiProdotto.avvioNuovaFornitura(codiceProdotto, false);		
				break;
			case _AGGIORNA_PREZZO:
				//RF14
				uiProdotto.avvioAggiornaPrezzo(prodotto);
				break;
			case _RIMUOVI_RIPRISTINA:
				//RF10
				uiProdotto.avvioRimuoviRipristinaNelCatalogo(codiceProdotto, disponibile);
				break;
			case _LISTA_FORNITURE:
				//RF13
				uiLista.avvioListaForniture(codiceProdotto);
				break;
			case _LISTA_ORDINI:
				uiLista.avvioListaOrdini("", codiceProdotto);
				break;
						
			case _AGGIUNGI_AL_CARRELLO:
				//RF09
				uiCarrello.avvioAggiungiAlCarrello(username, codiceProdotto);
				break;
			
		}

	}



	//RF08 Raffaele Camera e Vanessa Tafarella
	private void mostraErrore(String tipoErrore)
	{
		String messaggio="";

        if (tipoErrore.equals("errorNoFieldCompiled"))
		{
            messaggio="Nessun campo compilato";
		}
		if (tipoErrore.equals("errorNoResult"))
		{
            messaggio="Nessun prodotto trovato";
		}
		if (tipoErrore.equals("errorAnnoField"))
		{
            messaggio="L'anno deve contenere 4 caratteri";
			//annoField.setBackground(Color.YELLOW);
		}
		if(tipoErrore.equals("errorTypeAnnoField"))
		{
			messaggio="L'anno deve essere un numero di 4 cifre";
		}
		if (tipoErrore.equals("errorEditoreField"))
		{
            messaggio="L'editore deve contenere almeno 3 caratteri";
		}
		if (tipoErrore.equals("errorTitoloField"))
		{
            messaggio="Il titolo deve contenere almeno 3 caratteri";
		}
		if(tipoErrore.equals("errorAutoreField"))
		{
			messaggio = "L'autore deve contenere almeno 3 caratteri";
		}
		if(tipoErrore.equals("noRowSelected"))
		{
			messaggio = "Nessun prodotto selezionato";
		}

		messaggio = messaggio + "\n(clicca su OK o X per continuare)";

		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE);
	}
	//RF08 Raffaele Camera e Vanessa Tafarella
	private JTextField addFormField(JPanel panel, String label, int x, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(x, y, 80, 20); // Posizione e dimensioni
        JTextField jTextField = new JTextField();
        jTextField.setBounds(x + 90, y, 150, 20); // Posizione e dimensioni
        panel.add(jLabel);
        panel.add(jTextField);
        return jTextField;
    }

	//RF08 Raffaele Camera
	public void avvioRicercaProdotto(String tipo_utente, String username) throws RemoteException
	{	
		if(mostraBarraRicerca(tipo_utente)==OK_OPTION) {
			
			// Recupera lo stato delle checkbox
			boolean libroSelected = libroCheckbox.isSelected();
			boolean cdSelected = cdCheckbox.isSelected();
			boolean dvdSelected = dvdCheckbox.isSelected();
			boolean tuttiSelected = tuttiCheckbox.isSelected();

			//gestione del campo tipo
			ArrayList<String> tipoList = new ArrayList<String>();
			if(tuttiSelected){
				tipoList.add(_LIBRO_);
				tipoList.add(_CD_);
				tipoList.add(_DVD_);
			} else {
				if(libroSelected){
					tipoList.add(_LIBRO_);
				}
				if(cdSelected){
					tipoList.add(_CD_);
				}
				if(dvdSelected){
					tipoList.add(_DVD_);
				}
			}

			String autore = autoreField.getText();
			String titolo = titoloField.getText();
			String editori = editoreField.getText();
			String anno = annoField.getText();

			// Stampa i valori
			// System.out.println("Autore: " + autore +", Titolo: " + titolo+", Editore: " + editori+", Anno: " + anno+", Libro: " + libroSelected+
			//	", CD: " + cdSelected+", DVD: " + dvdSelected+", Tutti: " + tuttiSelected);

			if(gestoreRicerche.controllaParametri(titolo,  autore, editori, anno, tipoList)){
				if(autore!=null && !autore.equals("") && autore.length()<3){					
					mostraErrore("errorAutoreField");
					return ;
				}
				if(titolo!=null && !titolo.equals("") && titolo.length()<3){					
					mostraErrore("errorTitoloField");
					return ;
				}
				if(editori!=null && !editori.equals("") && editori.length()<3){					
					mostraErrore("errorEditoreField");
					return ;
				}
				if(anno!=null && !anno.equals("") && anno.length()!=4){					
					mostraErrore("errorAnnoField");					
					return ;
				}
				if(anno!=null && !anno.equals("") && anno.length()==4)
				try {
					//verifica inserimento numero per l'anno
					Integer.parseInt(anno);
					
				} catch (NumberFormatException e) {
					mostraErrore("errorTypeAnnoField");	
					return ;
				}
				boolean isCliente = (tipo_utente!=null && tipo_utente.equalsIgnoreCase("cliente")) ? true : false;
				ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto(titolo,  autore, editori, anno, tipoList, isCliente);
				mostraElenco(risultato, username, tipo_utente);

			} else {				
				mostraErrore("errorNoFieldCompiled");
				
			}
		}
		
	}

	
	public void avvioProdottiInEsaurimento() throws RemoteException {
		
		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti

		do {
			this.mostraFormGiacenza();		// Mostra all'utente il form per inserire la giacenza

			if (sceltaGiacenza == 0) {		// sceltaGiacenza = 0 quando viene premuto ok in mostraFormGiacenza()
				esitoGiacenza = gestoreRicerche.controlloGiacenza(giacenza);	// Viene richiamato controlloGiacenza per controllare la giacenza
				if(esitoGiacenza == 1 || esitoGiacenza == 2 || esitoGiacenza == 3)
					mostraErroreGiacenza(esitoGiacenza);	// In caso di errore viene richiamato mostraErroreGiacenza per visualizzare l'errore specifico
				else {
					listaProdotti = gestoreRicerche.ricercaProdottiConGiacenza(giacenza);		// Con la giacenza corretta viene richiamato ricercaProdottiConGiacenza per recuperare i dati dei prodotti
					if(listaProdotti.size() == 0) 
						mostraErroreGiacenza(4);		// Se l'ArrayList non ha i dati si richiama mostraErroreGiacenza con parametro 4
					else
						mostraListaProdotti(listaProdotti);		// mostraListaProdotti viene richiamato per visualizzare i dati in modo adeguato
				}
			}
		} while (sceltaGiacenza == 0);		// sceltaGiacenza != 0 quando l'utente preme torna al menu o il pulsante x

		esitoGiacenza = -1;		// esitoGiacenza = -1 viene inizializzato all'inizio e alla fine di avvioProdottiInEsaurimento()
	}
	
	private void mostraFormGiacenza() {

		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti

		// Definisce il pannello (2 righe, 1 colonna) e un messaggio di testo
		pannelloGiacenza = new JPanel(new GridLayout(2,1));
		pannelloGiacenza.add(new JLabel("Inserisci qui la giacenza"));

		// Assegna a campoGiacenza una casella di testo e un setToolTipText
		campoGiacenza = new JTextField("", 10);
		campoGiacenza.setToolTipText("Scrivere qui la giacenza");
		if (esitoGiacenza == 1 || esitoGiacenza == 2 || esitoGiacenza == 3)
			campoGiacenza.setBackground(Color.RED);		// Il background di campoGiacenza viene impostato con il rosso quando l'utente sbaglia a inserire la giacenza
		
		pannelloGiacenza.add(campoGiacenza);	// Aggiunge JTextField nel pannello

		// Definisce nome pulsanti e mostra interfaccia
		giacenzaPulsanti[0] = "Ok";
		giacenzaPulsanti[1] = "Torna al menu";
		sceltaGiacenza = UiRicerca.showOptionDialog(null, pannelloGiacenza, "Prodotti in esaurimento", DEFAULT_OPTION, PLAIN_MESSAGE, null, giacenzaPulsanti, null);

		if (sceltaGiacenza == 0)	// Quando viene premuto il pulsante ok il valore 0 viene memorizzato in sceltaGiacenza
			giacenza = campoGiacenza.getText();		// E il contenuto scritto dall'utente viene salvato nella stringa giacenza
	}
	
	private void mostraErroreGiacenza(int tipoErrore) {

		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti

		// In base al valore di tipoErrore viene visualizzata l'interfaccia che contiene il messaggio di errore specifico 
		if (tipoErrore == 1)
			UiRicerca.showMessageDialog(null, "La giacenza deve contenere solo caratteri numerici positivi.", "Errore giacenza invalida", UiRicerca.PLAIN_MESSAGE, null);
		if (tipoErrore == 2)
			UiRicerca.showMessageDialog(null, "La giacenza non deve essere troppo lunga o troppo elevata.", "Errore giacenza eccessiva", UiRicerca.PLAIN_MESSAGE, null);
		if (tipoErrore == 3)
			UiRicerca.showMessageDialog(null, "La giacenza deve essere maggiore di zero.", "Errore giacenza nulla", UiRicerca.PLAIN_MESSAGE, null);
		if (tipoErrore == 4) {
			// Definisce il pannello e un messaggio di testo
			pannelloGiacenza = new JPanel();
			pannelloGiacenza.add(new JLabel("Nessun prodotto presente con giacenza uguale o inferiore a quella selezionata."));

			// Definisce nome pulsanti e mostra interfaccia
			giacenzaPulsanti[0] = "Nuova ricerca";
			giacenzaPulsanti[1] = "Torna al menu";
			sceltaGiacenza = UiRicerca.showOptionDialog(null, pannelloGiacenza, "Errore lista vuota", DEFAULT_OPTION, PLAIN_MESSAGE, null, giacenzaPulsanti, null);
		}
	}
	
	private void mostraListaProdotti(ArrayList<HashMap<String, Object>> listaProdotti) {

		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti
		
		// Imposta JPanel e crea Jtable non modificabile, con numero di righe e colonne pari a listaProdotti
		pannelloGiacenza = new JPanel(new FlowLayout());
		tabellaProdotti = new JTable(listaProdotti.size(), listaProdotti.get(0).size()) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
            	return false;
            }
		};
		
		// Salva per colonne gli attributi e i valori all'interno di JTable
		int riga, colonna = 0;
		for (String attributo : listaProdotti.get(colonna).keySet()) {
			tabellaProdotti.getColumnModel().getColumn(colonna).setHeaderValue(attributo);		// Aggiunge e definisce l'attributo della colonna
			riga = 0;
    		for (HashMap<String, Object> prodotto : listaProdotti) {
				if ("disponibile".equals(attributo)) {		// Quando attributo = disponibile, si sostituiscono 1 e 0 con si e no
					Object valore = prodotto.get(attributo);
					valore = ((Integer) valore == 1) ? "si" : "no";
					tabellaProdotti.setValueAt(valore, riga, colonna);
				}
				else
					tabellaProdotti.setValueAt(prodotto.get(attributo), riga, colonna);		// Aggiunge il contenuto delle righe

				riga++;
			}
			colonna++;
		}

		tabellaProdotti.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);		// Disabilita ridimensionamento automatico

		// Creazione pannello scorrevole (utilizzando tabellaProdotti), definizione delle dimensioni e inserimento in pannelloGiacenza
		pannelloGiacenzaScorrevole = new JScrollPane(tabellaProdotti);
		pannelloGiacenzaScorrevole.setPreferredSize(new Dimension(650, 250));
		pannelloGiacenza.add(pannelloGiacenzaScorrevole);

		// Definisce nome pulsanti e mostra interfaccia
		giacenzaPulsanti[0] = "Nuova ricerca";
		giacenzaPulsanti[1] = "Torna al menu";
		sceltaGiacenza = UiRicerca.showOptionDialog(null, pannelloGiacenza, "Lista prodotti", DEFAULT_OPTION, PLAIN_MESSAGE, null, giacenzaPulsanti, null);
	}

	/**
	 * Funzione che avvia la funzione di Ricerca Utente
	 * @param genereUtente il tipo di utente (admin o staff) di chi vuole iniziare la ricerca
	 * @throws RemoteException
	 */

	public void avvioRicercaUtente(String genereUtente) throws RemoteException
	{
		// RF19 - Ricerca Utente
		// Riccardo Nazzari, Andrea Benedetto

		// set di sceltaUtente = 0 per iniziare il ciclo
		sceltaUtente = 0;

		// imposto esitoControllo = 0
		esitoControllo = 0;

		// loop fino a quando il controllo non è positivo (o l'utente esce dalla finestra)
		while((esitoControllo != 4) && (sceltaUtente != -1)){

			// reset per il primo ciclo
			if(esitoControllo == 0){
				fieldNome.setText("");
				fieldCognome.setText("");
				fieldUsername.setText("");
			}

			// mostra il form della ricerca
			mostraFormRicerca();

			// se viene selezionato nome-cognome, viene controllato tramite la funzione apposita per il nome
			// e cognome
			if(sceltaRicerca.equals("Ricerca per nome-cognome"))
			{
				esitoControllo = gestoreRicerche.controlloParametriRicercaUtente(nome, cognome);
				// result != -1, altrimenti viene mostrato questo errore quando viene chiusa la finestra per uscire
				if((esitoControllo == 2 || esitoControllo == 3) && (sceltaUtente != -1))
				{
					mostraErroreRicercaUtente(esitoControllo);
				}
			}
			// se viene selezionato username, viene controllato tramite la funzione apposita per lo username
			else if(sceltaRicerca.equals("Ricerca per username"))
			{
				esitoControllo = gestoreRicerche.controlloParametriRicercaUtente(username);
				// result != -1, altrimenti viene mostrato questo errore quando viene chiusa la finestra per uscire
				if((esitoControllo == 1) && (sceltaUtente != -1))
				{
					mostraErroreRicercaUtente(1);
				}
			}

			// se il controllo è andato a buon fine e l'utente è dello staff, si ricercano gli utenti NON bloccati
			if((esitoControllo == 4) && genereUtente.equals("staff") && sceltaUtente != -1)
			{
				// ricerca per nome-cognome di utenti non bloccati
				if(sceltaRicerca.equals("Ricerca per nome-cognome"))
				{
					elencoUtenti = gestoreRicerche.cercaUtenteNonBloccato(nome, cognome);
				}
				// ricerca per username di utenti non bloccati
				else if(sceltaRicerca.equals("Ricerca per username"))
				{
					elencoUtenti = gestoreRicerche.cercaUtenteNonBloccato(username);
				}
			}
			// se il controllo è andato a buon fine e l'utente è amministratore, si ricercano gli utenti bloccati e
			// non bloccati
			else if((esitoControllo == 4) && genereUtente.equals("amministratore") && sceltaUtente != -1)
			{
				// ricerca per nome-cognome di utenti bloccati e non bloccati
				if(sceltaRicerca.equals("Ricerca per nome-cognome"))
				{
					elencoUtenti = gestoreRicerche.cercaUtenteBloccatoNonBloccato(nome, cognome);
				}
				// ricerca per username di utenti bloccati e non bloccati
				else if(sceltaRicerca.equals("Ricerca per username"))
				{
					elencoUtenti = gestoreRicerche.cercaUtenteBloccatoNonBloccato(username);
				}
			}

			// se non sono stati trovati utenti viene mostrato il messaggio di errore
			if((elencoUtenti.isEmpty()) && (sceltaUtente != -1) && (esitoControllo == 4))
			{
				mostraErroreRicercaUtente(5);
			}

			// se sono stati trovati utenti viene mostrata la tabella con le relative opzioni
			if((!elencoUtenti.isEmpty()) && (sceltaUtente != -1) && (esitoControllo == 4))
			{
				mostraElencoRicercaUtente(elencoUtenti, genereUtente);
			}
		}
	}

	/**
	 * Funzione che mostra a schermo il form di Ricerca Utente.
	 * Alla variabile sceltaUtente viene assegnato il valore risultante da quello che l'utente
	 * decide sul form.
	 * @throws RemoteException
	 */
	private void mostraFormRicerca() throws RemoteException
	{
		// RF19 - Ricerca Utente
		// Riccardo Nazzari, Andrea Benedetto

		// mostro a schermo la finestra di inserimento dati per la ricerca
		sceltaUtente = JOptionPane.showOptionDialog(null, searchPanel, "Ricerca utente (clicca X per uscire)",
				DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, pulsanteRicerca, "Ricerca");

		// estraggo i dati
		sceltaRicerca = Objects.requireNonNull(comboMenu.getSelectedItem()).toString();
		nome = fieldNome.getText();
		cognome = fieldCognome.getText();
		username = fieldUsername.getText();

		// reset dei campi di ingresso per operazioni future
	}

	/**
	 * Funzione che mostra a schermo un errore specifico in base a cosa non va bene nelle diverse fasi
	 * della Ricerca Utente.
	 * @param tipo il tipo di errore che arriva dalla funzione che si occupa di controllare i parametri
	 * @throws RemoteException
	 */
	private void mostraErroreRicercaUtente(int tipo) throws RemoteException {
		// RF19 - Ricerca Utente
		// Riccardo Nazzari, Andrea Benedetto

		// creo variabile per il messaggio di errore da mostrare
		String messaggio;

		switch (tipo) {
			case 1: {
				messaggio = "Format username errato: assicurati di inserire\nalmeno tre caratteri nel campo \"username\".";
				showMessageDialog(null, messaggio, "Errore username (clicca X per chiudere)", ERROR_MESSAGE);
				break;
			}
			case 2: {
				messaggio = "Format nome errato: assicurati di inserire\nalmeno tre caratteri nel campo \"nome\" e di\n" +
						    "inserire solo caratteri letterali.";
				showMessageDialog(null, messaggio, "Errore nome (clicca X per chiudere)", ERROR_MESSAGE);
				break;
			}
			case 3: {
				messaggio = "Format cognome errato: assicurati di inserire\nalmeno tre caratteri nel campo \"cognome\" e di\n" +
						    "inserire solo caratteri letterali.";
				showMessageDialog(null, messaggio, "Errore cognome (clicca X per chiudere)", ERROR_MESSAGE);
				break;
			}
			case 5: {
				messaggio = "Non sono stati trovati risultati per la tua ricerca.";
				showMessageDialog(null, messaggio, "Nessun risultato (clicca X per chiudere)", ERROR_MESSAGE);
				break;
			}
			case 6: {
				messaggio = "Seleziona una riga tra i risultati ottenuti!";
				showMessageDialog(null, messaggio, "Nessun risultato selezionato", ERROR_MESSAGE);
				break;
			}

		}
	}

	/**
	 * Funzione che mostra l'elenco degli utenti trovati nel database.
	 * @param elencoUtenti l'elenco di utenti trovati dopo l'interrogazione al database.
	 * @param genereUtente il tipo di utente che ha effettuato la ricerca.
	 * @throws RemoteException
	 */
	private void mostraElencoRicercaUtente(ArrayList<HashMap<String, Object>> elencoUtenti, String genereUtente) throws RemoteException {

		// RF19 - Ricerca Utente
		// Riccardo Nazzari, Andrea Benedetto

		// inizializzazione variabili
		Object[][] utentiTabella;
		JTable table = null;
		int azione;

		// se l'utente che ha richiesto è di genere "staff", allora inizializzo la tabella con 4 colonne in quanto
		// deve essere mostrato solo nome-cognome-username-tipo
		if (genereUtente.equals("staff")) {
			utentiTabella = new Object[elencoUtenti.size()][colonneStaff.length];

			// estraggo i campi
			for (int i = 0; i < elencoUtenti.size(); i++) {
				utentiTabella[i][0] = elencoUtenti.get(i).get("nome");
				utentiTabella[i][1] = elencoUtenti.get(i).get("cognome");
				utentiTabella[i][2] = elencoUtenti.get(i).get("username");
				utentiTabella[i][3] = elencoUtenti.get(i).get("tipo");
			}
			// creo la tabella
			table = new JTable(utentiTabella, colonneStaff);
		}

		// se l'utente che ha richiesto è di genere "amministratore", allora inizializzo la tabella con 5 colonne in quanto
		// deve essere mostrato nome-cognome-username-tipo-attivo
		else if (genereUtente.equals("amministratore")) {
			utentiTabella = new Object[elencoUtenti.size()][colonneAmministratore.length];

			// estraggo i campi
			for (int i = 0; i < elencoUtenti.size(); i++) {
				utentiTabella[i][0] = elencoUtenti.get(i).get("nome");
				utentiTabella[i][1] = elencoUtenti.get(i).get("cognome");
				utentiTabella[i][2] = elencoUtenti.get(i).get("username");
				utentiTabella[i][3] = elencoUtenti.get(i).get("tipo");
				if (elencoUtenti.get(i).get("attivo").toString().equals("1")) {
					utentiTabella[i][4] = "true";
				} else {
					utentiTabella[i][4] = "false";
				}
			}
			// creo la tabella
			table = new JTable(utentiTabella, colonneAmministratore);
		}

		// mi assicuro che la tabella sia stata creata correttamente
		assert table != null;

		// modifico la dimensione della riga per ogni tabella
		table.setRowHeight(25);
		// comando per evitare che si possano spostare le colonne
		table.getTableHeader().setReorderingAllowed(false);

		// setto la tabella in modo che si possa selezionare solo un campo
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// creo la tabella scroll e ne gestisco le dimensioni
		JScrollPane tabella = new JScrollPane(table);
		tabella.setPreferredSize((new Dimension(500, 150)));

		do{
			// se il genereUtente è di tipo "staff" mostro solo i pulsanti per lo staff
			if (genereUtente.equals("staff"))
				azione = showOptionDialog(null, tabella, "Elenco utenti",
						DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
						pulsanteElencoUtentiStaff, null);

			// se non è "staff" allora è sicuramente "amministratore" e mostro solo i pulsanti per quello
			else
				azione = showOptionDialog(null, tabella, "Elenco utenti",
						DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
						pulsanteElencoUtentiAdmin, null);

			if(table.getSelectedRow() == -1 && azione != -1)
			{
				mostraErroreRicercaUtente(6);
			}
			// ciclo do-while che dura fino a quando non viene selezionato un campo utente e un'azione relativa, oppure
			// fino a quando l'utente non chiude la finestra per uscire
		}while (table.getSelectedRow() == -1 && azione != -1);


		if (azione == 0 && genereUtente.equals("staff")) {
			uiLista.avvioListaPagamenti(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
			// richiamo lista pagamenti
		} else if (azione == 1 && genereUtente.equals("staff")) {
			uiLista.avvioListaOrdini(table.getModel().getValueAt(table.getSelectedRow(), 2).toString(), -1);
			// richiamo lista ordini
		} else if (azione == 0 && genereUtente.equals("amministratore")) {
			// richiamo metodo blocca-sblocca
			uiUtente.avvioBloccaSbloccaUtente(table.getModel().getValueAt(table.getSelectedRow(), 2).toString(),
					Boolean.parseBoolean((table.getModel().getValueAt(table.getSelectedRow(), 4).toString())));

		}
	}

  	public void avvioProdottiPiuVenduti() throws RemoteException
	{	// RF22
	}
}
