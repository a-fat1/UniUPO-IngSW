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
	
	/**
	 * Controlla se l'utente ha inserito delle stringhe di nome e cognome valide.
	 * @param nome Il nome dell'utente che si sta registrando sul sistema informatico.
	 * @param cognome il cognome dell'utente che si sta registrando sul sistema informatico.
	 * @return ritorna 0 se il formato di nome e cognome sono corretti (senza numeri e con lunghezza >=3) 
	 * e se ambedue non contengono numeri. Altrimenti, viene ritornato 1 se il formato di nome è errato, 
	 * 2 se il formato di cognome è errato.
	 */
	public int controlloFormatoNomeCognome(String nome, String cognome) {
		int len1 = nome.length();
		int len2 = cognome.length();
		boolean bool1 = nome.matches(".*\\d.*");	//controlla se nome oppure cognome contengono un numero
		boolean bool2 = cognome.matches(".*\\d.*");
		
		if(len1>3 && !bool1)
		{
			if(len2>3 && !bool2)	return 0;
			else	return 2;
		}
		else
			return 1;
	}
	
	public void promptSalvaAccount(String nome, String cognome) throws RemoteException
	{
		dbUtenti.update("INSERT INTO 'main'.'Utente' ('username', 'nome', 'cognome') VALUES ("+nome+'.'+cognome+", "+nome+"', "+cognome+");");
	}
	
	public void richiestaAttivazioneAccount(String nome, String cognome, String tipoUtente) throws RemoteException
	{
		dbUtenti.update("UPDATE Utente SET tipo="+tipoUtente+" WHERE username="+nome+'.'+cognome);
	}
	
	public void aggiuntaCredenziali(String username) throws RemoteException
	{
		dbUtenti.update("UPDATE Utente SET password='' WHERE username="+username);
	}
}
