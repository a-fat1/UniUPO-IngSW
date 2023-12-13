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

public class UiLista extends JOptionPane implements UiListaInterfaccia {
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private GestoreRicercheInterfaccia gestoreRicerche;

	// attributi RF13
	private int codice = 0;
	private String listaForniture;
	private String dataInizio;
	private String dataFine;
	private int esitoControllo = 0;

	// elementi grafici RF13
	private JLabel labelDataInizio;
	private JLabel labelDataFine;
	private JTextField fieldDataInizio;
	private JTextField fieldDataFine;
	private JPanel panelData;

	public UiLista(String hostGestore) throws RemoteException, NotBoundException {
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

		gestoreRicerche = (GestoreRicercheInterfaccia) registryGestore.lookup("gestoreRicerche");
		
		labelDataInizio = new JLabel("Data iniziale");
		labelDataFine = new JLabel("Data finale");
		fieldDataInizio = new JTextField("",40);
		fieldDataFine = new JTextField("",40);
		panelData = new JPanel();
		panelData.setLayout(new BoxLayout(panelData, BoxLayout.PAGE_AXIS));
		panelData.add(labelDataInizio);
		panelData.add(fieldDataInizio);
		panelData.add(labelDataFine);
		panelData.add(fieldDataFine);

	}

	public void avvioListaOrdini() throws RemoteException { // RF11
	}

	public void avvioListaPagamenti() throws RemoteException { // RF12
	}

	public void avvioListaForniture() throws RemoteException { // RF13
	}

	public void mostraErrore(int esitoControllo) {

	}

	public void mostraFormRicercaPerData() {
		var ricerca = showConfirmDialog(null, panelData, "Ricerca per data",this.OK_OPTION);

		if (ricerca == this.OK_OPTION){
			var dataInizio = fieldDataInizio.getText();
			var dataFine = fieldDataFine.getText();
		}
	}
	
	public void mostraListaItem(ArrayList<HashMap<String, Object>> listaForniture) {

	}

	public void mostraListaData(ArrayList<HashMap<String, Object>> listaForniture) {

	}

}
