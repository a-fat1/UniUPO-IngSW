import static org.junit.Assert.*;

import DataBase.DbProdotti;
import DataBase.DbUtenti;
import Elaborazione.GestoreRicerche;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;


public class RF22ClassificaProdttii {
    DbProdotti dbprodotti = new DbProdotti();
    GestoreRicerche gestoreprodotti = new GestoreRicerche(dbProdotti);


    public void CreazioneLista() throws RemoteException {
        Arraylist<string> classifica = new Arralist<String>();
        classifica.add("Pupazzo di neve");
        classifica.add("Mondo di odio");
        classifica.add("nero inchiostro");
        classifica.add("guida sull'informatica");
    }


    @Test
    public void testClassificavuota() throws RemoteException {
        //ricerca ordini dato codiceProdotto senza risultato
        ArrayList<HashMap<String,Object>> classifica = GestoreRicerche.controlloProdotti(ClassificaProdotti);
        assertNotNull(classifica);
        assertEquals(classifica.size(),0);
    }

    @Test
    public void testcontrolloclassifica() throws RemoteException {
        ArrayList<HashMap<String, Object>> classifica = GestoreRicerche.controlloProdotti(ClassificaProdotti);
        assertNotNull(classifica);
        assertEquals(classifica.size(), 1);
    }
}

