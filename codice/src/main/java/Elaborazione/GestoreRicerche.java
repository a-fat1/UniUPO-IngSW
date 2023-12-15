package Elaborazione;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import DataBase.*;

public class GestoreRicerche implements GestoreRicercheInterfaccia
{
	private Registry registry;
	private DbUtentiInterfaccia dbUtenti;
	private DbProdottiInterfaccia dbProdotti;

	public GestoreRicerche(String host) throws RemoteException, NotBoundException
	{
		registry = LocateRegistry.getRegistry(host, 1098); 
       	 	dbUtenti = (DbUtentiInterfaccia) registry.lookup("dbUtenti");
		dbProdotti = (DbProdottiInterfaccia) registry.lookup("dbProdotti");
	}

	public GestoreRicerche(DbUtenti d1, DbProdotti d2) // per testing
	{
		dbUtenti = d1;
		dbProdotti = d2;
	}

	@Override
	public int controlloGiacenza(String giacenza) throws RemoteException
	{
		if (giacenza == null)
			return 1; 
		if (!giacenza.matches("\\d+")) 
            return 2;
		if(Integer.parseInt(giacenza) == 0)
			return 3;
		return 0; 
        
	}

	@Override
	public ArrayList<HashMap<String, Object>> ricercaProdotti(String giacenza)throws RemoteException {
		try {
			return dbProdotti.query("SELECT * FROM Prodotto WHERE Quantita ≤ " +giacenza);
		} catch (RemoteException e) {
			return new ArrayList<>();
		} 
	}
}

