package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreProdottiInterfaccia extends Remote 

{  

    // RF 13 Benetti-Chiappa
    public ArrayList<HashMap<String, Object>> ricercaListaForniture(int codice) throws RemoteException;
    public ArrayList<HashMap<String, Object>> ricercaListaForniture(String dataInizio, String dataFine) throws RemoteException;
    public int controlloParametri(String dataInizio, String dataFine) throws RemoteException;
   



    //RF15 (Nicolò Bianchetto, Kristian Rigo)
    HashMap<String, Boolean> controlloDatiFornitura(String data, Float costo, Integer quantita) throws RemoteException;
    void aggiungiFornitura(Integer codProdotto, String data, Float costo, Integer quantita) throws RemoteException;


    //RF16 Leonardo Galliera, Nicholas Ternullo
    int verificaCampi(String[] autori, String titolo, String editore, int anno) throws RemoteException;
    boolean controlloUnicita(String[] autori, String titolo, String editore, int anno, String tipo) throws RemoteException;
    HashMap<String, Object> aggiungiProdotto(String[] autori, String titolo, String editore, int anno, String tipo) throws RemoteException;


    //RF10
    void rimuoviProdotto(Integer codProdotto) throws RemoteException;
    void ripristinaProdotto(Integer codProdotto) throws RemoteException;

    //RF17
    boolean controlloPercentuale(int Percentuale) throws RemoteException;
    void incrementaPrezzi(int Percentuale) throws RemoteException;
    void decrementaPrezzi(int Percentuale) throws RemoteException;

    //RF14
    int controlloFormatoModificaPrezzo(float prezzoNuovo, float prezzoVecchio) throws RemoteException;
    void modificaPrezzo(float prezzo, int codice) throws RemoteException;
    
} 
