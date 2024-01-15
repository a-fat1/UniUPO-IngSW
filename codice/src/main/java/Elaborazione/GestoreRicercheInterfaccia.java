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

    //RF12: Lista Pagamenti
    //autori: Broglio, Cartieri
    public int controlloParametriListaPagamenti(String dataInizio, String dataFine) throws RemoteException;
    public ArrayList<HashMap<String, Object>> ricercaListaPagamentiData(String dataInizio, String dataFine) throws RemoteException;
    public ArrayList<HashMap<String, Object>> ricercaListaPagamentiUtente(String username) throws RemoteException;
    // RF18 Prodotti in esaurimento
    // Alessandro Fatone, Dario Guidotti
    int controlloGiacenza(String giacenza) throws RemoteException;
	ArrayList<HashMap<String, Object>> ricercaProdottiConGiacenza(String giacenza) throws RemoteException;

    // RF19 RicercaUtente
    // Nazzari Riccardo, Andrea Benedetto
    int controlloParametriRicercaUtente(String username) throws RemoteException;
    int controlloParametriRicercaUtente(String nome, String cognome) throws RemoteException;
    ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String username) throws RemoteException;
    ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String username) throws RemoteException;
    ArrayList<HashMap<String, Object>> cercaUtenteBloccatoNonBloccato(String nome, String cognome) throws RemoteException;
    ArrayList<HashMap<String, Object>> cercaUtenteNonBloccato(String nome, String cognome) throws RemoteException;

     //RF08 Raffaele Camera e Vanessa Tafarella
     public ArrayList<HashMap<String, Object>> ricercaProdotto(String titolo, String autore, String editore, String anno,
     ArrayList<String> tipoList, boolean isCliente) throws RemoteException;
    public boolean controllaParametri(String titolo, String autore, String editore, String anno, ArrayList<String> tipoList) throws RemoteException;

//RF22 Valentini Marco Cozzi Andrea
    //definisce i metodi utilizzati
    public ArrayList<HashMap<String, Object>> ricercaProdotti(String titolo, String autore, String editori, String anno, String tipo) throws RemoteException;
    
  	public boolean controllaValore(String tipo) throws RemoteException;


} 

