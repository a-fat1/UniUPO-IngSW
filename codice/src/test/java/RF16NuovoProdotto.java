import static org.junit.Assert.*;
import org.junit.Test;

import DataBase.DbProdotti;
import Elaborazione.GestoreProdotti;
import java.rmi.RemoteException;


public class RF16NuovoProdotto {

    DbProdotti dbProdotti = new DbProdotti();
    GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);

    @Test
    public void testVerificaCampi()
    {
        // OK
        assertEquals(0, gestoreProdotti.verificaCampi(new String[]{"autore"}, "titolo", "editore", 2007));

        // titolo mancante
        assertEquals(1, gestoreProdotti.verificaCampi(new String[]{"autore"}, "", "editore", 2007));

        // anno errato
        assertEquals(2, gestoreProdotti.verificaCampi(new String[]{"autore"}, "titolo", "editore", 200));
        assertEquals(2, gestoreProdotti.verificaCampi(new String[]{"autore"}, "titolo", "editore", 8000));

        // editore mancante
        assertEquals(3, gestoreProdotti.verificaCampi(new String[]{"autore"}, "titolo", "", 2007));

        // autore mancante
        assertEquals(4, gestoreProdotti.verificaCampi(new String[]{""}, "titolo", "editore", 2007));
        assertEquals(4, gestoreProdotti.verificaCampi(new String[]{}, "titolo", "editore", 2007));

        // tutti i campi errati
        assertEquals(1, gestoreProdotti.verificaCampi(new String[]{""}, "", "", 100));
    }

    @Test
    public void testControlloUnicita() throws RemoteException {
        //OK
        assertTrue(gestoreProdotti.controlloUnicita(new String[]{"autore"}, "titolo", "editore", 2000, "CD"));
        assertTrue(gestoreProdotti.controlloUnicita(new String[]{"autore1", "autore2"}, "titolo", "editore", 2000, "CD"));

        //non unico
        assertFalse(gestoreProdotti.controlloUnicita(new String[]{"Pink Floyd"}, "The Dark Side Of The Moon", "EMI", 1973, "CD"));
        assertFalse(gestoreProdotti.controlloUnicita(new String[]{"Paul Deitel", "Harvey Deitel"}, "Il linguaggio C. Fondamenti e tecniche di programmazione", "Pearson", 2022, "Libro"));
    }


    @Test
    public void testAggiungiProdotto(){

    }

}
