import DataBase.DbUtenti;
import Elaborazione.GestoreAccessi;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RF03AggiornaPassword {

    DbUtenti dbUtenti = new DbUtenti();
    GestoreAccessi gestoreAccessi = new GestoreAccessi(dbUtenti);

    @Test
    public void testVerificaPassword(){
        //Test con password uguali = return expected 0
        assertEquals(0, gestoreAccessi.verificaCredenziali("abc123", "abc123"));

        //Test con password diverse = return expected 4
        assertEquals(4, gestoreAccessi.verificaCredenziali("abd124", "abc123"));
    }

    @Test
    public void testNuovaPassword(){
         //Test lunghezza password <6 = return expected 1
        assertEquals(1, gestoreAccessi.controlloNuovaPassword("abc12"));

        //Test password senza lettera = return expected 2
        assertEquals(2, gestoreAccessi.controlloNuovaPassword("123456"));

        //Test password senza numeri = return expected 3
        assertEquals(3, gestoreAccessi.controlloNuovaPassword("abcdef"));

        //Test password corretta = return expected 0
        assertEquals(0, gestoreAccessi.controlloNuovaPassword("abc123"));
    }
}