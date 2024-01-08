package UserInterface;

<<<<<<< HEAD
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UiUtenteInterfaccia extends Remote
{
	void avvioCreaUtente() throws RemoteException;
	void avvioBloccaSbloccaUtente(String username, boolean attivo) throws RemoteException;
=======
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException; 
import java.rmi.NotBoundException;

public interface UiUtenteInterfaccia extends Remote
{
	//RF02
	void avvioCreaUtente(boolean cliente) throws RemoteException, NotBoundException;
	void avvioBloccaSbloccaUtente() throws RemoteException;
>>>>>>> 1855847a08ab915903caf747e4ced5b2af9178a1
	void avvioAggiornaDomicilio() throws RemoteException;
	
}
