import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.server.UnicastRemoteObject; 

import java.rmi.RemoteException; 
import java.rmi.NotBoundException; 
import java.rmi.AlreadyBoundException;
import java.util.HashMap;

import javax.swing.JFrame;

import UserInterface.*;
import Elaborazione.*;
import DataBase.*;

public class Prototipo extends JFrame
{	
  	public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException
	{
		int i;

		String component="all";
		String host="127.0.0.1";
		
		Registry registryDB;
		Registry registryGestore;
		Registry registryUI;

		DbUtenti dbUtenti;
		DbNotifiche dbNotifiche;
		DbProdotti dbProdotti;

		DbUtentiInterfaccia stubDbUtenti;
		DbNotificheInterfaccia stubDbNotifiche;
		DbProdottiInterfaccia stubDbProdotti;

		GestoreAccessi gestoreAccessi;
		GestoreNotifiche gestoreNotifiche;
		GestoreRicerche gestoreRicerche;
		GestoreProdotti gestoreProdotti;
		GestoreCarrelli gestoreCarrelli;

		GestoreAccessiInterfaccia stubGestoreAccessi;
		GestoreNotificheInterfaccia stubGestoreNotifiche;
		GestoreRicercheInterfaccia stubGestoreRicerche;
		GestoreProdottiInterfaccia stubGestoreProdotti;
		GestoreCarrelliInterfaccia stubGestoreCarrelli;

		UiLogin uiLogin;
		UiUtente uiUtente;
		UiNotifica uiNotifica;
		UiRicerca uiRicerca;
		UiLista uiLista;
		UiCarrello uiCarrello;
		UiProdotto uiProdotto;

		UiLoginInterfaccia stubUiLogin;
		UiUtenteInterfaccia stubUiUtente;
		UiNotificaInterfaccia stubUiNotifica;
		UiRicercaInterfaccia stubUiRicerca;
		UiListaInterfaccia stubUiLista;
		UiCarrelloInterfaccia stubUiCarrello;
		UiProdottoInterfaccia stubUiProdotto;

		System.out.print("\nargs:" + "\t");
		if (args.length==0)
			System.out.print("Nessuno");
		else
			for (i=0; i<args.length; i++)
				System.out.print(args[i] + "\t");
		System.out.println("\n");

		if (args.length > 0 && args[0].equals("help"))
		{
			
			System.out.println("args: -\t\t\t\tper eseguire DataBase, Elaborazione e UserInterface.");
			//System.out.println("args: all\t\t\tper eseguire DataBase, Elaborazione e UserInterface.");
			System.out.println("args: DB\t\t\tper eseguire solo DataBase.");
			System.out.println("args: Gestore [hostDataBase]\tper eseguire solo Elaborazione.");
			System.out.println("args: UI [hostElaborazione]\tper eseguire solo UserInterface.");
			System.exit(0);
		}
		
		if (args.length == 1)
			component=args[0];
		
		if (args.length == 2)
		{
			component=args[0];
			host=args[1];
		}		

		// AVVIO DB
		if (component.equals("all") || component.equals("DB"))
		{	
			// creazione registro per DB
			registryDB = LocateRegistry.createRegistry(1098); // default 1099

			dbUtenti = new DbUtenti();
			stubDbUtenti = (DbUtentiInterfaccia) UnicastRemoteObject.exportObject(dbUtenti, 2005);  
			registryDB.bind("dbUtenti", stubDbUtenti);
				
			dbNotifiche = new DbNotifiche();
			stubDbNotifiche = (DbNotificheInterfaccia) UnicastRemoteObject.exportObject(dbNotifiche, 2005);  
			registryDB.bind("dbNotifiche", stubDbNotifiche);

			dbProdotti = new DbProdotti();
			stubDbProdotti = (DbProdottiInterfaccia) UnicastRemoteObject.exportObject(dbProdotti, 2005);  
			registryDB.bind("dbProdotti", stubDbProdotti);
		}

		// AVVIO GESTORE
		if (component.equals("all") || component.equals("Gestore"))
		{		
			// creazione registro per gestori				
			registryGestore = LocateRegistry.createRegistry(1099);
		
			gestoreAccessi = new GestoreAccessi(host);
  			stubGestoreAccessi = (GestoreAccessiInterfaccia) UnicastRemoteObject.exportObject(gestoreAccessi, 2006);  
			registryGestore.bind("gestoreAccessi", stubGestoreAccessi); 

			gestoreNotifiche = new GestoreNotifiche(host);
  			stubGestoreNotifiche = (GestoreNotificheInterfaccia) UnicastRemoteObject.exportObject(gestoreNotifiche, 2006);  
			registryGestore.bind("gestoreNotifiche", stubGestoreNotifiche);

			gestoreRicerche = new GestoreRicerche(host);
  			stubGestoreRicerche = (GestoreRicercheInterfaccia) UnicastRemoteObject.exportObject(gestoreRicerche, 2006);  
			registryGestore.bind("gestoreRicerche", stubGestoreRicerche); 
			
			gestoreProdotti = new GestoreProdotti(host);
  			stubGestoreProdotti = (GestoreProdottiInterfaccia) UnicastRemoteObject.exportObject(gestoreProdotti, 2006);  
			registryGestore.bind("gestoreProdotti", stubGestoreProdotti);

			gestoreCarrelli = new GestoreCarrelli(host);
  			stubGestoreCarrelli = (GestoreCarrelliInterfaccia) UnicastRemoteObject.exportObject(gestoreCarrelli, 2006);  
			registryGestore.bind("gestoreCarrelli", stubGestoreCarrelli);
		}

		// AVVIO UI
		if (component.equals("all") || component.equals("UI"))
		{
			registryUI = LocateRegistry.createRegistry(1100); // default: 1099

			uiNotifica = new UiNotifica(host);
			stubUiNotifica = (UiNotificaInterfaccia) UnicastRemoteObject.exportObject(uiNotifica, 2007);	      
			registryUI.bind("uiNotifica", stubUiNotifica);

			uiLista = new UiLista(host);
			stubUiLista = (UiListaInterfaccia) UnicastRemoteObject.exportObject(uiLista, 2007);	
 	      	        registryUI.bind("uiLista", stubUiLista);

			uiUtente = new UiUtente(host);
			stubUiUtente = (UiUtenteInterfaccia) UnicastRemoteObject.exportObject(uiUtente, 2007);	
      	        	registryUI.bind("uiUtente", stubUiUtente);

			uiCarrello = new UiCarrello(host);
			stubUiCarrello = (UiCarrelloInterfaccia) UnicastRemoteObject.exportObject(uiCarrello, 2007);		
			registryUI.bind("uiCarrello", stubUiCarrello);

			uiProdotto = new UiProdotto(host);
			stubUiProdotto = (UiProdottoInterfaccia) UnicastRemoteObject.exportObject(uiProdotto, 2007);	

			registryUI.bind("uiProdotto", stubUiProdotto);

			uiRicerca = new UiRicerca(host);
			stubUiRicerca = (UiRicercaInterfaccia) UnicastRemoteObject.exportObject(uiRicerca, 2007);	
			registryUI.bind("uiRicerca", stubUiRicerca);

			uiLogin = new UiLogin(host);
			stubUiLogin = (UiLoginInterfaccia) UnicastRemoteObject.exportObject(uiLogin, 2007);	
 	      	        registryUI.bind("uiLogin", stubUiLogin);

			uiLogin.avvioLogin();
			System.exit(0);
		}
  	}
}
