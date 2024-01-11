package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreProdottiInterfaccia extends Remote 
{  
    //RF14
    int controlloFormatoModificaPrezzo(float prezzoNuovo, float prezzoVecchio) throws RemoteException;
    void modificaPrezzo(float prezzo, int codice) throws RemoteException;
} 
