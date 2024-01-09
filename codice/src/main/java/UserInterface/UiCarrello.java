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
	}
	
	public void avvioVisualizzaCarrello() throws RemoteException
	{	// RF05	
	}

	public void avvioEffettuaOrdine() throws RemoteException
	{	// RF06	
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

	
	public void avvioAggiungiAlCarrello() throws RemoteException
	{	// RF09	
	}
}
