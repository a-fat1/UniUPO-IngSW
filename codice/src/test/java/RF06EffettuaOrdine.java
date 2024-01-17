import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import DataBase.DbProdotti;
import DataBase.DbProdottiInterfaccia;
import Elaborazione.GestoreCarrelli;

public class RF06EffettuaOrdine
{
    DbProdotti dbProdotti = new DbProdotti();
    GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(dbProdotti);


    /*
    @Test
    public void RF06_ControllaNumeroCarta1 throws RemoteException()
    {
        GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(new DbProdotti());

        // Test caso in cui il numero di carta sia valido
        assertTrue(gestoreCarrelli.controllaNumeroCarta("1234567890123456"));

        // Test caso in cui il numero di carta non sia valido (lunghezza diversa da 16)
        assertFalse(gestoreCarrelli.controllaNumeroCarta("123"));

        // Test caso in cui il numero di carta non sia valido (contiene caratteri non numerici)
        assertFalse(gestoreCarrelli.controllaNumeroCarta("123456789012345a"));
    }
    }

    @Test
    public void RF06_ControllaNumeroCarta2()
    {
        //errore pochi numeri
        assertEquals("false", gestoreCarrelli.Rf06_ControllaNumeroCarta2(67281392467795));
    }

    @Test
    public void RF06_ControllaNumeroCarta3()
    {
        //ok
        assertEquals("ok", gestoreCarrelli.Rf06_ControllaNumeroCarta3(6728139246779538));
    }
    */

    /*
    @Test
    public void RF06_CalcolaPrezzoTotale1()
    {
        //prezzo errato
        assertEquals("prezzoErrato", gestoreCarrelli.Rf06_CalcolaPrezzoTotale1(-12,00));
    }

    @Test
    public void RF06_CalcolaPrezzoTotale2()
    {
        //prezzo corretto
        assertEquals("prezzoCorretto", gestoreCarrelli.Rf06_CalcolaPrezzoTotale2(25,50));
    }
    */
    @Test
    public void testControllaNumeroCarta() throws RemoteException {
        GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(new DbProdotti());

        // Test caso in cui il numero di carta sia valido
        assertTrue(gestoreCarrelli.controllaNumeroCarta("1234567890123456"));

        // Test caso in cui il numero di carta non sia valido (lunghezza diversa da 16)
        assertFalse(gestoreCarrelli.controllaNumeroCarta("123"));

        // Test caso in cui il numero di carta non sia valido (contiene caratteri non numerici)
        assertFalse(gestoreCarrelli.controllaNumeroCarta("123456789012345a"));
    }
    @Test
    public void testCalcolaPrezzoTotale() {
        
    	DbProdotti dbProdottiMock = new DbProdotti(); 
        GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(dbProdottiMock);

        ArrayList<HashMap<String, Object>> carrello = new ArrayList<>();
        HashMap<String, Object> prodotto1 = new HashMap<>();
        prodotto1.put("prodotto123", 10.0); 
        prodotto1.put("quantità", 2);
        carrello.add(prodotto1);

        List<String> prodotti = new ArrayList<>();
        prodotti.add("prodotto123");
 
        float risultato = gestoreCarrelli.calcolaPrezzoTotale(carrello, prodotti);

        boolean risult;
        if(risultato==20)
        	risult=true;
        else
        	risult= false;
        
        assertTrue(risult);
    }
    
    @Test
    public void testAggiornaOrdini() throws RemoteException {
        DbProdotti dbProdottiMock = new DbProdotti();

        GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(dbProdottiMock);

        ArrayList<HashMap<String, Object>> listaProdottiCarrello = new ArrayList<>();
        HashMap<String, Object> prodotto1 = new HashMap<>();
        prodotto1.put("username", "testUser");
        prodotto1.put("codiceProdotto", 1);
        prodotto1.put("quantitaProdotto", 2);
        listaProdottiCarrello.add(prodotto1);

        try {
            gestoreCarrelli.aggiornaOrdini(listaProdottiCarrello, "2023-11-01");
        } catch (RemoteException e) {
            fail("Eccezione non attesa: " + e.getMessage());
        }

        assertFalse(listaProdottiCarrello.isEmpty());

    }
}

