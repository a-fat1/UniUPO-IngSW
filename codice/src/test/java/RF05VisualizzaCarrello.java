import static org.junit.Assert.*;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import DataBase.DbProdotti;
import Elaborazione.GestoreCarrelli;

public class RF05VisualizzaCarrello {
    // RF05: visualizza carrello
    // autori: Bossola Fancesco, Oppezzo Raul

    private final GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(new DbProdotti());
    private ArrayList<HashMap<String, Object>> listaProdottiCarrello;
    private int esitoQuantita;

    @Test
    public void testcercaProdottiCarrello1() throws RemoteException { // username presente
        listaProdottiCarrello = gestoreCarrelli.cercaProdottiCarrello("ciro");
        assertEquals(2, listaProdottiCarrello.size());

        assertEquals(100002, (int) listaProdottiCarrello.get(0).get("codice"));
        assertEquals("Deep Purple", listaProdottiCarrello.get(0).get("autore"));
        assertEquals("Fireball", listaProdottiCarrello.get(0).get("titolo"));
        assertEquals("EMI", listaProdottiCarrello.get(0).get("editore"));
        assertEquals("CD", listaProdottiCarrello.get(0).get("tipo"));
        assertEquals(1971, (int) listaProdottiCarrello.get(0).get("anno"));
        assertEquals(18.0, (double) listaProdottiCarrello.get(0).get("prezzo"), 0.1);
        assertEquals(0, (int) listaProdottiCarrello.get(0).get("quantita"));
        assertEquals(1, (int) listaProdottiCarrello.get(0).get("disponibile"));
        assertEquals("ciro", listaProdottiCarrello.get(0).get("username"));
        assertEquals(100002, (int) listaProdottiCarrello.get(0).get("codiceProdotto"));
        assertEquals(1, (int) listaProdottiCarrello.get(0).get("quantitaProdotto"));

        assertEquals(100006, (int) listaProdottiCarrello.get(1).get("codice"));
        assertEquals("Ridley Scott", listaProdottiCarrello.get(1).get("autore"));
        assertEquals("Legend", listaProdottiCarrello.get(1).get("titolo"));
        assertEquals("20th Century Fox", listaProdottiCarrello.get(1).get("editore"));
        assertEquals("DVD", listaProdottiCarrello.get(1).get("tipo"));
        assertEquals(1985, (int) listaProdottiCarrello.get(1).get("anno"));
        assertEquals(10.0, (double) listaProdottiCarrello.get(1).get("prezzo"), 0.1);
        assertEquals(0, (int) listaProdottiCarrello.get(1).get("quantita"));
        assertEquals(1, (int) listaProdottiCarrello.get(1).get("disponibile"));
        assertEquals("ciro", listaProdottiCarrello.get(1).get("username"));
        assertEquals(100006, (int) listaProdottiCarrello.get(1).get("codiceProdotto"));
        assertEquals(1, (int) listaProdottiCarrello.get(1).get("quantitaProdotto"));
    }

    @Test
    public void testcercaProdottiCarrello2() throws RemoteException { // username assente
        listaProdottiCarrello = gestoreCarrelli.cercaProdottiCarrello("carlo");
        assertEquals(0, listaProdottiCarrello.size());
    }

    @Test
    public void testVerificaQuantita1() throws RemoteException { // esitoQuantita == 0 (nuovaQuantita valida)
        listaProdottiCarrello = gestoreCarrelli.cercaProdottiCarrello("clio");

        assertEquals(1, (int) listaProdottiCarrello.get(0).get("quantitaProdotto"));
        assertEquals(8, (int) listaProdottiCarrello.get(0).get("quantita"));

        esitoQuantita = gestoreCarrelli.verificaQuantita("7", listaProdottiCarrello.get(0));
        assertEquals(0, esitoQuantita);
    }

    @Test
    public void testVerificaQuantrita2() throws RemoteException { // esitoQuantita == 1 (nuovaQuantita non valida)
        listaProdottiCarrello = gestoreCarrelli.cercaProdottiCarrello("clio");

        assertEquals(1, (int) listaProdottiCarrello.get(0).get("quantitaProdotto"));
        assertEquals(8, (int) listaProdottiCarrello.get(0).get("quantita"));

        esitoQuantita = gestoreCarrelli.verificaQuantita("0", listaProdottiCarrello.get(0)); // nuovaQuantita < 1
        assertEquals(1, esitoQuantita);

        esitoQuantita = gestoreCarrelli.verificaQuantita("1", listaProdottiCarrello.get(0)); // nuovaQuantita == quantitaCarrello
        assertEquals(1, esitoQuantita);
    }

    @Test
    public void testVerificaQuantita3() throws RemoteException { // esitoQuantita == 2 (nuovaQuantita non disponibile)
        listaProdottiCarrello = gestoreCarrelli.cercaProdottiCarrello("ciro");

        assertEquals(1, (int) listaProdottiCarrello.get(0).get("quantitaProdotto"));
        assertEquals(0, (int) listaProdottiCarrello.get(0).get("quantita"));

        esitoQuantita = gestoreCarrelli.verificaQuantita("2", listaProdottiCarrello.get(0)); // quantitaRimanenteCatalogo < 0
        assertEquals(2, esitoQuantita);
    }

}
