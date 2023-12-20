import static org.junit.Assert.*;

import org.junit.Test;

import DataBase.DbUtenti;
import DataBase.DbProdotti;
import Elaborazione.GestoreProdotti;

public class RF17IncrementaDecrementaPrezzi {
    DbUtenti dbUtenti=new DbUtenti();
    DbProdotti dbProdotti= new DbProdotti();
    GestoreProdotti gestoreProdotti=new GestoreProdotti(dbProdotti);
    // ===========================================================

    @Test
    public void testControlloParametri1(){
        //percentuale idonea
        assertEquals(true,gestoreProdotti.controllaParametri(50));

    }

    @Test
    public void testControlloParametri2(){
        //percentuale non idonea
        assertEquals(false,gestoreProdotti.controllaParametri(110));
    }
}