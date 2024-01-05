import static org.junit.Assert.assertEquals;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import DataBase.DbProdotti;
import Elaborazione.GestoreCarrelli;

class RF07RimuoviProdottoDalCarrello {
	// RF07: rimuovi prodotto dal carrello
	// autori: Simone Aldo Borsa, Andrea Padoan

    private GestoreCarrelli gestoreCarrelli = new GestoreCarrelli(new DbProdotti());
    private ArrayList<HashMap<String, Object>> Carrello;
    private final String username = "ciro";
    
	@Test
	void testRimozioneProdottoDalCarrello() throws RemoteException {
		Carrello = gestoreCarrelli.cercaElementi(username);
		gestoreCarrelli.rimozioneProdottoDalCarrello(Carrello.get(0), username);
		
		assertEquals(1, Carrello.size());
	}
	
	@Test
	void testSvuotaCarrello() throws RemoteException {
		Carrello = gestoreCarrelli.cercaElementi(username);
		gestoreCarrelli.svuotaCarrello(Carrello, username);
		
		assertEquals(0, Carrello.size());
	}

}
