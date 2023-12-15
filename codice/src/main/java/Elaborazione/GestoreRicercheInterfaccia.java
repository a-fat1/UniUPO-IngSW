package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreRicercheInterfaccia extends Remote 
{
    //RF11: lista ordini
    //autore: Marino & Vecchio
    public ArrayList<HashMap<String, Object>> ricercaPerUtente(String username) throws RemoteException;
    public ArrayList<HashMap<String, Object>> ricercaPerProdotto(int codiceProdotto) throws RemoteException;
} 
