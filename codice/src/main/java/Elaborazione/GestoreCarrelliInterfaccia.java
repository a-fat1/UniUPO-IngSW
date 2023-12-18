package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreCarrelliInterfaccia extends Remote 
{
    // RF05: visualizza carrello
    // autori: Bossola Fancesco, Oppezzo Raul
    ArrayList<HashMap<String, Object>> cercaElementi(String username) throws RemoteException;

    // RF05: visualizza carrello
    // autori: Bossola Fancesco, Oppezzo Raul
    int verificaQuantita(int nuovaQuantita, HashMap<String, Object> elemento) throws RemoteException;

    // RF05: visualizza carrello
    // autori: Bossola Fancesco, Oppezzo Raul
    void modificaQuantita(int nuovaQuantita, HashMap<String, Object> elemento, String username) throws RemoteException;
} 
