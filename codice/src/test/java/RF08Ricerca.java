import static org.junit.Assert.*; 
import org.junit.Test;
import java.rmi.RemoteException;

import DataBase.DbUtenti;
import Elaborazione.GestoreAccessi;




public class TestGestoreRicerche {
    private GestoreRicerche gestoreRicerche;

    @BeforeEach
    public void setUp() throws RemoteException, NotBoundException {
        // Inizializza il GestoreRicerche per i test
        gestoreRicerche = new GestoreRicerche("localhost"); // Sostituisci con l'indirizzo corretto
    }

    @Test
    public void testControllaParametri() {
        // Testa il caso in cui tutti i parametri sono vuoti
        assertFalse(gestoreRicerche.controllaParametri("", "", "", 0, ""));

        // Testa il caso in cui almeno un parametro è diverso da zero
        assertTrue(gestoreRicerche.controllaParametri("Titolo", "", "", 0, ""));

        // Aggiungi altri casi di test se necessario
    }

    @Test
    public void testCercaProdotto() {
        // Testa il caso in cui tutti i parametri sono vuoti
        ArrayList<String> risultatiFallimento = gestoreRicerche.cercaProdotto("", "", "", 0, "");
        assertTrue(risultatiFallimento.contains("Errore: Almeno uno dei parametri di ricerca deve essere diverso da zero."));

        // Testa il caso in cui almeno un parametro è diverso da zero
        ArrayList<String> risultati = gestoreRicerche.cercaProdotto("Titolo", "", "", 0, "");
        assertFalse(risultati.contains("Nessun risultato trovato per i parametri di ricerca specificati."));

        // Aggiungi altri casi di test se necessario
    }
}