import static org.junit.Assert.*;
import org.junit.Test;
import java.rmi.RemoteException;
import Elaborazione.GestoreAccessi;
import org.junit.Before;
import DataBase.DbUtenti;
import java.util.ArrayList;
import java.util.HashMap;

public class RF24AggiornamentoDomicilio {
    private DbUtenti dbUtenti;
    private GestoreAccessi gestoreAccessi;

    @Before
    public void setUp() {
        dbUtenti = new DbUtenti();
        gestoreAccessi = new GestoreAccessi(dbUtenti);
    }

    @Test
    public void testFormatoCorretto() {
        int result = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "10", "12345", "Vercelli");
        assertEquals(0, result);

        int result1 = gestoreAccessi.controllaFormatoDomicilio("Via Roma", "42D", "12345", "Vercelli");
        assertEquals(0, result1);

        int result2 = gestoreAccessi.controllaFormatoDomicilio("Via Roma", "snc", "12345", "Vercelli");
        assertEquals(0, result2);

        int result3 = gestoreAccessi.controllaFormatoDomicilio("Via Pratico'", "42Bis", "12345", "Vercelli");
        assertEquals(0, result3);
    }

    @Test
    public void testFormatoViaErrato() {
        int result = gestoreAccessi.controllaFormatoDomicilio("", "42", "12345", "Vercelli");
        assertEquals(1, result);

        int result1 = gestoreAccessi.controllaFormatoDomicilio("Via 8", "42", "12345", "Vercelli");
        assertEquals(1, result1);

        int result2 = gestoreAccessi.controllaFormatoDomicilio("Via Roma@", "42", "12345", "Vercelli");
        assertEquals(1, result2);

        int result3 = gestoreAccessi.controllaFormatoDomicilio("Via Praticò", "42", "12345", "Vercelli");
        assertEquals(1, result3);
    }

    @Test
    public void testFormatoCapErrato() {
        int result = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "42", "ABCDE", "Vercelli");
        assertEquals(2, result);

        int result1 = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "42", "1234", "Vercelli");
        assertEquals(2, result1);

        int result2 = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "42", "123456", "Vercelli");
        assertEquals(2, result2);

        int result3 = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "42", "1234B", "Vercelli");
        assertEquals(2, result3);
    }

    @Test
    public void testFormatoLocalitaErrato() {
        int result = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "42", "12345", "");
        assertEquals(3, result);

        int result1 = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "42", "12345", "Vercelli1");
        assertEquals(3, result1);

        int result2 = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "42", "12345", "Verce@lli");
        assertEquals(3, result2);
    }

    @Test
    public void testFormatoNumeroErrato() {
        int result = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "0", "12345", "Vercelli");
        assertEquals(4, result);

        int result1 = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "sncc", "12345", "Vercelli");
        assertEquals(4, result1);

        int result2 = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "-12", "12345", "Vercelli");
        assertEquals(4, result2);

        int result3 = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "12B99", "12345", "Vercelli");
        assertEquals(4, result3);

        int result4 = gestoreAccessi.controllaFormatoDomicilio("Via Sant'Eusebio", "B12", "12345", "Vercelli");
        assertEquals(4, result4);
    }

    @Test
    public void testPiuCampiErrati() {
        int result = gestoreAccessi.controllaFormatoDomicilio("Via Roma", "1", "1234", "");
        assertEquals(5, result);

        int result1 = gestoreAccessi.controllaFormatoDomicilio("Via Roma", "1", "1234", "");
        assertEquals(5, result1);

        int result2 = gestoreAccessi.controllaFormatoDomicilio("Via Roma", "0", "1234", "");
        assertEquals(5, result2);

        int result3 = gestoreAccessi.controllaFormatoDomicilio("Via Roma 1", "1", "1234", "");
        assertEquals(5, result3);
    }

    @Test
    public void testUpdateExistingAddress() throws RemoteException {
        // Dati di esempio
        String username = "testUser";
        String via = "Via Nuova";
        String civico = "42";
        String cap = "00123";
        String localita = "Roma";

        // Inserisci un indirizzo di esempio nel database
        String insertQuery = "INSERT INTO Domicilio (Username, via, civico, cap, localita) VALUES ('" + username + "', 'Vecchia Via', 1, '00000', 'Vecchia Localita')";
        dbUtenti.update(insertQuery);

        // Verifica il numero di record prima dell'esecuzione del metodo
        String selectQueryBefore = "SELECT COUNT(*) FROM Domicilio WHERE Username = '" + username + "'";
        long countBefore = (long) dbUtenti.query(selectQueryBefore).get(0).get("COUNT(*)");

        // Esegui il metodo
        gestoreAccessi.promptSalvaDomicilio(username, via, civico, cap, localita);

        // Verifica il numero di record dopo l'esecuzione del metodo
        String selectQueryAfter = "SELECT COUNT(*) FROM Domicilio WHERE Username = '" + username + "'";
        long countAfter = (long) dbUtenti.query(selectQueryAfter).get(0).get("COUNT(*)");

        // Verifica che l'indirizzo sia stato aggiornato correttamente
        assertEquals(countBefore, countAfter); // Il numero di record deve rimanere lo stesso
        String selectQuery = "SELECT * FROM Domicilio WHERE Username = '" + username + "'";
        ArrayList<HashMap<String, Object>> result = dbUtenti.query(selectQuery);

        assertNotNull(result);
    }

    @Test
    public void testInsertNewAddress() throws RemoteException {
        // Dati di esempio
        String username = "testUser";
        String via = "Via Nuova";
        String civico = "42";
        String cap = "00123";
        String localita = "Roma";

        // Verifica il numero di record prima dell'esecuzione del metodo
        String selectQueryBefore = "SELECT COUNT(*) FROM Domicilio WHERE Username = '" + username + "'";
        long countBefore = (long) dbUtenti.query(selectQueryBefore).get(0).get("COUNT(*)");

        // Esegui il metodo
        gestoreAccessi.promptSalvaDomicilio(username, via, civico, cap, localita);

        // Verifica il numero di record dopo l'esecuzione del metodo
        String selectQueryAfter = "SELECT COUNT(*) FROM Domicilio WHERE Username = '" + username + "'";
        long countAfter = (long) dbUtenti.query(selectQueryAfter).get(0).get("COUNT(*)");

        // Verifica che l'indirizzo sia stato inserito correttamente
        assertEquals(countBefore + 1, countAfter); // Il numero di record deve aumentare di 1
        String selectQuery = "SELECT * FROM Domicilio WHERE Username = '" + username + "'";
        ArrayList<HashMap<String, Object>> result = dbUtenti.query(selectQuery);

        assertNotNull(result);

    }
}