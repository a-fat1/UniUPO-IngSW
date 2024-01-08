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
	private ArrayList<HashMap<String, Object>> elencoUtenti;
	private String sceltaRicerca;

	// RF19 - Ricerca Utente
	// Array di String per i pulsanti di ricerca e le colonne per la tabella dei dati estratti dal database
	private final String[] pulsanteRicerca;
	private final String[] pulsanteElencoUtentiStaff;
	private final String[] pulsanteElencoUtentiAdmin;
	private final String[] colonneStaff;
	private final String[] colonneAmministratore;
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
		// settings per UIRicercaUtente

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

		// RF19 - Ricerca Utente
		// Inizializzazione di ArrayList per gli utenti trovati
		elencoUtenti = new ArrayList<>();

	}
	
	public void avvioRicercaProdotto() throws RemoteException
	{	// RF08
	}

	public void avvioProdottiInEsaurimento() throws RemoteException
	{	// RF18	
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

		// set di esitoControllo con valore iniziale
		int esitoControllo = 0;
		// set di result = 0 per iniziare il ciclo
		sceltaUtente = 0;

		// loop fino a quando il controllo non è positivo (o l'utente esce dalla finestra)
		while((esitoControllo != 4) && (sceltaUtente != -1)){

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
			if((esitoControllo == 4) && genereUtente.equals("staff"))
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
			else if((esitoControllo == 4) && genereUtente.equals("amministratore"))
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
		fieldNome.setText("");
		fieldCognome.setText("");
		fieldUsername.setText("");
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
				messaggio = "format username errato: assicurati di inserire\nalmeno tre caratteri nel campo \"username\".";
				showMessageDialog(null, messaggio, "errore username (clicca X per chiudere)", ERROR_MESSAGE);
				break;
			}
			case 2: {
				messaggio = "format nome errato: assicurati di inserire\nalmeno tre caratteri nel campo \"nome\" e di\n" +
						    "inserire solo caratteri letterali";
				showMessageDialog(null, messaggio, "errore nome (clicca X per chiudere)", ERROR_MESSAGE);
				break;
			}
			case 3: {
				messaggio = "format cognome errato: assicurati di inserire\nalmeno tre caratteri nel campo \"cognome\" e di\n" +
						    "inserire solo caratteri letterali";
				showMessageDialog(null, messaggio, "errore cognome (clicca X per chiudere)", ERROR_MESSAGE);
				break;
			}
			case 5: {
				messaggio = "Non sono stati trovati risultati per la tua ricerca";
				showMessageDialog(null, messaggio, "nessun risultato (clicca X per chiudere)", ERROR_MESSAGE);
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
	private void mostraElencoRicercaUtente(ArrayList<HashMap<String, Object>> elencoUtenti, String genereUtente) throws RemoteException
	{

		// RF19 - Ricerca Utente
		// Riccardo Nazzari, Andrea Benedetto

		// inizializzazione variabili
		Object[][] utentiTabella;
		JTable table = null;
		int azione = -3;

		// se l'utente che ha richiesto è di genere "staff", allora inizializzo la tabella con 4 colonne in quanto
		// deve essere mostrato solo nome-cognome-username-tipo
		if(genereUtente.equals("staff"))
		{
			utentiTabella = new Object[elencoUtenti.size()][colonneStaff.length];

			// estraggo i campi
			for(int i=0; i < elencoUtenti.size(); i++)
			{
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
		else if(genereUtente.equals("amministratore"))
		{
			utentiTabella = new Object[elencoUtenti.size()][colonneAmministratore.length];

			// estraggo i campi
			for(int i=0; i < elencoUtenti.size(); i++)
			{
				utentiTabella[i][0] = elencoUtenti.get(i).get("nome");
				utentiTabella[i][1] = elencoUtenti.get(i).get("cognome");
				utentiTabella[i][2] = elencoUtenti.get(i).get("username");
				utentiTabella[i][3] = elencoUtenti.get(i).get("tipo");
				if(elencoUtenti.get(i).get("attivo").toString().equals("1")) {
					utentiTabella[i][4] = "True";
				}
				else {
					utentiTabella[i][4] = "False";
				}
			}
			// creo la tabella
			table = new JTable(utentiTabella, colonneAmministratore);
		}

		// mi assicuro che la tabella sia stata creata correttamente
        assert table != null;

		// modifico la dimensione della riga per ogni tabella
		table.setRowHeight(25);

		// setto la tabella in modo che si possa selezionare solo un campo
        ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// creo la tabella scroll e ne gestisco le dimensioni
		JScrollPane tabella = new JScrollPane(table);
		tabella.setPreferredSize((new Dimension(500, 150)));

		// ciclo while che dura fino a quando non viene selezionato un campo utente e un'azione relativa, oppure
		// fino a quando l'utente non chiude la finestra per uscire
		while(table.getSelectedRow() == -1 && azione != -1)
		{
			// se il genereUtente è di tipo "staff" mostro solo i pulsanti per lo staff
			if(genereUtente.equals("staff"))
				azione = showOptionDialog(null, tabella, "Elenco utenti",
						DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
						pulsanteElencoUtentiStaff, null);

			// se il genereUtente è di tipo "amministratore" mostro solo i pulsanti per l'amministratore
			else if(genereUtente.equals("amministratore"))
				azione = showOptionDialog(null, tabella, "Elenco utenti",
						DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
						pulsanteElencoUtentiAdmin, null);

		}

		if(azione == 0 && genereUtente.equals("staff"))
		{
			//uiLista.avvioListaPagamenti(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
			// richiamo lista pagamenti
		}
		else if(azione == 1 && genereUtente.equals("staff"))
		{
			//uiLista.avvioListaOrdini(table.getModel().getValueAt(table.getSelectedRow(), 2).toString(), -1);
			// richiamo lista ordini
		}
		else if(azione == 0 && genereUtente.equals("amministratore"))
		{
			//System.out.println(value);
			// richiamo blocca\sblocca utente
			//uiUtente.avvioBloccaSbloccaUtente(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
			// devo mandare anche il parametro per vedere se è attivo o meno
		}
	}


  	public void avvioProdottiPiuVenduti() throws RemoteException
	{	// RF22
	}
}
