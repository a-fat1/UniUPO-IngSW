package UserInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

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
	private JLabel labelUsername;
	private JLabel labelNome;
	private JLabel labelCognome;
	private JComboBox comboMenu;
	private BoxLayout boxLayout;
	private JTextField fieldUsername;
	private JTextField fieldNome;
	private JTextField fieldCognome;

	private String scelteMenuRicercaUtente[];
	private int result;

	private String nome;
	private String cognome;
	private String username;

	private String scelta;

	private String pulsanteRicerca[];
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

		scelteMenuRicercaUtente = new String[2];
		scelteMenuRicercaUtente[0] = "Ricerca per nome-cognome";
		scelteMenuRicercaUtente[1] = "Ricerca per username";
		comboMenu = new JComboBox(scelteMenuRicercaUtente);

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

		while(esitoControllo != 4){

			mostraFormRicerca();

			if(scelta.equals("Ricerca per nome-cognome"))
			{
				esitoControllo = gestoreRicerche.controlloParametriRicercaUtente(nome, cognome);
				System.out.println("Esito: " + esitoControllo);
			}
			else
			{
				esitoControllo = gestoreRicerche.controlloParametriRicercaUtente(username);
				System.out.println("Esito: " + esitoControllo);
			}
		}
	}

	private void mostraFormRicerca() throws RemoteException
	{

		result = JOptionPane.showOptionDialog(null, searchPanel, "Ricerca utente (clicca X per uscire)", DEFAULT_OPTION, QUESTION_MESSAGE, null, pulsanteRicerca, "Ricerca");
		scelta = comboMenu.getSelectedItem().toString();
		nome = fieldNome.getText();
		cognome = fieldCognome.getText();
		username = fieldUsername.getText();

	}


	private void mostraErroreRicercaUtente() throws RemoteException
	{

	}

	private void mostraElencoRicercaUtente() throws RemoteException
	{

	}


  	public void avvioProdottiPiuVenduti() throws RemoteException
	{	// RF22
	}
}
