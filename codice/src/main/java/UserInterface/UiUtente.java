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
<<<<<<< HEAD
	private int selezione;
	// elementi grafici

=======
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
	
>>>>>>> 1855847a08ab915903caf747e4ced5b2af9178a1
	public UiUtente(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

		uiLogin = null;
		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		gestoreAccessi = (GestoreAccessiInterfaccia) registryGestore.lookup("gestoreAccessi");
	}

<<<<<<< HEAD
	public void avvioCreaUtente() throws RemoteException
=======
	/**
	 * Metodo che implementa la logica del diagramma di sequenza.
	 * @param cliente se settato a true, chi visualizza la form è un cliente, altrimenti è un amministratore.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void avvioCreaUtente(boolean cliente) throws RemoteException, NotBoundException
>>>>>>> 1855847a08ab915903caf747e4ced5b2af9178a1
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
					avvioAggiornaDomicilio();
					uiNotifica.avvioGeneraNotifica("nuovo utente", clienteAmministratore);
				}
				else
				{
					mostraFormTipoUtente();
					if (annullamentoRichiesta) break;
				}

				gestoreAccessi.richiestaAttivazioneAccount(nome, cognome, tipoUtente);
				gestoreAccessi.aggiuntaCredenziali(nome+"."+cognome);
				uiLogin.avvioAggiornaUsername();
				uiLogin.avvioAggiornaPassword(false, username, "");
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





	public void avvioAggiornaDomicilio() throws RemoteException
	{ 	// RF24
	}
}
