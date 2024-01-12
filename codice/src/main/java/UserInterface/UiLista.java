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

public class UiLista extends JOptionPane implements UiListaInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private GestoreRicercheInterfaccia gestoreRicerche;

	// attributi
	
	// elementi grafici
	
	public UiLista(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		gestoreRicerche = (GestoreRicercheInterfaccia) registryGestore.lookup("gestoreRicerche");
	}
	
	public void avvioListaOrdini() throws RemoteException
	{	// RF11
	}

	public void avvioListaPagamenti() throws RemoteException
	{	// RF12
	}

	public void avvioListaForniture() throws RemoteException
	{	// RF13	
	}
}
