/*import static org.junit.Assert.*;
import org.junit.Test;
import java.rmi.RemoteException;

import DataBase.DbUtenti;
import Elaborazione.GestoreAccessi;

public class RF00Login 
{
    DbUtenti dbUtenti = new DbUtenti();
    GestoreAccessi gestoreAccessi = new GestoreAccessi(dbUtenti);

    // ===========================================================

    @Test
    public void testControlloFormatoCredenziali1() 
    {
        // password vuota
        assertEquals("errorePassword", gestoreAccessi.controllaFormatoCredenziali("dcr", ""));
    }

    @Test
    public void testControlloFormatoCredenziali2() 
    {
        // username vuota
        assertEquals("erroreUsername", gestoreAccessi.controllaFormatoCredenziali("", "123"));
    }

    @Test
    public void testControlloFormatoCredenziali3() 
    {
        // credenziali vuote
         assertEquals("erroreCredenziali", gestoreAccessi.controllaFormatoCredenziali("", ""));
    }
    
    @Test
    public void testControlloFormatoCredenziali4() 
    {
        // credenziali non vuote
         assertEquals("ok", gestoreAccessi.controllaFormatoCredenziali("dcr", "123"));
    }
    
    // ===========================================================

    @Test
    public void testRicercaCredenziali1() throws RemoteException
    {
        // password errata
        assertEquals("erroreAssente", gestoreAccessi.ricercaCredenziali("aldo", "xyz"));
    }

    @Test
    public void testRicercaCredenziali2() throws RemoteException
    {
        // username errata
        assertEquals("erroreAssente", gestoreAccessi.ricercaCredenziali("dcr", "abc123"));
    }

    @Test
    public void testRicercaCredenziali3() throws RemoteException
    {
        // credenziali errate
        assertEquals("erroreAssente", gestoreAccessi.ricercaCredenziali("dcr", "xyz"));
    }

    @Test
    public void testRicercaCredenziali4() throws RemoteException
    {
        // account bloccato
        assertEquals("erroreDisattivato", gestoreAccessi.ricercaCredenziali("alfio", "abc123"));
    }

    @Test
    public void testRicercaCredenziali5() throws RemoteException
    {
        // account attivo
	assertEquals("attivo", gestoreAccessi.ricercaCredenziali("aldo", "abc123"));
    }

    // ===========================================================

    @Test
    public void testRicercaUtente1() throws RemoteException
    {
        // utente assente
        assertNull(gestoreAccessi.ricercaUtente("dcr"));
    }

    @Test
    public void testRicercaUtente2() throws RemoteException
    {
        // utente presente
        assertNotNull(gestoreAccessi.ricercaUtente("aldo"));
    }
}
*/