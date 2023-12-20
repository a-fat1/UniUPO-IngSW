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
	
	// elementi grafici
	// RF19
	private JPanel searchPanel;

	private JPanel elencoUtentiRicerca;
	private JLabel labelUsername;
	private JLabel labelNome;
	private JLabel labelCognome;
	private JComboBox comboMenu;
	private BoxLayout boxLayout;
	private JTextField fieldUsername;
	private JTextField fieldNome;
	private JTextField fieldCognome;

	private int result;

	private String nome;
	private String cognome;
	private String username;

	private ArrayList<HashMap<String, Object>> elencoUtenti;
	private String scelta;

	private final String[] pulsanteRicerca;

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

		labelUsername = new JLabel("Inserisci uno username");
		labelNome = new JLabel("Inserisci un nome");
		labelCognome = new JLabel("Inserisci un cognome");
		fieldUsername = new JTextField("", 10);
		fieldNome = new JTextField("", 10);
		fieldCognome = new JTextField("", 10);

		String[] scelteMenuRicercaUtente = new String[2];
		scelteMenuRicercaUtente[0] = "Ricerca per nome-cognome";
		scelteMenuRicercaUtente[1] = "Ricerca per username";
		comboMenu = new JComboBox(scelteMenuRicercaUtente);

		colonneAmministratore = new String[4];
		colonneAmministratore[0] = "Nome";
		colonneAmministratore[1] = "Cognome";
		colonneAmministratore[2] = "Username";
		colonneAmministratore[3] = "Tipo utente";

		colonneStaff = new String[5];
		colonneStaff[0] = "Nome";
		colonneStaff[1] = "Cognome";
		colonneStaff[2] = "Username";
		colonneStaff[3] = "Tipo utente";
		colonneStaff[4] = "Attivo";

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

		elencoUtenti = new ArrayList<>();

	}
	
	public void avvioRicercaProdotto() throws RemoteException
	{	// RF08
	}

	public void avvioProdottiInEsaurimento() throws RemoteException
	{	// RF18	
	}

	public void avvioRicercaUtente(String genereUtente) throws RemoteException
	{	// RF19

		int esitoControllo = 0;

		while((esitoControllo != 4) && (result != -1)){

			mostraFormRicerca();

			if(scelta.equals("Ricerca per nome-cognome"))
			{
				esitoControllo = gestoreRicerche.controlloParametriRicercaUtente(nome, cognome);
				if((esitoControllo == 2 || esitoControllo == 3) && (result != -1))
				{
					mostraErroreRicercaUtente(esitoControllo);
				}
			}
			else
			{
				esitoControllo = gestoreRicerche.controlloParametriRicercaUtente(username);
				if((esitoControllo == 1) && (result != -1))
				{
					mostraErroreRicercaUtente(1);
				}
			}

			if((esitoControllo == 4) && genereUtente.equals("staff"))
			{
				if(scelta.equals("Ricerca per nome-cognome"))
				{
					elencoUtenti = gestoreRicerche.cercaUtenteNonBloccato(nome, cognome);
				}
				else if(scelta.equals("Ricerca per username"))
				{
					elencoUtenti = gestoreRicerche.cercaUtenteNonBloccato(username);
				}
			}
			else if((esitoControllo == 4) && genereUtente.equals("amministratore"))
			{
				if(scelta.equals("Ricerca per nome-cognome"))
				{
					elencoUtenti = gestoreRicerche.cercaUtenteBloccatoNonBloccato(nome, cognome);
				}
				else if(scelta.equals("Ricerca per username"))
				{
					elencoUtenti = gestoreRicerche.cercaUtenteBloccatoNonBloccato(username);
				}
			}

			if((elencoUtenti.isEmpty()) && (result != -1) && (esitoControllo == 4))
			{
				mostraErroreRicercaUtente(5);
			}

			if((!elencoUtenti.isEmpty()) && (result != -1) && (esitoControllo == 4))
			{
				mostraElencoRicercaUtente(elencoUtenti, genereUtente);
			}
		}
	}

	private void mostraFormRicerca() throws RemoteException
	{

		result = JOptionPane.showOptionDialog(null, searchPanel, "Ricerca utente (clicca X per uscire)", DEFAULT_OPTION, QUESTION_MESSAGE, null, pulsanteRicerca, "Ricerca");
		scelta = Objects.requireNonNull(comboMenu.getSelectedItem()).toString();
		nome = fieldNome.getText();
		cognome = fieldCognome.getText();
		username = fieldUsername.getText();

	}


	private void mostraErroreRicercaUtente(int tipo) throws RemoteException
	{
		String messaggio;

		if(tipo == 1)
		{
			messaggio = "format username errato: assicurati di inserire almeno tre caratteri nel campo \"username\".";
			showMessageDialog(null, messaggio, "errore username (clicca X per chiudere)", ERROR_MESSAGE);

		}

		if(tipo == 2)
		{
			messaggio = "format nome errato: assicurati di inserire almeno tre caratteri nel campo \"nome\" e di " +
						"inserire solo caratteri letterali";
			showMessageDialog(null, messaggio, "errore nome (clicca X per chiudere)", ERROR_MESSAGE);
		}

		if(tipo == 3)
		{
			messaggio = "format cognome errato: assicurati di inserire almeno tre caratteri nel campo \"cognome\" e di " +
						"inserire solo caratteri letterali";
			showMessageDialog(null, messaggio, "errore cognome (clicca X per chiudere)", ERROR_MESSAGE);
		}

		if(tipo == 5)
		{
			messaggio = "Non sono stati trovati risultati per la tua ricerca";
			showMessageDialog(null, messaggio, "nessun risultato (clicca X per chiudere)", ERROR_MESSAGE);
		}
	}

	private void mostraElencoRicercaUtente(ArrayList<HashMap<String, Object>> elencoUtenti, String genereUtente) throws RemoteException
	{
		System.out.println("genereUtente: " + genereUtente);
		System.out.println("Utenti trovati: " + elencoUtenti.toString());
	}


  	public void avvioProdottiPiuVenduti() throws RemoteException
	{	// RF22
	}
}
