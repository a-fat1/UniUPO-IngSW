import static org.junit.Assert.*;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.*;

import DataBase.DbProdotti;
import DataBase.DbUtenti;
import Elaborazione.GestoreRicerche;

public class RF22Classifica {
    DbProdotti dbProdotto = new DbProdotti();
    DbUtenti dbUtenti = new DbUtenti();
    GestoreRicerche gestoreRicerche = new GestoreRicerche(dbUtenti,dbProdotto);


    /*Test Sezione */
    //Titolo
    @Test
    public void TestricercaProdottinoTitolo() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("Mare", "", "", "", "CD");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }

    @Test
    public void TestricercaProdottiTitolo () throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("The Dark Side Of The Moon", "", "", "", "CD");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'CD' AND titolo LIKE 'The Dark Side Of The Moon' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
    // Autore
    @Test
    public void TestricercaProdottinoAutore() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "Luca", "", "", "CD");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }

    @Test
    public void TestricercaProdottiAutore () throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "R.E.M", "", "", "CD");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'CD' AND autore like 'R.E.M' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
    //Editore
    @Test
    public void TestricercaProdottinoEditore() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "ore", "", "CD");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }
     
    @Test
    public void TestricercaProdottiEditore () throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "EMI", "", "CD");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'CD' AND editore like 'EMI' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
    // Test Anno
    @Test
    public void TestricercaProdottinoAnno() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "", "2023", "CD");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }
    @Test
    public void TestricercaProdottiAnno() throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "", "1992", "CD");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'CD' AND anno like '1992' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
  //Test Libri
  @Test
    public void TestricercaProdottinoTitoloL() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("Mare", "", "", "", "Libro");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }

    @Test
    public void TestricercaProdottiTitoloL () throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("UML Distilled", "", "", "", "Libro");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'Libro' AND titolo LIKE 'UML Distilled' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
    // Autore
    @Test
    public void TestricercaProdottinoAutoreL() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "Luca", "", "", "Libro");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }

    @Test
    public void TestricercaProdottiAutoreL() throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "Martin Folwer", "", "", "Libro");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'Libro' AND autore like 'Pearson' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
    //Editore
    @Test
    public void TestricercaProdottinoEditoreL() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "ore", "", "Libro");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }
     
    @Test
    public void TestricercaProdottiEditoreL() throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "Pearson", "", "Libro");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'libro' AND editore LIKE 'Pearson' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
    // Test Anno
    @Test
    public void TestricercaProdottinoAnnoL() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "", "2023", "Libro");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }
    @Test
    public void TestricercaProdottiAnnoL() throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "", "2022", "Libro");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'libro' AND anno like '2022' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }

//Test DVD
    @Test
    public void TestricercaProdottinoTitoloC() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("Mare", "", "", "", "DVD");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }

    @Test
    public void TestricercaProdottiTitoloC() throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("Legend", "", "", "", "DVD");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'DVD' AND titolo LIKE 'Legend' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
// Autore
    @Test
    public void TestricercaProdottinoAutoreD() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "Luca", "", "", "DVD");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }

    @Test
    public void TestricercaProdottiAutoreD() throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "Tim Burton", "", "", "DVD");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'DVD' AND autore like 'Tim Burton' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
//Editore
    @Test
    public void TestricercaProdottinoEditoreD() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "ore", "", "DVD");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }
     
    @Test
    public void TestricercaProdottiEditoreD() throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "Warner Bros", "", "DVD");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'DVD' AND autore like 'Warner Bros' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }
// Test Anno
    @Test
    public void TestricercaProdottinoAnnoD() throws RemoteException{
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "", "2023", "DVD");
        assertNotNull(risultato);
        assertTrue(risultato.isEmpty()); // Deve restituire un array vuoto
    }
    @Test
    public void TestricercaProdottiAnnoD() throws RemoteException {
        ArrayList<HashMap<String, Object>> risultato = gestoreRicerche.ricercaProdotti("", "", "", "1989", "DVD");
        ArrayList<HashMap<String, Object>> lista = dbProdotto.query("SELECT Prodotto.codice, Prodotto.autore, Prodotto.titolo, Prodotto.editore, Prodotto.anno, SUM(Ordine.quantitaProdotto) AS quantita FROM Prodotto JOIN Ordine ON Prodotto.codice = Ordine.codiceProdotto WHERE tipo LIKE 'DVD' AND anno like '1989' GROUP BY Prodotto.codice, quantita, Prodotto.titolo ORDER BY quantita DESC");
        assertEquals(risultato, lista);
    }


// test ControllaValore
    @Test
    public void TestcontrollaValorenotiponull() {
        assertFalse(gestoreRicerche.controllaValore(null));
    }
    @Test
    public void TestcontrollaValorenotipoVuoto() {
        assertFalse(gestoreRicerche.controllaValore(""));
    }
    @Test
    public void TestcontrollaValorenotipoTrue() {
        assertTrue(gestoreRicerche.controllaValore("stringa"));
    }
        
    
}