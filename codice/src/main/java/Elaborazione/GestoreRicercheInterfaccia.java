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
    public int controlloParametri(String dataInizio, String dataFine) throws RemoteException;
    public ArrayList<HashMap<String, Object>> ricercaListaPagamentiData(String dataInizio, String dataFine) throws RemoteException;

    // RF19 RicercaUtente
    // Nazzari Riccardo, Andrea Benedetto

    int controlloParametriRicercaUtente(String username) throws RemoteException;
    int controlloParametriRicercaUtente(String nome, String cognome) throws RemoteException;

    ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String username) throws RemoteException;

    ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String username) throws RemoteException;

    ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String nome, String cognome) throws RemoteException;

    ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String nome, String cognome) throws RemoteException;
} 

