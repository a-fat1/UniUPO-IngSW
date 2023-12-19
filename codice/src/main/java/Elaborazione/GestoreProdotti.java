package Elaborazione;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.Year;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import DataBase.*;

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
						"SELECT f.*, p.autore ,p.autore, p.editore FROM Fornitura AS f JOIN Prodotto AS p on f.codiceProdotto=p.codice WHERE f.dataFornitura BETWEEN '"
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



	public HashMap<String, Boolean> controlloDatiFornitura(String data, Float costo, Integer quantita) {
		HashMap<String, Boolean> esitoControllo = new HashMap<>();
		for(String s : new String[] {"esitoData", "esitoCosto", "esitoQuantita"})
			esitoControllo.put(s, null);

		if(data != null && !data.isEmpty()) {
			try {
				String dataOra = data + " " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				LocalDateTime dataFornitura = LocalDateTime.parse(dataOra, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT));
				esitoControllo.put("esitoData", !dataFornitura.isAfter(LocalDateTime.now()));
			}
			catch(DateTimeParseException e) {
				esitoControllo.put("esitoData", false);
			}
		}
		if(costo != null) esitoControllo.put("esitoCosto", Float.compare(costo, 0.0f) > 0);
		if(quantita != null) esitoControllo.put("esitoQuantita", quantita > 0);

		return esitoControllo;
	}

	public void aggiungiFornitura(Integer codProdotto, String data, Float costo, Integer quantita) throws RemoteException {
		dbProdotti.update("INSERT INTO Fornitura (codiceProdotto, dataFornitura, costo, quantita) VALUES (" + codProdotto + ", '" + data + "', " + costo + ", " + quantita + ")");
		dbProdotti.update("UPDATE Prodotto SET quantita = quantita + " + quantita + " WHERE codice = " + codProdotto);
	}

	public int verificaCampi(String[] autori, String titolo, String editore, int anno){
		int lenTitolo = titolo.length();
		if(lenTitolo == 0) return 1; //titolo mancante

		if(anno<1900 || anno> Year.now().getValue()) return 2; //anno errato

		int lenEditore = editore.length();
		if(lenEditore == 0) return 3;	//editore mancante

		if(autori.length == 0) return 4;	//autore mancante
		int lenAutore = autori[0].length();
		if(lenAutore == 0) return 4;	//autore mancante

		return 0;
	}

	public boolean controlloUnicita(String[] autori, String titolo, String editore, int anno, String tipo) throws RemoteException {
		ArrayList<HashMap<String, Object>> prodotti = dbProdotti.query(
				"SELECT * FROM Prodotto " +
						"WHERE autore = \""+ String.join(", ", autori)+"\" AND titolo = \""+titolo+"\" AND editore = \""+editore+"\" AND anno = \"" + anno + "\" AND tipo = \"" + tipo + "\""
		);

		return prodotti.isEmpty();
	}

	public HashMap<String, Object> aggiungiProdotto(String[] autori, String titolo, String editore, int anno, String tipo) throws RemoteException {
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

}
