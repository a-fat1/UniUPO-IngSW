package UserInterface;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import javax.swing.*;

import UserInterface.*;
import Elaborazione.*;

public class UiUtente extends JOptionPane implements UiUtenteInterfaccia
{
	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private UiNotificaInterfaccia uiNotifica;
	private GestoreAccessiInterfaccia gestoreAccessi;
	

	// attributi
	private int selezione;
	// elementi grafici

	public UiUtente(String hostGestore) throws RemoteException, NotBoundException
	{
		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

		uiNotifica = (UiNotificaInterfaccia) registryUI.lookup("uiNotifica");
		gestoreAccessi = (GestoreAccessiInterfaccia) registryGestore.lookup("gestoreAccessi");
	}

	public void avvioCreaUtente() throws RemoteException
	{ 	// RF02
	}

	public void avvioBloccaSbloccaUtente(String username, boolean attivo) throws RemoteException
	{ 	// RF20
		if(attivo)
		{
			this.mostraFormBlocco();
			if(selezione==1) {

				gestoreAccessi.bloccoUtente(username);

			}
		}
		else{
			this.mostraFormSblocco();
			if(selezione==1) {

				gestoreAccessi.sbloccoUtente(username);

			}
		}
	}

	private void mostraFormBlocco() {
		String[] scelte= { "ANNULLA","CONFERMA"};
		selezione = JOptionPane.showOptionDialog(null, null, "FORM BLOCCO", 0, 2, null, scelte, null);

		if (selezione == 1) {
			JOptionPane.showMessageDialog(null, null, "Conferma lettura del messaggio(OK o X per continuare).", JOptionPane.INFORMATION_MESSAGE);
		}

		else {
			JOptionPane.showMessageDialog(null, null, "annullamento", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void mostraFormSblocco() {
		String[] scelte= { "ANNULLA","CONFERMA"};
		selezione = JOptionPane.showOptionDialog(null, null, "FORM SBLOCCO", 0, 3, null, scelte, null);

		if (selezione == 1) {
			JOptionPane.showMessageDialog(null, null, "Conferma lettura del messaggio(OK o X per continuare).", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(null, null, "annullamento", JOptionPane.ERROR_MESSAGE);
		}
	}





	public void avvioAggiornaDomicilio() throws RemoteException
	{ 	// RF24
	}
}
