import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import DataBase.DbUtenti;
import DataBase.DbProdotti;
import Elaborazione.GestoreRicerche;
public class RF19RicercaUtente {

    DbUtenti dbutenti = new DbUtenti();
    DbProdotti dbprodotti = new DbProdotti();
    GestoreRicerche gestorericerche = new GestoreRicerche(dbutenti, dbprodotti);

    // -------------------------------------------------------------

    @Test
    public void testControlloParametriUsername(){
        // controllo username lunghezza inferiore a 3
        assertEquals(1, gestorericerche.controlloParametri(""));
        assertEquals(1, gestorericerche.controlloParametri("a"));
        assertEquals(1, gestorericerche.controlloParametri("ab"));
    }

    @Test
    public void testControlloParametriUsername1(){
        // controllo username lunghezza corretta
        assertEquals(4, gestorericerche.controlloParametri("abc"));
        assertEquals(4, gestorericerche.controlloParametri("orchidea1"));
    }

    @Test
    public void testControlloParametriNomeCognome0(){
        // controllo nome lunghezza inferiore a 3
        assertEquals(2, gestorericerche.controlloParametri("", "abc"));
        assertEquals(2, gestorericerche.controlloParametri("a", "abc"));
        assertEquals(2, gestorericerche.controlloParametri("ab", "abc"));
    }

    @Test
    public void testControlloParametriNomeCognome1(){
        // controllo nome e cognome con lunghezza corretta
        assertEquals(4, gestorericerche.controlloParametri("abc", "abc"));
        assertEquals(4, gestorericerche.controlloParametri("Mario", "Rossi"));
    }

    @Test
    public void testControlloParametriNomeCognome2() {
        // controllo cognome lunghezza inferiore a 3
        assertEquals(3, gestorericerche.controlloParametri("abc", ""));
        assertEquals(3, gestorericerche.controlloParametri("abc", "a"));
        assertEquals(3, gestorericerche.controlloParametri("abc", "ab"));
    }

    @Test
    public void testControlloParametriNomeCognome8(){
        // controllo nome caratteri non consentiti
        assertEquals(2, gestorericerche.controlloParametri("abc123456789!?=)(", "abc"));
    }

    @Test
    public void testControlloParametriNomeCognome9(){
        // controllo cognome caratteri non consentiti
        assertEquals(3, gestorericerche.controlloParametri("abc", "abc123456789!?=)("));
    }


    @Test
    public void testDatabase0() throws RemoteException {
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteBloccatoNonBloccato("ald");
        assertEquals("Aldo", utenti.get(0).get("nome"));
        assertEquals("Bianchi", utenti.get(0).get("cognome"));
        assertEquals("aldo", utenti.get(0).get("username"));
    }

    @Test
    public void testDatabase1() throws RemoteException {
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteBloccatoNonBloccato("Ald", "Bia");
        assertEquals("Aldo", utenti.get(0).get("nome"));
        assertEquals("Bianchi", utenti.get(0).get("cognome"));
        assertEquals("aldo", utenti.get(0).get("username"));
    }

    @Test
    public void testDatabase2() throws RemoteException {
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteNonBloccato("ald");
        assertEquals("Aldo", utenti.get(0).get("nome"));
        assertEquals("Bianchi", utenti.get(0).get("cognome"));
        assertEquals("aldo", utenti.get(0).get("username"));
    }

    @Test
    public void testDatabase3() throws RemoteException {
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteNonBloccato("Ald", "Bia");
        assertEquals("Aldo", utenti.get(0).get("nome"));
        assertEquals("Bianchi", utenti.get(0).get("cognome"));
        assertEquals("aldo", utenti.get(0).get("username"));
    }

    @Test
    public void testDatabase4() throws RemoteException {
        // controllo nessun elemento nell'arrayList
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteNonBloccato("alfio");
        assertEquals(0, utenti.size());
    }

    /*

    @Test
    public void testDatabase5() throws RemoteException {
        // controllo numero corretto di elementi da username vuoto per utenti bloccati e non bloccati
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteBloccatoNonBloccato("");
        assertEquals(9, utenti.size());
    }

     */

    @Test
    public void testDatabase5() throws RemoteException {
        // controllo un solo elemento nell'arrayList
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteBloccatoNonBloccato("alfio");
        assertEquals(1, utenti.size());
        assertEquals(0, utenti.get(0).get("attivo"));
    }

    @Test
    public void testDatabase6() throws RemoteException {
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteNonBloccato("alfio");
        assertEquals(utenti.size(), 0);
    }

    /*
    @Test
    public void testDatabase4() throws RemoteException {
        // controllo numero corretto di elementi per utenti non bloccati (da username vuoto)
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteNonBloccato("");
        assertEquals(8, utenti.size());
    }

     */

    /*

    @Test
    public void testDatabase() throws RemoteException {
        // Ricerca con utenti multipli
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteNonBloccato("s");
        assertEquals(3, utenti.size());
        assertEquals("Sirio", utenti.get(0).get("nome"));
        assertEquals("Smith", utenti.get(0).get("cognome"));
        assertEquals("sirio", utenti.get(0).get("username"));
        assertEquals("Silvio", utenti.get(1).get("nome"));
        assertEquals("Taylor", utenti.get(1).get("cognome"));
        assertEquals("sisto", utenti.get(1).get("username"));
        assertEquals("Sara", utenti.get(2).get("nome"));
        assertEquals("Keynes", utenti.get(2).get("cognome"));
        assertEquals("soave", utenti.get(2).get("username"));
    }

     */
}
