import static org.junit.Assert.*;
import org.junit.Test;

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

    // ------------------------------------------------------------------------------------ //

    @Test
    public void testControlloParametriUsername() throws RemoteException {
        // controllo di username con lunghezza inferiore a 3
        assertEquals(1, gestorericerche.controlloParametri(""));
        assertEquals(1, gestorericerche.controlloParametri("a"));
        assertEquals(1, gestorericerche.controlloParametri("ab"));
    }

    @Test
    public void testControlloParametriUsername1() throws RemoteException {
        // controllo di username con lunghezza corretta
        assertEquals(4, gestorericerche.controlloParametri("abc"));
        assertEquals(4, gestorericerche.controlloParametri("orchidea1"));
    }

    @Test
    public void testControlloParametriNomeCognome0() throws RemoteException {
        // controllo di nome con una lunghezza inferiore a 3
        assertEquals(2, gestorericerche.controlloParametri("", "abc"));
        assertEquals(2, gestorericerche.controlloParametri("a", "abc"));
        assertEquals(2, gestorericerche.controlloParametri("ab", "abc"));
    }

    @Test
    public void testControlloParametriNomeCognome1() throws RemoteException {
        // controllo di nome e cognome con lunghezza corretta
        assertEquals(4, gestorericerche.controlloParametri("abc", "abc"));
        assertEquals(4, gestorericerche.controlloParametri("Mario", "Rossi"));
    }

    @Test
    public void testControlloParametriNomeCognome2() throws RemoteException {
        // controllo di cognome con una lunghezza inferiore a 3
        assertEquals(3, gestorericerche.controlloParametri("abc", ""));
        assertEquals(3, gestorericerche.controlloParametri("abc", "a"));
        assertEquals(3, gestorericerche.controlloParametri("abc", "ab"));
    }

    @Test
    public void testControlloParametriNomeCognome8() throws RemoteException {
        // controllo di nome con caratteri non consentiti
        assertEquals(2, gestorericerche.controlloParametri("abc123456789!?=)(", "abc"));
    }

    @Test
    public void testControlloParametriNomeCognome9() throws RemoteException {
        // controllo di cognome con caratteri non consentiti
        assertEquals(3, gestorericerche.controlloParametri("abc", "abc123456789!?=)("));
    }


    @Test
    public void testDatabase0() throws RemoteException {
        // ricerca di un utente non bloccato tramite username nel database
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteBloccatoNonBloccato("aldo");
        assertEquals("Aldo", utenti.get(0).get("nome"));
        assertEquals("Bianchi", utenti.get(0).get("cognome"));
        assertEquals("aldo", utenti.get(0).get("username"));
    }

    @Test
    public void testDatabase1() throws RemoteException {
        // ricerca di un utente non bloccato tramite nome e cognome nel database
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteBloccatoNonBloccato("Aldo", "Bianchi");
        assertEquals("Aldo", utenti.get(0).get("nome"));
        assertEquals("Bianchi", utenti.get(0).get("cognome"));
        assertEquals("aldo", utenti.get(0).get("username"));
    }

    @Test
    public void testDatabase2() throws RemoteException {
        // ricerca di un utente tramite nome e cognome nel database
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteBloccatoNonBloccato("Aldo", "Bianchi");
        assertEquals("Aldo", utenti.get(0).get("nome"));
        assertEquals("Bianchi", utenti.get(0).get("cognome"));
        assertEquals("aldo", utenti.get(0).get("username"));
    }

    @Test
    public void testDatabase3() throws RemoteException {
        // ricerca di un utente bloccato tramite nome e cognome nel database
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteBloccatoNonBloccato("Alfio", "Verdi");
        assertEquals("Alfio", utenti.get(0).get("nome"));
        assertEquals("Verdi", utenti.get(0).get("cognome"));
        assertEquals("alfio", utenti.get(0).get("username"));
        assertEquals(0, utenti.get(0).get("attivo"));
    }

    @Test
    public void testDatabase4() throws RemoteException {
        // ricerca di un utente bloccato tramite username nel database
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteBloccatoNonBloccato("alfio");
        assertEquals("Alfio", utenti.get(0).get("nome"));
        assertEquals("Verdi", utenti.get(0).get("cognome"));
        assertEquals("alfio", utenti.get(0).get("username"));
        assertEquals(0, utenti.get(0).get("attivo"));
    }

    @Test
    public void testDatabase5() throws RemoteException {
        // ricerca di un utente tramite nome e cognome nel database
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteNonBloccato("Aldo", "Bianchi");
        assertEquals("Aldo", utenti.get(0).get("nome"));
        assertEquals("Bianchi", utenti.get(0).get("cognome"));
        assertEquals("aldo", utenti.get(0).get("username"));
        assertEquals(1, utenti.get(0).get("attivo"));
    }

    @Test
    public void testDatabase6() throws RemoteException {
        // ricerca di un utente bloccato tramite username nel database
        // controllo che non sia presente nulla (gli utenti bloccati vengono scartati dalla funzione)
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteNonBloccato("alfio");
        assertEquals(0, utenti.size());

    }


    @Test
    public void testDatabase7() throws RemoteException {
        // ricerca di un utente bloccato tramite nome e cognome nel database
        // controllo che non sia presente nulla (gli utenti bloccati vengono scartati dalla funzione)
        ArrayList<HashMap<String, Object>> utenti = gestorericerche.cercaUtenteNonBloccato("alfio");
        assertEquals(0, utenti.size());
    }

}
