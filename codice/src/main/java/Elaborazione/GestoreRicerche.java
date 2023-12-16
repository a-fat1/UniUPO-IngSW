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

	public int controlloParametri(String dataInizio, String dataFine)
	{
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

	/*public ArrayList<HashMap<String, Object>> ricercaListaPagamentiUtente(String username)
	{

	}*/

	public ArrayList<HashMap<String, Object>> ricercaListaPagamentiData(String dataInizio, String dataFine) throws RemoteException
	{
		String comandoSql1,comandoSql2;
		ArrayList<HashMap<String, Object>> pagamentiTot, utentiTot, pagamenti;

		System.out.println("GestoreRicerche.ricercaListaPagamentiData(" + dataInizio +", " + dataFine+ ")\n");

		//comandoSql = "SELECT * FROM Pagamento JOIN Utente ON Pagamento.username=Utente.username WHERE \"" + dataInizio + "\"<= dataOrdine AND dataOrdine<=\"" + dataFine + "\" ;";

		comandoSql1 = "SELECT * FROM Pagamento;";
		pagamentiTot = dbProdotti.query(comandoSql1);

		comandoSql2 = "SELECT * FROM Utente;";
		utentiTot = dbUtenti.query(comandoSql2);

		pagamenti=joinPagamentoUtente(pagamentiTot,utentiTot,dataInizio,dataFine);

		return pagamenti;
	}

	private ArrayList<HashMap<String, Object>> joinPagamentoUtente(ArrayList<HashMap<String, Object>> pagamentiTot,
																   ArrayList<HashMap<String, Object>> utentiTot, String dataInizio, String dataFine) throws RemoteException
	{
		ArrayList<HashMap<String, Object>> pagamenti=new ArrayList<>();

		DateTimeFormatter formato1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate data1 = LocalDate.parse(dataInizio, formato1);
		LocalDate data2 =  LocalDate.parse(dataFine, formato1);

		for(HashMap<String, Object> pagamento: pagamentiTot)
		{
			for(HashMap<String, Object> utente: utentiTot)
			{
				String stringDataOrdine= pagamento.get("dataOrdine").toString().substring(0,10);
				LocalDate dataOrdine = LocalDate.parse(stringDataOrdine, formato2);
				boolean cond = data1.isBefore(dataOrdine) && dataOrdine.isBefore(data2) || data1.isEqual(dataOrdine) && dataOrdine.isBefore(data2) ||
						data1.isEqual(dataOrdine) && dataOrdine.isEqual(data2) || data1.isBefore(dataOrdine) && dataOrdine.isEqual(data2);
				if(pagamento.get("username").equals(utente.get("username")) && cond)
				{
					HashMap<String, Object> temp = new HashMap<>();
					temp.putAll(pagamento);
					temp.putAll(utente);
					pagamenti.add(temp);
				}
			}
		}
		System.out.println(pagamenti.toString()+"\n");
		return pagamenti;
	}
}
