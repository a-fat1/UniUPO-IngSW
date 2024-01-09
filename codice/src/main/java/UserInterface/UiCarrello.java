package UserInterface;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;

import java.awt.GridLayout; /*RF09*/

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
		
		this.showMessageDialog(null, "Il carrello e' stato aggiornato", "Carrello 			aggiornato", this.PLAIN_MESSAGE, null);

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
		public void mostraErrore(int numberError){
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
