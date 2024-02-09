import static org.junit.Assert.*;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import DataBase.DbProdotti;
import Elaborazione.GestoreCarrelli;

public class RF07RimuoviProdottoDalCarrello {
	// RF07: rimuovi prodotto dal carrello
	// autori: Simone Aldo Borsa, Andrea Padoan

    private GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(new DbProdotti());
    private ArrayList<HashMap<String, Object>> Carrello;
    private final String username = "ciro";
    int valorePrevisto;
    
	@Test
	public void testRimozioneProdottoDalCarrello() throws RemoteException {
		Carrello = gestoreCarrelli.cercaProdottiCarrello(username);
		valorePrevisto = Carrello.size() - 1;
		HashMap<String, Object> prodottoRimosso = Carrello.get(0);
		int quantitaProdottoNelCarrello = (int) Carrello.get(0).get("quantitaProdotto");
		
		gestoreCarrelli.rimozioneProdottoDalCarrello(Carrello, Carrello.get(0), username);
		assertEquals(valorePrevisto, Carrello.size());
		
		gestoreCarrelli.aggiornamentoQuantita(quantitaProdottoNelCarrello, (int) prodottoRimosso.get("codice"), username);
	}
	
	@Test
	public void testSvuotaCarrello() throws RemoteException {
		Carrello = gestoreCarrelli.cercaProdottiCarrello(username);
		valorePrevisto = 0;
		ArrayList<Integer> quantita = new ArrayList<Integer>();
		
		ArrayList<HashMap<String, Object>> tempC = new ArrayList<HashMap<String, Object>>(Carrello);
		for(HashMap<String, Object> k : Carrello) {
			quantita.add((int) k.get("quantitaProdotto"));
		}
		
		gestoreCarrelli.svuotaCarrello(Carrello, username, false);
		assertEquals(valorePrevisto, Carrello.size());
		
		for(int i = 0; i < quantita.size(); i++) {
			gestoreCarrelli.aggiornamentoQuantita(quantita.get(i), (int) tempC.get(i).get("codiceProdotto"), username);
		}
	}

}
