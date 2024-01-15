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

	public ArrayList<HashMap<String, Object>> cercaProdottiCarrello(String username) throws RemoteException {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		return dbProdotti.query("SELECT * "
				+ "FROM Carrello JOIN Prodotto ON Carrello.codiceProdotto = Prodotto.codice "
				+ "WHERE Carrello.username = \"" + username + "\";");
	}

	public int verificaQuantita(String nuovaQuantita, HashMap<String, Object> prodottoSelezionato) throws RemoteException {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		int quantitaCarrello = (int) prodottoSelezionato.get("quantitaProdotto");
		int quantitaCatalogo = (int) prodottoSelezionato.get("quantita");
		int quantitaRimanenteCatalogo;
		int esito;

		try {
			if (Integer.parseInt(nuovaQuantita) < 1 || Integer.parseInt(nuovaQuantita) == quantitaCarrello) { // nuovaQuantita non valida
				esito = 1;
			}
			else {
				quantitaRimanenteCatalogo = quantitaCatalogo + (quantitaCarrello - Integer.parseInt(nuovaQuantita));
				if (quantitaRimanenteCatalogo < 0) { // nuovaQuantita non disponibile
					esito = 2;
				}
				else { // nuovaQuantita valida
					esito = 0;
				}
			}
		} catch (NumberFormatException e) {
			esito = 1;
		}

		return esito;
	}

	public void modificaQuantita(String nuovaQuantita, HashMap<String, Object> prodottoSelezionato, String username) throws RemoteException {
		// RF05: visualizza carrello
		// autori: Bossola Fancesco, Oppezzo Raul

		dbProdotti.update("UPDATE Carrello "
				+ "SET quantitaProdotto = " + nuovaQuantita + " "
				+ "WHERE username = \"" + username + "\" "
				+ "AND codiceProdotto = " + prodottoSelezionato.get("codice") + ";");
		dbProdotti.update("UPDATE Prodotto "
				+ "SET quantita = " + ((int) prodottoSelezionato.get("quantita")
				+ ((int) prodottoSelezionato.get("quantitaProdotto") - Integer.parseInt(nuovaQuantita))) + " "
				+ "WHERE codice = " + prodottoSelezionato.get("codice") + ";");
	}

	
public boolean controllaNumeroCarta(String NumeroCarta) {
//RF06 Effettua Ordine
// Autori: Virginia Luini, Jorelle MENGAPTCHE
		boolean Controllo=false;
		
	System.out.println("Inserisce il numero di carta: " + NumeroCarta);
	 
	 if(NumeroCarta.length()==16 ) {
		 
		  
		  Controllo=true;
		  
	 }
	 else {
		 
		System.out.println("Numero carta non valido: deve contener 16 caratteri");
		 Controllo=false;
		 
	     }
	 
	 return Controllo;
}


public float calcolaPrezzoTotale(ArrayList<HashMap<String, Object>> carrello) throws RemoteException {
	/*RF06: Calcola prezzototale
	 * Autori: MENGAPTCHE ,LUINI*/

	float somma = 0;

	for(var prodotto : carrello) {
		var prezzo =  dbProdotti.query("SELECT prezzo FROM Prodotto WHERE codice LIKE " + "\"" + prodotto.get("codiceProdotto") + "\"");
		somma += Float.parseFloat(prezzo.get(0).get("prezzo").toString())*Integer.parseInt(prodotto.get("quantitaProdotto").toString());
 	}

	return somma;
}

public void rimozioneProdottoDalCarrello(ArrayList<HashMap<String, Object>> carrello, HashMap<String, Object> elemento, String username) throws RemoteException {
	// RF07: rimuovi prodotto dal carrello
	// autori: Simone Aldo Borsa, Andrea Padoan

	if(carrello.size() == 0) return;
	
//		Codice commentato per evitare di modificare il database in fase di testing
//		
//		dbProdotti.update("UPDATE Prodotto "
//				+ "SET Prodotto.quantita = "
//				+ elemento.get("quantita") + (int) elemento.get("quantitaProdotto")
//				+ "WHERE Carrello.codiceProdotto = " + elemento.get("codiceProdotto") + ";");
//		
//		dbProdotti.update("DELETE FROM Carrello "
//				+ "WHERE Carrello.username = \"" + username
//				+ "\" AND Carrello.codiceProdotto = " + elemento.get("codice") + ";");
	
	carrello.remove(elemento);
}

public void svuotaCarrello(ArrayList<HashMap<String, Object>> carrello, String username) throws RemoteException {
	// RF07: rimuovi prodotto dal carrello
	// autori: Simone Aldo Borsa, Andrea Padoan
	
	if(carrello.size() == 0) return;
	
//		Codice commentato per evitare di modificare il database in fase di testing
//	
//		for(HashMap<String, Object> elemento : carrello) {
//			dbProdotti.update("UPDATE Prodotto "
//					+ "SET Prodotto.quantita = "
//					+ elemento.get("quantita") + (int) elemento.get("quantitaProdotto")
//					+ "WHERE Carrello.codiceProdotto = " + elemento.get("codiceProdotto") + ";");
//		}
//		
//		dbProdotti.update("DELETE FROM Carrello "
//				+ "WHERE Carrello.username = \"" + username + ";");
	
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
	quantita + " WHERE codice =" + codiceProdotto + /*" AND username = " + username +*/ ";";
	
	dbProdotti.update(query);

	System.out.println("Aggiornamento riuscito.");

	
	query = "INSERT INTO Carrello (username, codiceProdotto, quantitaProdotto) VALUES ('" + username + "'," + codiceProdotto + "," + quantita + ");" ;

	dbProdotti.update(query);

	System.out.println("Aggiornamento carrello riuscito.");

	}

}

