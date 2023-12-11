package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface UiProdottoInterfaccia extends Remote
{
	void avvioRimuoviRipristinaNelCatalogo() throws RemoteException;
	void avvioAggiornaPrezzo() throws RemoteException;
	void avvioNuovaFornitura() throws RemoteException;
	void avvioNuovoProdotto() throws RemoteException;
	void avvioIncrementaDecrementaPrezzi() throws RemoteException;
}

