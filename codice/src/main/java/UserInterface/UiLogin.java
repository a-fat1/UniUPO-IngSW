package UserInterface;

import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;

import DataBase.DbUtenti;
import UserInterface.*;
import Elaborazione.*;

public class UiLogin extends JOptionPane implements UiLoginInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private UiUtenteInterfaccia uiUtente;
	private UiNotificaInterfaccia uiNotifica;
	private UiRicercaInterfaccia uiRicerca;
	private UiListaInterfaccia uiLista;
	private UiCarrelloInterfaccia uiCarrello;
	private UiProdottoInterfaccia uiProdotto;
	private GestoreAccessiInterfaccia gestoreAccessi;

	// attributi
	// RF00: login (Codetta)
	private int scelta;
	private int sceltaMenu;
	private String username;
	private String password;
	private HashMap<String, Object> utente;
	private	String esitoControllo;	
	private	String esitoRicerca;

	// attributi
	//RF03: Aggiorna password
	private String passwordAttuale;
	private String nuovaPassword;
	private int esitoControlloPassword;
	private int richiesta;
	
	// elementi grafici
	// RF00: login (Codetta)
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPanel loginPanel;
	private String pulsantiLogin[];
	private String pulsantiMenuStaff[];
	private String pulsantiMenuCliente[];
	private String pulsantiMenuAmministratore[];
	private JLabel menuLabel1;
	private JLabel menuLabel2;
	private JList<String> menuList;
	private JPanel menuPanel;

	// elementi grafici
	//RF03: Aggiorna password
	private JLabel passwordAttualeLabel;
	private JLabel nuovaPasswordLabel;
	private JPasswordField passwordAttualeField;
	private JPasswordField nuovaPasswordField;
	private JPanel aggiornaPasswordPanel;
	private JPanel nuovaPasswordPanel;
	BoxLayout boxLayoutNP;
	BoxLayout boxLayoutAP;

	public UiLogin(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiUtente = (UiUtenteInterfaccia) registryUI.lookup("uiUtente");
		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		uiRicerca = (UiRicercaInterfaccia) registryUI.lookup("uiRicerca");
		uiLista = (UiListaInterfaccia) registryUI.lookup("uiLista");
		uiCarrello = (UiCarrelloInterfaccia) registryUI.lookup("uiCarrello");
		uiProdotto = (UiProdottoInterfaccia) registryUI.lookup("uiProdotto"); 

		gestoreAccessi = (GestoreAccessiInterfaccia) registryGestore.lookup("gestoreAccessi");
		 
		usernameLabel = new JLabel("username");
		passwordLabel = new JLabel("password");
		usernameField = new JTextField("", 10);
		usernameField.setToolTipText("scrivere qui la username");
		passwordField = new JPasswordField("", 10);
		passwordField.setToolTipText("scrivere qui la password");
		passwordField.setEchoChar('*');

		loginPanel = new JPanel(new GridLayout(2,2));
		loginPanel.add(usernameLabel);
		loginPanel.add(usernameField);
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordField); 

		pulsantiLogin = new String[2];
		pulsantiLogin[0] = "Crea utente";
		pulsantiLogin[1] = "Login";

		pulsantiMenuStaff = new String[10];
		pulsantiMenuStaff[0] = "Aggiorna username";
		pulsantiMenuStaff[1] = "Aggiorna password";
		pulsantiMenuStaff[2] = "Ricerca prodotto";
		pulsantiMenuStaff[3] = "Ricerca utente";
		pulsantiMenuStaff[4] = "Lista forniture";
		pulsantiMenuStaff[5] = "Lista pagamenti";
		pulsantiMenuStaff[6] = "Prodotti piu' venduti";
		pulsantiMenuStaff[7] = "Nuovo prodotto";
		pulsantiMenuStaff[8] = "Incrementa / decrementa prezzi";
		pulsantiMenuStaff[9] = "Prodotti in esaurimento";
		
		pulsantiMenuCliente = new String[8];
		pulsantiMenuCliente[0] = "Aggiorna username";
		pulsantiMenuCliente[1] = "Aggiorna password";
		pulsantiMenuCliente[2] = "Ricerca prodotto";
		pulsantiMenuCliente[3] = "Visualizza carrello";
		pulsantiMenuCliente[4] = "Lista ordini";
		pulsantiMenuCliente[5] = "Lista pagamenti";
		pulsantiMenuCliente[6] = "Prodotti piu' venduti";
		pulsantiMenuCliente[7] = "Aggiorna domicilio";
		
		pulsantiMenuAmministratore = new String[5];
		pulsantiMenuAmministratore[0] = "Aggiorna username";
		pulsantiMenuAmministratore[1] = "Aggiorna password";
		pulsantiMenuAmministratore[2] = "Crea utente";
		pulsantiMenuAmministratore[3] = "Ricerca utente";
		pulsantiMenuAmministratore[4] = "Ricerca notifiche";

		menuLabel1 = new JLabel();
		menuLabel2 = new JLabel("Seleziona servizio. (X per logout)");
		menuList = new JList<String>();
		menuList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);	
		menuList.setToolTipText("seleziona un servizio e clicca OK");	

		menuPanel = new JPanel(new BorderLayout(5, 5));
		menuPanel.add(menuLabel1, BorderLayout.NORTH);
		menuPanel.add(menuLabel2, BorderLayout.CENTER);
		menuPanel.add(menuList, BorderLayout.SOUTH);

		passwordAttualeLabel= new JLabel("Password attuale");
		passwordAttualeField= new JPasswordField("", 10);

		aggiornaPasswordPanel = new JPanel();
		boxLayoutAP = new BoxLayout(aggiornaPasswordPanel, BoxLayout.PAGE_AXIS);
		aggiornaPasswordPanel.setLayout(boxLayoutAP);
		aggiornaPasswordPanel.add(passwordAttualeLabel);
		aggiornaPasswordPanel.add(passwordAttualeField);
		aggiornaPasswordPanel.add(new JLabel("Inserire la password attualmente usata"));

		nuovaPasswordLabel= new JLabel("Nuova password");
		nuovaPasswordField= new JPasswordField("", 10);

		nuovaPasswordPanel = new JPanel();
		boxLayoutNP = new BoxLayout(nuovaPasswordPanel, BoxLayout.PAGE_AXIS);
		nuovaPasswordPanel.setLayout(boxLayoutNP);
		nuovaPasswordPanel.add(nuovaPasswordLabel);
		nuovaPasswordPanel.add(nuovaPasswordField);
		nuovaPasswordPanel.add(new JLabel("La password deve essere almeno 6 caratteri e contenere almeno una lettera e un numero"));
	}
	
	public void avvioLogin() throws RemoteException
	{	
		//RF00: login
    		//autore: Codetta
		do
		{
			this.mostraFormLogin();
			
			if (scelta==0) 
				uiUtente.avvioCreaUtente();

			if (scelta==1) // login
			{		
				esitoControllo=gestoreAccessi.controllaFormatoCredenziali(username, password);
				if (esitoControllo.contains("errore"))
					this.mostraErrore(esitoControllo);
				else
				{
					esitoRicerca=gestoreAccessi.ricercaCredenziali(username, password);
					if (esitoRicerca.contains("errore"))
						this.mostraErrore(esitoRicerca);
					else
					{
						utente=gestoreAccessi.ricercaUtente(username);
						uiNotifica.avvioVisualizzaNotifiche();
						do
						{
							this.mostraMenu((String)utente.get("nome"), ((String)utente.get("tipo")));
							if (sceltaMenu==0)
								this.avvioAggiornaUsername();
							if (sceltaMenu==1)
								this.avvioAggiornaPassword(true,username,password);
							if (sceltaMenu==2 && !((String)utente.get("tipo")).equals("amministratore"))
								uiRicerca.avvioRicercaProdotto();
							if (sceltaMenu==2 && ((String)utente.get("tipo")).equals("amministratore"))			
								uiUtente.avvioCreaUtente();
							if (sceltaMenu==3 && ((String)utente.get("tipo")).equals("cliente"))
								uiCarrello.avvioVisualizzaCarrello();
							if (sceltaMenu==3 && !((String)utente.get("tipo")).equals("cliente"))
								uiRicerca.avvioRicercaUtente();
							if (sceltaMenu==4 && ((String)utente.get("tipo")).equals("staff"))
								uiLista.avvioListaForniture();
							if (sceltaMenu==4 && ((String)utente.get("tipo")).equals("cliente"))
								uiLista.avvioListaOrdini();
							if (sceltaMenu==4 && ((String)utente.get("tipo")).equals("amministratore"))
								uiNotifica.avvioRicercaNotifiche();
							if (sceltaMenu==5 && !((String)utente.get("tipo")).equals("amministratore"))
								uiLista.avvioListaPagamenti();
							if (sceltaMenu==6 && !((String)utente.get("tipo")).equals("amministratore"))
								uiRicerca.avvioProdottiPiuVenduti();
							if (sceltaMenu==7 && ((String)utente.get("tipo")).equals("cliente"))
								uiUtente.avvioAggiornaDomicilio();
							if (sceltaMenu==7 && ((String)utente.get("tipo")).equals("staff"))
								uiProdotto.avvioNuovoProdotto();
							if (sceltaMenu==8 && ((String)utente.get("tipo")).equals("staff"))
								uiProdotto.avvioIncrementaDecrementaPrezzi();
							if (sceltaMenu==9 && ((String)utente.get("tipo")).equals("staff"))
								uiRicerca.avvioProdottiInEsaurimento();
						}
						while (sceltaMenu != -1);
					}
				}
			}					
		}
		while (scelta != -1);
	}

	private void mostraFormLogin()
	{
		//RF00: login
    		//autore: Codetta
		scelta = this.showOptionDialog(null, loginPanel, "Login (clicca su X per uscire)", DEFAULT_OPTION, QUESTION_MESSAGE, null, pulsantiLogin, "Login");

		if (scelta == 0) // crea utente 
		{
			usernameField.setText("");
			passwordField.setText("");
			usernameField.setBackground(Color.WHITE);
			passwordField.setBackground(Color.WHITE);
		}
		if (scelta == 1) // login
		{
			username = usernameField.getText();
			password = new String(passwordField.getPassword());
			usernameField.setBackground(Color.WHITE);
			passwordField.setBackground(Color.WHITE);
    		}	
	}		

	private void mostraErrore(String tipoErrore)
	{
		//RF00: login
    		//autore: Codetta
		String messaggio="";

        	if (tipoErrore.equals("erroreUsername"))
		{
            		messaggio="Username mancante.";
			usernameField.setBackground(Color.YELLOW);
		}
		if (tipoErrore.equals("errorePassword"))
		{
            		messaggio="Password mancante.";
			passwordField.setBackground(Color.YELLOW);
		}
		if (tipoErrore.equals("erroreCredenziali"))
		{
            		messaggio="Credenziali mancanti.";
			usernameField.setBackground(Color.YELLOW);
			passwordField.setBackground(Color.YELLOW);
		}
		if (tipoErrore.equals("erroreAssente"))
		{
            		messaggio="Credenziali errate.";
			usernameField.setBackground(Color.RED);
			passwordField.setBackground(Color.RED);
		}
		if (tipoErrore.equals("erroreDisattivato"))
		{
            		messaggio="Account bloccato.";
			usernameField.setBackground(Color.RED);
			passwordField.setBackground(Color.RED);
		}

		messaggio = messaggio + "\n(clicca su OK o X per continuare)";

		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE);
	}

	private void mostraMenu(String nomeUtente, String tipoUtente)
	{
		//RF00: login
    		//autore: Codetta

		int pulsante;

		usernameField.setText("");
		passwordField.setText("");
		usernameField.setBackground(Color.WHITE);
		passwordField.setBackground(Color.WHITE);

		menuLabel1.setText("Ciao\n " + nomeUtente + "! (" + tipoUtente + ")");

		if (tipoUtente.equals("staff"))
			menuList.setListData(pulsantiMenuStaff);
		if (tipoUtente.equals("cliente"))
			menuList.setListData(pulsantiMenuCliente);
		if (tipoUtente.equals("amministratore"))
			menuList.setListData(pulsantiMenuAmministratore);
		menuList.setSelectedIndex(0);

		pulsante = this.showConfirmDialog(null, menuPanel, "Menu " + tipoUtente, this.DEFAULT_OPTION, this.QUESTION_MESSAGE, null);
		if (pulsante == this.OK_OPTION)
			sceltaMenu = menuList.getSelectedIndex();
		else
			sceltaMenu = -1;
	}

	public void avvioAggiornaUsername() throws RemoteException
	{ 	// RF23
	}

	public void avvioAggiornaPassword(Boolean loggato, String username, String password) throws RemoteException
	{ 	// RF03
		if(loggato){
			do{
				this.mostraFormPasswordAttuale();

				if(richiesta==OK_OPTION) {
					esitoControlloPassword = gestoreAccessi.verificaCredenziali(passwordAttuale, password);

					if (esitoControlloPassword == 4) {
						this.mostraErrore(4);
					}
				}
				if(richiesta==CANCEL_OPTION){
					return;
				}
			} while(esitoControlloPassword!=0);
		}

		do {
			this.mostraFormNuovaPassword();

			esitoControlloPassword=gestoreAccessi.controlloNuovaPassword(nuovaPassword);

			if(richiesta==CANCEL_OPTION){
				return;
			}
			if(esitoControlloPassword==1){
				this.mostraErrore(1);
			}
			if(esitoControlloPassword==2){
				this.mostraErrore(2);
			}
			if(esitoControlloPassword==3){
				this.mostraErrore(3);
			}
			if(esitoControlloPassword==0){
				//query utente
				this.mostraMessaggioDiSuccesso();
            }

		}while (esitoControlloPassword!=0 && richiesta == OK_OPTION);
	}

	private void mostraFormPasswordAttuale(){
		richiesta = this.showConfirmDialog(null, aggiornaPasswordPanel, "Aggiorna password", this.OK_CANCEL_OPTION);

		passwordAttuale=new String(passwordAttualeField.getPassword());

		passwordAttualeField.setBackground(Color.white);
	}

	private void mostraErrore(int nErrore){

		String messaggio="";

		if(nErrore==4){
			passwordAttualeField.setBackground(Color.yellow);
			messaggio="ERRORE"+"\nLa password inserita non coincide con quella attuale"+"\nSi prega di riprovare";

		}
		if(nErrore==1){
			nuovaPasswordField.setBackground(Color.yellow);
			messaggio="ERRORE"+"\nLa password inserita deve contenere almeno 6 caratteri"+"\nSi prega di riprovare";
		}
		if(nErrore==2){
			nuovaPasswordField.setBackground(Color.yellow);
			messaggio="ERRORE"+"\nLa password inserita deve contenere almeno una lettera"+"\nSi prega di riprovare";
		}
		if(nErrore==3){
			nuovaPasswordField.setBackground(Color.yellow);
			messaggio="ERRORE"+"\nLa password inserita deve contenere almeno un numero"+"\nSi prega di riprovare";
		}

		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE);
	}

	private void mostraFormNuovaPassword(){
		richiesta = this.showConfirmDialog(null, nuovaPasswordPanel, "Aggiorna password", this.OK_CANCEL_OPTION);

		nuovaPassword=new String(nuovaPasswordField.getPassword());

		nuovaPasswordField.setBackground(Color.white);
	}

	private void mostraMessaggioDiSuccesso(){
		String messaggio= "La password e' stata cambiata con successo!"+"\nOra potrai accedere con la tua nuova password";
		this.showMessageDialog(null, messaggio, "Aggiorna Password", this.INFORMATION_MESSAGE);
	}
}
