package UserInterface;

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
	private String titolo;
	private String autore;
	private String editore;
	private int anno;
	private String tipo;
	
	// elementi grafici
	
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
	
	/*public void avvioRicercaProdotto() throws RemoteException
	{	
		// RF08	
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiUtente = (UiUtenteInterfaccia) registryUI.lookup("uiUtente");
		gestoreRicerche = (GestoreRicercheInterfaccia) registryGestore.lookup("gestoreRicerche");
		titolo= "", autore="", editore="", anno="", tipo="";
		boolean esito = gestoreRicerche.controllaParametri(titolo, autore, editore, anno, tipo);
		ArrayList<String> risultato = null;
		if(esito){
			risultato = gestoreRicerche.cercaProdotto( titolo, autore);
			for(String elemento : risultato){
				System.out.println(elemento);
			}
		} else {
			System.out.println("Non hai inserito tutti i parametri obbligatori");
		}
	}*/
	
	public void avvioRicercaProdotto() throws RemoteException {
	    // Dichiarazione e inizializzazione delle variabili
	    String titolo = "Il tuo titolo";
	    String autore = "Il tuo autore";
	    String editore = "Il tuo editore";
	    String anno = "Il tuo anno";  // Assicurati che il tipo sia corretto (Integer o String)
	    String tipo = "Il tuo tipo";
	    String hostGestore = "Indirizzo IP del tuo host";  // Sostituisci con l'indirizzo IP corretto

	    try {
	        // Inizializzazione dei registri
	        registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
	        registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

	        // Inizializzazione delle interfacce
	        uiUtente = (UiUtenteInterfaccia) registryUI.lookup("uiUtente");
	        gestoreRicerche = (GestoreRicercheInterfaccia) registryGestore.lookup("gestoreRicerche");
	        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto(titolo, autore, editore, anno, tipo);
	        //mostrare i risuiltati ottenuti nella finestra java swing..
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	public void avvioProdottiInEsaurimento() throws RemoteException
	{	// RF18	
	}

	public void avvioRicercaUtente() throws RemoteException
	{	// RF19	
	}

  	public void avvioProdottiPiuVenduti() throws RemoteException
	{	// RF22
	}
}
