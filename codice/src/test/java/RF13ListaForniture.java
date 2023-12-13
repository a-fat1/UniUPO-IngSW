import static org.junit.Assert.*;
import org.junit.Test;

import DataBase.DbProdotti;

import Elaborazione.GestoreProdotti;

public class RF13ListaForniture {

    DbProdotti dbProdotti = new DbProdotti();
    GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);

    @Test
    public void testRicercaListaForniturePerItem() {
        assertNotNull(gestoreProdotti.ricercaListaForniture(100006));
        assertEquals(0, gestoreProdotti.ricercaListaForniture(2).size());
        assertEquals(1, gestoreProdotti.ricercaListaForniture(100006).size());
    }

    @Test
    public void testRicercaListaForniturePerData() {
        assertNotNull(gestoreProdotti.ricercaListaForniture("01/12/2023", "04/12/2023"));
        assertEquals(12,gestoreProdotti.ricercaListaForniture("01/12/2023", "04/12/2023").size());
        assertEquals(0,gestoreProdotti.ricercaListaForniture("07/12/2023", "01/12/2023").size());
    }

    @Test
    public void testControlloParametri() {
        assertEquals(0, gestoreProdotti.controlloParametri("01/12/2023", "04/12/2023"));
        assertEquals(1, gestoreProdotti.controlloParametri("04/12/2023", "01/12/2023"));
        assertThrows(NullPointerException.class, () -> gestoreProdotti.controlloParametri(null, "04/12/2023"));
        assertThrows(NullPointerException.class, () -> gestoreProdotti.controlloParametri("04/12/2023", null));
    }

}