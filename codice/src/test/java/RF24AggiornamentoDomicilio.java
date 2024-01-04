import static org.junit.Assert.*;

import org.junit.After;
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
    public void testPromptSalvaDomicilio() throws RemoteException {
        // Inizializza il database con un record per l'utente di test
        setUp();
        String usernameTest = "testUser";
        String viaTest = "Via Test";
        String civicoTest = "42";
        String capTest = "12345";
        String localitaTest = "CittaTest";

        // Esegue promptSalvaDomicilio per aggiornare l'indirizzo
        gestoreAccessi.promptSalvaDomicilio(usernameTest, viaTest, civicoTest, capTest, localitaTest);

        // Verifica che l'indirizzo sia stato aggiornato correttamente
        String selectQueryAfter = "SELECT * FROM Domicilio WHERE Username = '" + usernameTest + "'";
        ArrayList<HashMap<String, Object>> resultAfter = dbUtenti.query(selectQueryAfter);

        // Assicurati che ci sia solo un record per l'utente di test
        assertEquals(1, resultAfter.size());

        // Verifica che i valori siano stati aggiornati correttamente
        assertEquals(viaTest, resultAfter.get(0).get("via"));
        assertEquals(civicoTest, resultAfter.get(0).get("civico"));
        assertEquals(capTest, resultAfter.get(0).get("cap"));
        assertEquals(localitaTest, resultAfter.get(0).get("localita"));
    }

    @After
    public void tearDown() {
        // Ripulisce il database dopo ciascun test
        String deleteQuery = "DELETE FROM Domicilio WHERE Username = 'testUser'";
        dbUtenti.update(deleteQuery);
    }
}