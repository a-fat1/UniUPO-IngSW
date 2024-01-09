import static org.junit.Assert.*;

import Elaborazione.GestoreAccessi;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import DataBase.DbUtenti;
import DataBase.DbProdotti;
import Elaborazione.GestoreProdotti;

public class RF17IncrementaDecrementa {
    final private DbProdotti dbProdotti = new DbProdotti();
    final private GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);
    // ===========================================================

    @Test
    public void testControlloPercentualeCorretta(){
        //percentuale idonea
        assertEquals(true,gestoreProdotti.controlloPercentuale(100));
        assertEquals(true,gestoreProdotti.controlloPercentuale(50));

    }

    @Test
    public void testControlloPercentualeScorretta(){
        //percentuale non idonea
        assertEquals(false,gestoreProdotti.controlloPercentuale(110));
        assertEquals(false,gestoreProdotti.controlloPercentuale(-1));
    }
}
