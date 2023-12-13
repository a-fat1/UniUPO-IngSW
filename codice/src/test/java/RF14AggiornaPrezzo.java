import static org.junit.Assert.*; 
import org.junit.Test;

import DataBase.DbProdotti;
import Elaborazione.GestoreProdotti;

public class RF14AggiornaPrezzo
{
    DbProdotti dbProdotti = new DbProdotti();
    GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);

    //========================================================================
    
    @Test
    public void TestRF14_ControlloFormatoModificaPrezzo1()
    {
        //errore negativo
         assertEquals("erroreNegativo", gestoreProdotti.Rf14_ControlloFormatoModificaPrezzo(-123, 69));
    }

    @Test
    public void TestRF14_ControlloFormatoModificaPrezzo2()
    {
        //errore non modificato
        assertEquals("erroreNonModificato", gestoreProdotti.Rf14_ControlloFormatoModificaPrezzo(2, 2));
    }

    @Test
    public void TestRF14_ControlloFormatoModificaPrezzo3()
    {
        //corretto
        assertEquals("OK", gestoreProdotti.Rf14_ControlloFormatoModificaPrezzo(5, 2));
    }
}