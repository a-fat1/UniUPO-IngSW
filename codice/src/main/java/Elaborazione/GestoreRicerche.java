package Elaborazione;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

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
	
	// Metodo per controllare i parametri di ricerca
	
    public GestoreRicerche(DbProdotti dbProdotti2) {
		dbProdotti = dbProdotti2;
	}

	public boolean controllaParametri(String titolo, String autore, String editore, String anno, String tipo) {
        /*
        return titolo != null && !titolo.isEmpty() &&
               autore != null && !autore.isEmpty() &&
               editore != null && !editore.isEmpty() &&
               anno!=null && anno > 0 &&
               tipo != null && !tipo.isEmpty();*/
        
        // se tutte le lunghezze di tutti i paramentri è 0 torna false , altrimenti true
        return !(
                  (titolo==null || titolo.isEmpty()) && 
                  (autore==null || autore.isEmpty()) &&
                  (editore==null || editore.isEmpty()) &&
                  (anno==null || anno.isEmpty()) &&
                  (tipo==null || tipo.isEmpty())
                );
    }
	
    // Metodo che effettua la chiamata a dbProdotti solo se passa il check sui campi
	public ArrayList<HashMap<String, Object>> ricercaProdotto(String titolo, String autore, String editore, String anno,
			String tipo) throws RemoteException {
        
        if (controllaParametri(titolo, autore, editore, anno, tipo)) {
        	ArrayList<HashMap<String, Object>> risultato = dbProdotti.ricercaProdotto(titolo, autore, editore, anno, tipo);
        	stampaLista(risultato);
        	return risultato;
        }

        System.out.println("Obbligatorio inserire almeno un campo di ricerca.");
        return null;
	}
	

	// Metodo per stampare l'ArrayList
    public static void stampaLista(ArrayList<HashMap<String, Object>> list) {
    	if(list==null || list.isEmpty()) {
    		System.out.println("Nessun risultato trovato.");
    		return;
    	}
    	
        for (HashMap<String, Object> item : list) {
            System.out.println("Riga:");
            for (String key : item.keySet()) {
                System.out.println("  " + key + ": " + item.get(key));
            }
            System.out.println();
        }
    }

}
