package Elaborazione;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreRicercheInterfaccia extends Remote {
    int controlloGiacenza(String giacenza) throws RemoteException;
	ArrayList<HashMap<String, Object>> ricercaProdotti(String giacenza) throws RemoteException;
}
