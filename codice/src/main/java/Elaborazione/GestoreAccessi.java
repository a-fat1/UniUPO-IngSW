package Elaborazione;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import DataBase.*;

public class GestoreAccessi implements GestoreAccessiInterfaccia
{
	private Registry registry;
	private DbUtentiInterfaccia dbUtenti;

	public GestoreAccessi(String host) throws RemoteException, NotBoundException
	{
		registry = LocateRegistry.getRegistry(host, 1098); 
       	 	dbUtenti = (DbUtentiInterfaccia) registry.lookup("dbUtenti");
	}

	public GestoreAccessi(DbUtenti d1) // per testing
	{
		dbUtenti = d1;
	}

	public String controllaFormatoCredenziali(String username, String password)
	{
		//RF00: login
    		//autore: Codetta

		int len1, len2;
		String esitoControllo;

		System.out.println("GestoreAccessi.controllaFormatoCredenziali(" + username + ", " + password + ")\n");

		len1=username.length();
		len2=password.length();
		
		if (len1==0 && len2==0)
			esitoControllo="erroreCredenziali";
		else
			if (len1==0)
				esitoControllo="erroreUsername";
			else
				if (len2==0)
					esitoControllo="errorePassword";
				else
					esitoControllo="ok";

		return esitoControllo;
	}		

	public String ricercaCredenziali(String username, String password) throws RemoteException
	{
		//RF00: login
    		//autore: Codetta

		String comandoSql;
		String esitoRicerca;
		ArrayList<HashMap<String, Object>> credenziali = null;

		System.out.println("GestoreAccessi.ricercaCredenziali(" + username + ", " + password + ")\n");

		comandoSql = "SELECT * FROM Credenziali WHERE username = \"" + username + "\" AND password = \"" + password + "\" ;";
		credenziali=dbUtenti.query(comandoSql);	
		
		if (credenziali.size()==0)
			esitoRicerca="erroreAssente";
		else
			if ((int)credenziali.get(0).get("attivo")==1)
				esitoRicerca="attivo";
			else
				esitoRicerca="erroreDisattivato";
		
		return esitoRicerca;			
	}

	public HashMap<String, Object> ricercaUtente(String username) throws RemoteException
	{
		//RF00: login
    		//autore: Codetta

		String comandoSql, esitoRicerca;
		ArrayList<HashMap<String, Object>> utenti = null;
		HashMap<String, Object> utente=null;

		System.out.println("GestoreAccessi.ricercaUtente(" + username + ")\n");

		comandoSql = "SELECT * FROM Utente WHERE username = \"" + username + "\" ;";
		utenti = dbUtenti.query(comandoSql);
		
		if (utenti.size() == 1)
			utente = utenti.get(0);
		
		return utente;			
	}
}
