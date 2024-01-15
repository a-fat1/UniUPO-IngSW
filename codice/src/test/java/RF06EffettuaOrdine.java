import static org.junit.Assert.*; 
import org.junit.Test;


import DataBase.DbProdotti;
import Elaborazione.GestoreCarrelli;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class RF06EffettuaOrdine 
{
    DbProdotti dbProdotti = new DbProdotti();
    GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(dbProdotti);


    
    @Test
    public void RF06_ControllaNumeroCarta1() throws RemoteException
    {
        //errore troppi numeri
        assertFalse(gestoreCarrelli.controllaNumeroCarta("672813924677863895"));
        
    }

    @Test
    public void RF06_ControllaNumeroCarta2() throws RemoteException
    {
        //errore pochi numeri
        assertFalse(gestoreCarrelli.controllaNumeroCarta("67281392467795"));
    }

    @Test
    public void RF06_ControllaNumeroCarta3() throws RemoteException
    {
        //ok
        assertTrue(gestoreCarrelli.controllaNumeroCarta("6728139246779538"));
    }
    

   @Test
    public void testCalcolaPrezzoTotale() {
        
    	DbProdotti dbProdottiMock = new DbProdotti();
        GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(dbProdottiMock);

        System.out.println("inizio il test");
        ArrayList<HashMap<String, Object>> carrello = new ArrayList<>();
        HashMap<String, Object> prodotto1 = new HashMap<>();
        prodotto1.put("prodotto123",  10.0); 
        prodotto1.put("quantità",  2);
        carrello.add(prodotto1);

        ArrayList<String> prodotti = new ArrayList<>();
        prodotti.add("prodotto123");

      
        float risultato = gestoreCarrelli.calcolaPrezzoTotale( carrello, prodotti);
      System.out.println("il risultato : "+risultato);
        
        assertTrue(risultato== 20.0); // Sostituisci con il risultato atteso
    }

    
}

