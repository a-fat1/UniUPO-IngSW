package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.HashMap; 

public interface UiProdottoInterfaccia extends Remote
{
	void avvioRimuoviRipristinaNelCatalogo() throws RemoteException;
	void avvioAggiornaPrezzo(HashMap<String, Object> P) throws RemoteException;
	void avvioNuovaFornitura() throws RemoteException;
	void avvioNuovoProdotto() throws RemoteException;
	void avvioIncrementaDecrementaPrezzi() throws RemoteException;
}

