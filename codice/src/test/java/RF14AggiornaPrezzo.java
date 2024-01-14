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
    public void TestControlloFormatoModificaPrezzo1()
    {
        //errore negativo
         assertEquals(1, gestoreProdotti.controlloFormatoModificaPrezzo(-123, 69));
    }

    @Test
    public void TestControlloFormatoModificaPrezzo2()
    {
        //errore non modificato
        assertEquals(2, gestoreProdotti.controlloFormatoModificaPrezzo(2, 2));
    }

    @Test
    public void TestControlloFormatoModificaPrezzo3()
    {
        //corretto
        assertEquals(0, gestoreProdotti.controlloFormatoModificaPrezzo(5, 2));
    }
}