package Elaborazione;


import DataBase.DbProdotti;
import DataBase.DbProdottiInterfaccia;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;

public class GestoreProdotti implements GestoreProdottiInterfaccia {
	private Registry registry;
	private DbProdottiInterfaccia dbProdotti;

	public GestoreProdotti(String host) throws RemoteException, NotBoundException {
		registry = LocateRegistry.getRegistry(host, 1098);
		dbProdotti = (DbProdottiInterfaccia) registry.lookup("dbProdotti");
	}

	public GestoreProdotti(DbProdotti d1) // per testing
	{
		dbProdotti = d1;
	}


	@Override
	public ArrayList<HashMap<String, Object>> ricercaListaForniture(int codice) throws RemoteException {// RF 13
																										// Benetti-Chiappa
		try {
			return dbProdotti.query("SELECT * FROM Fornitura WHERE codiceProdotto=" + codice);
		} catch (RemoteException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public ArrayList<HashMap<String, Object>> ricercaListaForniture(String dataInizio, String dataFine)
			throws RemoteException {// RF 13 Benetti-Chiappa

		if (controlloParametri(dataInizio, dataFine) == 0)
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				DateTimeFormatter stringFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dataIn = LocalDateTime.of(LocalDate.parse(dataInizio, formatter), LocalTime.MIDNIGHT);
				LocalDateTime dataFin = LocalDateTime.of(LocalDate.parse(dataFine, formatter), LocalTime.MAX);

				return dbProdotti.query(
						"SELECT f.*, p.autore ,p.titolo, p.editore FROM Fornitura AS f JOIN Prodotto AS p on f.codiceProdotto=p.codice WHERE f.dataFornitura BETWEEN '"
								+ dataIn.format(stringFormatter) + "' AND '" + dataFin.format(stringFormatter) + "'");
			} catch (RemoteException e) {
				return new ArrayList<>();
			}
		return new ArrayList<>();
	}

	@Override
	public int controlloParametri(String dataInizio, String dataFine) throws RemoteException {// RF 13 Benetti-Chiappa
		int esitoControllo = 0;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dataIn = null;
		LocalDate dataFin = null;
		try {
			dataIn = LocalDate.parse(dataInizio, formatter);
			dataFin = LocalDate.parse(dataFine, formatter);
		} catch (Exception e) {
			esitoControllo = 1;
		}

		if (dataIn != null && dataFin != null) {
			if (dataIn.isBefore(dataFin))
				esitoControllo = 0;
			else {
				esitoControllo = 2;
			}
		}
		return esitoControllo;
	}



	//RF15 (Nicolò Bianchetto, Kristian Rigo)
	public HashMap<String, Boolean> controlloDatiFornitura(String data, Float costo, Integer quantita) {
		HashMap<String, Boolean> esitoControllo = new HashMap<>();
		for(String s : new String[] {"esitoData", "esitoCosto", "esitoQuantità"})
			esitoControllo.put(s, null);

		if(data != null && !data.isEmpty()) {
			try {
				String dataOra = data + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				LocalDateTime dataFornitura = LocalDateTime.parse(dataOra, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT));
				esitoControllo.put("esitoData", !dataFornitura.isAfter(LocalDateTime.now()));
			}
			catch(DateTimeParseException e) {
				esitoControllo.put("esitoData", false);
			}
		}
		if(costo != null) esitoControllo.put("esitoCosto", Float.compare(costo, 0.0f) > 0);
		if(quantita != null) esitoControllo.put("esitoQuantità", quantita > 0);

		return esitoControllo;
	}

	//RF15 (Nicolò Bianchetto, Kristian Rigo)
	public void aggiungiFornitura(Integer codProdotto, String data, Float costo, Integer quantita) throws RemoteException {
		String dataOra = data + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		dbProdotti.update("INSERT INTO Fornitura (codiceProdotto, dataFornitura, costo, quantita) VALUES ("+codProdotto+", '"+dataOra+"', "+costo+", "+quantita+")");
		dbProdotti.update("UPDATE Prodotto SET quantita = quantita + "+quantita+" WHERE codice = "+codProdotto);
	}

	/**
	 * @param autori array String di autori inseriti dall'utente
	 * @param titolo String titolo
	 * @param editore String editore
	 * @param anno int anno
	 * @return int codice di successo 0, o il codice di errore 1-4
	 * @throws RemoteException
	 *
	 * Controlla che i campi inseriti siano corretti, al primo
	 * errato che incontra ritorna il codice di errore corrispondente
	 */
	@Override
	public int verificaCampi(String[] autori, String titolo, String editore, int anno) throws RemoteException{
		//RF16
		int lenTitolo = titolo.length();
		if(lenTitolo <= 0) return 1; //titolo mancante

		if(anno<1900 || anno> Year.now().getValue()) return 2; //anno errato

		int lenEditore = editore.length();
		if(lenEditore <= 0) return 3;	//editore mancante

		int esisteAutore = autori.length;
		if(esisteAutore <= 0) return 4;	//autore mancante
		int lenAutore = autori[0].length();
		if(lenAutore == 0) return 4;	//autore mancante

		return 0;
	}

	/**
	 * @param autori array String di autori inseriti dall'utente
	 * @param titolo String titolo
	 * @param editore String editore
	 * @param anno int anno
	 * @param tipo String tipo
	 * @return true se il prodotto è unico, altrimenti false
	 * @throws RemoteException
	 *
	 * Effettua una query al database cercando prodotti con gli stessi dati di quelli inseriti
	 * Se nulla viene ritornato allora ritorna true (successo, prodotto unico) altrimenti false
	 */
	@Override
	public boolean controlloUnicita(String[] autori, String titolo, String editore, int anno, String tipo) throws RemoteException {
		//RF16
		ArrayList<HashMap<String, Object>> prodotti = dbProdotti.query(
				"SELECT * FROM Prodotto " +
						"WHERE autore = \""+ String.join(", ", autori)+"\" AND titolo = \""+titolo+"\" AND editore = \""+editore+"\" AND anno = \"" + anno + "\" AND tipo = \"" + tipo + "\""
		);

		return prodotti.isEmpty();
	}

	/**
	 * @param autori array String di autori inseriti dall'utente
	 * @param titolo String titolo
	 * @param editore String editore
	 * @param anno int anno
	 * @param tipo String tipo
	 * @return HashMap<String, Object> cha rappresenta il prodotto appena aggiunto
	 * @throws RemoteException
	 *
	 * Effettua una query al database che aggiunge in nuovo prodotto
	 * Il database assegna un Codice al prodotto appena inserito (AUTOINCREMENT)
	 * Effettua una query per ricevere il prodotto con codice più alto (quello appena inserito) e lo ritorna
	 */
	@Override
	public HashMap<String, Object> aggiungiProdotto(String[] autori, String titolo, String editore, int anno, String tipo) throws RemoteException {
		//RF16
		dbProdotti.update(
				"INSERT INTO Prodotto(autore, titolo, editore, anno, tipo, prezzo, quantita, disponibile) " +
						"VALUES (\""+ String.join(", ", autori) +"\", \""+ titolo +"\", \""+ editore +"\", \""+ anno +"\", \""+ tipo +"\", null, 0, True)"
				);

		ArrayList<HashMap<String, Object>> prodotti = dbProdotti.query(
				"SELECT * " +
						"FROM Prodotto " +
						"WHERE Codice = (SELECT MAX(Codice) FROM Prodotto)"
		);
		return prodotti.get(0);

	}

	public void rimuoviProdotto(Integer codProdotto) throws RemoteException{
		//RF10 :Rimuovi Ripristina
		//Autori: Filidoro Mahfoud
		dbProdotti.update("UPDATE Prodotto SET Disponibile=False WHERE codice =" +codProdotto);
	}
	public void ripristinaProdotto(Integer codProdotto) throws RemoteException{
		//RF10 :Rimuovi Ripristina
		//Autori: Filidoro Mahfoud
		dbProdotti.update("UPDATE Prodotto SET Disponibile=True WHERE codice =" +codProdotto);
	}
	
	public boolean controlloPercentuale( int Percentuale){
		//RF17 Incrementea/Decrementa
		boolean esitoControllo;
		if(Percentuale > 0 && Percentuale <= 100){
			esitoControllo=true;
		}else
		{
			esitoControllo=false;
		}
		return esitoControllo;
	}

	public void incrementaPrezzi(int Percentuale) throws RemoteException{
		//RF17 Incrementea/Decrementa
		dbProdotti.update("UPDATE Prodotto SET prezzo = prezzo * (1 + " + Percentuale + " / 100)");
		}

	public void decrementaPrezzi(int Percentuale) throws RemoteException{
			//RF17 Incrementea/Decrementa
			dbProdotti.update("UPDATE Prodotto SET prezzo = prezzo * (1 - " + Percentuale + " / 100)");
	}

	public int controlloFormatoModificaPrezzo(float prezzoNuovo, float prezzoVecchio)
	{	
		//RF14
		
		int controllo;

		if(prezzoNuovo <= 0)
			controllo = 1;
		else if(prezzoNuovo == prezzoVecchio)
		{
			controllo = 2;
		}
		else 
		{
			controllo = 0;
		}
		
		return controllo;
	}

	public void modificaPrezzo(float prezzo, int codice) throws RemoteException
	{
		//RF14
		String query = "UPDATE Prodotto SET prezzo ="+prezzo+" WHERE codice ="+codice;
		dbProdotti.update(query);
	}
}
