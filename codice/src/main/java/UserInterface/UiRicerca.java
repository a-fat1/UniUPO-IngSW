package UserInterface;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;
import java.awt.*;

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
	private int scelta, esitoGiacenza = -1;
	private String giacenza;
	ArrayList<HashMap<String, Object>> listaProdotti;
	
	// elementi grafici
	private JPanel pannelloGiacenza;
	private JTextField campoGiacenza;
	private JTable tabellaProdotti;
	private JScrollPane pannelloGiacenzaScorrevole;
	private String giacenzaPulsanti[] = new String[2];
	
	public UiRicerca(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiLista = (UiListaInterfaccia) registryUI.lookup("uiLista");
		uiUtente = (UiUtenteInterfaccia) registryUI.lookup("uiUtente");
		uiCarrello = (UiCarrelloInterfaccia) registryUI.lookup("uiCarrello");
		uiProdotto = (UiProdottoInterfaccia) registryUI.lookup("uiProdotto");
		gestoreRicerche = (GestoreRicercheInterfaccia) registryGestore.lookup("gestoreRicerche");
	}
	
	public void avvioRicercaProdotto() throws RemoteException
	{	// RF08
	}

	public void avvioProdottiInEsaurimento() throws RemoteException {
		
		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti

		do {
			this.mostraFormGiacenza();		// Mostra all'utente il form per inserire la giacenza

			if (scelta == 0) {		// Scelta = 0 quando viene premuto ok in mostraFormGiacenza()
				esitoGiacenza = gestoreRicerche.controlloGiacenza(giacenza);	// Viene richiamato controlloGiacenza per controllare la giacenza
				if(esitoGiacenza == 1 || esitoGiacenza == 2 || esitoGiacenza == 3)
					mostraErrore(esitoGiacenza);	// In caso di errore viene richiamato mostraErrore per visualizzare l'errore specifico
				else {
					System.out.println("esitoGiacenza = " + esitoGiacenza + "\n");
					listaProdotti = gestoreRicerche.ricercaProdotti(giacenza);		// Con la giacenza corretta viene richiamato ricercaProdotti per recuperare i dati dei prodotti
					if(listaProdotti.size() == 0) {
						System.out.println("dimensione listaProdotti = " + listaProdotti.size() + "\n");
						mostraErrore(4);		// Se l'ArrayList non ha i dati si richiama mostraErrore con parametro 4
					}
					else
						mostraListaProdotti(listaProdotti);		// mostraListaProdotti viene richiamato per visualizzare i dati in modo adeguato
				}
			}
		} while (scelta == 0);		// scelta != 0 quando l'utente preme torna al menu o il pulsante x

		esitoGiacenza = -1;		// esitoGiacenza = -1 viene inizializzato all'inizio e alla fine di avvioProdottiInEsaurimento()
		System.out.println("esitoGiacenza = " + esitoGiacenza + " --> fine avvioProdottiInEsaurimento()\n");
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
		if (esitoGiacenza == 1 || esitoGiacenza == 2 || esitoGiacenza == 3) {
			// Il background di campoGiacenza viene impostato con il rosso quando l'utente sbaglia a inserire la giacenza
			campoGiacenza.setBackground(Color.RED);
			System.out.println("esitoGiacenza = " + esitoGiacenza + " --> imposta background rosso\n");
		}
		else if (esitoGiacenza == -1)	// Con la prima esecuzione di mostraFormGiacenza(), invece, il background rimane bianco
			System.out.println("esitoGiacenza = " + esitoGiacenza + " --> inizio avvioProdottiInEsaurimento()\n");
		
		pannelloGiacenza.add(campoGiacenza);	// Aggiunge JTextField nel pannello

		// Definisce nome pulsanti e mostra interfaccia
		giacenzaPulsanti[0] = "Ok";
		giacenzaPulsanti[1] = "Torna al menu";
		scelta = UiRicerca.showOptionDialog(null, pannelloGiacenza, "Prodotti in esaurimento", DEFAULT_OPTION, PLAIN_MESSAGE, null, giacenzaPulsanti, null);

		if (scelta == 0) {		// Quando viene premuto il pulsante ok il valore 0 viene memorizzato in scelta
			giacenza = campoGiacenza.getText();		// E il contenuto scritto dall'utente viene salvato nella stringa giacenza
			System.out.println("scelta = " + scelta + " --> ok\n");
			System.out.println("giacenza = " + giacenza + "\n");
		}
		else if(scelta == 1)
				System.out.println("scelta = " + scelta + " --> torna al menu\n");
			else
				System.out.println("scelta = " + scelta + " --> pulsante X\n");
	}
	
	private void mostraErrore(int tipoErrore) {

		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti

		System.out.println("tipoErrore = " + tipoErrore + "\n");

		// In base al valore di tipoErrore viene visualizzata l'interfaccia che contiene il messaggio di errore specifico 
		if (tipoErrore == 1)
			UiRicerca.showMessageDialog(null, "La giacenza deve contenere solo caratteri numerici positivi.", "Errore giacenza invalida", UiRicerca.PLAIN_MESSAGE, null);
		if (tipoErrore == 2)
			UiRicerca.showMessageDialog(null, "La giacenza deve contenere un numero intero inferiore a " + Integer.MAX_VALUE + ".", "Errore giacenza vuota", UiRicerca.PLAIN_MESSAGE, null);
		if (tipoErrore == 3)
			UiRicerca.showMessageDialog(null, "La giacenza deve essere maggiore di zero.", "Errore giacenza nulla", UiRicerca.PLAIN_MESSAGE, null);
		if (tipoErrore == 4) {
			// Definisce il pannello e un messaggio di testo
			pannelloGiacenza = new JPanel();
			pannelloGiacenza.add(new JLabel("Nessun prodotto presente con giacenza uguale o inferiore a quella selezionata."));

			// Definisce nome pulsanti e mostra interfaccia
			giacenzaPulsanti[0] = "Nuova ricerca";
			giacenzaPulsanti[1] = "Torna al menu";
			scelta = UiRicerca.showOptionDialog(null, pannelloGiacenza, "Errore lista vuota", DEFAULT_OPTION, PLAIN_MESSAGE, null, giacenzaPulsanti, null);
		}

		if (scelta == 0)
			if (tipoErrore == 1 || tipoErrore == 2 || tipoErrore == 3)
				System.out.println("scelta = " + scelta + " --> ok o pulsante X\n");
			else
				System.out.println("scelta = " + scelta + " --> nuova ricerca\n");
		else if(scelta == 1)
				System.out.println("scelta = " + scelta + " --> torna al menu\n");
			else
				System.out.println("scelta = " + scelta + " --> pulsante X\n");
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
		System.out.println("Colonne tabella");
		for (String attributo : listaProdotti.get(colonna).keySet()) {
			tabellaProdotti.getColumnModel().getColumn(colonna).setHeaderValue(attributo);	// Aggiunge e definisce l'attributo della colonna
			System.out.println("\n" + attributo + ":");
			riga = 0;
    		for (HashMap<String, Object> prodotto : listaProdotti) {
				tabellaProdotti.setValueAt(prodotto.get(attributo), riga, colonna);		// Aggiunge il contenuto delle righe
				System.out.println(prodotto.get(attributo));
				riga++;
			}
			colonna++;
		}
		System.out.println();

		tabellaProdotti.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);		// Disabilita ridimensionamento automatico

		// Creazione pannello scorrevole (utilizzando tabellaProdotti), definizione delle dimensioni e inserimento in pannelloGiacenza
		pannelloGiacenzaScorrevole = new JScrollPane(tabellaProdotti);
		pannelloGiacenzaScorrevole.setPreferredSize(new Dimension(650, 250));
		pannelloGiacenza.add(pannelloGiacenzaScorrevole);

		// Definisce nome pulsanti e mostra interfaccia
		giacenzaPulsanti[0] = "Nuova ricerca";
		giacenzaPulsanti[1] = "Torna al menu";
		scelta = UiRicerca.showOptionDialog(null, pannelloGiacenza, "Lista prodotti", DEFAULT_OPTION, PLAIN_MESSAGE, null, giacenzaPulsanti, null);

		if (scelta == 0)
				System.out.println("scelta = " + scelta + " --> nuova ricerca\n");
			else if(scelta == 1)
					System.out.println("scelta = " + scelta + " --> torna al menu\n");
				else
					System.out.println("scelta = " + scelta + " --> pulsante X\n");
	}

	public void avvioRicercaUtente() throws RemoteException
	{	// RF19	
	}

  	public void avvioProdottiPiuVenduti() throws RemoteException
	{	// RF22
	}
}
