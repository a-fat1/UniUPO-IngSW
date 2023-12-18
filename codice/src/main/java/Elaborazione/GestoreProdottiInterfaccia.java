package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreProdottiInterfaccia extends Remote 
{
    HashMap<String, Boolean> controlloDatiFornitura(String data, Float costo, Integer quantita) throws RemoteException;
    void aggiungiFornitura(Integer codProdotto, String data, Float costo, Integer quantita) throws RemoteException;

    void rimuoviProdotto(Integer codProdotto) throws RemoteException;

    void ripristinaProdotto(Integer codProdotto) throws RemoteException;
} 
