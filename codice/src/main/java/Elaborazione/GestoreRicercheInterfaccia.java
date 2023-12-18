package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreRicercheInterfaccia extends Remote 
{
    // RF19 RicercaUtente
    // Nazzari Riccardo, Andrea Benedetto

    int controlloParametri(String username);
    int controlloParametri(String nome, String cognome);

    ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String username) throws RemoteException;

    ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String username) throws RemoteException;

    ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String nome, String cognome) throws RemoteException;

    ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String nome, String cognome) throws RemoteException;
} 
