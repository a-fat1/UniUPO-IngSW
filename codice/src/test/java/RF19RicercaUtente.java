import static org.junit.Assert.*;
import org.junit.Test;
import java.rmi.RemoteException;

import DataBase.DbUtenti;
import DataBase.DbProdotti;
import Elaborazione.GestoreRicerche;
public class RF19RicercaUtente {

    DbUtenti dbutenti = new DbUtenti();
    DbProdotti dbprodotti = new DbProdotti();
    GestoreRicerche gestorericerche = new GestoreRicerche(dbutenti, dbprodotti);

    // -------------------------------------------------------------

    @Test
    public void testControlloParametriUsername0(){
        assertEquals(1, gestorericerche.controlloParametri(""));
    }

    @Test
    public void testControlloParametriUsername1(){
        assertEquals(1, gestorericerche.controlloParametri("a"));
    }

    @Test
    public void testControlloParametriUsername2(){
        assertEquals(1, gestorericerche.controlloParametri("ab"));
    }

    @Test
    public void testControlloParametriUsername3(){
        assertEquals(4, gestorericerche.controlloParametri("abc"));
    }

    @Test
    public void testControlloParametriNomeCognome0(){
        assertEquals(2, gestorericerche.controlloParametri("", "abc"));
    }
    @Test
    public void testControlloParametriNomeCognome1(){
        assertEquals(2, gestorericerche.controlloParametri("a", "abc"));
    }
    @Test
    public void testControlloParametriNomeCognome2(){
        assertEquals(2, gestorericerche.controlloParametri("ab", "abc"));
    }
    @Test
    public void testControlloParametriNomeCognome3(){
        assertEquals(4, gestorericerche.controlloParametri("abc", "abc"));
    }

    @Test
    public void testControlloParametriNomeCognome4(){
        assertEquals(3, gestorericerche.controlloParametri("abc", ""));
    }
    @Test
    public void testControlloParametriNomeCognome5(){
        assertEquals(3, gestorericerche.controlloParametri("abc", "a"));
    }
    @Test
    public void testControlloParametriNomeCognome6(){
        assertEquals(3, gestorericerche.controlloParametri("abc", "ab"));
    }
    @Test
    public void testControlloParametriNomeCognome7(){
        assertEquals(4, gestorericerche.controlloParametri("abc", "abc"));
    }

    @Test
    public void testControlloParametriNomeCognome8(){
        assertEquals(2, gestorericerche.controlloParametri("abc123456789!?=)(", "abc"));
    }

    @Test
    public void testControlloParametriNomeCognome9(){
        assertEquals(3, gestorericerche.controlloParametri("abc", "abc123456789!?=)("));
    }

    /*
    @Test
    public void testDatabase0() throws RemoteException {
        System.out.println(gestorericerche.cercaUtenteBloccatoNonBloccato("ald"));
    }

    @Test
    public void testDatabase1() throws RemoteException {
        System.out.println(gestorericerche.cercaUtenteBloccatoNonBloccato("Ald", "Bia"));
    }

    @Test
    public void testDatabase2() throws RemoteException {
        System.out.println(gestorericerche.cercaUtenteNonBloccato("ald"));
    }

    @Test
    public void testDatabase3() throws RemoteException {
        System.out.println(gestorericerche.cercaUtenteNonBloccato("Ald", "Bia"));
    }
     */
}
