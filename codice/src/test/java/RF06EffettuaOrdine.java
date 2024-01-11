import static org.junit.Assert.*; 
import org.junit.Test;

import DataBase.DbProdotti;
import Elaborazione.GestoreCarrelli;

public class RF06EffettuaOrdine
{
    DbProdotti dbProdotti = new DbProdotti();
    GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(dbProdotti);


    /*
    @Test
    public void RF06_ControllaNumeroCarta1()
    {
        //errore troppi numeri
        assertEquals("false", gestoreCarrelli.Rf06_ControllaNumeroCarta1(672813924677863895));
    }

    @Test
    public void RF06_ControllaNumeroCarta2()
    {
        //errore pochi numeri
        assertEquals("false", gestoreCarrelli.Rf06_ControllaNumeroCarta2(67281392467795));
    }

    @Test
    public void RF06_ControllaNumeroCarta3()
    {
        //ok
        assertEquals("ok", gestoreCarrelli.Rf06_ControllaNumeroCarta3(6728139246779538));
    }
    */

    /*
    @Test
    public void RF06_CalcolaPrezzoTotale1()
    {
        //prezzo errato
        assertEquals("prezzoErrato", gestoreCarrelli.Rf06_CalcolaPrezzoTotale1(-12,00));
    }

    @Test
    public void RF06_CalcolaPrezzoTotale2()
    {
        //prezzo corretto
        assertEquals("prezzoCorretto", gestoreCarrelli.Rf06_CalcolaPrezzoTotale2(25,50));
    }
    */
}

