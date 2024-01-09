package Elaborazione;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import DataBase.*;

public class GestoreCarrelli implements GestoreCarrelliInterfaccia
{
	private Registry registry;
	private DbProdottiInterfaccia dbProdotti;

	public GestoreCarrelli(String host) throws RemoteException, NotBoundException
	{
		registry = LocateRegistry.getRegistry(host, 1098); 
       	 	dbProdotti = (DbProdottiInterfaccia) registry.lookup("dbProdotti");
	}

	public GestoreCarrelli(DbProdotti d1) // per testing
	{
		dbProdotti = d1;
	}

	public ArrayList<HashMap<String, Object>> cercaElementi(String username) throws RemoteException {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		System.out.println("GestoreCarrelli.cercaElementi(" + username + ")\n");

		return dbProdotti.query("SELECT * "
				+ "FROM Carrello JOIN Prodotto ON Carrello.codiceProdotto = Prodotto.codice "
				+ "WHERE Carrello.username = \"" + username + "\";");
	}

	public int verificaQuantita(int nuovaQuantita, HashMap<String, Object> elemento) throws RemoteException {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		System.out.println("GestoreCarrelli.verificaQuantita(" + nuovaQuantita + ", " + elemento + ")\n");

		int quantitaCarrello = (int) elemento.get("quantitaProdotto");
		int quantitaCatalogo = (int) elemento.get("quantita");
		int quantitaRimanenteCatalogo;
		int esito;

		if (nuovaQuantita < 1 || nuovaQuantita == quantitaCarrello) { // nuovaQuantita non valida
			esito = 1;
		}
		else {
			quantitaRimanenteCatalogo = quantitaCatalogo + (quantitaCarrello - nuovaQuantita);
			if (quantitaRimanenteCatalogo < 0) { // nuovaQuantita non disponibile
				esito = 2;
			}
			else { // nuovaQuantita valida
				esito = 0;
			}
		}

		return esito;
	}

	public void modificaQuantita(int nuovaQuantita, HashMap<String, Object> elemento, String username) throws RemoteException {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		System.out.println("GestoreCarrelli.modificaQuantita("  + nuovaQuantita + ", " + elemento + ", " + username + ")\n");

		dbProdotti.query("UPDATE Carrello "
				+ "SET Carrello.quantitaProdotto = " + nuovaQuantita
				+ "WHERE Carrello.username = \"" + username
				+ "\" AND Carrello.codiceProdotto = " + elemento.get("codice") + ";");
		dbProdotti.query("UPDATE Prodotto "
				+ "SET Prodotto.quantita = "
				+ elemento.get("quantita") + ((int) elemento.get("quantitaProdotto") - nuovaQuantita)
				+ "WHERE Prodotto.codice = " + elemento.get("codice") + ";");
	}

	public void rimozioneProdottoDalCarrello(ArrayList<HashMap<String, Object>> carrello, HashMap<String, Object> elemento, String username) throws RemoteException {
		// RF07: rimuovi prodotto dal carrello
		// autori: Simone Aldo Borsa, Andrea Padoan

		if(carrello.size() == 0) return;
		
		dbProdotti.update("UPDATE Prodotto "
				+ "SET Prodotto.quantita = "
				+ elemento.get("quantita") + (int) elemento.get("quantitaProdotto")
				+ "WHERE Carrello.codiceProdotto = " + elemento.get("codiceProdotto") + ";");
		
		dbProdotti.update("DELETE FROM Carrello "
				+ "WHERE Carrello.username = \"" + username
				+ "\" AND Carrello.codiceProdotto = " + elemento.get("codice") + ";");
		
		carrello.remove(elemento);
	}
	
	public void svuotaCarrello(ArrayList<HashMap<String, Object>> carrello, String username) throws RemoteException {
		// RF07: rimuovi prodotto dal carrello
		// autori: Simone Aldo Borsa, Andrea Padoan
		
		if(carrello.size() == 0) return;
		
		for(HashMap<String, Object> elemento : carrello) {
			dbProdotti.update("UPDATE Prodotto "
					+ "SET Prodotto.quantita = "
					+ elemento.get("quantita") + (int) elemento.get("quantitaProdotto")
					+ "WHERE Carrello.codiceProdotto = " + elemento.get("codiceProdotto") + ";");
		}
		
		dbProdotti.update("DELETE FROM Carrello "
				+ "WHERE Carrello.username = \"" + username + ";");
		
		carrello.clear();
	}


	//RF 09 - aggiunta al carrello
	//autori: Fasano Lorenzo, Iacobucci Luca;
	public boolean controlloLimiteQuantita(int quantita) throws RemoteException{
		if(quantita > 0 && quantita < 4)
			return true;
		return false;
	}

	//RF 09 - aggiunta al carrello
	//autori: Fasano Lorenzo, Iacobucci Luca;
	public boolean controlloDisponibilita(int codiceProdotto) throws RemoteException{
		
		String query = "SELECT * FROM Prodotto WHERE codice =" + codiceProdotto + " AND disponibile = 1;";

		//nel caso in cui venga ritornato un hasmap con almeno un elemento, la ricerca e' andata a buon fine
		if(dbProdotti.query(query).size() != 0){
			return true;
		}

		return false;

		}

		//RF 09 - aggiunta al carrello
		//autori: Fasano Lorenzo, Iacobucci Luca;
		public void aggiornamentoQuantita(int quantita, int codiceProdotto, String username) throws RemoteException{
		String query = "UPDATE Prodotto SET quantita = quantita - " + 
		quantita + " WHERE codiceProdotto =" + codiceProdotto +"AND username = " + username + ";";
		
		dbProdotti.update(query);

		System.out.println("Aggiornamento riuscito.");

		}






}
