package UserInterface;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;

import DataBase.DbProdotti;
import UserInterface.*;
import Elaborazione.*;

public class UiLista extends JOptionPane implements UiListaInterfaccia {
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private GestoreRicercheInterfaccia gestoreRicerche;

	// attributi
	// RF 13 Benetti-Chiappa
	private int codice;
	private String listaForniture;
	private String dataInizio;
	private String dataFine;
	private int esitoControllo;

	// elementi grafici
	// RF 13 Benetti-Chiappa
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
		fieldDataInizio = new JTextField("", 40);
		fieldDataFine = new JTextField("", 40);
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

	@Override
	public void avvioListaForniture(int codice) throws RemoteException { // RF13
		DbProdotti dbProdotti = new DbProdotti();
		GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);
		var listaForniture = gestoreProdotti.ricercaListaForniture(codice);
		if (listaForniture.size() == 0)
			mostraErrore(3);
		mostraListaItem(listaForniture);
		}
	}

	@Override
	public void avvioListaForniture( ) throws RemoteException { // RF13
		DbProdotti dbProdotti = new DbProdotti();
		GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);
		
		do{
			mostraFormRicercaPerData();
			esitoControllo=gestoreProdotti.controlloParametri(dataInizio, dataFine);
			if (esitoControllo==1 || esitoControllo==2){
				mostraErrore(esitoControllo);
			}
			else{
				var listaForniture=gestoreProdotti.ricercaListaForniture(dataInizio, dataFine);
				if (listaForniture.size()==0)
					mostraErrore(3);
				mostraListaData(listaForniture);
			}
		}while(esitoControllo==1 || esitoControllo==2);
		

	}

	@Override
	public void mostraErrore(int esitoControllo) {

	}

	@Override
	public void mostraFormRicercaPerData() {
		var ricerca = showConfirmDialog(null, panelData, "Ricerca per data", this.OK_OPTION);

		if (ricerca == this.OK_OPTION) {
			var dataInizio = fieldDataInizio.getText();
			var dataFine = fieldDataFine.getText();
		}
	}

	@Override
	public void mostraListaItem(ArrayList<HashMap<String, Object>> listaForniture) {

	}

	@Override
	public void mostraListaData(ArrayList<HashMap<String, Object>> listaForniture) {

	}

}
