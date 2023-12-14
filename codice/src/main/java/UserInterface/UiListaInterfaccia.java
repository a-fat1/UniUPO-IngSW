package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap; 

public interface UiListaInterfaccia extends Remote
{
	void avvioListaOrdini() throws RemoteException;
	void avvioListaPagamenti() throws RemoteException;

	// RF 13 Benetti-Chiappa
	void avvioListaForniture( int  codice) ;
	void avvioListaForniture() ;
	void mostraErrore(int esitoControllo);
	void mostraFormRicercaPerData();
	void mostraListaItem(ArrayList<HashMap<String, Object>> listaForniture);
	void mostraListaData(ArrayList<HashMap<String, Object>> listaForniture);
}

