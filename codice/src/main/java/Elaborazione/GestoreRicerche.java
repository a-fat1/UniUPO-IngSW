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

	public int controlloGiacenza(String giacenza) throws RemoteException {

	    // RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti

		if (giacenza == null || !giacenza.matches("\\d+"))	// Controllo per giacenza nulla e caratteri numerici
			return 1;

		String valoreMassimoIntero = String.valueOf(Integer.MAX_VALUE);		// Controllo lunghezza giacenza con Integer.MAX_VALUE
		if (giacenza.length() > valoreMassimoIntero.length())
			return 2;
		else
			if(giacenza.length() == valoreMassimoIntero.length())	// Quando hanno la stessa lunghezza
				for (int i = 0; i < valoreMassimoIntero.length(); i++)	// Controllo giacenza con Integer.MAX_VALUE per ogni carattere
					if(giacenza.charAt(i) > valoreMassimoIntero.charAt(i))
						return 2;

		if (Integer.parseInt(giacenza) == 0)	// Controllo giacenza diversa da zero
			return 3;

		return 0;
	}

	public ArrayList<HashMap<String, Object>> ricercaProdotti(String giacenza) throws RemoteException {

        // RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti
		
		// Se la query ha successo ritorna un ArrayList con il contenuto desiderato,
		// altrimenti viene ritornato un ArrayList vuoto
		try {
			return dbProdotti.query("SELECT * FROM Prodotto WHERE Quantita <= " + giacenza);
		} catch (RemoteException e) {
			return new ArrayList<>();
		}
    }
}
