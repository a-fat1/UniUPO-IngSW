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

public class UiCarrello extends JOptionPane implements UiCarrelloInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private UiNotificaInterfaccia uiNotifica;
	private GestoreCarrelliInterfaccia gestoreCarrelli;

	// attributi
	
	// elementi grafici
	
	public UiCarrello(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		gestoreCarrelli = (GestoreCarrelliInterfaccia) registryGestore.lookup("gestoreCarrelli"); 
	}
	
	public void avvioVisualizzaCarrello() throws RemoteException
	{	// RF05	
	}

	public void avvioEffettuaOrdine() throws RemoteException
	{	// RF06	
	}

	public void avvioRimuoviProdottiDalCarrello() throws RemoteException
	{	// RF07	
	}

	public void avvioAggiungiAlCarrello() throws RemoteException
	{	// RF09	
	}
}
