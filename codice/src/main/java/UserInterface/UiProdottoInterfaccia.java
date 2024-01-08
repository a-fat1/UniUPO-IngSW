package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface UiProdottoInterfaccia extends Remote
{
	void avvioRimuoviRipristinaNelCatalogo() throws RemoteException;
	void avvioAggiornaPrezzo() throws RemoteException;
	/**
	 * nuovoProdotto = true se chiamato da RF16, false se chiamato da RF08
	 */
	void avvioNuovaFornitura(Integer codProdotto, boolean nuovoProdotto) throws RemoteException; // RF15 (Nicolò Bianchetto, Kristian Rigo)
	void avvioNuovoProdotto() throws RemoteException;
	void avvioIncrementaDecrementaPrezzi() throws RemoteException;
}

