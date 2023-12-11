package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface UiListaInterfaccia extends Remote
{
	void avvioListaOrdini() throws RemoteException;
	void avvioListaPagamenti() throws RemoteException;
	void avvioListaForniture() throws RemoteException;
}

