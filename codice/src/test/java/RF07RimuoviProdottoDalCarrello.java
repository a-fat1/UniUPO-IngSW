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
		if(Carrello.size() == 0) return;
		valorePrevisto = Carrello.size() - 1;
		HashMap<String, Object> prodottoRimosso = Carrello.get(0);
		String quantita = (String) Carrello.get(0).get("quantita").toString();
		
		System.out.println("Quantità originale: " + Carrello.size());
		gestoreCarrelli.rimozioneProdottoDalCarrello(Carrello, Carrello.get(0), username);
		assertEquals(valorePrevisto, Carrello.size());
		System.out.println("Quantità dopo lo svuotamento: " + Carrello.size());
		
		gestoreCarrelli.modificaQuantita(quantita, prodottoRimosso, username);
		System.out.println("Quantità ripristinata: " + Carrello.size());
	}
	
//	@Test
//	public void testSvuotaCarrello() throws RemoteException {
//		Carrello = gestoreCarrelli.cercaProdottiCarrello(username);
//		valorePrevisto = 0;
//		ArrayList<String> quantita = new ArrayList<String>();
//		
//		ArrayList<HashMap<String, Object>> temp = new ArrayList<HashMap<String, Object>>(Carrello);
//		for(HashMap<String, Object> k : Carrello) {
//			quantita.add((String) k.get("quantita").toString());
//		}
//		
////		System.out.println("Quantità originale: " + Carrello.size());
//		
//		gestoreCarrelli.svuotaCarrello(Carrello, username);
//		assertEquals(valorePrevisto, Carrello.size());
////		System.out.println("Quantità dopo lo svuotamento: " + Carrello.size());
//		
////		for(int i = 0; i < quantita.size(); i++) {
////			gestoreCarrelli.modificaQuantita(quantita.get(i), temp.get(i), username);
////			gestoreCarrelli.
////		}
////		System.out.println("Quantità ripristinata: " + Carrello.size());
//	}

}
