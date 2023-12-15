import static org.junit.Assert.*;

import DataBase.DbNotifiche;
import Elaborazione.GestoreNotifiche;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.HashMap;

import DataBase.DbUtenti;
import Elaborazione.GestoreAccessi;

public class RF04GeneraNotifica {

    DbNotifiche dbNotifiche = new DbNotifiche();
    GestoreNotifiche gestoreNotifiche = new GestoreNotifiche(dbNotifiche);

    /*
     * Test per verificare il corretto funzionamento del metodo "verificaCorrettezzaDati"
     */
    @Test
    public void testVerificaCorrettezzaDati1() throws RemoteException {
        // data in formato errato
        assertEquals("errore formato data", gestoreNotifiche.verificaCorrettezzaDati("12-12-12", "11:00:00", "Nuovo utente: Carla Rossi."));
        assertEquals("errore formato data", gestoreNotifiche.verificaCorrettezzaDati("2024-20-01", "1111:00", "Nuovo utente: Carla Rossi."));
        assertEquals("errore formato data", gestoreNotifiche.verificaCorrettezzaDati("2023-20-01", "11:00:00", ""));
        assertEquals("errore formato data", gestoreNotifiche.verificaCorrettezzaDati("", "11:00", ""));
    }

    @Test
    public void testVerificaCorrettezzaDati2() throws RemoteException {
        // ora in formato errato
        assertEquals("errore formato ora", gestoreNotifiche.verificaCorrettezzaDati("2024-12-12", "99:00", "Nuovo utente: Carla Rossi."));
        assertEquals("errore formato ora", gestoreNotifiche.verificaCorrettezzaDati("2024-12-12", "23:89", "Nuovo utente: Carla Rossi."));
        assertEquals("errore formato ora", gestoreNotifiche.verificaCorrettezzaDati("2024-12-12", "100:00", ""));
        assertEquals("errore formato ora", gestoreNotifiche.verificaCorrettezzaDati("2024-12-12", "", ""));
    }

    @Test
    public void testVerificaCorrettezzaDati3() throws RemoteException {
        // data non coerente
        assertEquals("errore data", gestoreNotifiche.verificaCorrettezzaDati("2020-11-12", "11:00", "Nuovo utente: Carla Rossi."));
        assertEquals("errore data", gestoreNotifiche.verificaCorrettezzaDati("2020-11-12", "11:00", ""));
    }

    @Test
    public void testVerificaCorrettezzaDati4() throws RemoteException {
        // testo notifica vuoto
        assertEquals("errore testo notifica", gestoreNotifiche.verificaCorrettezzaDati("2024-11-12", "11:00:00", ""));
    }

    @Test
    public void testVerificaCorrettezzaDati5() throws RemoteException {
        assertEquals("ok", gestoreNotifiche.verificaCorrettezzaDati("2024-11-12", "11:00:00", "Nuovo utente: Carla Rossi."));
    }

    /*
     * Test per verificare il corretto funzionamento dei metodi "generaTestoNotifica*"
     */
    @Test
    public void testGeneraTestoNotifica1() throws RemoteException {
        // genera testo prodotto
        HashMap<String, Object> prodotto = new HashMap<>();

        prodotto.put("tipo", "Libro");
        prodotto.put("autore", "Nikolaj S. Piskunov");
        prodotto.put("titolo", "Calcolo Integrale e Differenziale 2");

        assertEquals("Nuovo Libro: Nikolaj S. Piskunov, Calcolo Integrale e Differenziale 2.", gestoreNotifiche.generaTestoNotificaProdotto(prodotto));
    }

    @Test
    public void testGeneraTestoNotifica2() throws RemoteException {
        // genera testo ordine
        HashMap<String, Object> ordine = new HashMap<>();

        ordine.put("username", "clea99");

        assertEquals("Nuovo Ordine: effettuato da clea99.", gestoreNotifiche.generaTestoNotificaOrdine(ordine));
    }

    @Test
    public void testGeneraTestoNotifica3() throws RemoteException {
        // genera testo avviso
        assertEquals("Avviso: ", gestoreNotifiche.generaTestoNotificaAvviso());
    }

    @Test
    public void testGeneraTestoNotifica4() throws RemoteException {
        // genera testo utente
        HashMap<String, Object> utente = new HashMap<>();

        utente.put("nome", "Aldo");
        utente.put("cognome", "Bruni");

        assertEquals("Nuovo Cliente: Aldo Bruni.", gestoreNotifiche.generaTestoNotificaUtente(utente));
    }

    @Test
    public void testInserisci() throws RemoteException {
        HashMap<String, Object> dataScadenza = new HashMap<>();
        HashMap<String, Object> dataPubblicazione = new HashMap<>();

        dataScadenza.put("data", "2024-11-12");
        dataPubblicazione.put("data", "2024-12-12");
        dataScadenza.put("ora", "11:00");
        dataPubblicazione.put("ora", "11:00");

        gestoreNotifiche.inserimentoNotifica(dataPubblicazione, dataScadenza, "Avviso: test", "staff");
    }

}