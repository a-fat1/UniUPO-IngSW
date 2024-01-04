package UserInterface;

import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface UiRicercaInterfaccia extends Remote
{
	void avvioRicercaProdotto(String tipoUtente) throws RemoteException;
	void avvioProdottiInEsaurimento() throws RemoteException;
	void avvioRicercaUtente() throws RemoteException;
	void avvioProdottiPiuVenduti() throws RemoteException;
}

