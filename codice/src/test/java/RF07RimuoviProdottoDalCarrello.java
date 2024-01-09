import static org.junit.Assert.assertEquals;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

//import org.junit.jupiter.api.Test;
import org.junit.Test;

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
		Carrello = gestoreCarrelli.cercaElementi(username);
		if(Carrello.size() == 0) return;
		valorePrevisto = Carrello.size() - 1;
		
		gestoreCarrelli.rimozioneProdottoDalCarrello(Carrello, Carrello.get(0), username);
		
		assertEquals(valorePrevisto, Carrello.size());
	}
	
	@Test
	public void testSvuotaCarrello() throws RemoteException {
		Carrello = gestoreCarrelli.cercaElementi(username);
		valorePrevisto = 0;
		
		gestoreCarrelli.svuotaCarrello(Carrello, username);
		
		assertEquals(valorePrevisto, Carrello.size());
	}

}
