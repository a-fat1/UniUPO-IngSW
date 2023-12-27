package UserInterface;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.awt.Font;
import java.awt.GridLayout;
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
	private UiNotificaInterfaccia uiNotifica;
	private GestoreAccessiInterfaccia gestoreAccessi;
	private UiLoginInterfaccia uiLogin;

	// attributi
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
	
	public UiUtente(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		gestoreAccessi = (GestoreAccessiInterfaccia) registryGestore.lookup("gestoreAccessi");
	}
	
	public void avvioCreaUtente(boolean cliente) throws RemoteException
	{ 	// RF02
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
				//gestoreAccessi.promptSalvaAccount(nome, cognome);   //commentato in quanto lavora sul db
				username = nome+"."+cognome;
				clienteAmministratore.put("username", username);
				clienteAmministratore.put("nome", nome);
				clienteAmministratore.put("cognome", cognome);
				if (cliente)
				{
					tipoUtente = "cliente";
					clienteAmministratore.put("tipo", tipoUtente);
					avvioAggiornaDomicilio();
					uiNotifica.avvioGeneraNotifica("nuovo utente", null, null, clienteAmministratore);
				}
				else
				{
					mostraFormTipoUtente();
				}

				//lavorano sul DB, quindi sono commentati
				//gestoreAccessi.richiestaAttivazioneAccount(nome, cognome, tipoUtente);
				//gestoreAccessi.aggiuntaCredenziali(nome+"."+cognome);
				uiLogin.avvioAggiornaUsername();
				uiLogin.avvioAggiornaPassword(true, username, "");
			}


		}while (!annullamentoRichiesta && esitoControllo!=0);
	}
	
	private void mostraFormRegistrazione() throws RemoteException {
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(new GridLayout(0, 2, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Nome");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel);
		
		textFieldNome = new JTextField();
		contentPane.add(textFieldNome);
		textFieldNome.setColumns(10);
		
		JLabel lblCognome = new JLabel("Cognome");
		lblCognome.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblCognome);
		
		textFieldCognome = new JTextField();
		contentPane.add(textFieldCognome);
		textFieldCognome.setColumns(10);
		pulsantiRegistrazione = new String[2];
		pulsantiRegistrazione[0] = "Cancella";
		pulsantiRegistrazione[1] = "Avanti";

		int scelta = JOptionPane.showOptionDialog(null, contentPane, "Registrazione (clicca su X o cancella per uscire)", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, pulsantiRegistrazione, "Registrati");
		
		if (scelta == 0) {
			annullamentoRichiesta = true;    //utente vuole quittare
		} else annullamentoRichiesta = false;
	}
	
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

	private void mostraFormTipoUtente(){
		//creazione di contentPaneError
		setBounds(100, 100, 450, 300);
		contentPaneDialog = new JPanel();
		contentPaneDialog.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPaneDialog.setLayout(new GridLayout(0, 2, 30, 50));

		JLabel lblNewLabel = new JLabel("ruolo");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPaneDialog.add(lblNewLabel);

		// array of string containing cities
		String s1[] = {"staff", "amministratore"};

		// create checkbox
		c1 = new JComboBox(s1);

		contentPaneDialog.add(lblNewLabel);

		// add combobox to panel
		contentPaneDialog.add(c1);

		//visualizzazione contentPaneError
		JOptionPane.showConfirmDialog(null, contentPaneDialog, "registra utente (clicca X per uscire)", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null);

		//ottieni il tipoUtente selezionato
		tipoUtente = (String)c1.getSelectedItem();
	}

	public void avvioBloccaSbloccaUtente() throws RemoteException
	{ 	// RF20	
	}

	public void avvioAggiornaDomicilio() throws RemoteException
	{ 	// RF24
	}
}
