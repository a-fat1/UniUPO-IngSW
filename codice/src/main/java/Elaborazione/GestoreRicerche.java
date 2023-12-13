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


	// controllo dei parametri per username
	public int controlloParametri(String username){

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		int len1 = username.length();

		if(len1 < 3)
			return 1;
		else
			return 4;
	}

	// controllo dei parametri per nome e cognome
	public int controlloParametri(String nome, String cognome) {

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		int len1 = nome.length();
		boolean bool1 = !nome.matches("[a-zA-Z]+");
		int len2 = cognome.length();
		boolean bool2 = !cognome.matches("[a-zA-Z]+");

		if ((len1 < 3) || (bool1)){
			return 2;
		}else{
			if((len2 < 3) || (bool2)){
				return 3;
			}else{
				return 4;
			}
		}
	}

	public ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String username) throws RemoteException {

		String comandoSql, esitoRicerca;
		ArrayList<HashMap<String, Object>> utenti = null;


		comandoSql = "SELECT Utente.nome, Utente.cognome, Utente.username, Utente.tipo, Credenziali.attivo\n" +
					"FROM Utente JOIN Credenziali ON Utente.username = Credenziali.username\n" +
					"WHERE Utente.username LIKE \""+ username + "%\";";
		utenti = dbUtenti.query(comandoSql);

		return utenti;
	}


	public ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String nome, String cognome) throws RemoteException {

		String comandoSql, esitoRicerca;
		ArrayList<HashMap<String, Object>> utenti = null;


		comandoSql = "SELECT Utente.nome, Utente.cognome, Utente.username, Utente.tipo, Credenziali.attivo\n" +
				"FROM Utente JOIN Credenziali ON Utente.username = Credenziali.username\n" +
				"WHERE Utente.nome LIKE \""+ nome + "%\" AND Utente.cognome LIKE \""+ cognome + "%\";";
		utenti = dbUtenti.query(comandoSql);

		return utenti;
	}

	public ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String username) throws RemoteException {

		String comandoSql, esitoRicerca;
		ArrayList<HashMap<String, Object>> utenti = null;


		comandoSql = "SELECT Utente.nome, Utente.cognome, Utente.username, Utente.tipo, Credenziali.attivo\n" +
				"FROM Utente JOIN Credenziali ON Utente.username = Credenziali.username\n" +
				"WHERE Utente.username LIKE \""+ username + "%\" AND Credenziali.attivo == 1;";
		utenti = dbUtenti.query(comandoSql);

		return utenti;
	}


	public ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String nome, String cognome) throws RemoteException {

		String comandoSql, esitoRicerca;
		ArrayList<HashMap<String, Object>> utenti = null;


		comandoSql = "SELECT Utente.nome, Utente.cognome, Utente.username, Utente.tipo, Credenziali.attivo\n" +
				"FROM Utente JOIN Credenziali ON Utente.username = Credenziali.username\n" +
				"WHERE Utente.nome LIKE \""+ nome + "%\" AND Utente.cognome LIKE \""+ cognome + "%\" AND CREDENZIALI.attivo == 1;";
		utenti = dbUtenti.query(comandoSql);

		return utenti;
	}
}
