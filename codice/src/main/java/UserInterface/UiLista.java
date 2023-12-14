package UserInterface;

import java.util.HashMap;
import java.util.Vector;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.checkerframework.checker.units.qual.t;

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

	private JScrollPane scrollPanelListaForniture;
	private JTable tableListaForniture;

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
		tableListaForniture = new JTable();
		scrollPanelListaForniture = new JScrollPane(tableListaForniture);

	}

	public void avvioListaOrdini() throws RemoteException { // RF11
	}

	public void avvioListaPagamenti() throws RemoteException { // RF12
	}

	@Override
	public void avvioListaForniture(int codice) throws RemoteException { // RF 13 Benetti-Chiappa
		DbProdotti dbProdotti = new DbProdotti();
		GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);
		var listaForniture = gestoreProdotti.ricercaListaForniture(codice);
		if (listaForniture.size() == 0)
			mostraErrore(3);
		mostraListaItem(listaForniture);

	}

	@Override
	public void avvioListaForniture() throws RemoteException { // RF 13 Benetti-Chiappa
		DbProdotti dbProdotti = new DbProdotti();
		GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);

		do {
			mostraFormRicercaPerData();
			esitoControllo = gestoreProdotti.controlloParametri(dataInizio, dataFine);
			if (esitoControllo == 1 || esitoControllo == 2) {
				mostraErrore(esitoControllo);
			} else {
				var listaForniture = gestoreProdotti.ricercaListaForniture(dataInizio, dataFine);
				if (listaForniture.size() == 0) {
					mostraErrore(3);
					break;
				}
				mostraListaData(listaForniture);
			}
		} while (esitoControllo == 1 || esitoControllo == 2);

	}

	@Override
	public void mostraErrore(int esitoControllo) throws RemoteException {// RF 13 Benetti-Chiappa

		String messaggio = "";

		if (esitoControllo == 1) {
			messaggio = "Data non esistente/invalida";
		}
		if (esitoControllo == 2) {
			messaggio = "La seconda data deve essere successiva alla prima";
		}
		if (esitoControllo == 3) {
			messaggio = "Forniture in tale range di date non trovate";
		}
		if (esitoControllo == 4) {
			messaggio = "Forniture non trovate per questo prodotto";
		}
		messaggio = messaggio + "\n(clicca su OK o X per continuare)";
		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE, null);
	}

	@Override
	public void mostraFormRicercaPerData() throws RemoteException {// RF 13 Benetti-Chiappa
		var ricerca = showConfirmDialog(null, panelData, "Ricerca per data", this.OK_OPTION);

		if (ricerca == this.OK_OPTION) {
			dataInizio = fieldDataInizio.getText();
			dataFine = fieldDataFine.getText();
		}
	}

	@Override
	public void mostraListaItem(ArrayList<HashMap<String, Object>> listaForniture) throws RemoteException {// RF 13
																											// Benetti-Chiappa
		Vector<String> columnNames = new Vector<>();
		Vector<Vector<Object>> data = new Vector<>();

		if (!listaForniture.isEmpty()) {

			for (String columnName : listaForniture.get(0).keySet()) {
				columnNames.add(columnName);
			}

			for (HashMap<String, Object> riga : listaForniture) {
				Vector<Object> rowData = new Vector<>();
				for (String columnName : columnNames) {
					rowData.add(riga.get(columnName));
				}
				data.add(rowData);
			}

		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		tableListaForniture.setModel(model);
	}

	@Override
	public void mostraListaData(ArrayList<HashMap<String, Object>> listaForniture) throws RemoteException {// RF 13
																											// Benetti-Chiappa
		Vector<String> columnNames = new Vector<>();
		Vector<Vector<Object>> data = new Vector<>();

		if (!listaForniture.isEmpty()) {

			for (String columnName : listaForniture.get(0).keySet()) {
				columnNames.add(columnName);
			}

			for (HashMap<String, Object> riga : listaForniture) {
				Vector<Object> rowData = new Vector<>();
				for (String columnName : columnNames) {
					rowData.add(riga.get(columnName));
				}
				data.add(rowData);
			}

		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		tableListaForniture.setModel(model);
	}
}
UiLista.java
6 KB