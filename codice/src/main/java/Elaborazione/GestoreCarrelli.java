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
	//	String regex = "\\d+";
		
	System.out.println("Inserisce il numero di carta: " + NumeroCarta);
	 
	 if(NumeroCarta.length()==16 /*&& Pattern.matches(regex, NumeroCarta)*/) {
		 
		  
		  Controllo=true;
		  
	 }
	 else {
		 
		System.out.println("Numero carta non valido: deve contener 16 caratteri");
		 Controllo=false;
		 
	     }
	 
	 return Controllo;
}	


public void aggiornaOrdini(ArrayList<HashMap<String, Object>> listaProdottiCarrello, String dataOrdine) throws RemoteException{
	//RF 06 - effettua ordine
	//autori: Virginia Luini, Jorelle MENGAPTCHE
	String username;	
	int codiceProdotto;
	int quantitaProdotto;
	String query="";
				//1234567891234567
	for (var ordine : listaProdottiCarrello) {
	        // Estrai quantità e prezzo dall'ordine
		username = ordine.get("username").toString();
		System.out.println(username);		
		codiceProdotto =(int)ordine.get("codiceProdotto");    
		quantitaProdotto =(int)ordine.get("quantitaProdotto");
		query = "INSERT INTO Ordine (username, dataOrdine, codiceProdotto, quantitaProdotto) VALUES ('" + username + "'," + " '" + dataOrdine + "', " + codiceProdotto + ", "+ quantitaProdotto + ");" ;
	        // Calcola il costo totale del prodotto moltiplicando la quantità per il prezzo
		dbProdotti.update(query);    
	    }

}

public void aggiornaPagamenti(String username,String dataOrdine,float prezzoTotale,String numeroCarta,int sceltaMostraFormCarta) throws RemoteException{
	//RF 09 - aggiunta al carrello
	//autori: Fasano Lorenzo, Iacobucci Luca;
	String tipoCarta="";
	String query="";
	System.out.println(prezzoTotale);
	if(sceltaMostraFormCarta==0) {
		tipoCarta="Visa";
		
	}else {
		if(sceltaMostraFormCarta==1) {
			tipoCarta="Mastercard";
		}else {
			if(sceltaMostraFormCarta==2)
				tipoCarta="AmEX";
		}
			
	}
	
	query = "INSERT INTO Pagamento (username, dataOrdine, importo, numeroCarta, tipoCarta) VALUES ('" + username + "'," + " '" + dataOrdine + "', " + prezzoTotale + ", '" + numeroCarta + "', '"+ tipoCarta + "');" ;
	        // Calcola il costo totale del prodotto moltiplicando la quantità per il prezzo
	dbProdotti.update(query);    
}



	public void rimozioneProdottoDalCarrello(ArrayList<HashMap<String, Object>> carrello, HashMap<String, Object> elemento, String username) throws RemoteException {
		// RF07: rimuovi prodotto dal carrello
		// autori: Simone Aldo Borsa, Andrea Padoan

		if(carrello.size() == 0) return;
			
			int q = (int) elemento.get("quantita") + (int) elemento.get("quantitaProdotto");
			dbProdotti.update("UPDATE Prodotto "
					+ "SET quantita = "
					+ q
					+ " WHERE codice = " + elemento.get("codiceProdotto") + ";");
			
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
				int q = (int) elemento.get("quantita") + (int) elemento.get("quantitaProdotto");
				dbProdotti.update("UPDATE Prodotto "
						+ "SET quantita = "
						+ q
						+ " WHERE codice = " + elemento.get("codiceProdotto") + ";");
			}
			
			dbProdotti.update("DELETE FROM Carrello "
					+ "WHERE Carrello.username = \"" + username + "\";");
		
		carrello.clear();
	}


public boolean controlloLimiteQuantita(int quantita) throws RemoteException{
//RF 09 - aggiunta al carrello
//autori: Fasano Lorenzo, Iacobucci Luca;
	if(quantita > 0 && quantita < 4)
		return true;
	return false;
}

public boolean controlloDisponibilita(int codiceProdotto) throws RemoteException{
//RF 09 - aggiunta al carrello
//autori: Fasano Lorenzo, Iacobucci Luca;

	//cerco prodotto nel database disponibile
	String query = "SELECT * FROM Prodotto WHERE codice =" + codiceProdotto + " AND disponibile = 1 AND quantita > 0;";

	//nel caso in cui venga ritornato un hasmap con almeno un elemento, la ricerca e' andata a buon fine
	if(dbProdotti.query(query).size() != 0){
		return true;
	}
	return false;
	}

	public void aggiornamentoQuantita(int quantita, int codiceProdotto, String username) throws RemoteException{
	//RF 09 - aggiunta al carrello
	//autori: Fasano Lorenzo, Iacobucci Luca;

		String query = "UPDATE Prodotto SET quantita = quantita - " + quantita + " WHERE codice =" + codiceProdotto + ";";
		dbProdotti.update(query);
		System.out.println("Aggiornamento riuscito.");

		query = "INSERT INTO Carrello (username, codiceProdotto, quantitaProdotto) VALUES ('" + username + "'," + codiceProdotto + ","+ quantita + ");" ;
		dbProdotti.update(query);

		}

}

