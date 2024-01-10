package UserInterface;

import Elaborazione.GestoreProdottiInterfaccia;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

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



	// RF16 Nuova Fornitura Galliera, Ternullo
	private JPanel nuovoProdottoPanel;
	private JLabel titoloProdottoLabel;
	private JLabel annoProdottoLabel;
	private JLabel editoreProdottoLabel;
	private JLabel autoriProdottoLabel;
	private JLabel tipoProdottoLabel;
	private JTextField titoloProdottoField;
	private JTextField editoreProdottoField;
	private JTextField[] autoriProdottoField;
	private static final int NUM_AUTORI = 5;
	private JComboBox<Integer> annoProdottoCombo;
	private JComboBox<String> tipoProdottoCombo;
	private JPanel erroreVerificaPanel;
	private JLabel messaggioErrore;
	private JPanel erroreControlloPanel;

	//RF17 Incrementa/Decrementa prezzi
	private JPanel increDecrePanel;
	private JTextField percentualeField;
	private JLabel percentualeLabel;
	private JLabel sceltaLabel;
	private JPanel letturaIncreDecrePanel;
	private JRadioButton[] increDecreRadioButton;
	private JButton okButton;
	private JPanel errorePanel;
	private JPanel letturaErrorePanel;
	private JButton okErroreButton;
	private JPanel prezziAggiornati;
	private JPanel letturaPrezziAggiornatiPanel;
	private JLabel prezziAggiornatiLabel;
	private JButton okaggiornaButton;
	private JLabel lavoroLabel;
	private int percentuale;
	private boolean esito;
	private int sceltaVoce;

	private JLabel labelRimozione; //RF10
	private JLabel labelRipristino; //RF10
	private JPanel rimozionePanel;//RF10
	private JPanel ripristinoPanel;//RF10




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


		// RF 16
		nuovoProdottoPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_START;
		nuovoProdottoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		titoloProdottoLabel = new JLabel("Titolo");
		annoProdottoLabel = new JLabel("Anno Pubblicazione  ");
		editoreProdottoLabel = new JLabel("Editore");
		autoriProdottoLabel = new JLabel("Autori (Riempi dall'alto)");
		tipoProdottoLabel = new JLabel("Tipo");
		titoloProdottoField = new JTextField("",10);
		editoreProdottoField = new JTextField("",10);

		annoProdottoCombo = new JComboBox<>();
		for (int year=1900; year <= Year.now().getValue(); year++) {
			annoProdottoCombo.addItem(year);
		}

		autoriProdottoField = new JTextField[NUM_AUTORI];
		JPanel autoriPanel = new JPanel(new GridLayout(NUM_AUTORI, 1));
		for(int i=0; i<NUM_AUTORI; i++){
			autoriProdottoField[i] = new JTextField("", 10);
			autoriPanel.add(autoriProdottoField[i]);
		}

		tipoProdottoCombo = new JComboBox<>();
		tipoProdottoCombo.addItem("CD");
		tipoProdottoCombo.addItem("DVD");
		tipoProdottoCombo.addItem("Libro");

		constraints.gridy = 0;
		constraints.gridx = 0;
		nuovoProdottoPanel.add(titoloProdottoLabel, constraints);
		constraints.gridx = 1;
		nuovoProdottoPanel.add(titoloProdottoField, constraints);

		constraints.gridy = 1;
		constraints.gridx = 0;
		nuovoProdottoPanel.add(annoProdottoLabel, constraints);
		constraints.gridx = 1;
		annoProdottoCombo.setBorder(new EmptyBorder(5, 0, 5, 0));
		nuovoProdottoPanel.add(annoProdottoCombo, constraints);

		constraints.gridy = 2;
		constraints.gridx = 0;
		nuovoProdottoPanel.add(editoreProdottoLabel, constraints);
		constraints.gridx = 1;
		nuovoProdottoPanel.add(editoreProdottoField, constraints);

		constraints.gridy = 3;
		constraints.gridx = 0;
		constraints.gridheight = NUM_AUTORI;
		nuovoProdottoPanel.add(autoriProdottoLabel, constraints);
		constraints.gridx = 1;
		autoriPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		nuovoProdottoPanel.add(autoriPanel, constraints);

		constraints.gridy = NUM_AUTORI+4;
		constraints.gridx = 0;
		constraints.gridheight = 1;
		nuovoProdottoPanel.add(tipoProdottoLabel, constraints);
		constraints.gridx = 1;
		tipoProdottoCombo.setBorder(new EmptyBorder(5, 0, 0, 0));
		nuovoProdottoPanel.add(tipoProdottoCombo, constraints);


		//RF17

		increDecrePanel=new JPanel(new GridBagLayout());
		GridBagConstraints constraintss = new GridBagConstraints();
		constraintss.anchor = GridBagConstraints.LINE_START;
		percentualeLabel= new JLabel("Percentuale");
		sceltaLabel=new JLabel("Scelta");
		percentualeField= new JTextField("",10);
		increDecreRadioButton=new JRadioButton[2];
		increDecreRadioButton[0]=new JRadioButton("Incrementa");
		increDecreRadioButton[1]=new JRadioButton("Decrementa");


		constraintss.gridx=0;
		constraintss.gridy=0;
		constraintss.gridwidth=1;
		increDecrePanel.add(percentualeLabel,constraintss);
		constraintss.gridx=1;
		constraintss.gridy=0;
		increDecrePanel.add(percentualeField,constraintss);
		constraintss.gridx=0;
		constraintss.gridy=1;
		constraintss.gridwidth=1;
		increDecrePanel.add(sceltaLabel,constraintss);
		constraintss.gridx=1;
		constraintss.gridy=1;
		increDecrePanel.add(increDecreRadioButton[0],constraintss);
		constraintss.gridx=1;
		constraintss.gridy=2;
		increDecrePanel.add(increDecreRadioButton[1],constraintss);
		

		//RF10 (Filidoro Michele, Mahfoud Ayoub)
		labelRimozione= new JLabel("Rimuovere prodotto dal catalogo?");
		labelRipristino=new JLabel("Ripristinare prodotto nel catalogo?");
		ripristinoPanel=new JPanel(new FlowLayout());
		ripristinoPanel.add(labelRipristino);
		rimozionePanel=new JPanel(new FlowLayout());
		rimozionePanel.add(labelRimozione);
	}

	public void avvioRimuoviRipristinaNelCatalogo(Integer codProdotto, Integer Disponibile) throws RemoteException
	{	// RF10
		/*
		switch (Disponibile) {
			case 0 -> mostraFormRipristino(codProdotto);
			case 1 -> mostraFormRimozione(codProdotto);
			default -> mostraErroreDisponibile(codProdotto);
		}
		*/
		switch (Disponibile) {
			case 0: mostraFormRipristino(codProdotto);
			break;
			case 1: mostraFormRimozione(codProdotto);
			break;
			default: mostraErroreDisponibile(codProdotto);
			break;
		}
	}

	public void avvioAggiornaPrezzo() throws RemoteException
	{	// RF14	
	}

	public void avvioNuovaFornitura(Integer codProdotto, boolean nuovoProdotto) throws RemoteException {
		// RF15 (Nicolò Bianchetto, Kristian Rigo)
		this.codProdotto = codProdotto;
		mostraFormNuovaFornitura(nuovoProdotto);
		dataFornituraField.setText("");
		costoFornituraField.setText("");
		quantitaFornituraField.setText("");
		dataFornituraField.setBackground(Color.WHITE);
		costoFornituraField.setBackground(Color.WHITE);
		quantitaFornituraField.setBackground(Color.WHITE);
	}

	public void avvioNuovoProdotto() throws RemoteException
	{	// RF16
		mostraFormNuovoProdotto();
	}

	public void avvioIncrementaDecrementaPrezzi() throws RemoteException
	{	// RF17
		this.mostraFormIncrementaDecrementa();

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
				String data = dataFornituraField.getText().trim();
				Float costo = null;
				Integer quantita = null;
				if(!costoFornituraField.getText().isEmpty()) {
					try {
						costo = Float.parseFloat(costoFornituraField.getText().trim().replace(",", "."));
					} catch(NumberFormatException e) {
						costo = -1.0f;
					}
				}
				if(!quantitaFornituraField.getText().isEmpty()) {
					try {
						quantita = Integer.parseInt(quantitaFornituraField.getText().trim());
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
		successoFornituraPanel.removeAll(); //per rimuovere i messaggi di successo precedenti
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
				erroreFornituraPanel.add(new JLabel("- La quantità deve essere un numero intero maggiore di 0."));
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

	private void mostraFormNuovoProdotto() throws RemoteException {
		//RF16
		int esitoVerifica;
		boolean esitoControllo;
		HashMap<String, Object> ultimoProdotto;

		String[] autori;
		String titolo;
		String editore;
		String tipo;
		int anno;

		// elimina dati preinseriti
		titoloProdottoField.setText("");
		editoreProdottoField.setText("");
		for(JTextField t: autoriProdottoField) t.setText("");

		// cicla finchè il prodotto inserito possa venire aggiunto
		do{
			int scelta = showConfirmDialog(null, nuovoProdottoPanel, "Nuovo Prodotto (x per uscire)", JOptionPane.OK_CANCEL_OPTION);
			if(scelta == JOptionPane.CLOSED_OPTION || scelta == JOptionPane.CANCEL_OPTION) return;

			ArrayList<String> autoriList = new ArrayList<>();
			for(JTextField autoreF: autoriProdottoField){
				if(autoreF.getText().isEmpty()) break;
				autoriList.add(autoreF.getText());
			}
			autori = autoriList.toArray(new String[0]);

			titolo = titoloProdottoField.getText();
			editore = editoreProdottoField.getText();
			tipo = Objects.requireNonNull(tipoProdottoCombo.getSelectedItem()).toString();
			anno = Integer.parseInt(Objects.requireNonNull(annoProdottoCombo.getSelectedItem()).toString());

			// verifica campi
			esitoVerifica = gestoreProdotti.verificaCampi(autori, titolo, editore, anno);
			if(esitoVerifica != 0) mostraErroreVerifica(esitoVerifica);

			// controllo unicita'
			esitoControllo = gestoreProdotti.controlloUnicita(autori, titolo, editore, anno, tipo);
			if(!esitoControllo) mostraErroreControllo();

		} while(esitoVerifica != 0 || !esitoControllo); // se i dati inseriti non sono corretti o il prodotto esiste già cicla

		ultimoProdotto = gestoreProdotti.aggiungiProdotto(autori, titolo, editore, anno, tipo);

		//avvioAggiornaPrezzo(ultimoProdotto);
		avvioNuovaFornitura((Integer) ultimoProdotto.get("codice"), true);
		//UserInterface.UiNotifica.avvioGeneraNotifica("nuovo prodotto", ultimoProdotto);
	}

	private void mostraErroreVerifica(int codice){
		//RF16

		erroreVerificaPanel = new JPanel();
		messaggioErrore = new JLabel();
		/*switch (codice) {
			case 1 -> messaggioErrore.setText("Errore: Titolo mancante");
			case 2 -> messaggioErrore.setText("Errore: Anno errato");
			case 3 -> messaggioErrore.setText("Errore: Editore mancante");
			case 4 -> messaggioErrore.setText("Errore: Autore mancante");

			default -> messaggioErrore.setText("Errore");
		}*/
		switch(codice) {
			case 1: messaggioErrore.setText("Errore: Titolo mancante"); break;
			case 2: messaggioErrore.setText("Errore: Anno errato"); break;
			case 3: messaggioErrore.setText("Errore: Editore mancante"); break;
			case 4: messaggioErrore.setText("Errore: Autore mancante"); break;
			default: messaggioErrore.setText("Errore"); break;
		}
		erroreVerificaPanel.add(messaggioErrore);
		showMessageDialog(null, erroreVerificaPanel, "ERRORE (x o OK per confermare lettura)", JOptionPane.ERROR_MESSAGE);
	}

	private void mostraErroreControllo(){
		//RF16

		erroreControlloPanel = new JPanel();
		messaggioErrore = new JLabel("Errore: Prototto gia' esistente");
		erroreControlloPanel.add(messaggioErrore);
		showMessageDialog(null, erroreControlloPanel, "ERRORE (x o OK per confermare lettura)", JOptionPane.ERROR_MESSAGE);
	}

	//RF17
	private void mostraFormIncrementaDecrementa() throws RemoteException{
		while((esito==false || sceltaVoce==-1)||(esito==false && sceltaVoce==-1)) {
			String[] scelta = {"ok"};
			int sceltaPannello;
			sceltaPannello = this.showOptionDialog(null, increDecrePanel, "Inserire percentuale e scelta (premere X per uscire)", this.DEFAULT_OPTION, this.QUESTION_MESSAGE, null, scelta, "ok");
			if (sceltaPannello == this.CLOSED_OPTION) {
				return;
			} else {
				String valore = percentualeField.getText();
				if (valore.equals("")) {
					this.mostraErrore();
					//break;
				} else {
					percentuale = Integer.parseInt(valore);
					this.controllaCredenzialiIncreDecrePrezzi();
					this.selezioneControllo();

					if (esito == false && sceltaVoce == -1) {
						this.mostraErrori();
					} else if (esito == false || sceltaVoce == -1) {
						this.mostraErrore();
					}
				}

				if (esito == true || sceltaVoce != -1) {
					if(sceltaVoce==0){
						gestoreProdotti.incrementaPrezzi(percentuale);
					}else if(sceltaVoce==1){
						gestoreProdotti.decrementaPrezzi(percentuale);
					}

					this.mostraPrezzi();

				}
			}
		}

	}
	//RF17
	private void controllaCredenzialiIncreDecrePrezzi(){
		if (percentuale > 0 && percentuale <= 100) {
			esito = true;
		}
	}
	//RF17
	private void selezioneControllo(){
		sceltaVoce=-1;
		for(int i=0; i<2; i++){
			if(increDecreRadioButton[i].isSelected()){
				sceltaVoce=i;
			}
		}
	}
	//RF17
	private void mostraErrore(){
		String message="";
		if(esito==false){
			message="Percentuale non idonea";
			this.showMessageDialog(null,message,"Errore",this.ERROR_MESSAGE,null);

		}
		if(sceltaVoce==-1){
			message="Selezionare incrementa o decrementa";
			this.showMessageDialog(null,message,"Errore",this.ERROR_MESSAGE,null);
		}

	}
	//RF17
	private void mostraErrori(){
		if(sceltaVoce==-1 && esito==false){
			message="percentuale errata\n selezionare scelta";
			this.showMessageDialog(null,message,"Errore",this.ERROR_MESSAGE,null);
		}
	}
	//RF17
	private void mostraPrezzi(){
		String message="";
		if(esito==true && sceltaVoce!=-1){
			message="Prezzi aggiornati con successo";
			this.showMessageDialog(null,message,"Prezzi aggiornati",this.INFORMATION_MESSAGE,null);
		}

	}


	//RF10
	private void mostraFormRimozione(Integer codProdotto) throws RemoteException {
		labelRimozione=new JLabel("Prodotto n."+codProdotto);
		rimozionePanel.add(labelRimozione);
		int scelta;
		String[] scelte={"Annulla", "Conferma"};

		scelta=this.showOptionDialog(null,rimozionePanel,"Rimozione Prodotto", this.DEFAULT_OPTION,this.QUESTION_MESSAGE,null,scelte,"Annulla");
		if(scelta==JOptionPane.CLOSED_OPTION || scelta==0) return;
		if(scelta==1){
			gestoreProdotti.rimuoviProdotto(codProdotto);
			mostraSuccessoRimozione(codProdotto);
		}

	}
	//RF10
	private void mostraFormRipristino(Integer codProdotto) throws RemoteException {

		labelRipristino=new JLabel("Prodotto n."+codProdotto);
		ripristinoPanel.add(labelRipristino);

		int scelta;
		String[] scelte={"Annulla", "Conferma"};

		scelta=this.showOptionDialog(null,ripristinoPanel,"Ripristino Prodotto", this.DEFAULT_OPTION,this.QUESTION_MESSAGE,null,scelte,"Annulla");
		if(scelta==JOptionPane.CLOSED_OPTION || scelta==0) return;
		if(scelta==1){
			gestoreProdotti.ripristinaProdotto(codProdotto);
			mostraSuccessoRipristino(codProdotto);
		}
	}

	//RF10
	private void mostraSuccessoRipristino(Integer codProdotto){
		String messaggio;
		messaggio="Ripristino prodotto n." +codProdotto +" avvenuto con successo";
		this.showMessageDialog(null,messaggio,"Esito ripristino", this.INFORMATION_MESSAGE,null);
	}
	//RF10
	private void mostraSuccessoRimozione(Integer codProdotto){
		String messaggio;
		messaggio="Rimozione prodotto n." +codProdotto +" avvenuta con successo";
		this.showMessageDialog(null,messaggio,"Esito rimozione", this.INFORMATION_MESSAGE,null);

	}
	//RF10
	private void mostraErroreDisponibile(Integer codProdotto){
		String messaggio;
		messaggio="Valore 'Disponibile' per il prodotto n." +codProdotto+ " diverso da 0 o 1, correggere valore";
		this.showMessageDialog(null,messaggio,"errore",this.ERROR_MESSAGE,null);
	}
}
