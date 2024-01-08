package UserInterface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.google.common.collect.Table;
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
		fieldDataInizio = new JTextField("", 10);
		fieldDataFine = new JTextField("", 10);
		panelData = new JPanel(new GridLayout(2,2));
		panelData.add(labelDataInizio);
		panelData.add(fieldDataInizio);
		panelData.add(labelDataFine);
		panelData.add(fieldDataFine);
		tableListaForniture = new JTable();
		scrollPanelListaForniture = new JScrollPane(tableListaForniture);
		
	}

	public void avvioListaOrdini(String username, int codiceProdotto) throws RemoteException {
		// RF11
		ArrayList<HashMap<String,Object>> ordini = null;

		if(!username.isEmpty())
			ordini = gestoreRicerche.ricercaPerUtente(username);
		else
			ordini = gestoreRicerche.ricercaPerProdotto(codiceProdotto);

		if(ordini.isEmpty())
			this.mostraErroreOrdini();
		else
			this.mostraOrdini(ordini);
	}

	private void mostraErroreOrdini() {
		showMessageDialog(null, "Non ci sono ordini. \n (clicca OK o X per proseguire)", "Errore", ERROR_MESSAGE);
	}

	private void mostraOrdini(ArrayList<HashMap<String,Object>> ordini){
		JTable tableOrdini;
		JScrollPane paneOrdini;
		int r = ordini.size();

		tableOrdini = new JTable(r,10){
			public boolean editCellAt(int row,int column,java.util.EventObject e){
				return false;
			}
		};

		ArrayList<String> header = new ArrayList<>(Arrays.asList("username","data ordine","codice prodotto","titolo","autore","editore","tipo","anno","prezzo","quantita'"));

		for(int i = 0; i < 10; i++){
			tableOrdini.getColumnModel().getColumn(i).setHeaderValue(header.get(i));
			tableOrdini.getColumnModel().getColumn(i).setPreferredWidth(100);
		}

		for(int i = 0; i < r; i++){
			tableOrdini.setValueAt((String) ordini.get(i).get("username"),i,0);
			tableOrdini.setValueAt((String) ordini.get(i).get("dataOrdine"),i,1);
			tableOrdini.setValueAt((int) ordini.get(i).get("codiceProdotto"),i,2);
			tableOrdini.setValueAt((String) ordini.get(i).get("titolo"),i,3);
			tableOrdini.setValueAt((String) ordini.get(i).get("autore"),i,4);
			tableOrdini.setValueAt((String) ordini.get(i).get("editore"),i,5);
			tableOrdini.setValueAt((String) ordini.get(i).get("tipo"),i,6);
			tableOrdini.setValueAt((int) ordini.get(i).get("anno"),i,7);
			tableOrdini.setValueAt((double) ordini.get(i).get("prezzo"),i,8);
			tableOrdini.setValueAt((int) ordini.get(i).get("quantitaProdotto"),i,9);
		}

		tableOrdini.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int selectedRow = tableOrdini.getSelectedRow();
					int selectedColumn = tableOrdini.getSelectedColumn();
					Object cellValue = tableOrdini.getValueAt(selectedRow, selectedColumn);

					JOptionPane.showMessageDialog(null, cellValue,"Valore della cella", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		tableOrdini.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableOrdini.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		paneOrdini = new JScrollPane(tableOrdini);

		paneOrdini.setPreferredSize(new Dimension(1002, 100));

		JOptionPane.showMessageDialog(null, paneOrdini, "Lista Ordini", JOptionPane.INFORMATION_MESSAGE, null);
	}

	public void avvioListaPagamenti() throws RemoteException { // RF12
	}

	@Override
	public void avvioListaForniture(int codice) throws RemoteException { // RF 13 Benetti-Chiappa
		DbProdotti dbProdotti = new DbProdotti();
		GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);
		ArrayList<HashMap<String, Object>> listaForniture = gestoreProdotti.ricercaListaForniture(codice);
		if (listaForniture.size() == 0){
			mostraErrore(3);
			return;
		}
		mostraLista(listaForniture);
		showMessageDialog(null, scrollPanelListaForniture, "Lista forniture di " + codice , JOptionPane.PLAIN_MESSAGE);

	}

	@Override
	public void avvioListaForniture() throws RemoteException { // RF 13 Benetti-Chiappa
		DbProdotti dbProdotti = new DbProdotti();
		GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);
		ArrayList<HashMap<String, Object>> listaForniture = new ArrayList<>();

		do {
			mostraFormRicercaPerData();
			if (dataInizio == null || dataFine == null)
				return;
			esitoControllo = gestoreProdotti.controlloParametri(dataInizio, dataFine);
			if (esitoControllo == 1 || esitoControllo == 2) {
				mostraErrore(esitoControllo);
			} else {
				listaForniture = gestoreProdotti.ricercaListaForniture(dataInizio, dataFine);
				if (listaForniture.size() == 0) {
					mostraErrore(4);
					break;
				}
				mostraLista(listaForniture);
				showMessageDialog(null, scrollPanelListaForniture, "Lista forniture da "+ dataInizio + " a " + dataFine , JOptionPane.PLAIN_MESSAGE);
			}
		} while (esitoControllo == 1 || esitoControllo == 2);
	}

	@Override
	public void mostraErrore(int esitoControllo) throws RemoteException {// RF 13 Benetti-Chiappa

		String messaggio = "";

		if (esitoControllo == 1) {
			messaggio = "Data non esistente/invalida, usare formato yyyy-MM-dd";
		}
		if (esitoControllo == 2) {
			messaggio = "La seconda data deve essere successiva alla prima";
		}
		if (esitoControllo == 3) {
			messaggio = "Forniture non trovate per questo prodotto";
		}
		if (esitoControllo == 4) {
			messaggio = "Forniture in tale range di date non trovate";
		}
		messaggio = messaggio + "\n(clicca su OK o X per continuare)";
		int risultato = JOptionPane.showOptionDialog(
				null,
				messaggio,
				"Errore",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.ERROR_MESSAGE,
				null,
				new Object[] { "OK" },
				null);
		if (risultato == JOptionPane.CLOSED_OPTION) {
			return;
		}
	}

	@Override
	public void mostraFormRicercaPerData() throws RemoteException {// RF 13 Benetti-Chiappa
		int ricerca = showOptionDialog(null, panelData, "Ricerca per data", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { "OK" }, -1);

		if (ricerca == this.OK_OPTION) {
			dataInizio = fieldDataInizio.getText();
			dataFine = fieldDataFine.getText();
		} else if (ricerca == JOptionPane.CLOSED_OPTION) {
			dataInizio = null;
			dataFine = null;
		}

	}

	@Override
	public void mostraLista(ArrayList<HashMap<String, Object>> listaForniture) throws RemoteException {// RF 13
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
