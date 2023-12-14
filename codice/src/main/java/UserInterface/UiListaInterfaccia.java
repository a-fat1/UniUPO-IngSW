package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap; 

public interface UiListaInterfaccia extends Remote
{
	void avvioListaOrdini() throws RemoteException;
	void avvioListaPagamenti() throws RemoteException;
	void avvioListaForniture( int  codice) throws RemoteException;
	void avvioListaForniture() throws RemoteException;
	void mostraErrore(int esitoControllo);
	void mostraFormRicercaPerData();
	void mostraListaItem(ArrayList<HashMap<String, Object>> listaForniture);
	void mostraListaData(ArrayList<HashMap<String, Object>> listaForniture);
}

