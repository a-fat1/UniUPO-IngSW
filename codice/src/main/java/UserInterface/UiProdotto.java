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
	
	// elementi grafici
	
	public UiProdotto(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099); 

		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		uiLista = (UiListaInterfaccia) registryUI.lookup("uiLista");
		gestoreProdotti = (GestoreProdottiInterfaccia) registryGestore.lookup("gestoreProdotti"); 
	}
	
	public void avvioRimuoviRipristinaNelCatalogo() throws RemoteException
	{	// RF10
	}

	public void avvioAggiornaPrezzo() throws RemoteException
	{	// RF14	
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
