package UserInterface;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;

import UserInterface.*;
import Elaborazione.*;

public class UiProdotto extends JOptionPane implements UiProdottoInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private UiNotificaInterfaccia uiNotifica;
	private UiListaInterfaccia uiLista;
	private GestoreProdottiInterfaccia gestoreProdotti;

	// attributi
	private Integer codProdotto; //RF15
	
	// elementi grafici
	
	private JLabel dataFornituraLabel; //RF15
	private JLabel costoFornituraLabel; //RF15
	private JLabel quantitaFornituraLabel; //RF15
	private JTextField dataFornituraField; //RF15
	private JTextField costoFornituraField; //RF15
	private JTextField quantitaFornituraField; //RF15
	private JPanel nuovaFornituraPanel; //RF15
	private JLabel successoFornituraLabel; //RF15
	private JPanel successoFornituraPanel; //RF15
	private JPanel erroreFornituraPanel; //RF15
	
	
	public UiProdotto(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		uiLista = (UiListaInterfaccia) registryUI.lookup("uiLista");
		gestoreProdotti = (GestoreProdottiInterfaccia) registryGestore.lookup("gestoreProdotti"); 
		
		// RF15 (Nicolò Bianchetto, Kristian Rigo)
		dataFornituraLabel = new JLabel("Data fornitura (AAAA-MM-GG): ");
		costoFornituraLabel = new JLabel("Costo fornitura: ");
		quantitaFornituraLabel = new JLabel("Quantità fornitura: ");
		dataFornituraField = new JTextField();
		costoFornituraField = new JTextField();
		quantitaFornituraField = new JTextField();
		nuovaFornituraPanel = new JPanel(new GridLayout(3, 2));
		nuovaFornituraPanel.add(dataFornituraLabel);
		nuovaFornituraPanel.add(dataFornituraField);
		nuovaFornituraPanel.add(costoFornituraLabel);
		nuovaFornituraPanel.add(costoFornituraField);
		nuovaFornituraPanel.add(quantitaFornituraLabel);
		nuovaFornituraPanel.add(quantitaFornituraField);
		successoFornituraLabel = new JLabel();
		successoFornituraPanel = new JPanel();
		erroreFornituraPanel = new JPanel();
		erroreFornituraPanel.setLayout(new BoxLayout(erroreFornituraPanel, BoxLayout.PAGE_AXIS));
	}
	
	public void avvioRimuoviRipristinaNelCatalogo() throws RemoteException
	{	// RF10
	}

	public void avvioAggiornaPrezzo() throws RemoteException
	{	// RF14	
	}

	private void mostraFormNuovaFornitura(boolean nuovoProdotto) throws RemoteException {
		// RF15 (Nicolò Bianchetto, Kristian Rigo)
		int scelta = JOptionPane.OK_OPTION;
		while(scelta == JOptionPane.OK_OPTION) {
			if(nuovoProdotto)
				showMessageDialog(null, nuovaFornituraPanel, "Nuova fornitura per prodotto " + codProdotto, JOptionPane.QUESTION_MESSAGE);
			else
				scelta = showConfirmDialog(null, nuovaFornituraPanel, "Nuova fornitura per prodotto " + codProdotto, JOptionPane.OK_CANCEL_OPTION);

			if(scelta == JOptionPane.OK_OPTION) {
				String data = dataFornituraField.getText().strip();
				Float costo = null;
				Integer quantita = null;
				if(!costoFornituraField.getText().isBlank()) {
					try {
						costo = Float.parseFloat(costoFornituraField.getText().strip().replace(",", "."));
					} catch(NumberFormatException e) {
						costo = -1.0f;
					}
				}
				if(!quantitaFornituraField.getText().isBlank()) {
					try {
						quantita = Integer.parseInt(quantitaFornituraField.getText().strip());
					} catch(NumberFormatException e) {
						quantita = -1;
					}
				}
				HashMap<String, Boolean> esitoControllo = gestoreProdotti.controlloDatiFornitura(data, costo, quantita);
				if(esitoControllo.values().stream().allMatch(Boolean.TRUE::equals)) {
					gestoreProdotti.aggiungiFornitura(codProdotto, data, costo, quantita);
					scelta = JOptionPane.CLOSED_OPTION;
					mostraSuccessoFornitura();
				}
				else mostraErroreFornitura(esitoControllo);
			}
		}
	}

	private void mostraSuccessoFornitura() {
		// RF15 (Nicolò Bianchetto, Kristian Rigo)
		successoFornituraLabel.setText("La nuova fornitura per il prodotto " + codProdotto + " è stata aggiunta con successo");
		successoFornituraPanel.add(successoFornituraLabel);
		showMessageDialog(null, successoFornituraPanel, "Successo", JOptionPane.INFORMATION_MESSAGE);
	}

	private void mostraErroreFornitura(HashMap<String, Boolean> esitoControllo) {
		// RF15 (Nicolò Bianchetto, Kristian Rigo)
		erroreFornituraPanel.removeAll(); //per rimuovere i messaggi di errore precedenti
		erroreFornituraPanel.add(new JLabel("Attenzione! Errore nell'aggiunta della nuova fornitura."));
		erroreFornituraPanel.add(Box.createVerticalStrut(10)); //per aggiungere spazio
		dataFornituraField.setBackground(Color.WHITE);
		costoFornituraField.setBackground(Color.WHITE);
		quantitaFornituraField.setBackground(Color.WHITE);

		String valoriErrati = Arrays.stream(new String[] {"Data", "Costo", "Quantità"}).filter(
				s -> Boolean.FALSE.equals(esitoControllo.get("esito" + s))
		).collect(Collectors.joining(", "));

		if(!valoriErrati.isEmpty()) {
			erroreFornituraPanel.add(new JLabel("I seguenti valori sono errati: " + valoriErrati));
			if(Boolean.FALSE.equals(esitoControllo.get("esitoData"))) {
				dataFornituraField.setBackground(Color.RED);
				erroreFornituraPanel.add(new JLabel("- La data deve essere nel formato AAAA-MM-GG e non deve essere successiva alla data odierna."));
			}
			if(Boolean.FALSE.equals(esitoControllo.get("esitoCosto"))) {
				costoFornituraField.setBackground(Color.RED);
				erroreFornituraPanel.add(new JLabel("- Il costo deve essere un numero maggiore di 0."));
			}
			if(Boolean.FALSE.equals(esitoControllo.get("esitoQuantità"))) {
				quantitaFornituraField.setBackground(Color.RED);
				erroreFornituraPanel.add(new JLabel("- La quantità deve essere un numero maggiore di 0."));
			}
		}

		String valoriAssenti = Arrays.stream(new String[] {"Data", "Costo", "Quantità"}).filter(
				s -> esitoControllo.get("esito" + s) == null
		).collect(Collectors.joining(", "));

		if(!valoriAssenti.isEmpty()) {
			erroreFornituraPanel.add(Box.createVerticalStrut(10)); //per aggiungere spazio
			erroreFornituraPanel.add(new JLabel("I seguenti valori sono assenti: " + valoriAssenti));
			if(esitoControllo.get("esitoData") == null) dataFornituraField.setBackground(Color.YELLOW);
			if(esitoControllo.get("esitoCosto") == null) costoFornituraField.setBackground(Color.YELLOW);
			if(esitoControllo.get("esitoQuantità") == null) quantitaFornituraField.setBackground(Color.YELLOW);
		}

		showMessageDialog(null, erroreFornituraPanel, "Errore", JOptionPane.ERROR_MESSAGE);
	}
}
