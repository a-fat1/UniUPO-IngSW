import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Test;

import DataBase.DbProdotti;

import Elaborazione.GestoreProdotti;

public class RF13ListaForniture {

    DbProdotti dbProdotti = new DbProdotti();
    GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);

    @Test
    public void testRicercaListaForniturePerItem() throws RemoteException {
        assertNotNull(gestoreProdotti.ricercaListaForniture(100006));
        assertEquals(0, gestoreProdotti.ricercaListaForniture(2).size());
        assertEquals(1, gestoreProdotti.ricercaListaForniture(100006).size());
    }

    @Test
    public void testRicercaListaForniturePerData() throws RemoteException {
        assertNotNull(gestoreProdotti.ricercaListaForniture("2023-01-12", "2023-04-12"));
        assertEquals(12,gestoreProdotti.ricercaListaForniture("2023-12-01", "2023-12-04").size());
        assertEquals(0,gestoreProdotti.ricercaListaForniture("2023-12-07", "2023-12-01").size());
    }

    @Test
    public void testControlloParametri() throws RemoteException {
        assertEquals(0, gestoreProdotti.controlloParametri("2023-12-01", "2023-12-04"));
        assertEquals(2, gestoreProdotti.controlloParametri("2023-12-04", "2023-12-01"));
        assertEquals(1, gestoreProdotti.controlloParametri(null, "2023-04-12"));
        assertEquals(1, gestoreProdotti.controlloParametri("2023-04-12", null));
    }

    }

}