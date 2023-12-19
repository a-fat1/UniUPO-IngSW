package Elaborazione;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import DataBase.*;

public class GestoreProdotti implements GestoreProdottiInterfaccia
{
	private Registry registry;
	private DbProdottiInterfaccia dbProdotti;

	public GestoreProdotti(String host) throws RemoteException, NotBoundException
	{
		registry = LocateRegistry.getRegistry(host, 1098); 
       	 	dbProdotti = (DbProdottiInterfaccia) registry.lookup("dbProdotti");
	}

	public GestoreProdotti(DbProdotti d1) // per testing
	{
		dbProdotti = d1;
	}

	public int Rf14_ControlloFormatoModificaPrezzo(float prezzoNuovo, float prezzoVecchio)
	{	
		int controllo;

		if(prezzoNuovo <= 0)
			controllo = 1;
		else if(prezzoNuovo == prezzoVecchio)
		{
			controllo = 2;
		}
		else 
		{
			controllo = 0;
		}
		
		return controllo;
	}

	public void ModificaPrezzo(float prezzo, int codice) throws RemoteException
	{
		String query = "UPDATE Prodotto SET prezzo ="+prezzo+" WHERE codice ="+codice;
		dbProdotti.update(query);
	}
}