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

	// attributi
	//RF02
	private String nome;
	private String cognome;
	private boolean annullamentoRichiesta;
	private int esitoControllo;
	
	// elementi grafici
	//RF02
	private JPanel contentPane;
	private JPanel contentPaneError;
	private JTextField textFieldCognome;
	private JTextField textFieldNome;
	private String pulsantiRegistrazione[];
	
	public UiUtente(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		gestoreAccessi = (GestoreAccessiInterfaccia) registryGestore.lookup("gestoreAccessi");
	}
	
	public void avvioCreaUtente() throws RemoteException
	{ 	// RF02
		do
		{
			mostraFormRegistrazione();
		}while (!annullamentoRichiesta && esitoControllo!=0);
	}
	
	private void mostraFormRegistrazione() throws RemoteException {
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(new GridLayout(0, 2, 50, 5));
		
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
		
		if (scelta == 0)
		{
			annullamentoRichiesta = true;	//utente vuole quittare
			return;
		}
		else if (scelta == 1)
		{
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
			}
		}
	}
	
	private void mostraErrore(int esitoControllo)
	{
		
		if (esitoControllo == 1)	//errore nome
		{
			//creazione di contentPaneError
			setBounds(100, 100, 450, 300);
			contentPaneError = new JPanel();
			contentPaneError.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPaneError.setLayout(new GridLayout(0, 1, 50, 50));
			
			JLabel lblNewLabel = new JLabel("format nome errato: troppi pochi caratteri (min:3) oppure il nome contiene un numero");
			contentPaneError.add(lblNewLabel);
			
			//visualizzazione contentPaneError
			JOptionPane.showConfirmDialog(null, contentPaneError, "errore nome (premi X o OK)", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
		}
		else if (esitoControllo == 2)
		{
			//creazione di contentPaneError
			setBounds(100, 100, 450, 300);
			contentPaneError = new JPanel();
			contentPaneError.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPaneError.setLayout(new GridLayout(0, 1, 50, 50));
			
			JLabel lblNewLabel = new JLabel("format cognome errato: troppi pochi caratteri (min:3) oppure il cognome contiene un numero");
			contentPaneError.add(lblNewLabel);
			
			//visualizzazione contentPaneError
			JOptionPane.showConfirmDialog(null, contentPaneError, "errore cognome (premi X o OK)", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
		}
	}

	public void avvioBloccaSbloccaUtente() throws RemoteException
	{ 	// RF20	
	}

	public void avvioAggiornaDomicilio() throws RemoteException
	{ 	// RF24
	}
}
