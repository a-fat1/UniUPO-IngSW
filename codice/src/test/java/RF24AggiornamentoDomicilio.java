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
    public void testVariCampiErrati() {
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
    public void testSalvaERecuperaDomicilio() throws RemoteException {
        setUp();
        // Dati di test
        String usernameTest = "sisto";
        String viaTest = "Via Vittorio";
        String civicoTest = "101";
        String capTest = "10100";
        String localitaTest = "Torino";

        // Esegue promptSalvaDomicilio per inserire l'indirizzo
        gestoreAccessi.promptSalvaDomicilio(usernameTest, viaTest, civicoTest, capTest, localitaTest);

        // Esegue promptRecuperaDomicilio per recuperare l'indirizzo
        String[] result = gestoreAccessi.promptRecuperaDomicilio(usernameTest);

        // Verifica che l'array result sia di lunghezza 4
        assertEquals(4, result.length);

        // Verifica che le parti dell'indirizzo siano corrette
        assertEquals(viaTest, result[0]);
        assertEquals(civicoTest, result[1]);
        assertEquals(capTest, result[2]);
        assertEquals(localitaTest, result[3]);

    }

}