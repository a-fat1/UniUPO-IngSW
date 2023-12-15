package Elaborazione;

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
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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

}
