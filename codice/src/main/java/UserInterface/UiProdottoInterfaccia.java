package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.HashMap; 

public interface UiProdottoInterfaccia extends Remote
{
	void avvioRimuoviRipristinaNelCatalogo(Integer codProdotto, Integer disponibile) throws RemoteException;
	void avvioAggiornaPrezzo(HashMap<String, Object> P) throws RemoteException;
	/**
	 * nuovoProdotto = true se chiamato da RF16, false se chiamato da RF08
	 */
	void avvioNuovaFornitura(Integer codProdotto, boolean nuovoProdotto) throws RemoteException; // RF15 (Nicolò Bianchetto, Kristian Rigo)
	void avvioNuovoProdotto() throws RemoteException;
	void avvioIncrementaDecrementaPrezzi() throws RemoteException;
}

