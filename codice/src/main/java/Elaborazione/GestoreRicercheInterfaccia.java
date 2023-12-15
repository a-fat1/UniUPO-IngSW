package Elaborazione;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreRicercheInterfaccia extends Remote {
    // compilare qui le firme - metodo normale vuoto
    public int controlloGiacenza(String giacenza) throws RemoteException;

    public ArrayList<HashMap<String, Object>> ricercaProdotti(String giacenza) throws RemoteException;

}
