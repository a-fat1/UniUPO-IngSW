import static org.junit.Assert.*;

import org.junit.Test;
import java.util.HashMap;
import DataBase.DbProdotti;
import Elaborazione.GestoreProdotti;


public class RF15NuovaFornitura {
    final private DbProdotti dbProdotti = new DbProdotti();
    final private GestoreProdotti gestoreProdotti = new GestoreProdotti(dbProdotti);

    // ===========================================================

    @Test
     public void testControlloDatiFornituraCorretti() {
        HashMap<String, Boolean> expected = new HashMap<>();
        for(String s : new String[] {"esitoData", "esitoCosto", "esitoQuantità"}) expected.put(s, true);
        // data corretta, costo corretto, quantità corretta
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2020-12-01", 25.0f, 5));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2020-02-29", 0.10f, 1));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2000-02-29", 9.999f, 4));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2010-05-31", 1.10f, 30));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2015-08-15", 12.50f, 20));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2015-02-28", 0.01f, 1));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2023-12-13", 20.0f, 8));
    }

    @Test
    public void testControlloDatiFornituraScorretti() {
        HashMap<String, Boolean> expected = new HashMap<>();
        for(String s : new String[] {"esitoData", "esitoCosto", "esitoQuantità"}) expected.put(s, false);
        // data errata, costo errata, quantità errata
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-02-29", -25.0f, -5));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("17-01-2020", 0.0f, 0));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("01-17-2020", -0.01f, -1));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021/02/29", 0.0f, 0));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2050-01-01", 0.0f, -3));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("questa non è una data", 0.0f, 0));
    }

    @Test
    public void testControlloDatiFornituraAssenti() {
        // data assente, costo assente, quantità assente
        HashMap<String, Boolean> expected = new HashMap<>();
        for(String s : new String[] {"esitoData", "esitoCosto", "esitoQuantità"}) expected.put(s, null);
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura(null, null, null));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("", null, null));

        expected.put("esitoData", true);
        // data corretta, costo assente, quantità assente
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-02-28", null, null));

        expected.put("esitoData", false);
        // data errata, costo assente, quantità assente
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2050-05-20", null, null));

        expected.put("esitoData", null);
        expected.put("esitoCosto", true);
        expected.put("esitoQuantità", true);
        // data assente, costo corretto, quantità corretta
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura(null, 1.5f, 3));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("", 0.5f, 5));

        expected.put("esitoCosto", false);
        expected.put("esitoQuantità", false);
        // data assente, costo errato, quantità errata
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura(null, -1.5f, -3));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("", 0.0f, 0));
    }

    @Test
    public void testControlloDatiFornituraMisti() {
        HashMap<String, Boolean> expected = new HashMap<>();

        expected.put("esitoData", true);
        expected.put("esitoCosto", false);
        expected.put("esitoQuantità", false);
        //data corretta, costo errato, quantità errata
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-02-28", -0.5f, -5));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-04-30", 0.0f, 0));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2023-02-28", -0.1f, -1));

        expected.put("esitoData", false);
        expected.put("esitoCosto", true);
        //data errata, costo corretto, quantità errata
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-02-29", 0.5f, -5));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-04-31", 25.0f, 0));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-04-aa", 0.1f, -1));

        expected.put("esitoCosto", false);
        expected.put("esitoQuantità", true);
        //data errata, costo errato, quantità corretta
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-02-29", -0.5f, 5));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-04-31", 0.0f, 1));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("abracadabra", -0.1f, 10));

        expected.put("esitoCosto", true);
        //data errata, costo corretto, quantità corretta
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-02-29", 0.5f, 5));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-04-31", 25.0f, 1));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("202l-05-3l", 0.1f, 1));

        expected.put("esitoData", true);
        expected.put("esitoCosto", false);
        //data corretta, costo errato, quantità corretta
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-02-28", -0.5f, 5));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-04-30", 0.0f, 1));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-05-31", -0.1f, 1));

        expected.put("esitoCosto", true);
        expected.put("esitoQuantità", false);
        //data corretta, costo corretto, quantità errata
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-02-28", 0.5f, -5));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-04-30", 25.0f, 0));
        assertEquals(expected, gestoreProdotti.controlloDatiFornitura("2021-05-31", 0.1f, -1));
    }
}
