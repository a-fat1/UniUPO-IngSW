package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreProdottiInterfaccia extends Remote 
{  

    // RF 13 Benetti-Chiappa
    public ArrayList<HashMap<String, Object>> ricercaListaForniture(int codice);
    public ArrayList<HashMap<String, Object>> ricercaListaForniture(String dataInizio, String dataFine);
    public int controlloParametri(String dataInizio, String dataFine);
   
} 
