import static org.junit.Assert.*;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import DataBase.DbProdotti;
import Elaborazione.GestoreRicerche;

public class RF08RicercaProdotti
{
    DbProdotti dbProdotti = new DbProdotti();
    GestoreRicerche gestoreRicerche = new GestoreRicerche(dbProdotti);

    // ===========================================================

    @Test
    public void testControllaParametriFail() throws RemoteException 
    {
        // Test con tutti i parametri vuoti
    	ArrayList<HashMap<String, Object>> risultato=gestoreRicerche.ricercaProdotto("", "", "", "", "");
        assertNull(risultato);
    }

    @Test
    public void testControllaParametriSuccess() throws RemoteException 
    {
        // Test con almeno un parametro non vuoto
        assertTrue(gestoreRicerche.controllaParametri("Titolo", "", "", "", ""));
    }

    // Aggiungi altri test a seconda dei casi che vuoi coprire

    // ===========================================================

    @Test
    public void testCercaProdottoByTitoloNonPresente() throws RemoteException {
        // Test con risultato vuoto per titolo "TitoloInesistente"
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto("TitoloInesistente", "", "", "", "");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un arraylist vuoto
    }

    @Test
    public void testCercaProdottoByTitoloPresente() throws RemoteException
    {
        // Test con risultato non vuoto
    	String titolo = "Fireball";
    	
    	ArrayList<HashMap<String, Object>> prodottiList =dbProdotti.query("SELECT * FROM Prodotto WHERE titolo like '%"+titolo+"%' ");
    	int size = prodottiList.size();
    	
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto(titolo, "", "", "", "");
        assertNotNull(risultato);
        assertEquals(size, risultato.size()); // Deve restituire 1 record per il titolo "Fireball"
    }

    @Test
    public void testCercaProdottiByAutore() throws RemoteException
    {
        // Test con risultato non vuoto per autore "Pearson"
    	String editore = "Pearson";
    	
    	ArrayList<HashMap<String, Object>> prodottiList =dbProdotti.query("SELECT * FROM Prodotto WHERE editore like '%"+editore+"%' ");
    	int size = prodottiList.size();
    	System.out.println("size = "+size);
    	
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto("", "",editore, "", "");
        assertNotNull(risultato);
        assertEquals(size, risultato.size()); // Deve restituire 3 record per autore "Pearson"
    }

    @Test //caso non previsto ma verifichiamo lo stesso 
    public void testCercaProdottoSkipControl() throws RemoteException
    {
        // Test senza parametri, dovrebbe tornare null
    	ArrayList<HashMap<String, Object>> prodottiList =dbProdotti.query("SELECT * FROM Prodotto");
    	int size = prodottiList.size();
    	System.out.println("size = "+size);
    	ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotto("", "", "", "", "");
        assertNull(risultato);
        
    }

    // Aggiungi altri test a seconda dei casi che vuoi coprire
}
