package UserInterface;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import UserInterface.*;
import Elaborazione.*;

public class UiRicerca extends JOptionPane implements UiRicercaInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private UiListaInterfaccia uiLista;
	private UiUtenteInterfaccia uiUtente;
	private UiCarrelloInterfaccia uiCarrello;
	private UiProdottoInterfaccia uiProdotto;
	private GestoreRicercheInterfaccia gestoreRicerche;

	// attributi
	private int scelta, esitoGiacenza = -1;		// RF18
	private String giacenza;	// RF18
	ArrayList<HashMap<String, Object>> listaProdotti;		//RF18
	//RF08
	private static final String _CD_ = "CD"; 
	private static final String _DVD_ = "DVD";
	private static final String _LIBRO_ = "Libro";
	private static final String _NUOVA_FORNITURA = "Nuova Fornitura";
	private static final String _AGGIORNA_PREZZO = "Aggiorna il Prezzo";
	private static final String _RIMUOVI_RIPRISTINA = "Rimuovi/Ripristina";
	private static final String _LISTA_FORNITURE = "Lista Forniture";
	private static final String _LISTA_ORDINI = "Lista Ordini";
	private static final String _AGGIUNGI_AL_CARRELLO = "Aggiungi al Carrello";
	
	// elementi grafici
	private JPanel pannelloGiacenza;	// RF18
	private JTextField campoGiacenza;	// RF18
	private JTable tabellaProdotti;		// RF18
	private JScrollPane pannelloGiacenzaScorrevole;		// RF18
	private String giacenzaPulsanti[] = new String[2];	// RF18
	//RF08
	JPanel ricercaPanel;
	JPanel mainPanel;
	private JTextField autoreField; 	
	private JTextField titoloField; 	
	private JTextField editoreField; 
	private JTextField annoField; 	

    private JCheckBox libroCheckbox;
    private JCheckBox cdCheckbox;
    private JCheckBox dvdCheckbox;
    private JCheckBox tuttiCheckbox;
	
	public UiRicerca(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiLista = (UiListaInterfaccia) registryUI.lookup("uiLista");
		uiUtente = (UiUtenteInterfaccia) registryUI.lookup("uiUtente");
		uiCarrello = (UiCarrelloInterfaccia) registryUI.lookup("uiCarrello");
		uiProdotto = (UiProdottoInterfaccia) registryUI.lookup("uiProdotto");
		gestoreRicerche = (GestoreRicercheInterfaccia) registryGestore.lookup("gestoreRicerche");
		//RF08
		//if(ricercaPanel==null)
		ricercaPanel = new JPanel();

        // Etichetta in cima al corpo della finestra
        JLabel labelText = new JLabel("Inserire titolo e/o autore e premere OK");
        labelText.setBounds(20, 10, 300, 20); // Posizione e dimensioni
        ricercaPanel.add(labelText);

        // Etichetta "premere X per uscire"
        JLabel exitLabel = new JLabel("(premere X per uscire)");
        exitLabel.setBounds(20, 35, 200, 20); // Posizione e dimensioni
        ricercaPanel.add(exitLabel);

        // Form con campi e checkbox
       
        ricercaPanel.setBounds(20, 70, 420, 250); // Posizione e dimensioni

        // Campi Autore, Titolo, Editore, Anno
        autoreField =addFormField(ricercaPanel, "Autore:", 35, 80);
        titoloField =addFormField(ricercaPanel, "Titolo:", 35, 110);
        editoreField =addFormField(ricercaPanel, "Editore:", 35, 140);
        annoField =addFormField(ricercaPanel, "Anno:", 35, 170);
        
        JLabel tipoLabel = new JLabel("Tipo:");
        tipoLabel.setBounds(315, 70, 200, 20); // Posizione e dimensioni
        ricercaPanel.add(tipoLabel);

        // Checkbox con etichetta "Tipo"
        libroCheckbox = new JCheckBox("Libro");
        libroCheckbox.setBounds(315, 100, 100, 20);
        cdCheckbox = new JCheckBox("CD");
        cdCheckbox.setBounds(315, 125, 100, 20);
        dvdCheckbox = new JCheckBox("DVD");
        dvdCheckbox.setBounds(315, 150, 100, 20);
        tuttiCheckbox = new JCheckBox("Tutti");
        tuttiCheckbox.setBounds(315, 175, 100, 20);
     	// Aggiungi ItemListener a tuttiCheckbox
        tuttiCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                libroCheckbox.setSelected(selected);
                cdCheckbox.setSelected(selected);
                dvdCheckbox.setSelected(selected);
            }
        });

        ricercaPanel.add(libroCheckbox);
        ricercaPanel.add(cdCheckbox);
        ricercaPanel.add(dvdCheckbox);
        ricercaPanel.add(tuttiCheckbox);
	}
	
	//RF08 Raffaele Camera
	private int mostraBarraRicerca(String tipo_utente)
	{
		//mostra ricercaPanel
		System.out.println("dentro al metodo: avvioRicercaProdotto");

		//rinizializza i campi
		libroCheckbox.setSelected(false);
		dvdCheckbox.setSelected(false);
		tuttiCheckbox.setSelected(false);
		cdCheckbox.setSelected(false);
		autoreField.setText("");
		editoreField.setText("");
		titoloField.setText("");
		annoField.setText("");


		ricercaPanel.setPreferredSize(new Dimension(430, 260));
		ricercaPanel.setLayout(null);
		ricercaPanel.setVisible(true);

		String pulsanti[] = {"CERCA"};

		return showOptionDialog(
			null, 
			ricercaPanel, 
			"Ricerca Prodotto", 
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.PLAIN_MESSAGE, 
			null, pulsanti,"Cerca");
	}

	//RF08 Raffaele Camera
	private void mostraElenco(ArrayList<HashMap<String, Object>> risultato, String tipoUtente){
		//nel caso ho 0 risultati mostro dialog
		if(risultato!=null && risultato.size()==0){					
			mostraErrore("errorNoResult");
			return ;
		}
		boolean isCliente = (tipoUtente!=null && tipoUtente.equalsIgnoreCase("cliente")) ? true : false;						
		// Creazione del modello della tabella con le colonne specificate nell'ordine desiderato
		DefaultTableModel tableModel = new DefaultTableModel(
			new Object[][]{},  // Nessuna riga iniziale
			isCliente ? new String[]{"autore", "titolo", "editore", "anno", "tipo", "prezzo", "quantità"}
			: new String[]{"autore", "titolo", "editore", "anno", "tipo", "prezzo", "quantità", "disponibilità"} // Ordine delle colonne
		);

		// Popolare la tabella con i dati
		for (HashMap<String, Object> riga : risultato) {
			Object[] rowData = {
					riga.get("autore"),
					riga.get("titolo"),
					riga.get("editore"),
					riga.get("anno"),
					riga.get("tipo"),
					riga.get("prezzo"),
					riga.get("quantita"),
					isCliente ? "" : riga.get("disponibile")  // Nasconde la colonna "Disponibilità" se cliente è true
			};
			tableModel.addRow(rowData);
		}

		// Creazione della tabella
		JTable table = new JTable(tableModel);
		// Creazione dello scroll pane per la tabella
		JScrollPane scrollPane = new JScrollPane(table);
		String pulsanti [] ;

		if (!isCliente) {
			pulsanti = new String[]{
				_NUOVA_FORNITURA,
				_AGGIORNA_PREZZO,
				_RIMUOVI_RIPRISTINA,
				_LISTA_FORNITURE,
				_LISTA_ORDINI
			};
		} else {
			pulsanti = new String[]{
				_AGGIUNGI_AL_CARRELLO
			};

		}
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		// mainPanel.add(buttonPanel);

		//mostra risultatiPanel
		System.out.println("dentro al metodo: avvioRisultatiRicerca");
		mainPanel.setPreferredSize(new Dimension(830, 260));
		//risultatiPanel.setLayout(null);
		mainPanel.setVisible(true);

		int res = showOptionDialog(
			null, 
			mainPanel, 
			"Prodotti trovati", 
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.PLAIN_MESSAGE, 
			null, pulsanti, null);

		String pulsantePremuto = pulsanti[res];
		//System.out.println("Hai cliccato su: " + pulsantePremuto);
		int selectedRow = table.getSelectedRow();
		if(selectedRow<0){
			mostraErrore("noRowSelected");
			return;
		} 

		switch (pulsantePremuto) {
			case _NUOVA_FORNITURA:
				//RF15				
				break;
			case _AGGIORNA_PREZZO:
				//RF14
				break;
			case _RIMUOVI_RIPRISTINA:
				//RF10
				break;
			case _LISTA_FORNITURE:
				//RF13
				break;
			case _LISTA_ORDINI:
				//RF11
				break;
			case _AGGIUNGI_AL_CARRELLO:
				//RF09
				
				System.out.println("selectedRow = "+selectedRow);
				System.out.println("oggetto selezionato: "+risultato.get(selectedRow));
				break;
			
		}

	}



		//RF08 Raffaele Camera
	private void mostraErrore(String tipoErrore)
	{
		String messaggio="";

        if (tipoErrore.equals("errorNoFieldCompiled"))
		{
            messaggio="Nessun campo compilato";
		}
		if (tipoErrore.equals("errorNoResult"))
		{
            messaggio="Nessun prodotto trovato";
		}
		if (tipoErrore.equals("errorAnnoField"))
		{
            messaggio="L'anno deve contenere 4 caratteri";
			//annoField.setBackground(Color.YELLOW);
		}
		if(tipoErrore.equals("errorTypeAnnoField"))
		{
			messaggio="L'anno deve essere un numero di 4 cifre";
		}
		if (tipoErrore.equals("errorEditoreField"))
		{
            messaggio="L'editore deve contenere almeno 3 caratteri";
		}
		if (tipoErrore.equals("errorTitoloField"))
		{
            messaggio="Il titolo deve contenere almeno 3 caratteri";
		}
		if(tipoErrore.equals("errorAutoreField"))
		{
			messaggio = "L'autore deve contenere almeno 3 caratteri";
		}
		if(tipoErrore.equals("noRowSelected"))
		{
			messaggio = "Nessun prodotto selezionato";
		}

		messaggio = messaggio + "\n(clicca su OK o X per continuare)";

		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE);
	}
	//RF08 Raffaele Camera
	private JTextField addFormField(JPanel panel, String label, int x, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(x, y, 80, 20); // Posizione e dimensioni
        JTextField jTextField = new JTextField();
        jTextField.setBounds(x + 90, y, 150, 20); // Posizione e dimensioni
        panel.add(jLabel);
        panel.add(jTextField);
        return jTextField;
    }
	//RF08 Raffaele Camera
	public void avvioRicercaProdotto(String tipo_utente) throws RemoteException
	{	
		if(mostraBarraRicerca(tipo_utente)==OK_OPTION) {
			
			// Recupera lo stato delle checkbox
			boolean libroSelected = libroCheckbox.isSelected();
			boolean cdSelected = cdCheckbox.isSelected();
			boolean dvdSelected = dvdCheckbox.isSelected();
			boolean tuttiSelected = tuttiCheckbox.isSelected();

			//gestione del campo tipo
			ArrayList<String> tipoList = new ArrayList<String>();
			if(tuttiSelected){
				tipoList.add(_LIBRO_);
				tipoList.add(_CD_);
				tipoList.add(_DVD_);
			} else {
				if(libroSelected){
					tipoList.add(_LIBRO_);
				}
				if(cdSelected){
					tipoList.add(_CD_);
				}
				if(dvdSelected){
					tipoList.add(_DVD_);
				}
			}

			String autore = autoreField.getText();
			String titolo = titoloField.getText();
			String editori = editoreField.getText();
			String anno = annoField.getText();

			// Stampa i valori
			// System.out.println("Autore: " + autore +", Titolo: " + titolo+", Editore: " + editori+", Anno: " + anno+", Libro: " + libroSelected+
			//	", CD: " + cdSelected+", DVD: " + dvdSelected+", Tutti: " + tuttiSelected);

			if(gestoreRicerche.controllaParametri(titolo,  autore, editori, anno, tipoList)){
				if(autore!=null && !autore.equals("") && autore.length()<3){					
					mostraErrore("errorAutoreField");
					return ;
				}
				if(titolo!=null && !titolo.equals("") && titolo.length()<3){					
					mostraErrore("errorTitoloField");
					return ;
				}
				if(editori!=null && !editori.equals("") && editori.length()<3){					
					mostraErrore("errorEditoreField");
					return ;
				}
				if(anno!=null && !anno.equals("") && anno.length()!=4){					
					mostraErrore("errorAnnoField");					
					return ;
				}
				if(anno!=null && !anno.equals("") && anno.length()==4)
				try {
					//verifica inserimento numero per l'anno
					Integer.parseInt(anno);
					
				} catch (NumberFormatException e) {
					mostraErrore("errorTypeAnnoField");	
					return ;
				}
				boolean isCliente = (tipo_utente!=null && tipo_utente.equalsIgnoreCase("cliente")) ? true : false;
				ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto(titolo,  autore, editori, anno, tipoList, isCliente);
				mostraElenco(risultato, tipo_utente);

			} else {				
				mostraErrore("errorNoFieldCompiled");
				
			}
		}
		
	}

	
	public void avvioProdottiInEsaurimento() throws RemoteException {
		
		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti

		do {
			this.mostraFormGiacenza();		// Mostra all'utente il form per inserire la giacenza

			if (scelta == 0) {		// Scelta = 0 quando viene premuto ok in mostraFormGiacenza()
				esitoGiacenza = gestoreRicerche.controlloGiacenza(giacenza);	// Viene richiamato controlloGiacenza per controllare la giacenza
				if(esitoGiacenza == 1 || esitoGiacenza == 2 || esitoGiacenza == 3)
					mostraErrore(esitoGiacenza);	// In caso di errore viene richiamato mostraErrore per visualizzare l'errore specifico
				else {
					System.out.println("esitoGiacenza = " + esitoGiacenza + "\n");
					listaProdotti = gestoreRicerche.ricercaProdotti(giacenza);		// Con la giacenza corretta viene richiamato ricercaProdotti per recuperare i dati dei prodotti
					if(listaProdotti.size() == 0) {
						System.out.println("dimensione listaProdotti = " + listaProdotti.size() + "\n");
						mostraErrore(4);		// Se l'ArrayList non ha i dati si richiama mostraErrore con parametro 4
					}
					else
						mostraListaProdotti(listaProdotti);		// mostraListaProdotti viene richiamato per visualizzare i dati in modo adeguato
				}
			}
		} while (scelta == 0);		// scelta != 0 quando l'utente preme torna al menu o il pulsante x

		esitoGiacenza = -1;		// esitoGiacenza = -1 viene inizializzato all'inizio e alla fine di avvioProdottiInEsaurimento()
		System.out.println("esitoGiacenza = " + esitoGiacenza + " --> fine avvioProdottiInEsaurimento()\n");
	}
	
	private void mostraFormGiacenza() {

		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti

		// Definisce il pannello (2 righe, 1 colonna) e un messaggio di testo
		pannelloGiacenza = new JPanel(new GridLayout(2,1));
		pannelloGiacenza.add(new JLabel("Inserisci qui la giacenza"));

		// Assegna a campoGiacenza una casella di testo e un setToolTipText
		campoGiacenza = new JTextField("", 10);
		campoGiacenza.setToolTipText("Scrivere qui la giacenza");
		if (esitoGiacenza == 1 || esitoGiacenza == 2 || esitoGiacenza == 3) {
			// Il background di campoGiacenza viene impostato con il rosso quando l'utente sbaglia a inserire la giacenza
			campoGiacenza.setBackground(Color.RED);
			System.out.println("esitoGiacenza = " + esitoGiacenza + " --> imposta background rosso\n");
		}
		else if (esitoGiacenza == -1)	// Con la prima esecuzione di mostraFormGiacenza(), invece, il background rimane bianco
			System.out.println("esitoGiacenza = " + esitoGiacenza + " --> inizio avvioProdottiInEsaurimento()\n");
		
		pannelloGiacenza.add(campoGiacenza);	// Aggiunge JTextField nel pannello

		// Definisce nome pulsanti e mostra interfaccia
		giacenzaPulsanti[0] = "Ok";
		giacenzaPulsanti[1] = "Torna al menu";
		scelta = UiRicerca.showOptionDialog(null, pannelloGiacenza, "Prodotti in esaurimento", DEFAULT_OPTION, PLAIN_MESSAGE, null, giacenzaPulsanti, null);

		if (scelta == 0) {		// Quando viene premuto il pulsante ok il valore 0 viene memorizzato in scelta
			giacenza = campoGiacenza.getText();		// E il contenuto scritto dall'utente viene salvato nella stringa giacenza
			System.out.println("scelta = " + scelta + " --> ok\n");
			System.out.println("giacenza = " + giacenza + "\n");
		}
		else if(scelta == 1)
				System.out.println("scelta = " + scelta + " --> torna al menu\n");
			else
				System.out.println("scelta = " + scelta + " --> pulsante X\n");
	}
	
	private void mostraErrore(int tipoErrore) {

		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti

		System.out.println("tipoErrore = " + tipoErrore + "\n");

		// In base al valore di tipoErrore viene visualizzata l'interfaccia che contiene il messaggio di errore specifico 
		if (tipoErrore == 1)
			UiRicerca.showMessageDialog(null, "La giacenza deve contenere solo caratteri numerici positivi.", "Errore giacenza invalida", UiRicerca.PLAIN_MESSAGE, null);
		if (tipoErrore == 2)
			UiRicerca.showMessageDialog(null, "La giacenza non deve essere troppo lunga o troppo elevata.", "Errore giacenza eccessiva", UiRicerca.PLAIN_MESSAGE, null);
		if (tipoErrore == 3)
			UiRicerca.showMessageDialog(null, "La giacenza deve essere maggiore di zero.", "Errore giacenza nulla", UiRicerca.PLAIN_MESSAGE, null);
		if (tipoErrore == 4) {
			// Definisce il pannello e un messaggio di testo
			pannelloGiacenza = new JPanel();
			pannelloGiacenza.add(new JLabel("Nessun prodotto presente con giacenza uguale o inferiore a quella selezionata."));

			// Definisce nome pulsanti e mostra interfaccia
			giacenzaPulsanti[0] = "Nuova ricerca";
			giacenzaPulsanti[1] = "Torna al menu";
			scelta = UiRicerca.showOptionDialog(null, pannelloGiacenza, "Errore lista vuota", DEFAULT_OPTION, PLAIN_MESSAGE, null, giacenzaPulsanti, null);
		}

		if (scelta == 0)
			if (tipoErrore == 1 || tipoErrore == 2 || tipoErrore == 3)
				System.out.println("scelta = " + scelta + " --> ok o pulsante X\n");
			else
				System.out.println("scelta = " + scelta + " --> nuova ricerca\n");
		else if(scelta == 1)
				System.out.println("scelta = " + scelta + " --> torna al menu\n");
			else
				System.out.println("scelta = " + scelta + " --> pulsante X\n");
	}
	
	private void mostraListaProdotti(ArrayList<HashMap<String, Object>> listaProdotti) {

		// RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti
		
		// Imposta JPanel e crea Jtable non modificabile, con numero di righe e colonne pari a listaProdotti
		pannelloGiacenza = new JPanel(new FlowLayout());
		tabellaProdotti = new JTable(listaProdotti.size(), listaProdotti.get(0).size()) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
            	return false;
            }
		};
		
		// Salva per colonne gli attributi e i valori all'interno di JTable
		int riga, colonna = 0;
		System.out.println("Colonne tabella");
		for (String attributo : listaProdotti.get(colonna).keySet()) {
			tabellaProdotti.getColumnModel().getColumn(colonna).setHeaderValue(attributo);	// Aggiunge e definisce l'attributo della colonna
			System.out.println("\n" + attributo + ":");
			riga = 0;
    		for (HashMap<String, Object> prodotto : listaProdotti) {
				tabellaProdotti.setValueAt(prodotto.get(attributo), riga, colonna);		// Aggiunge il contenuto delle righe
				System.out.println(prodotto.get(attributo));
				riga++;
			}
			colonna++;
		}
		System.out.println();

		tabellaProdotti.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);		// Disabilita ridimensionamento automatico

		// Creazione pannello scorrevole (utilizzando tabellaProdotti), definizione delle dimensioni e inserimento in pannelloGiacenza
		pannelloGiacenzaScorrevole = new JScrollPane(tabellaProdotti);
		pannelloGiacenzaScorrevole.setPreferredSize(new Dimension(650, 250));
		pannelloGiacenza.add(pannelloGiacenzaScorrevole);

		// Definisce nome pulsanti e mostra interfaccia
		giacenzaPulsanti[0] = "Nuova ricerca";
		giacenzaPulsanti[1] = "Torna al menu";
		scelta = UiRicerca.showOptionDialog(null, pannelloGiacenza, "Lista prodotti", DEFAULT_OPTION, PLAIN_MESSAGE, null, giacenzaPulsanti, null);

		if (scelta == 0)
				System.out.println("scelta = " + scelta + " --> nuova ricerca\n");
			else if(scelta == 1)
					System.out.println("scelta = " + scelta + " --> torna al menu\n");
				else
					System.out.println("scelta = " + scelta + " --> pulsante X\n");
	}

	public void avvioRicercaUtente() throws RemoteException
	{	// RF19	
	}

  	public void avvioProdottiPiuVenduti() throws RemoteException
	{	// RF22
	}
}
