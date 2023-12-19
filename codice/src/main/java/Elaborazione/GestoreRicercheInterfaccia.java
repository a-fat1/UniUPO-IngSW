package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreRicercheInterfaccia extends Remote 
{
    public int controlloParametri(String dataInizio, String dataFine) throws RemoteException;
    public ArrayList<HashMap<String, Object>> ricercaListaPagamentiData(String dataInizio, String dataFine) throws RemoteException;
} 
