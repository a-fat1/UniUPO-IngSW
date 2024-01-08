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
    public void testControlloGiacenzaNonNumerica() throws RemoteException {
        // Test per verificare l'errore della giacenza invalida (caratteri non consentiti, compresi i numeri negativi)
        assertEquals(1, gestoreRicerche.controlloGiacenza("abc"));
    }

    @Test
    public void testControlloGiacenzaOverflow1() throws RemoteException {
        // Test per verificare l'errore di overflow (di caratteri in eccesso)
        assertEquals(2, gestoreRicerche.controlloGiacenza("1000000000000"));
    }

    @Test
    public void testControlloGiacenzaOverflow2() throws RemoteException {
        // Test per verificare l'errore di overflow (con un valore troppo elevato)
        assertEquals(2, gestoreRicerche.controlloGiacenza("2147483648"));
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
    public void testRicercaProdottiVuota() throws RemoteException {
        // Test per verificare che la lista non sia nulla e che sia vuota
        ArrayList<HashMap<String, Object>> prodotti = gestoreRicerche.ricercaProdottiConGiacenza("-1");
        assertNotNull(prodotti);
        assertTrue(prodotti.size() == 0);
    }

    @Test
    public void testRicercaProdottiConDati() throws RemoteException {
        // Test per verificare che la lista non sia nulla e che contenga dati
        ArrayList<HashMap<String, Object>> prodotti = gestoreRicerche.ricercaProdottiConGiacenza("5");
        assertNotNull(prodotti);
        assertTrue(prodotti.size() > 0);
    }
}
