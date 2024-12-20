package UserInterface;

import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.awt.Frame.*;
import java.rmi.NotBoundException;

import java.time.LocalDateTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import UserInterface.*;
import Elaborazione.*;

public class UiUtente extends JOptionPane implements UiUtenteInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private UiLoginInterfaccia uiLogin;
	private UiNotificaInterfaccia uiNotifica;
	private GestoreAccessiInterfaccia gestoreAccessi;


	// attributi
	private int selezione;
	// elementi grafici

	//RF02
	private String nome;
	private String cognome;
	private boolean annullamentoRichiesta;
	private int esitoControllo;
	private String tipoUtente;
	private HashMap<String, Object> clienteAmministratore = new HashMap<String, Object>();

	// elementi grafici
	//RF02
	private JPanel contentPane;
	private JPanel contentPaneDialog;
	private JTextField textFieldCognome;
	private JTextField textFieldNome;
	private JComboBox c1;
	private String pulsantiRegistrazione[];

	//RF24
	private int ckFormato;
	private JPanel contentDomicilio;
	private JLabel lblVia;
	private JLabel lblCivico;
	private JLabel lblLocalita;
	private JLabel lblCap;
	private JTextField textField_via;
	private JTextField textField_civico;
	private JTextField textField_localita;
	private JTextField textField_cap;
	private String pulsantiDomicilio[];
	private String via;
	private String civico;
	private String localita;
	private String cap;

	public UiUtente(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

		uiLogin = null;
		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		gestoreAccessi = (GestoreAccessiInterfaccia) registryGestore.lookup("gestoreAccessi");
	}


	/**
	 * Metodo che implementa la logica del diagramma di sequenza.
	 * @param cliente se settato a true, chi visualizza la form è un cliente, altrimenti è un amministratore.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void avvioCreaUtente(boolean cliente) throws RemoteException, NotBoundException
	{ 	// RF02
		if (uiLogin == null) // Codetta: UiLogin <-----> UiUtente
			uiLogin = (UiLoginInterfaccia) registryUI.lookup("uiLogin");

		String username = "";
		do
		{
			mostraFormRegistrazione();
			// Codetta
			//if (annullamentoRichiesta)	break;
			if (!annullamentoRichiesta) {
			nome = textFieldNome.getText();
			cognome = textFieldCognome.getText();

			esitoControllo = gestoreAccessi.controlloFormatoNomeCognome(nome, cognome);
			if (esitoControllo == 1 || esitoControllo == 2)
			{
				mostraErrore(esitoControllo);
			}
			else if (esitoControllo == 0)
			{
				// Codetta
				//username = nome+"."+cognome;
				username="user" + LocalDateTime.now().toString();
				gestoreAccessi.promptSalvaAccount(nome, cognome, username);
				
				clienteAmministratore.put("username", username);
				clienteAmministratore.put("nome", nome);
				clienteAmministratore.put("cognome", cognome);
				if (cliente)
				{
					tipoUtente = "cliente";
					clienteAmministratore.put("tipo", tipoUtente);
					avvioAggiornaDomicilio(username,true);
					uiNotifica.avvioGeneraNotifica("nuovo utente", clienteAmministratore);
				}
				else
				{
					mostraFormTipoUtente();
					// Codetta
					// if (annullamentoRichiesta) break;
				}

				gestoreAccessi.richiestaAttivazioneAccount(nome, cognome, tipoUtente, username);
				// Codetta
				//gestoreAccessi.aggiuntaCredenziali(nome+"."+cognome);
				gestoreAccessi.aggiuntaCredenziali(username);
				username = uiLogin.avvioAggiornaUsername(username, true);
				uiLogin.avvioAggiornaPassword(false, username, "");
			}
			}

		}while (!annullamentoRichiesta && esitoControllo!=0);
	}

	/**
	 * metodo che implementa (e visualizza) la form di registrazione. Visibile sia da clienti che da amministratori.
	 * Fa immettere nome e cognome per la registrazione dell'account.
	 * @throws RemoteException
	 */
	private void mostraFormRegistrazione() throws RemoteException { // RF02
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(new GridLayout(0, 2, 5, 5));
		
		JLabel lblNewLabel = new JLabel("nome");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblNewLabel);

		textFieldNome = new JTextField();
		textFieldCognome = new JTextField();

		//In caso di errore sul formato del nome, mostra all'utente il textField incriminato
		if (esitoControllo==1) {
			textFieldNome.setBackground(Color.YELLOW);
			textFieldNome.setText(nome);
			textFieldCognome.setText(cognome);
		}
		else textFieldNome.setBackground(Color.WHITE);

		contentPane.add(textFieldNome);
		textFieldNome.setColumns(10);
		
		JLabel lblCognome = new JLabel("cognome");
		lblCognome.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblCognome);

		//In caso di errore sul formato del cognome, mostra all'utente il textField incriminato
		if (esitoControllo==2) {
			textFieldCognome.setBackground(Color.YELLOW);
			textFieldNome.setText(nome);
			textFieldCognome.setText(cognome);
		}
		else textFieldCognome.setBackground(Color.WHITE);

		contentPane.add(textFieldCognome);
		textFieldCognome.setColumns(10);
		pulsantiRegistrazione = new String[2];
		pulsantiRegistrazione[0] = "Cancella";
		pulsantiRegistrazione[1] = "Avanti";

		int scelta = JOptionPane.showOptionDialog(null, contentPane, "Registrazione (clicca su X o cancella per uscire)", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, pulsantiRegistrazione, "Registrati");
		if (scelta == 0 || scelta == -1) {
			annullamentoRichiesta = true;    //utente vuole quittare
		} else annullamentoRichiesta = false;
	}

	/**
	 * metodo necessario a mostrare l'errore commesso dall'utente nell'input di nome e cognome,
	 * in mostraFormRegistrazione().
	 * @param esitoControllo se settato a 1, viene mostrato 'errore nome', se settato a 2 viene mostrato 'errore cognome'.
	 */
	private void mostraErrore(int esitoControllo) // RF02
	{
		
		if (esitoControllo == 1)	//errore nome
		{
			//creazione di contentPaneError
			setBounds(100, 100, 450, 300);
			contentPaneDialog = new JPanel();
			contentPaneDialog.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPaneDialog.setLayout(new GridLayout(0, 1, 50, 50));
			
			JLabel lblNewLabel = new JLabel("format nome errato: troppi pochi caratteri (min:3) oppure il nome contiene un numero");
			contentPaneDialog.add(lblNewLabel);
			
			//visualizzazione contentPaneError
			JOptionPane.showConfirmDialog(null, contentPaneDialog, "errore nome (premi X o OK)", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
		}
		else if (esitoControllo == 2)
		{
			//creazione di contentPaneError
			setBounds(100, 100, 450, 300);
			contentPaneDialog = new JPanel();
			contentPaneDialog.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPaneDialog.setLayout(new GridLayout(0, 1, 50, 50));
			
			JLabel lblNewLabel = new JLabel("format cognome errato: troppi pochi caratteri (min:3) oppure il cognome contiene un numero");
			contentPaneDialog.add(lblNewLabel);
			
			//visualizzazione contentPaneError
			JOptionPane.showConfirmDialog(null, contentPaneDialog, "errore cognome (premi X o OK)", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
		}
	}

	/**
	 * metodo che implementa la visualizzazione (solo per amministratori) della tendina per la scelta del
	 * ruolo dell'utente appena registrato.
	 */
	private void mostraFormTipoUtente(){ // RF02
		//creazione di contentPaneError
		setBounds(100, 100, 450, 300);
		contentPaneDialog = new JPanel();
		contentPaneDialog.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPaneDialog.setLayout(new GridLayout(0, 2, 30, 50));

		JLabel lblNewLabel = new JLabel("ruolo");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPaneDialog.add(lblNewLabel);

		// array of string containing cities
		String s1[] = {"staff", "amministratore"};

		// create checkbox
		c1 = new JComboBox(s1);

		contentPaneDialog.add(lblNewLabel);

		// add combobox to panel
		contentPaneDialog.add(c1);

		//visualizzazione contentPaneError
		int scelta = JOptionPane.showConfirmDialog(null, contentPaneDialog, "registra utente (clicca X per uscire)", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null);

		if (scelta==-1) annullamentoRichiesta = true;
		//ottieni il tipoUtente selezionato
		tipoUtente = (String)c1.getSelectedItem();
	}

	public void avvioBloccaSbloccaUtente(String username, boolean attivo) throws RemoteException
	{ 	// RF20
		if(attivo)
		{
			this.mostraFormBlocco();
			if(selezione==1) {

				gestoreAccessi.bloccoUtente(username);

			}
		}
		else{
			this.mostraFormSblocco();
			if(selezione==1) {

				gestoreAccessi.sbloccoUtente(username);

			}
		}
	}

	private void mostraFormBlocco() { // RF20
		String[] scelte= { "ANNULLA","CONFERMA"};
		selezione = JOptionPane.showOptionDialog(null, null, "FORM BLOCCO", 0, 2, null, scelte, null);

		if (selezione == 1) {
			JOptionPane.showMessageDialog(null, null, "Conferma lettura del messaggio(OK o X per continuare).", JOptionPane.INFORMATION_MESSAGE);
		}

		else {
			JOptionPane.showMessageDialog(null, null, "annullamento", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void mostraFormSblocco() { // RF20
		String[] scelte= { "ANNULLA","CONFERMA"};
		selezione = JOptionPane.showOptionDialog(null, null, "FORM SBLOCCO", 0, 3, null, scelte, null);

		if (selezione == 1) {
			JOptionPane.showMessageDialog(null, null, "Conferma lettura del messaggio(OK o X per continuare).", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(null, null, "annullamento", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	public void avvioAggiornaDomicilio(String username, boolean nuovo) throws RemoteException {
		int sceltaPulsante = 0;

		do {
			sceltaPulsante = mostraFormDomicilio(username, nuovo);

			ckFormato = 0;

			if ((nuovo == true && sceltaPulsante == 0) || (nuovo == false && sceltaPulsante == 1)) {
				// Recupera i valori inseriti dall'utente
				via = textField_via.getText();
				civico = textField_civico.getText();
				cap = textField_cap.getText();
				localita = textField_localita.getText();

				// Controlla se ci sono errori di formato
				ckFormato = gestoreAccessi.controllaFormatoDomicilio(via, civico, cap, localita);

				if (ckFormato > 0)
					this.mostraErroreFormatoDomicilio(ckFormato);
				else {
					// Chiamata al metodo per aggiornare il domicilio
					gestoreAccessi.promptSalvaDomicilio(username, via, civico, cap, localita);
					this.mostraSuccessoAggiornamentoDomicilio();
				}
			}
		} while ((nuovo == true && (ckFormato > 0 || sceltaPulsante == - 1))
					|| (nuovo == false && ckFormato > 0));
	}

	/**
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	private int mostraFormDomicilio(String username, boolean nuovo) throws RemoteException {
		int sceltaPulsante = 0;

		setBounds(100, 100, 450, 300);
		contentDomicilio = new JPanel(new GridLayout(0, 2, 50, 5));

		// Via
		lblVia = new JLabel("Via");
		lblVia.setHorizontalAlignment(SwingConstants.RIGHT);
		contentDomicilio.add(lblVia);

		textField_via = new JTextField();
		contentDomicilio.add(textField_via);
		textField_via.setColumns(10);


		// Civico
		lblCivico = new JLabel("Civico");
		lblCivico.setHorizontalAlignment(SwingConstants.RIGHT);
		contentDomicilio.add(lblCivico);

		textField_civico = new JTextField();
		contentDomicilio.add(textField_civico);
		textField_civico.setColumns(10);


		// Località
		lblLocalita = new JLabel("Localita'");
		lblLocalita.setHorizontalAlignment(SwingConstants.RIGHT);
		contentDomicilio.add(lblLocalita);

		textField_localita = new JTextField();
		contentDomicilio.add(textField_localita);
		textField_localita.setColumns(10);


		// CAP
		lblCap = new JLabel("CAP");
		lblCap.setHorizontalAlignment(SwingConstants.RIGHT);
		contentDomicilio.add(lblCap);

		textField_cap = new JTextField();
		contentDomicilio.add(textField_cap);
		textField_cap.setColumns(10);


		// Pulsanti
		pulsantiDomicilio = new String[2];
		pulsantiDomicilio[0] = "Indietro";
		pulsantiDomicilio[1] = "Conferma";

		// Controlla se arrivo dal menu' cliente
		if (nuovo == false) {
			// Recupero dati del domicilio dal DB
			String[] partiDomicilio = gestoreAccessi.promptRecuperaDomicilio(username);

			if (partiDomicilio.length == 4) {
				textField_via.setText(partiDomicilio[0]);
				textField_civico.setText(partiDomicilio[1]);
				textField_cap.setText(partiDomicilio[2]);
				textField_localita.setText(partiDomicilio[3]);
			}
		}

		// Controllo se ho avuto errori per colorare di rosso la textField errata
		if (ckFormato == 1) {
			textField_via.setBackground(Color.RED);
		}
		else
			textField_via.setBackground(Color.WHITE);

		if(ckFormato == 4) {
			textField_civico.setBackground(Color.RED);
		}
		else
			textField_civico.setBackground(Color.WHITE);

		if(ckFormato == 3) {
			textField_localita.setBackground(Color.RED);
		}
		else
			textField_localita.setBackground(Color.WHITE);

		if (ckFormato == 2) {
			textField_cap.setBackground(Color.RED);
		}
		else
			textField_cap.setBackground(Color.WHITE);

		// Setto le textFields con i valori precedentemente inseriti
		if (ckFormato > 0) {
			textField_via.setText(via);
			textField_civico.setText(civico);
			textField_localita.setText(localita);
			textField_cap.setText(cap);
		}

		/* Controlla se il metodo e' stato invocato da RF02 (creazione utente)
		   per la generazione dei pulsanti*/
		if (nuovo) {
			sceltaPulsante = JOptionPane.showOptionDialog(
					null,
					contentDomicilio,
					"Aggiornamento Domicilio (clicca X per resettare i campi)",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
					null,
					new Object[]{pulsantiDomicilio[1]},
					"Aggiorna");
		}
		else { // Il metodo è invocato dal menu' del client
			sceltaPulsante = JOptionPane.showOptionDialog(
					null,
					contentDomicilio,
					"Aggiornamento Domicilio (clicca su X o Indietro per uscire)",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
					null,
					pulsantiDomicilio,
					"Aggiorna");
		}

		return sceltaPulsante;
	}
	/**
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	private void mostraErroreFormatoDomicilio(int msg) {
		String messaggio = "";

		switch (msg) {
			case 1:
				messaggio = "Via vuota o formato errato";
				break;
			case 2:
				messaggio = "Il cap non ha 5 cifre";
				break;
			case 3:
				messaggio = "Localita' assente o formato errato";
				break;
			case 4:
				messaggio = "Numero civico assente, nullo o negativo";
				break;
			case 5:
				messaggio = "Vari campi errati";
				break;

		}

		messaggio = messaggio + "\n(clicca su OK o X per continuare)";

		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE);
	}
	/**
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	private void mostraSuccessoAggiornamentoDomicilio() {
		String messaggio = "Successo!";

		messaggio = messaggio + "\n(clicca su OK o X per continuare)";

		this.showMessageDialog(null, messaggio, "Successo", this.INFORMATION_MESSAGE);
	}
}
