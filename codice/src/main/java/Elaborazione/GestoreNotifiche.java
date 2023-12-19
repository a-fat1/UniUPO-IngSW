package Elaborazione;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import DataBase.*;

public class GestoreNotifiche implements GestoreNotificheInterfaccia
{
	private Registry registry;
	private DbNotificheInterfaccia dbNotifiche;

	public GestoreNotifiche(String host) throws RemoteException, NotBoundException
	{
		registry = LocateRegistry.getRegistry(host, 1098); 
       	 	dbNotifiche = (DbNotificheInterfaccia) registry.lookup("dbNotifiche");
	}

	public GestoreNotifiche(DbNotifiche d1) // per testing
	{
		dbNotifiche = d1;

	}

	public ArrayList<HashMap<String, Object>> ricercaNotifiche(String tipoUtente, String myDateObj) throws RemoteException{
		String comandoSql;
		ArrayList<HashMap<String,Object>> notifica=null;
		comandoSql="SELECT * FROM Notifica WHERE(tipoUtente='"+tipoUtente+"' OR tipoUtente='tutti')"+" AND dataScadenza>='"+myDateObj+"'";
		try{
			notifica=dbNotifiche.query(comandoSql);
		}
		catch (Exception e){
			System.err.println(e.toString());
			e.printStackTrace();
		}
		return notifica;
	}
}
