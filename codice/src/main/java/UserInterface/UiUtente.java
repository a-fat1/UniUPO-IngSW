package UserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		gestoreAccessi = (GestoreAccessiInterfaccia) registryGestore.lookup("gestoreAccessi");

		// RF24
		avvioAggiornaDomicilio();
	}
	
	public void avvioCreaUtente() throws RemoteException
	{ 	// RF02
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

	public void avvioAggiornaDomicilio() throws RemoteException {
		String username = "";
		// Creazione della finestra di dialogo
		JFrame frame = new JFrame("Aggiorna Domicilio");
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new GridLayout(0, 2, 10, 10));

		// Campi di input
		JLabel labelVia = new JLabel("Via:");
		JTextField textFieldVia = new JTextField();
		JLabel labelCivico = new JLabel("Civico:");
		JTextField textFieldCivico = new JTextField();
		JLabel labelCap = new JLabel("CAP:");
		JTextField textFieldCap = new JTextField();
		JLabel labelLocalita = new JLabel("Località:");
		JTextField textFieldLocalita = new JTextField();

		// Bottone di conferma
		JButton confermaButton = new JButton("Conferma");

		// Aggiungi i componenti al frame
		frame.add(labelVia);
		frame.add(textFieldVia);
		frame.add(labelCivico);
		frame.add(textFieldCivico);
		frame.add(labelCap);
		frame.add(textFieldCap);
		frame.add(labelLocalita);
		frame.add(textFieldLocalita);
		frame.add(new JLabel()); // spazio vuoto
		frame.add(confermaButton);

		// ActionListener per il bottone di conferma
		confermaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// Recupera i valori inseriti dall'utente
					String via = textFieldVia.getText();
					String civico = textFieldCivico.getText();
					String cap = textFieldCap.getText();
					String localita = textFieldLocalita.getText();

					int ckFormato = gestoreAccessi.controllaFormatoDomicilio(via,civico,cap,localita);
					if (ckFormato != 0)
						mostraErroreForamtoDomicilio(ckFormato);
					/*
						showMessageDialog(null, "Successo", "Successo", INFORMATION_MESSAGE);
					 */
					// Chiudi la finestra di dialogo
					frame.dispose();

					// Chiamata al metodo per aggiornare il domicilio
					gestoreAccessi.promptSalvaDomicilio(username, via, civico, cap, localita);
				} catch (RemoteException ex) {
					ex.printStackTrace();
				}
			}
		});

		// Rendi la finestra di dialogo visibile
		frame.setVisible(true);
	}


	private void mostraErroreForamtoDomicilio(int msg) {

		switch (msg) {
			case 1:
				showMessageDialog(null, "Via vuota", "Errore", ERROR_MESSAGE);
				break;
			case 2:
				showMessageDialog(null, "Numero civico assente, nullo o negativo", "Errore", ERROR_MESSAGE);
				break;
			case 3:
				showMessageDialog(null, "Il cap non ha 5 cifre", "Errore", ERROR_MESSAGE);
				break;
			case 4:
				showMessageDialog(null, "Località assente", "Errore", ERROR_MESSAGE);
				break;
			case 5:
				showMessageDialog(null, "Vari campi errati", "Errore", ERROR_MESSAGE);
				break;
		}
	}


}
