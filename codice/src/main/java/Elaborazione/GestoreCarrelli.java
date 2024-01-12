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
}
