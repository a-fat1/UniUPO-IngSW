package UserInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import java.time.LocalTime;

import javax.swing.*;

import UserInterface.*;
import Elaborazione.*;

public class UiNotifica extends JOptionPane implements UiNotificaInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private GestoreNotificheInterfaccia gestoreNotifiche;

	// attributi
	
	// elementi grafici
	
	public UiNotifica(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		gestoreNotifiche = (GestoreNotificheInterfaccia) registryGestore.lookup("gestoreNotifiche"); 
	}
	
	public void avvioVisualizzaNotifiche(String tipoUtente) throws RemoteException
	{ 	// RF01
		int id;
		String testo, tipodiUtente,dataPubblicazione,dataScadenza;
		ArrayList<HashMap<String,Object>> lista=null;
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// Formattare la data utilizzando il formato personalizzato
		String formattedDate = myDateObj.format(formatter);
		lista=gestoreNotifiche.ricercaNotifiche(tipoUtente,formattedDate);
		int dim=lista.size();
		if(dim==0)
			System.out.println("\nNessuna notifica!\n");
		else{
			for (int i = 0; i < dim; i++) {
				id = (int) lista.get(i).get("numero");
				testo = (String) lista.get(i).get("testo");
				dataPubblicazione = (String) lista.get(i).get("dataPubblicazione");
				dataScadenza = (String) lista.get(i).get("dataScadenza");
				tipodiUtente = (String) lista.get(i).get("tipoUtente");
			}
			System.out.println(dim);
		}

	}

	public void avvioGeneraNotifica() throws RemoteException
	{ 	// RF04
	}

	public void avvioRicercaNotifiche() throws RemoteException
	{ 	// RF21	
	}
}
