package UserInterface;

import java.awt.*;
import java.util.HashMap;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;

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

	//attributi
	// RF23: Aggiorna Usernamme
	private String nuovo_username;
	private String messaggioUsername;
	private int esito;
	private int richiestaUsername;
	private boolean dup;

	//elementi grafici
	// RF23: Aggiorna Username
	private JTextField fieldUsername = new JTextField("", 15);
	JPanel panel;

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

		pulsantiMenuAmministratore = new String[6];
		pulsantiMenuAmministratore[0] = "Aggiorna username";
		pulsantiMenuAmministratore[1] = "Aggiorna password";
		pulsantiMenuAmministratore[2] = "Crea utente";
		pulsantiMenuAmministratore[3] = "Ricerca utente";
		pulsantiMenuAmministratore[4] = "Ricerca notifiche";
		pulsantiMenuAmministratore[5] = "Genera notifica";

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
		nuovaPasswordPanel.add(new JLabel("La password deve essere almeno 6 caratteri"));
		nuovaPasswordPanel.add(new JLabel("e contenere almeno una lettera e un numero"));
	}

	public void avvioLogin() throws RemoteException, NotBoundException
	{
		//RF00: login
    		//autore: Codetta
		do
		{
			this.mostraFormLogin();

			if (scelta==0)
				uiUtente.avvioCreaUtente(true);

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
						uiNotifica.avvioVisualizzaNotifiche((String)utente.get("tipo"));
						do
						{
							this.mostraMenu((String)utente.get("nome"), ((String)utente.get("tipo")));
							if (sceltaMenu==0)
								this.avvioAggiornaUsername(username);
							if (sceltaMenu==1)
								password=this.avvioAggiornaPassword(true,username,password);
							if (sceltaMenu==2 && !((String)utente.get("tipo")).equals("amministratore"))
								uiRicerca.avvioRicercaProdotto((String)utente.get("tipo"), username);
							if (sceltaMenu==2 && ((String)utente.get("tipo")).equals("amministratore"))
								uiUtente.avvioCreaUtente(false);
							if (sceltaMenu==3 && ((String)utente.get("tipo")).equals("cliente"))
								uiCarrello.avvioVisualizzaCarrello(username);
							if (sceltaMenu==3 && !((String)utente.get("tipo")).equals("cliente"))
								uiRicerca.avvioRicercaUtente((String)utente.get("tipo"));
							if (sceltaMenu==4 && ((String)utente.get("tipo")).equals("staff"))
								uiLista.avvioListaForniture();
							if (sceltaMenu==4 && ((String)utente.get("tipo")).equals("cliente"))
								uiLista.avvioListaOrdini((String) utente.get("username"), -1 );
							if (sceltaMenu==4 && ((String)utente.get("tipo")).equals("amministratore"))
								uiNotifica.avvioRicercaNotifiche();
							if (sceltaMenu==5 && ((String)utente.get("tipo")).equals("cliente"))
								uiLista.avvioListaPagamenti((String)utente.get("username"));
							if (sceltaMenu==5 && ((String)utente.get("tipo")).equals("staff"))
								uiLista.avvioListaPagamenti(null);
							if (sceltaMenu==6 && !((String)utente.get("tipo")).equals("amministratore"))
								uiRicerca.avvioProdottiPiuVenduti();
							if (sceltaMenu==7 && ((String)utente.get("tipo")).equals("cliente"))
								uiUtente.avvioAggiornaDomicilio(username, false);
							if (sceltaMenu==7 && ((String)utente.get("tipo")).equals("staff"))
								uiProdotto.avvioNuovoProdotto();
							if (sceltaMenu==8 && ((String)utente.get("tipo")).equals("staff"))
								uiProdotto.avvioIncrementaDecrementaPrezzi();
							if (sceltaMenu==9 && ((String)utente.get("tipo")).equals("staff"))
								uiRicerca.avvioProdottiInEsaurimento();
							if (sceltaMenu==5 && ((String)utente.get("tipo")).equals("amministratore"))
								uiNotifica.avvioGeneraNotifica("avviso", null);
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

	public String avvioAggiornaUsername(String vecchio_username) throws RemoteException
	{
		// RF23: Aggiorna Username
		//Brivio Marco, Serio Giulia
		esito = 0;
		nuovo_username = "";
		dup = false;
		do {
			this.mostraFormCambio(vecchio_username);
			if (nuovo_username != null) {
				try {
					dup = gestoreAccessi.verificaDuplicato(nuovo_username);
					if (dup) {
						mostraErroreInterfaccia(4);
					} else {
						esito = gestoreAccessi.verifica(vecchio_username, nuovo_username);
						if (esito == 1 || esito == 2 || esito == 3) {
							mostraErroreInterfaccia(esito);
						} else {
							gestoreAccessi.cambio(vecchio_username, nuovo_username);
							messaggioUsername = ("Cambio avvenuto con successo!");
							this.messaggio(messaggioUsername);
							username = nuovo_username;
							fieldUsername.setText("");
						}
					}
				} catch (RemoteException e) {
					messaggioUsername = ("Errore generale, riprovare");
					this.showMessageDialog(null, messaggioUsername, "Errore", this.OK_OPTION);
					fieldUsername.setText("");
				}
			}
		} while (dup == true || esito == 1 || esito == 2 || esito == 3);
		return nuovo_username;
	}

	private void mostraFormCambio(String vecchio) {
		// RF23: Aggiorna Username
		// Brivio Marco, Serio Giulia
		panel = new JPanel(new GridLayout(4, 2));
		panel.add(new JLabel("Username attuale: " + vecchio));
		panel.add(new JPanel());
		panel.add(new JPanel());
		panel.add(new JPanel());
		panel.add(new JLabel("Nuova username: "));
		panel.add(fieldUsername);

		richiestaUsername = JOptionPane.showConfirmDialog(
				null,
				panel,
				"Aggiorna la tua username",
				JOptionPane.OK_CANCEL_OPTION
		);
		if (richiestaUsername == JOptionPane.OK_OPTION) {
			nuovo_username = fieldUsername.getText();
		} else {
			nuovo_username = null;
		}
    }
	private void mostraErroreInterfaccia(int valore){
		// RF23: Aggiorna Username
		// Brivio Marco, Serio Giulia
		switch (valore) {
			case 1:
				messaggioUsername = "Formato username errato\ndeve avere almeno 3 caratteri";
				this.showMessageDialog(null, messaggioUsername, "Errore lunghezza", this.ERROR_MESSAGE);
				fieldUsername.setBackground(Color.YELLOW);
				fieldUsername.setText("");
				break;
			case 2:
				messaggioUsername = "Lo username deve essere\ndiverso da quello vecchio";
				this.showMessageDialog(null, messaggioUsername, "Errore username", this.ERROR_MESSAGE);
				fieldUsername.setBackground(Color.YELLOW);
				fieldUsername.setText("");
				break;
			case 3:
				messaggioUsername = "Lo username deve essere\ndiverso da quello vecchio e\ndeve avere almeno 3 caratteri";
				this.showMessageDialog(null, messaggioUsername, "Errore generale", this.ERROR_MESSAGE);
				fieldUsername.setBackground(Color.YELLOW);
				fieldUsername.setText("");
				break;
			case 4:
				messaggioUsername = ("Lo username esiste già");
				this.showMessageDialog(null, messaggioUsername, "Username duplicato", this.ERROR_MESSAGE);
				fieldUsername.setBackground(Color.YELLOW);
				fieldUsername.setText("");
				break;
			default:
				break;
		}
	}

	public void messaggio(String msg){
		// RF23: Aggiorna Username
		// Brivio Marco, Serio Giulia
		this.showMessageDialog(null, msg, "Cambio avvenuto", this.INFORMATION_MESSAGE);
		fieldUsername.setBackground(Color.WHITE);
	}

	/**
	 * Metodo di avvio del servizio di aggiornamento della password
	 * @param loggato variabile necessaria a capire se l'utente che sta cambiando la password risulta già loggato
	 * @param username	l'username dell'utente a cui sarà cambiata la password
	 * @param password	la password attualmente in uso, che verrà controllata in caso di utente già loggato (loggato: true)
	 * @return la nuova password aggiornata, altrimenti in caso di annullamento della richiesta la password attuale
	 * @throws RemoteException
	 */
	public String avvioAggiornaPassword(Boolean loggato, String username, String password) throws RemoteException
	{ 	// RF03
		//autori: Pietro Balossino, Andrija Jovic

		if(loggato){
			do{
				this.mostraFormPasswordAttuale();

				if(richiesta==OK_OPTION) {
					esitoControlloPassword = gestoreAccessi.verificaCredenziali(passwordAttuale, password);

					if (esitoControlloPassword == 4) {
						this.mostraErrore(4);
					}
				}
				if(richiesta==CANCEL_OPTION || richiesta==DEFAULT_OPTION){
					return password;
				}
			} while(esitoControlloPassword!=0);
		}

		do {
			this.mostraFormNuovaPassword();

			esitoControlloPassword=gestoreAccessi.controlloNuovaPassword(nuovaPassword);

			if(loggato) {
				if (richiesta == CANCEL_OPTION || richiesta == DEFAULT_OPTION) {
					return password;
				}
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
				gestoreAccessi.AggiornaPassword(username, nuovaPassword);
				password=nuovaPassword;
				this.mostraMessaggioDiSuccesso();
            }

		}while (esitoControlloPassword!=0);
		return password;
	}

	/**
	 *  Metodo per fornire finestra pop-up per l'inserimento della Password attuale dell'utente loggato
	 *  al fine di verificarla e consentire poi il cambio della stessa.
	 */
	private void mostraFormPasswordAttuale(){
		// RF03
		//autori: Pietro Balossino, Andrija Jovic

		passwordAttualeField.setText("");

		richiesta = this.showConfirmDialog(null, aggiornaPasswordPanel, "Aggiorna password", this.OK_CANCEL_OPTION);

		passwordAttuale=new String(passwordAttualeField.getPassword());

		passwordAttualeField.setBackground(Color.white);
	}

	/**
	 * Metodo che, presi in carico i 4 tipi di errore, si occupa di generare i messaggi pop-up di errore corrispondenti.
	 * @param nErrore il tipo di errore che si è generato
	 */
	private void mostraErrore(int nErrore){
		// RF03
		//autori: Pietro Balossino, Andrija Jovic

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

	/**
	 * Metodo che genera la finestra di dialogo al fine di far inserire all'utente la nuova password.
	 */
	private void mostraFormNuovaPassword(){
		// RF03
		//autori: Pietro Balossino, Andrija Jovic

		nuovaPasswordField.setText("");

		richiesta = this.showConfirmDialog(null, nuovaPasswordPanel, "Aggiorna password", this.OK_CANCEL_OPTION);

		nuovaPassword=new String(nuovaPasswordField.getPassword());

		nuovaPasswordField.setBackground(Color.white);
	}

	/**
	 * Metodo che genera la finestra di dialogo che informa l'utente del successo del cambiamento della Password
	 */
	private void mostraMessaggioDiSuccesso(){
		// RF03
		//autori: Pietro Balossino, Andrija Jovic
		String messaggio= "La password e' stata cambiata con successo!"+"\nOra potrai accedere con la tua nuova password";
		this.showMessageDialog(null, messaggio, "Aggiorna Password", this.INFORMATION_MESSAGE);
	}
}
