package Elaborazione;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import DataBase.*;

public class GestoreProdotti implements GestoreProdottiInterfaccia
{
	private Registry registry;
	private DbProdottiInterfaccia dbProdotti;

	public GestoreProdotti(String host) throws RemoteException, NotBoundException
	{
		registry = LocateRegistry.getRegistry(host, 1098); 
       	 	dbProdotti = (DbProdottiInterfaccia) registry.lookup("dbProdotti");
	}

	public GestoreProdotti(DbProdotti d1) // per testing
	{
		dbProdotti = d1;
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
		dbProdotti.update("INSERT INTO Fornitura (codiceProdotto, dataFornitura, costo, quantita) VALUES ("+codProdotto+", '"+data+"', "+costo+", "+quantita+")");
		dbProdotti.update("UPDATE Prodotto SET quantita = quantita + "+quantita+" WHERE codice = "+codProdotto);
	}

	public void rimuoviProdotto(Integer codProdotto) throws RemoteException{
		//RF10 :Rimuovi Ripristina
		//Autori: Filidoro Mahfoud
		dpProdotti.update("UPDATE Prodotto SET Disponibile=False WHERE codice =" +codProdotto);
	}
	public void ripristinaProdotto(Integer codProdotto) throws RemoteException{
		//RF10 :Rimuovi Ripristina
		//Autori: Filidoro Mahfoud
		dpProdotti.update("UPDATE Prodotto SET Disponibile=True WHERE codice =" +codProdotto);
	}
}
