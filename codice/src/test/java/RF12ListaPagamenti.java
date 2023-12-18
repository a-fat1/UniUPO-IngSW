import static org.junit.Assert.*;

import Elaborazione.GestoreAccessi;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import DataBase.DbUtenti;
import DataBase.DbProdotti;
import Elaborazione.GestoreRicerche;

public class RF12ListaPagamenti
{
    DbUtenti dbUtenti = new DbUtenti();
    DbProdotti dbProdotti = new DbProdotti();
    GestoreRicerche gestoreRicerche = new GestoreRicerche(dbUtenti, dbProdotti);

    @Test
    public void testControlloParametri1() throws RemoteException
    {
        // formato dataInizio non corretto
        assertEquals(1, gestoreRicerche.controlloParametri("12/14/2022", "01/12/2023"));
    }

    @Test
    public void testControlloParametri2() throws RemoteException
    {
        // formato dataFine non corretto
        assertEquals(1, gestoreRicerche.controlloParametri("12/12/2023", "01/13/2023"));
    }

    @Test
    public void testControlloParametri3() throws RemoteException
    {
        // dataInizio > dataFine
        assertEquals(2, gestoreRicerche.controlloParametri("12/02/2023", "01/01/2023"));
    }

    @Test
    public void testControlloParametri4() throws RemoteException
    {
        // formato dataInizio non corretto
        assertEquals(0, gestoreRicerche.controlloParametri("12/01/2023", "20/01/2023"));
    }

    @Test
    public void testricercaListaPagamentiData1() throws RemoteException
    {
        assertFalse(gestoreRicerche.ricercaListaPagamentiData("01/12/2023","31/12/2023").isEmpty());
        assertEquals(gestoreRicerche.ricercaListaPagamentiData("01/12/2023","31/12/2023").size(),2);
    }

    @Test
    public void testricercaListaPagamentiData2() throws RemoteException
    {
        assertFalse(gestoreRicerche.ricercaListaPagamentiData("01/12/2023","05/12/2023").isEmpty());
        assertEquals(gestoreRicerche.ricercaListaPagamentiData("01/12/2023","05/12/2023").size(),1);
    }

    @Test
    public void testricercaListaPagamentiData3() throws RemoteException
    {
        assertTrue(gestoreRicerche.ricercaListaPagamentiData("01/12/2023","04/12/2023").isEmpty());
        assertEquals(gestoreRicerche.ricercaListaPagamentiData("01/12/2023","04/12/2023").size(),0);
    }
}
