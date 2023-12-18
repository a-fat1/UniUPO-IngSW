import static org.junit.Assert.*; 
import org.junit.Test;
import java.rmi.RemoteException;

import java.util.HashMap;
import java.util.ArrayList;

import DataBase.DbProdotti;
import DataBase.DbUtenti;
import Elaborazione.GestoreRicerche;

public class RF18ProdottiInEsaurimento {

    DbUtenti dbUtenti = new DbUtenti();
    DbProdotti dbProdotti = new DbProdotti();
    GestoreRicerche gestoreRicerche = new GestoreRicerche(dbUtenti, dbProdotti);

    // ===========================================================

    @Test
    public void testControlloGiacenzaNull() throws RemoteException {
        // Test per verificare l'errore della giacenza nulla lista non sia nulla e che sia vuota
        assertEquals(1, gestoreRicerche.controlloGiacenza(null));
    }

    @Test
    public void testControlloGiacenzaNonNumerico() throws RemoteException {
        // Test per verificare l'errore della giacenza invalida (caratteri non consentiti, compresi i numeri negativi)
        assertEquals(2, gestoreRicerche.controlloGiacenza("abc"));
    }

    @Test
    public void testControlloGiacenzaZero() throws RemoteException {
        // Test per verificare l'errore della giacenza uguale a 0
        assertEquals(3, gestoreRicerche.controlloGiacenza("0"));
        
    }

    @Test
    public void testControlloGiacenzaValida() throws RemoteException {
        // Test per verificare la giacenza corretta
        assertEquals(0, gestoreRicerche.controlloGiacenza("10"));
    }
    
    // ===========================================================

    @Test
    public void testRicercaProdottiNonNull() throws RemoteException {
        // Test per verificare che la lista non sia nulla e che sia vuota
        ArrayList<HashMap<String, Object>> prodotti = gestoreRicerche.ricercaProdotti("-1");
        assertNotNull(prodotti);
        assertTrue(prodotti.size() == 0);
    }

    @Test
    public void testRicercaProdottiConDati() throws RemoteException {
        // Test per verificare che la lista non sia nulla e che contenga dati
        ArrayList<HashMap<String, Object>> prodotti = gestoreRicerche.ricercaProdotti("5");
        assertNotNull(prodotti);
        assertTrue(prodotti.size() > 0);
    }
}
