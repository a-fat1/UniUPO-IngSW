package UserInterface;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;

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
	//RF14: aggiornaPrezzo
	private int richiesta;
	private float prezzo;
	private int controllo;

	// elementi grafici
	//RF14: aggiornaPrezzo
	private JLabel prezzoVecchioLabel;
	private JLabel prezzoNuovoLabel;
	private JLabel prezzoVecchio;
	private JTextField prezzoNuovoField;
	private JPanel modificaPrezzoPanel;
	private String pulsantiAggiornaPrezzo[];

	
	public UiProdotto(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		uiLista = (UiListaInterfaccia) registryUI.lookup("uiLista");
		gestoreProdotti = (GestoreProdottiInterfaccia) registryGestore.lookup("gestoreProdotti"); 

		//RF14
		prezzoVecchioLabel = new JLabel("prezzo precedente");
		prezzoVecchio = new JLabel("");
		prezzoVecchio.setHorizontalAlignment(JTextField.RIGHT);
		prezzoNuovoLabel = new JLabel("prezzo nuovo");
		prezzoNuovoField = new JTextField("0");
		prezzoNuovoField.setHorizontalAlignment(JTextField.RIGHT);
		prezzoNuovoField.setToolTipText("Inserire qui il nuovo prezzo");

		modificaPrezzoPanel = new JPanel(new GridLayout(2,2));
		modificaPrezzoPanel.add(prezzoVecchioLabel);
		modificaPrezzoPanel.add(prezzoVecchio);
		modificaPrezzoPanel.add(prezzoNuovoLabel);
		modificaPrezzoPanel.add(prezzoNuovoField);

		pulsantiAggiornaPrezzo = new String[2];
		pulsantiAggiornaPrezzo[0] = "Annulla";
		pulsantiAggiornaPrezzo[1] = "Modifica";
	}
	
	public void avvioRimuoviRipristinaNelCatalogo() throws RemoteException
	{	// RF10
	}

	public void avvioAggiornaPrezzo(HashMap<String, Object> P) throws RemoteException
	{
		//RF14
		do{
			controllo = 0;
			mostraFormAggiornaPrezzo(P);
			if(richiesta==0 && (float)P.get("prezzo")==0){
				mostraErrore(3);
			}
			else{
				if(richiesta==1){
					try{
						prezzo = Float.parseFloat(prezzoNuovoField.getText());
						controllo = gestoreProdotti.controlloFormatoModificaPrezzo(prezzo,(float)P.get("prezzo"));
						if(controllo==1 || controllo==2)
							mostraErrore(controllo);
						else{
							gestoreProdotti.modificaPrezzo(prezzo, (int)P.get("codice"));
							mostraMessaggio("Modifica confermata");
						}
					}
					catch(NumberFormatException e){
						controllo=4;
						mostraErrore(controllo);
					}
				}
			}
		}while((richiesta==0 && (float)P.get("prezzo")==0) || controllo!=0);
	}

	private void mostraFormAggiornaPrezzo(HashMap<String,Object> P) throws RemoteException
	{
		//RF14
		prezzoVecchio.setText(P.get("prezzo").toString());
		richiesta = this.showOptionDialog(null, modificaPrezzoPanel, "Aggiorna prezzo (clicca su X o annulla per uscire)", DEFAULT_OPTION, QUESTION_MESSAGE, null, pulsantiAggiornaPrezzo, "Modifica");
	}

	private void mostraErrore(int e)
	{
		//RF14
		String messaggio = new String("");
		if(e==1){
			messaggio = "Inserire un numero positivo";
		}
		if(e==2){
			messaggio = "Inserire un prezzo diverso dal precedente";
		}
		if(e==3){
			messaggio = "Impossibile uscire con prezzo nullo";
		}
		if(e==4){
			messaggio = "Inserire un numero";
		}
		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE, null);
	}

	private void mostraMessaggio(String messaggio){
		//RF14
		this.showMessageDialog(null, messaggio, "Messaggio", this.INFORMATION_MESSAGE, null);
	}

	public void avvioNuovaFornitura() throws RemoteException
	{	// RF15	
	}

	public void avvioNuovoProdotto() throws RemoteException
	{	// RF16	
	}

	public void avvioIncrementaDecrementaPrezzi() throws RemoteException
	{	// RF17	
	}
}