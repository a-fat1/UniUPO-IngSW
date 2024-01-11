package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreCarrelliInterfaccia extends Remote 
{
    // RF05: visualizza carrello
    // autori: Bossola Fancesco, Oppezzo Raul
    ArrayList<HashMap<String, Object>> cercaProdottiCarrello(String username) throws RemoteException;
    int verificaQuantita(String nuovaQuantita, HashMap<String, Object> prodottoSelezionato) throws RemoteException;
    void modificaQuantita(String nuovaQuantita, HashMap<String, Object> prodottoSelezionato, String username) throws RemoteException;
    
    
    // RF07: rimuovi prodotto dal carrello
 	// autori: Simone Aldo Borsa, Andrea Padoan
    void rimozioneProdottoDalCarrello(ArrayList<HashMap<String, Object>> carrello, HashMap<String, Object> elemento, String username) throws RemoteException;
    
    // RF07: rimuovi prodotto dal carrello
  	// autori: Simone Aldo Borsa, Andrea Padoan
    public void svuotaCarrello(ArrayList<HashMap<String, Object>> carrello, String username) throws RemoteException;


  //RF09: aggiunta al carrello
  //Autori: Fasano Lorenzo, Iacobucci Luca;
   public boolean controlloLimiteQuantita(int quantita) throws RemoteException;
   public boolean controlloDisponibilita(int codiceProdotto) throws RemoteException;
   public void aggiornamentoQuantita(int quantita, int codiceProdotto, String username) throws RemoteException;

} 
