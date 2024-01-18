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

