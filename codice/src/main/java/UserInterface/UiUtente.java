package UserInterface;

import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.awt.Frame.*;
import java.rmi.NotBoundException;

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

	// elementi grafici
	private int ckFormato;
	private JPanel contetDomicilio;
	private JTextField textField_via;
	private JTextField textField_civico;
	private JTextField textField_localita;
	private JTextField textField_cap;
	private String pulsantiDomicilio[];

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
		if (uiLogin == null)
			uiLogin = (UiLoginInterfaccia) registryUI.lookup("uiLogin");

		String username = "";
		do
		{
			mostraFormRegistrazione();
			if (annullamentoRichiesta)	break;

			nome = textFieldNome.getText();
			cognome = textFieldCognome.getText();

			esitoControllo = gestoreAccessi.controlloFormatoNomeCognome(nome, cognome);
			if (esitoControllo == 1 || esitoControllo == 2)
			{
				mostraErrore(esitoControllo);
			}
			else if (esitoControllo == 0)
			{
				gestoreAccessi.promptSalvaAccount(nome, cognome);
				username = nome+"."+cognome;
				clienteAmministratore.put("username", username);
				clienteAmministratore.put("nome", nome);
				clienteAmministratore.put("cognome", cognome);
				if (cliente)
				{
					tipoUtente = "cliente";
					clienteAmministratore.put("tipo", tipoUtente);
					System.out.println("11");
					avvioAggiornaDomicilio(username,true);
					System.out.println("22");
					uiNotifica.avvioGeneraNotifica("nuovo utente", clienteAmministratore);
					System.out.println("33");
				}
				else
				{
					mostraFormTipoUtente();
					if (annullamentoRichiesta) break;
				}
				System.out.println("1");
				gestoreAccessi.richiestaAttivazioneAccount(nome, cognome, tipoUtente);
				System.out.println("2");
				gestoreAccessi.aggiuntaCredenziali(nome+"."+cognome);
				System.out.println("3");
				uiLogin.avvioAggiornaUsername();
				System.out.println("4");
				uiLogin.avvioAggiornaPassword(false, username, "");
				System.out.println("5");
			}


		}while (!annullamentoRichiesta && esitoControllo!=0);
	}

	/**
	 * metodo che implementa (e visualizza) la form di registrazione. Visibile sia da clienti che da amministratori.
	 * Fa immettere nome e cognome per la registrazione dell'account.
	 * @throws RemoteException
	 */
	private void mostraFormRegistrazione() throws RemoteException {
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(new GridLayout(0, 2, 5, 5));
		
		JLabel lblNewLabel = new JLabel("nome");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblNewLabel);

		textFieldNome = new JTextField();

		//In caso di errore sul formato del nome, mostra all'utente il textField incriminato
		if (esitoControllo==1) textFieldNome.setBackground(Color.YELLOW);
		else textFieldNome.setBackground(Color.WHITE);

		contentPane.add(textFieldNome);
		textFieldNome.setColumns(10);
		
		JLabel lblCognome = new JLabel("cognome");
		lblCognome.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblCognome);
		
		textFieldCognome = new JTextField();

		//In caso di errore sul formato del cognome, mostra all'utente il textField incriminato
		if (esitoControllo==2) textFieldCognome.setBackground(Color.YELLOW);
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
	private void mostraErrore(int esitoControllo)
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
	private void mostraFormTipoUtente(){
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

	private void mostraFormBlocco() {
		String[] scelte= { "ANNULLA","CONFERMA"};
		selezione = JOptionPane.showOptionDialog(null, null, "FORM BLOCCO", 0, 2, null, scelte, null);

		if (selezione == 1) {
			JOptionPane.showMessageDialog(null, null, "Conferma lettura del messaggio(OK o X per continuare).", JOptionPane.INFORMATION_MESSAGE);
		}

		else {
			JOptionPane.showMessageDialog(null, null, "annullamento", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void mostraFormSblocco() {
		String[] scelte= { "ANNULLA","CONFERMA"};
		selezione = JOptionPane.showOptionDialog(null, null, "FORM SBLOCCO", 0, 3, null, scelte, null);

		if (selezione == 1) {
			JOptionPane.showMessageDialog(null, null, "Conferma lettura del messaggio(OK o X per continuare).", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(null, null, "annullamento", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void avvioAggiornaDomicilio(String username, boolean nuovo) throws RemoteException {
		ckFormato = 0;
		int scelta = 0;

		do {
			scelta = mostraFormDomicilio(username,nuovo);

			if (annullamentoRichiesta)
				break;

			if (scelta == 1) {
				// Recupera i valori inseriti dall'utente
				String via = textField_via.getText();
				String civico = textField_civico.getText();
				String cap = textField_cap.getText();
				String localita = textField_localita.getText();

				// Controlla se ci sono errori di formato
				ckFormato = gestoreAccessi.controllaFormatoDomicilio(via, civico, cap, localita);
				if (ckFormato > 0) {
					this.mostraErroreFormatoDomicilio(ckFormato);
				} else {
					// Chiamata al metodo per aggiornare il domicilio
					gestoreAccessi.promptSalvaDomicilio(username, via, civico, cap, localita);
					this.mostraSuccessoAggiornamentoDomicilio();
				}
			}
		} while ( ckFormato > 0  );
		System.out.println("SCELTA " + scelta);
	}

	private int mostraFormDomicilio(String username,boolean nuovo) throws RemoteException {
		int scelta = 0;

		setBounds(100, 100, 450, 300);
		contetDomicilio = new JPanel(new GridLayout(0, 2, 50, 5));

		JLabel lblVia = new JLabel("Via");
		lblVia.setHorizontalAlignment(SwingConstants.RIGHT);
		contetDomicilio.add(lblVia);

		textField_via = new JTextField();
		contetDomicilio.add(textField_via);
		textField_via.setColumns(10);

		if (ckFormato == 1)
			textField_via.setBackground(Color.YELLOW);
		else
			textField_via.setBackground(Color.WHITE);

		JLabel lblCivico = new JLabel("Civico");
		lblCivico.setHorizontalAlignment(SwingConstants.RIGHT);
		contetDomicilio.add(lblCivico);

		textField_civico = new JTextField();
		contetDomicilio.add(textField_civico);
		textField_civico.setColumns(10);

		if(ckFormato == 4)
			textField_civico.setBackground(Color.YELLOW);
		else
			textField_civico.setBackground(Color.WHITE);

		JLabel lblLocalita = new JLabel("Località");
		lblLocalita.setHorizontalAlignment(SwingConstants.RIGHT);
		contetDomicilio.add(lblLocalita);

		textField_localita = new JTextField();
		contetDomicilio.add(textField_localita);
		textField_localita.setColumns(10);

		if(ckFormato == 3)
			textField_localita.setBackground(Color.YELLOW);
		else
			textField_localita.setBackground(Color.WHITE);

		JLabel lblCap = new JLabel("Cap");
		lblCap.setHorizontalAlignment(SwingConstants.RIGHT);
		contetDomicilio.add(lblCap);

		textField_cap = new JTextField();
		contetDomicilio.add(textField_cap);
		textField_cap.setColumns(10);

		if (ckFormato == 2)
			textField_cap.setBackground(Color.YELLOW);
		else
			textField_cap.setBackground(Color.WHITE);

		pulsantiDomicilio = new String[2];
		pulsantiDomicilio[0] = "Indietro";
		pulsantiDomicilio[1] = "Conferma";

		// Controlla se il metodo e' stato invocato da RF02 (creazione utente)
		if (nuovo) {
			scelta = JOptionPane.showOptionDialog(null,
					contetDomicilio,
					"Aggiornamento Domicilio (clicca su X per uscire)",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
					null,
					new Object[]{pulsantiDomicilio[1]},
					"Aggiorna");
		}
		else { // Il metodo è invocato dal menu' del client
			String[] partiDomicilio = gestoreAccessi.promptRecuperaDomicilio(username);

			if (partiDomicilio.length == 4) {
				textField_via.setText(partiDomicilio[0]);
				textField_civico.setText(partiDomicilio[1]);
				textField_cap.setText(partiDomicilio[2]);
				textField_localita.setText(partiDomicilio[3]);
			}

			scelta = JOptionPane.showOptionDialog(null,
					contetDomicilio,
					"Aggiornamento Domicilio (clicca su X o Indietro per uscire)",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
					null,
					pulsantiDomicilio,
					"Aggiorna");
		}

		if(scelta == -1 || scelta == 0)
			annullamentoRichiesta = true;

		return scelta;
	}

	private void mostraErroreFormatoDomicilio(int msg) {
		String messaggio = "";

		switch (msg) {
			case 1:
				messaggio = "Via vuota";
				//textField_via.setBackground(Color.YELLOW);
				break;
			case 2:
				messaggio = "Il cap non ha 5 cifre";
				//textField_cap.setBackground(Color.YELLOW);
				break;
			case 3:
				messaggio = "Località assente";
				//textField_localita.setBackground(Color.YELLOW);
				break;
			case 4:
				messaggio = "Numero civico assente, nullo o negativo";
				//textField_civico.setBackground(Color.YELLOW);
				break;
			case 5:
				messaggio = "Vari campi errati";
				break;

		}

		messaggio = messaggio + "\n(clicca su OK o X per continuare)";

		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE);
	}

	private void mostraSuccessoAggiornamentoDomicilio() {
		String messaggio = "Successo!";

		messaggio = messaggio + "\n(clicca su OK o X per continuare)";

		this.showMessageDialog(null, messaggio, "Successo", this.INFORMATION_MESSAGE);
	}
}
