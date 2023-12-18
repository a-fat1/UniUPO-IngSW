import static org.junit.Assert.*;

import DataBase.DbProdotti;
import DataBase.DbUtenti;
import Elaborazione.GestoreRicerche;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;


public class RF11ListaOrdini {
    DbUtenti dbUtenti = new DbUtenti();
    DbProdotti dbProdotti = new DbProdotti();
    GestoreRicerche gestoreRicerche = new GestoreRicerche(dbUtenti,dbProdotti);

    @Test
    public void testRicercaPerUtente() throws RemoteException {
        //ricerca ordini dato username
        ArrayList<HashMap<String,Object>> ordini = gestoreRicerche.ricercaPerUtente("claudia");
        assertNotNull(ordini);
        assertEquals(ordini.size(),4);
        assertEquals(ordini.get(0).get("username"), "claudia");
        assertEquals(ordini.get(1).get("username"), "claudia");
        assertEquals(ordini.get(2).get("username"), "claudia");
        assertEquals(ordini.get(3).get("username"), "claudia");

        assertEquals(ordini.get(0).get("dataOrdine"), "2023-12-05 14:00:00");
        assertEquals(ordini.get(0).get("codiceProdotto"), 100004);
        assertEquals(ordini.get(0).get("quantitaProdotto"), 1);
        assertEquals(ordini.get(0).get("titolo"), "Il linguaggio C. Fondamenti e tecniche di programmazione");
        assertEquals(ordini.get(0).get("autore"), "Paul Deitel, Harvey Deitel");
        assertEquals(ordini.get(0).get("editore"), "Pearson");
        assertEquals(ordini.get(0).get("tipo"), "Libro");
        assertEquals(ordini.get(0).get("anno"), 2022);
        assertEquals(ordini.get(0).get("prezzo"), 39.9);
    }

    @Test
    public void testRicercaPerProdotto() throws RemoteException {
        //ricerca ordini dato codiceProdotto
        ArrayList<HashMap<String,Object>> ordini = gestoreRicerche.ricercaPerProdotto(100004);
        assertNotNull(ordini);
        assertEquals(ordini.size(),1);
        assertEquals(ordini.get(0).get("codiceProdotto"), 100004);

        assertEquals(ordini.get(0).get("dataOrdine"), "2023-12-05 14:00:00");
        assertEquals(ordini.get(0).get("username"), "claudia");
        assertEquals(ordini.get(0).get("quantitaProdotto"), 1);
        assertEquals(ordini.get(0).get("titolo"), "Il linguaggio C. Fondamenti e tecniche di programmazione");
        assertEquals(ordini.get(0).get("autore"), "Paul Deitel, Harvey Deitel");
        assertEquals(ordini.get(0).get("editore"), "Pearson");
        assertEquals(ordini.get(0).get("tipo"), "Libro");
        assertEquals(ordini.get(0).get("anno"), 2022);
        assertEquals(ordini.get(0).get("prezzo"), 39.9);
    }

    @Test
    public void testRicercaPerProdottoNoRisultato() throws RemoteException {
        //ricerca ordini dato codiceProdotto senza risultato
        ArrayList<HashMap<String,Object>> ordini = gestoreRicerche.ricercaPerProdotto(87688);
        assertNotNull(ordini);
        assertEquals(ordini.size(),0);
    }

    @Test
    public void testRicercaPerUtenteNoRisultato() throws RemoteException {
        //ricerca ordini dato username senza risultato
        ArrayList<HashMap<String,Object>> ordini = gestoreRicerche.ricercaPerUtente("Fabio");
        assertNotNull(ordini);
        assertEquals(ordini.size(),0);
    }
}

