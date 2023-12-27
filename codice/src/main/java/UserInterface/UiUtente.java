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
	private UiLoginInterfaccia uiLogin;
	private UiNotificaInterfaccia uiNotifica;
	private GestoreAccessiInterfaccia gestoreAccessi;

	// attributi
	
	// elementi grafici
	//RF02
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private String pulsantiRegistrazione[];
	
	public UiUtente(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiLogin = null;
		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		gestoreAccessi = (GestoreAccessiInterfaccia) registryGestore.lookup("gestoreAccessi");
	}
	
	public void avvioCreaUtente(boolean cliente) throws RemoteException, NotBoundException
	{ 	// RF02

		if (uiLogin == null)
			uiLogin = (UiLoginInterfaccia) registryUI.lookup("uiLogin");

		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(new GridLayout(0, 2, 50, 5));
		
		JLabel lblNewLabel = new JLabel("Nome");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel);
		
		textField_1 = new JTextField();
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblCognome = new JLabel("Cognome");
		lblCognome.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblCognome);
		
		textField = new JTextField();
		contentPane.add(textField);
		textField.setColumns(10);
		pulsantiRegistrazione = new String[2];
		pulsantiRegistrazione[0] = "Cancella";
		pulsantiRegistrazione[1] = "Avanti";
		
		JOptionPane.showOptionDialog(null, contentPane, "Registrazione (clicca su X o cancella per uscire)", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, pulsantiRegistrazione, "Registrati");
	}

	public void avvioBloccaSbloccaUtente() throws RemoteException
	{ 	// RF20	
	}

	public void avvioAggiornaDomicilio() throws RemoteException
	{ 	// RF24
	}
}
