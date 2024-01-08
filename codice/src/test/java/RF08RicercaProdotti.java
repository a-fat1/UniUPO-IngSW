import static org.junit.Assert.*;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import DataBase.DbProdotti;
import DataBase.DbUtenti;
import Elaborazione.GestoreRicerche;

public class RF08RicercaProdotti
{
    DbProdotti dbProdotti = new DbProdotti();
    DbUtenti dbUtenti = new DbUtenti();
    GestoreRicerche gestoreRicerche = new GestoreRicerche(dbUtenti, dbProdotti);


    @Test
    public void testControllaParametriFail() throws RemoteException 
    {
        // Test con tutti i parametri vuoti
        assertFalse(gestoreRicerche.controllaParametri("", "", "", "", null));
    }

    @Test
    public void testControllaParametriSuccess() throws RemoteException 
    {
        // Test con almeno un parametro non vuoto
        assertTrue(gestoreRicerche.controllaParametri("Titolo", "", "", "", null));
    }


    @Test
    public void testCercaProdottoByTitoloNonPresente() throws RemoteException {
        // Test con risultato vuoto per titolo "TitoloInesistente"
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto("TitoloInesistente", "", "", "", null, false);
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un arraylist vuoto
    }

    @Test
    public void testCercaProdottoByTitoloPresente() throws RemoteException
    {
        // Test con risultato non vuoto
    	String titolo = "Fireball";
    	
    	ArrayList<HashMap<String, Object>> prodottiList = dbProdotti.query("SELECT * FROM Prodotto WHERE titolo like '%" + titolo + "%'");
    	int size = prodottiList.size();
    	
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto(titolo, "", "", "", null, false);
        assertNotNull(risultato);
        assertFalse(risultato.isEmpty());
        assertEquals(size, risultato.size()); // Deve restituire 1 record per il titolo "Fireball"
    }

    @Test
    public void testCercaProdottiByEditore() throws RemoteException
    {
        // Test con risultato non vuoto per editore "Pearson"
    	String editore = "Pearson";
    	
    	ArrayList<HashMap<String, Object>> prodottiList = dbProdotti.query("SELECT * FROM Prodotto WHERE editore like '%" + editore + "%'");
    	int size = prodottiList.size();
    	
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto("", "", editore, "", null, false);
        assertNotNull(risultato);
        assertFalse(risultato.isEmpty());
        assertEquals(size, risultato.size()); // Deve restituire 3 record per autore "Pearson"
    }

    @Test
    public void testNoFilter() throws RemoteException 
    {
        ArrayList<HashMap<String, Object>> prodottiList = dbProdotti.query("SELECT * FROM Prodotto");
    	int size = prodottiList.size();
        
        // Test con tutti i parametri vuoti e senza effettuare controllo sui campi compilati
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto("", "", "", "", null, false);
        assertNotNull(risultato);
        assertFalse(risultato.isEmpty());
        assertEquals(size, risultato.size()); 
    }
     
    /**
     * verifico che per lo stesso autore venga visualizzato se disponibile ma non quantita
     * @throws RemoteException
     */
    @Test
    public void testDisponibileAndQuantitaCliente1() throws RemoteException
    {
        String autore = "Deep Purple";
        ArrayList<HashMap<String, Object>> prodottiList = dbProdotti.query("SELECT * FROM Prodotto WHERE autore = '"+autore+"' AND disponibile=1 AND quantita=0");
    	int size = prodottiList.size();
    	
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto("", autore, "", "", null, true);
        assertNotNull(risultato);
        assertFalse(risultato.isEmpty());
        assertEquals(size, risultato.size()); 
    }
    

    /**
     * verifico che il cliente non veda i risultati restituiti quando disponibile = 0 e quantita = 0 per stesso autore
     * @throws RemoteException
     */
    /* 
    @Test
    public void testDisponibileAndQuantitaCliente2() throws RemoteException
    {
        ArrayList<HashMap<String, Object>> prodottiList = dbProdotti.query("SELECT * FROM Prodotto WHERE autore='Martin Fowler' and disponibile=0 AND quantita=0;");
    	int size = prodottiList.size();
    	
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto("", "Martin Fowler", "", "", null, true);
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty());
        assertNotEquals(size, risultato.size()); 
    }
    */
       /**
     * verifico che per lo stesso autore venga visualizzato se non disponibile ma con quantita>0
     * @throws RemoteException
     */
    /* 
    @Test
    public void testNonDisponibileAndQuantitaCliente1() throws RemoteException
    {
        String autore = "Raffaele";
        ArrayList<HashMap<String, Object>> prodottiList = dbProdotti.query("SELECT * FROM Prodotto WHERE autore = '"+autore+"' AND disponibile=0 AND quantita>0");
    	int size = prodottiList.size();
    	
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto("", autore, "", "", null, true);
        assertNotNull(risultato);
        assertFalse(risultato.isEmpty());
        assertEquals(size, risultato.size()); 
    }
*/
    
}