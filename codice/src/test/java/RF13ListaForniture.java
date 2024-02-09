import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Test;

import DataBase.DbProdotti;
import DataBase.DbUtenti;

import Elaborazione.GestoreRicerche;

public class RF13ListaForniture {

    DbUtenti dbUtenti = new DbUtenti();
    DbProdotti dbProdotti = new DbProdotti();
    GestoreRicerche gestoreRicerche = new GestoreRicerche(dbUtenti, dbProdotti);

    @Test
    public void testRicercaListaForniturePerItem() throws RemoteException {
        assertNotNull(gestoreRicerche.ricercaListaForniture(100006));
        assertEquals(0, gestoreRicerche.ricercaListaForniture(2).size());
        assertEquals(1, gestoreRicerche.ricercaListaForniture(100006).size());
    }

    @Test
    public void testRicercaListaForniturePerData() throws RemoteException {
        assertNotNull(gestoreRicerche.ricercaListaForniture("2023-01-12", "2023-04-12"));
        assertEquals(12,gestoreRicerche.ricercaListaForniture("2023-12-01", "2023-12-04").size());
        assertEquals(0,gestoreRicerche.ricercaListaForniture("2023-12-07", "2023-12-01").size());
    }

    @Test
    public void testControlloParametri() throws RemoteException {
        assertEquals(0, gestoreRicerche.controlloParametri("2023-12-01", "2023-12-04"));
        assertEquals(2, gestoreRicerche.controlloParametri("2023-12-04", "2023-12-01"));
        assertEquals(1, gestoreRicerche.controlloParametri(null, "2023-04-12"));
        assertEquals(1, gestoreRicerche.controlloParametri("2023-04-12", null));
    }

    

}
