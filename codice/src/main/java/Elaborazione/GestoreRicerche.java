package Elaborazione;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
		ArrayList<HashMap<String, Object>> ordini = null;

		System.out.println("GestoreRicerche.ricercaPerUtente(" + username + ")\n");
		comandoSql = "SELECT Ordine.username,Ordine.dataOrdine,Ordine.codiceProdotto,Ordine.quantitaProdotto,Prodotto.autore,Prodotto.titolo,Prodotto.editore,Prodotto.tipo,Prodotto.anno,Prodotto.prezzo FROM Ordine JOIN Prodotto ON Ordine.codiceProdotto = Prodotto.codice WHERE username = \"" + username + "\";";
		ordini = dbProdotti.query(comandoSql);

		return ordini;
	}

	public ArrayList<HashMap<String, Object>> ricercaPerProdotto(int codiceProdotto) throws RemoteException {
		//RF11: lista ordini
		//autore: Marino & Vecchio

		String comandoSql;
		ArrayList<HashMap<String, Object>> ordini = null;

		System.out.println("GestoreRicerche.ricercaPerProdotto(" + codiceProdotto + ")\n");
		comandoSql = "SELECT Ordine.username,Ordine.dataOrdine,Ordine.codiceProdotto,Ordine.quantitaProdotto,Prodotto.autore,Prodotto.titolo,Prodotto.editore,Prodotto.tipo,Prodotto.anno,Prodotto.prezzo FROM Ordine JOIN Prodotto ON Ordine.codiceProdotto = Prodotto.codice WHERE codice = \"" + codiceProdotto + "\";";
		ordini = dbProdotti.query(comandoSql);

		return ordini;
	}
	public int controlloParametriListaPagamenti(String dataInizio, String dataFine) throws RemoteException
	{
		//RF12: Lista Pagamenti
		//autori: Broglio, Cartieri
		int esitoControllo=0;
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String matchStr = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}$";

		if(dataInizio.matches(matchStr) && dataFine.matches(matchStr) )
		{
			LocalDate data1 = LocalDate.parse(dataInizio, formatoData);
			LocalDate data2 =  LocalDate.parse(dataFine, formatoData);
			if(data1.isBefore(data2))
			{
				esitoControllo = 0;
			}
			else
			{
				esitoControllo = 2;
			}
		}
		else
		{
			esitoControllo = 1;
		}
		return esitoControllo;
	}

	public ArrayList<HashMap<String, Object>> ricercaListaPagamentiData(String dataInizio, String dataFine) throws RemoteException
	{
		//RF12: Lista Pagamenti
		//autori: Broglio, Cartieri
		String comandoSql1,comandoSql2;
		ArrayList<HashMap<String, Object>> pagamentiTot, utentiTot, listaPagamenti=null;

		System.out.println("GestoreRicerche.ricercaListaPagamentiData(" + dataInizio +", " + dataFine+ ")\n");

		comandoSql1 = "SELECT * FROM Pagamento;";
		pagamentiTot = dbProdotti.query(comandoSql1);

		comandoSql2 = "SELECT * FROM Utente;";
		utentiTot = dbUtenti.query(comandoSql2);

		if(!pagamentiTot.isEmpty() && !utentiTot.isEmpty())
			listaPagamenti=joinPagamentoUtente(pagamentiTot,utentiTot,dataInizio,dataFine);

		return listaPagamenti;
	}

	public ArrayList<HashMap<String, Object>> ricercaListaPagamentiUtente(String username) throws RemoteException
	{
		//RF12: Lista Pagamenti
		//autori: Broglio, Cartieri
		String comandoSql1, comandoSql2;
		ArrayList<HashMap<String, Object>> pagamentiTot, utentiTot, listaPagamenti=null;

		System.out.println("GestoreRicerche.ricercaListaPagamentiUtente(" + username + ")\n");
		comandoSql1 = "SELECT * FROM Pagamento;";
		pagamentiTot = dbProdotti.query(comandoSql1);

		comandoSql2 = "SELECT * FROM Utente WHERE username = \"" + username + "\";";
		utentiTot = dbUtenti.query(comandoSql2);

		if(!pagamentiTot.isEmpty() && !utentiTot.isEmpty())
			listaPagamenti = joinPagamentoUtente(pagamentiTot, utentiTot, null, null);
		return listaPagamenti;
	}

	private ArrayList<HashMap<String, Object>> joinPagamentoUtente(ArrayList<HashMap<String, Object>> pagamentiTot,
																   ArrayList<HashMap<String, Object>> utentiTot, String dataInizio, String dataFine) throws RemoteException
	{
		//RF12: Lista Pagamenti
		//autori: Broglio, Cartieri
		ArrayList<HashMap<String, Object>> listaPagamenti=new ArrayList<>();

		for(HashMap<String, Object> pagamento: pagamentiTot)
		{
			for(HashMap<String, Object> utente: utentiTot)
			{
				boolean cond = true;
				if(dataInizio!=null && dataFine!=null)
				{
					DateTimeFormatter formato1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate data1 = LocalDate.parse(dataInizio, formato1);
					LocalDate data2 = LocalDate.parse(dataFine, formato1);
					String stringDataOrdine = pagamento.get("dataOrdine").toString().substring(0, 10);
					LocalDate dataOrdine = LocalDate.parse(stringDataOrdine, formato2);
					cond = data1.isBefore(dataOrdine) && dataOrdine.isBefore(data2) || data1.isEqual(dataOrdine) && dataOrdine.isBefore(data2) ||
							data1.isEqual(dataOrdine) && dataOrdine.isEqual(data2) || data1.isBefore(dataOrdine) && dataOrdine.isEqual(data2);
				}

				if(pagamento.get("username").equals(utente.get("username")) && cond)
				{
					HashMap<String, Object> temp = new HashMap<>();
					temp.putAll(pagamento);
					temp.putAll(utente);
					listaPagamenti.add(temp);
				}
			}
		}
		return listaPagamenti;
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
	
	public ArrayList<HashMap<String, Object>> ricercaProdottiConGiacenza(String giacenza) throws RemoteException {

        // RF18: Prodotti in esaurimento
		// Alessandro Fatone, Dario Guidotti
		
		try {	// Se la query ha successo ritorna un ArrayList con il contenuto desiderato
			return dbProdotti.query("SELECT * FROM Prodotto WHERE Quantita <= " + giacenza);
		} catch (RemoteException e) {
			return new ArrayList<>();	// In caso contrario viene ritornato un ArrayList vuoto
		}
    }

	/**
	 * Controlla se l'utente ha inserito una stringa username valida.
	 * @param username che viene inserito dall'utente.
	 * @return ritorna 1 in caso in cui la lunghezza della stringa sia < 3, altrimenti ritorna 4.
	 * @throws RemoteException
	 */

	public int controlloParametriRicercaUtente(String username) throws RemoteException {

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
	public int controlloParametriRicercaUtente(String nome, String cognome) throws RemoteException {

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

	//RF08 Raffaele Camera e Vanessa Tafarella
	public boolean controllaParametri(String titolo, String autore, String editore, String anno, ArrayList<String> tipoList) {
        boolean isTitoloEmptyOrNull = titolo==null || titolo.isEmpty();
		boolean isAutoreEmptyOrNull =autore==null || autore.isEmpty();
		boolean isEditoreEmptyOrNull =editore==null || editore.isEmpty();
		boolean isAnnoEmptyOrNull = anno == null || anno.isEmpty();
		boolean isTipoEmptyOrNull = tipoList==null || tipoList.size()==0;

		boolean isCompiledAtLeastOne = !isTitoloEmptyOrNull || !isAutoreEmptyOrNull || !isEditoreEmptyOrNull || !isAnnoEmptyOrNull || !isTipoEmptyOrNull; 
        
        // se tutte le lunghezze di tutti i paramentri è 0 torna false , altrimenti true
        return isCompiledAtLeastOne;
    }
	//RF08 Raffaele Camera e Vanessa Tafarella
    // Metodo che effettua la chiamata a dbProdotti solo se passa il check sui campi
	public ArrayList<HashMap<String, Object>> ricercaProdotto(String titolo, String autore, String editore, String anno,
			ArrayList<String> tipoList, boolean isCliente) throws RemoteException {
        
		ArrayList<HashMap<String, Object>> risultati = null;

        
		String comandoSql = "SELECT * FROM Prodotto WHERE 1=1 ";

		if (titolo != null && !titolo.isEmpty())
			comandoSql += "AND Titolo LIKE '%"+titolo+"%' ";
		if (autore != null && !autore.isEmpty())
			comandoSql += "AND Autore LIKE '%"+autore+"%' ";
		if (editore != null && !editore.isEmpty())
			comandoSql += "AND Editore LIKE '%"+editore+"%' ";
		if (anno != null && !anno.isEmpty())
			comandoSql += "AND Anno = "+anno+" ";
		
		
		if (tipoList != null && tipoList.size()>0){
			comandoSql += "AND Tipo IN ( ";
			for(int i=0; i<tipoList.size(); i++){
				comandoSql += " '"+tipoList.get(i)+"' ";
				// Aggiungi la virgola se non è l'ultimo elemento
				if (i < tipoList.size() - 1) {
					comandoSql += ",";
				}
			}
			comandoSql+=")";
		}

		if(isCliente){
			comandoSql += " AND (disponibile = 1 AND quantita > 0) ";
		}
			
		risultati = dbProdotti.query(comandoSql);
		if(risultati!=null){
			System.out.println( risultati.size());
		   }
		return risultati;
	}
}
