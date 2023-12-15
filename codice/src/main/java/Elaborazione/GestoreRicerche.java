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

	public ArrayList<HashMap<String, Object>> ricercaPerUtente(String username) throws RemoteException
	{
		//RF11: lista ordini
		//autore: Marino & Vecchio

		String comandoSql;
		ArrayList<HashMap<String, Object>> ordini;

		System.out.println("GesyoreRicerche.ricercaPerUtente(" + username + ")\n");
		comandoSql = "SELECT Ordine.username,Ordine.dataOrdine,Ordine.codiceProdotto,Ordine.quantitaProdotto,Prodotto.autore,Prodotto.titolo,Prodotto.editore,Prodotto.tipo,Prodotto.anno,Prodotto.prezzo FROM Ordine JOIN Prodotto ON Ordine.codiceProdotto = Prodotto.codice WHERE username = \"" + username + "\";";
		ordini = dbProdotti.query(comandoSql);

		return ordini;
	}

	public ArrayList<HashMap<String, Object>> ricercaPerProdotto(int codiceProdotto) throws RemoteException
	{
		//RF11: lista ordini
		//autore: Marino & Vecchio

		String comandoSql;
		ArrayList<HashMap<String, Object>> ordini;

		System.out.println("GestoreRicerche.ricercaPerProdotto(" + codiceProdotto + ")\n");
		comandoSql = "SELECT Ordine.username,Ordine.dataOrdine,Ordine.codiceProdotto,Ordine.quantitaProdotto,Prodotto.autore,Prodotto.titolo,Prodotto.editore,Prodotto.tipo,Prodotto.anno,Prodotto.prezzo FROM Ordine JOIN Prodotto ON Ordine.codiceProdotto = Prodotto.codice WHERE codice = \"" + codiceProdotto + "\";";
		ordini = dbProdotti.query(comandoSql);

		return ordini;
	}
}
