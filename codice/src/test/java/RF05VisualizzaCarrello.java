/*import static org.junit.Assert.*;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import DataBase.DbProdotti;
import Elaborazione.GestoreCarrelli;

public class RF05VisualizzaCarrello {
    // RF05: visualizza carrello
    // autori: Bossola Fancesco, Oppezzo Raul

    private GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(new DbProdotti());
    private ArrayList<HashMap<String, Object>> listaElementi;
    private int esito;

    @Test
    public void testCercaElementi1() throws RemoteException { // username presente
        listaElementi = gestoreCarrelli.cercaElementi("ciro");
        assertEquals(2, listaElementi.size());

        assertEquals(100002, (int) listaElementi.get(0).get("codice"));
        assertEquals("Deep Purple", listaElementi.get(0).get("autore"));
        assertEquals("Fireball", listaElementi.get(0).get("titolo"));
        assertEquals("EMI", listaElementi.get(0).get("editore"));
        assertEquals("CD", listaElementi.get(0).get("tipo"));
        assertEquals(1971, (int) listaElementi.get(0).get("anno"));
        assertEquals(18.0, (double) listaElementi.get(0).get("prezzo"), 0.1);
        assertEquals(0, (int) listaElementi.get(0).get("quantita"));
        assertEquals(1, (int) listaElementi.get(0).get("disponibile"));
        assertEquals("ciro", listaElementi.get(0).get("username"));
        assertEquals(100002, (int) listaElementi.get(0).get("codiceProdotto"));
        assertEquals(1, (int) listaElementi.get(0).get("quantitaProdotto"));

        assertEquals(100006, (int) listaElementi.get(1).get("codice"));
        assertEquals("Ridley Scott", listaElementi.get(1).get("autore"));
        assertEquals("Legend", listaElementi.get(1).get("titolo"));
        assertEquals("20th Century Fox", listaElementi.get(1).get("editore"));
        assertEquals("DVD", listaElementi.get(1).get("tipo"));
        assertEquals(1985, (int) listaElementi.get(1).get("anno"));
        assertEquals(10.0, (double) listaElementi.get(1).get("prezzo"), 0.1);
        assertEquals(0, (int) listaElementi.get(1).get("quantita"));
        assertEquals(1, (int) listaElementi.get(1).get("disponibile"));
        assertEquals("ciro", listaElementi.get(1).get("username"));
        assertEquals(100006, (int) listaElementi.get(1).get("codiceProdotto"));
        assertEquals(1, (int) listaElementi.get(1).get("quantitaProdotto"));
    }

    @Test
    public void testCercaElementi2() throws RemoteException { // username assente
        listaElementi = gestoreCarrelli.cercaElementi("carlo");
        assertEquals(0, listaElementi.size());
    }

    @Test
    public void testVerificaQuantita1() throws RemoteException { // esito == 0 (nuovaQuantita valida)
        listaElementi = gestoreCarrelli.cercaElementi("clio");

        assertEquals(1, (int) listaElementi.get(0).get("quantitaProdotto"));
        assertEquals(8, (int) listaElementi.get(0).get("quantita"));

        esito = gestoreCarrelli.verificaQuantita(7, listaElementi.get(0));
        assertEquals(0, esito);
    }

    @Test
    public void testVerificaQuantrita2() throws RemoteException { // esito == 1 (nuovaQuantita non valida)
        listaElementi = gestoreCarrelli.cercaElementi("clio");

        assertEquals(1, (int) listaElementi.get(0).get("quantitaProdotto"));
        assertEquals(8, (int) listaElementi.get(0).get("quantita"));

        esito = gestoreCarrelli.verificaQuantita(0, listaElementi.get(0)); // nuovaQuantita < 1
        assertEquals(1, esito);

        esito = gestoreCarrelli.verificaQuantita(1, listaElementi.get(0)); // nuovaQuantita == quantitaCarrello
        assertEquals(1, esito);
    }

    @Test
    public void testVerificaQuantita3() throws RemoteException { // esito == 2 (nuovaQuantita non disponibile)
        listaElementi = gestoreCarrelli.cercaElementi("ciro");

        assertEquals(1, (int) listaElementi.get(0).get("quantitaProdotto"));
        assertEquals(0, (int) listaElementi.get(0).get("quantita"));

        esito = gestoreCarrelli.verificaQuantita(2, listaElementi.get(0)); // quantitaRimanenteCatalogo < 0
        assertEquals(2, esito);
    }

}*/
