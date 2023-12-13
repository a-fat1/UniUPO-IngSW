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

	public int controlloParametri(String username){

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		int len1 = username.length();

		if(len1 < 3)
			return 1;
		else
			return 4;
	}

	public int controlloParametri(String nome, String cognome) {

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		int len1 = nome.length();
		boolean bool1 = false;
		int len2 = cognome.length();
		boolean bool2 = false;

		for (int i=0; i<len1; i++) {
			if (!Character.isAlphabetic(nome.charAt(i))) bool1 = true;
		}

		for (int j=0; j<len2; j++) {
			if (!Character.isAlphabetic(cognome.charAt(j))) bool2 = true;
		}

		if (len1<3 || bool1) return 2;
		else if (len2<3 || bool2) return 3;
		return 4;
	}
}
