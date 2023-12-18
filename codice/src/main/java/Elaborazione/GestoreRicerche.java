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


	/**
	 * Controlla se l'utente ha inserito una stringa username valida.
	 * @param username che viene inserito dall'utente.
	 * @return ritorna 1 in caso in cui la lunghezza della stringa sia < 3, altrimenti ritorna 4.
	 * @throws RemoteException
	 */

	public int controlloParametri(String username) throws RemoteException {

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		// ricavo la lunghezza della stringa
		int len1 = username.length();

		if(len1 < 3)
			return 1;
		else
			return 4;
	}

	/**
	 * Controlla se l'utente ha inserito delle stringhe nome e cognome valide.
	 * @param nome che viene inserito dall'utente.
	 * @param cognome che viene inserito dall'utente
	 * @return ritorna 2 se il nome non è lungo almeno 3 caratteri o se contiene dei caratteri non
	 * alfabetici, ritorna 3 se il cognome non è lungo almeno 3 caratteri o se contiene dei caratteri
	 * non alfabetici, ritorna 4 se invece nome e cognome sono validi.
	 * @throws RemoteException
	 */
	public int controlloParametri(String nome, String cognome) throws RemoteException {

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		// ricavo la lunghezza della stringa del nome
		int len1 = nome.length();
		// controllo la presenza di caratteri non letterali nella stringa nome
		boolean bool1 = !nome.matches("[a-zA-Z]+");
		// ricavo la lunghezza della stringa del cognome
		int len2 = cognome.length();
		// controllo la presenza di caratteri non letterali nella stringa cognome
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

	/**
	 * Funzione che cerca nel database in base allo username fornito dall'utente, senza controllare
	 * che l'utente sia bloccato o meno.
	 * @param username lo username che bisogna ricercare nel database.
	 * @return ritorna un'ArrayList degli utenti trovati, sotto forma di HashMap.
	 * @throws RemoteException
	 */
	public ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String username) throws RemoteException {

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		String comandoSql;
		ArrayList<HashMap<String, Object>> utenti;

		comandoSql = "SELECT Utente.nome, Utente.cognome, Utente.username, Utente.tipo, Credenziali.attivo\n" +
					 "FROM Utente JOIN Credenziali ON Utente.username = Credenziali.username\n" +
					 "WHERE Utente.username LIKE \""+ username + "%\";";

		utenti = dbUtenti.query(comandoSql);

		return utenti;
	}

	/**
	 * Funzione che cerca nel database in base alla coppia nome-cognome fornita dall'utente, senza controllare
	 * che l'utente sia bloccato o meno.
	 * @param nome il nome che bisogna ricercare nel database.
	 * @param cognome il cognome che bisogna ricercare nel database.
	 * @return ritorna un'ArrayList degli utenti trovati, sotto forma di HashMap.
	 * @throws RemoteException
	 */
	public ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String nome, String cognome) throws RemoteException {

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		String comandoSql;
		ArrayList<HashMap<String, Object>> utenti;

		comandoSql = "SELECT Utente.nome, Utente.cognome, Utente.username, Utente.tipo, Credenziali.attivo\n" +
				     "FROM Utente JOIN Credenziali ON Utente.username = Credenziali.username\n" +
				     "WHERE Utente.nome LIKE \""+ nome + "%\" AND Utente.cognome LIKE \""+ cognome + "%\";";
		utenti = dbUtenti.query(comandoSql);

		return utenti;
	}

	/**
	 * Funzione che cerca nel database in base allo username fornito dall'utente, controllando che
	 * l'utente non sia bloccato (identificato dal valore "1" che significa utente non bloccato).
	 * @param username lo username che bisogna ricercare nel database.
	 * @return ritorna un'ArrayList degli utenti trovati, sotto forma di HashMap.
	 * @throws RemoteException
	 */
	public ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String username) throws RemoteException {

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		String comandoSql;
		ArrayList<HashMap<String, Object>> utenti;

		comandoSql = "SELECT Utente.nome, Utente.cognome, Utente.username, Utente.tipo, Credenziali.attivo\n" +
				     "FROM Utente JOIN Credenziali ON Utente.username = Credenziali.username\n" +
				     "WHERE Utente.username LIKE \""+ username + "%\" AND Credenziali.attivo == 1;";
		utenti = dbUtenti.query(comandoSql);

		return utenti;
	}

	/**
	 * Funzione che cerca nel database in base alla coppia nome-cognome fornita dall'utente, controllando che
	 * l'utente non sia bloccato (identificato dal valore "1" che significa utente non bloccato).
	 * @param nome il nome inserito dall'utente per la ricerca.
	 * @param cognome il cognome inserito dall'utente per la ricerca.
	 * @return ritorna un'ArrayList degli utenti trovati, sotto forma di HashMap.
	 * @throws RemoteException
	 */
	public ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String nome, String cognome) throws RemoteException {

		// RF19
		// Riccardo Nazzari, Andrea Benedetto

		String comandoSql;
		ArrayList<HashMap<String, Object>> utenti;

		comandoSql = "SELECT Utente.nome, Utente.cognome, Utente.username, Utente.tipo, Credenziali.attivo\n" +
				     "FROM Utente JOIN Credenziali ON Utente.username = Credenziali.username\n" +
				     "WHERE Utente.nome LIKE \""+ nome + "%\" AND Utente.cognome LIKE \""+ cognome + "%\" AND CREDENZIALI.attivo == 1;";
		utenti = dbUtenti.query(comandoSql);

		return utenti;
	}
}
