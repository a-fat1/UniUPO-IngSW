package UserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Color;

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

	// elementi grafici
	private JPanel contentPane;
	private JTextField textField_via;
	private JTextField textField_civico;
	private JTextField textField_localita;
	private JTextField textField_cap;
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
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(new GridLayout(0, 2, 50, 5));

		JLabel lblNewLabel = new JLabel("Nome");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblNewLabel);

		textField_via = new JTextField();
		contentPane.add(textField_via);
		textField_via.setColumns(10);

		JLabel lblCognome = new JLabel("Cognome");
		lblCognome.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblCognome);

		textField_civico = new JTextField();
		contentPane.add(textField_civico);
		textField_civico.setColumns(10);
		pulsantiRegistrazione = new String[2];
		pulsantiRegistrazione[0] = "Cancella";
		pulsantiRegistrazione[1] = "Avanti";

		JOptionPane.showOptionDialog(null, contentPane, "Registrazione (clicca su X o cancella per uscire)", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, pulsantiRegistrazione, "Registrati");
	}

	public void avvioBloccaSbloccaUtente() throws RemoteException
	{ 	// RF20	
	}

	public void avvioAggiornaDomicilio(String username, boolean nuovo) throws RemoteException {
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(new GridLayout(0, 2, 50, 5));

		JLabel lblVia = new JLabel("Via");
		lblVia.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblVia);

		textField_via = new JTextField();
		contentPane.add(textField_via);
		textField_via.setColumns(10);

		JLabel lblCivico = new JLabel("Civico");
		lblCivico.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblCivico);

		textField_civico = new JTextField();
		contentPane.add(textField_civico);
		textField_civico.setColumns(10);

		JLabel lblLocalita = new JLabel("Località");
		lblLocalita.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblLocalita);

		textField_localita = new JTextField();
		contentPane.add(textField_localita);
		textField_localita.setColumns(10);

		JLabel lblCap = new JLabel("Cap");
		lblCap.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblCap);

		textField_cap = new JTextField();
		contentPane.add(textField_cap);
		textField_cap.setColumns(10);

		pulsantiRegistrazione = new String[2];
		pulsantiRegistrazione[0] = "Indietro";
		pulsantiRegistrazione[1] = "Conferma";

		int ckFormato = 0;
		int scelta = 0;

		do {
			// Controlla se il metodo e' stato invocato da RF02 (creazione utente)
			if (nuovo) {

				scelta = JOptionPane.showOptionDialog(null,
						contentPane,
						"Aggiornamento Domicilio (clicca su X per uscire)",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
						null,
						new Object[]{pulsantiRegistrazione[1]},
						"Aggiorna");
			}
			else { // Il metodo è invocato dal menu' del client
				String[] partiDomicilio = gestoreAccessi.promptRecuperaDomicilio(username);

				if (partiDomicilio.length == 4) {
					System.out.println("4");
					textField_via.setText(partiDomicilio[0]);
					textField_civico.setText(partiDomicilio[1]);
					textField_cap.setText(partiDomicilio[2]);
					textField_localita.setText(partiDomicilio[3]);
				}

				scelta = JOptionPane.showOptionDialog(null,
						contentPane,
						"Aggiornamento Domicilio (clicca su X o Indietro per uscire)",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
						null,
						pulsantiRegistrazione,
						"Aggiorna");
			}

			if (scelta == 1) {
				textField_via.setBackground(Color.WHITE);
				textField_cap.setBackground(Color.WHITE);
				textField_localita.setBackground(Color.WHITE);
				textField_civico.setBackground(Color.WHITE);

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
		} while(ckFormato > 0 && scelta == 1);
	}

	private void mostraErroreFormatoDomicilio(int msg) {
		String messaggio = "";

		switch (msg) {
			case 1:
				messaggio = "Via vuota";
				textField_via.setBackground(Color.YELLOW);
				break;
			case 2:
				messaggio = "Il cap non ha 5 cifre";
				textField_cap.setBackground(Color.YELLOW);
				break;
			case 3:
				messaggio = "Località assente";
				textField_localita.setBackground(Color.YELLOW);
				break;
			case 4:
				messaggio = "Numero civico assente, nullo o negativo";
				textField_civico.setBackground(Color.YELLOW);
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
