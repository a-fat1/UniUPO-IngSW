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
    
//RF06: effettua ordine
// autori:Luini, Mengaptche
  boolean controllaNumeroCarta(String NumeroCarta) throws RemoteException;    
  public void aggiornaOrdini(ArrayList<HashMap<String, Object>> listaProdottiCarrello, String dataOrdine) throws RemoteException;
  public void aggiornaPagamenti(String username,String dataOrdine,float prezzoTotale,String numeroCarta,int sceltaMostraFormCarta) throws RemoteException;
  
  
    // RF07: rimuovi prodotto dal carrello
 	// autori: Simone Aldo Borsa, Andrea Padoan
    void rimozioneProdottoDalCarrello(ArrayList<HashMap<String, Object>> carrello, HashMap<String, Object> elemento, String username) throws RemoteException;
    
    // RF07: rimuovi prodotto dal carrello
  	// autori: Simone Aldo Borsa, Andrea Padoan
    public void svuotaCarrello(ArrayList<HashMap<String, Object>> carrello, String username, boolean ordine) throws RemoteException;


  //RF09: aggiunta al carrello
  //Autori: Fasano Lorenzo, Iacobucci Luca;
   public boolean controlloLimiteQuantita(int quantita) throws RemoteException;
   // Codetta: quantita'
   public boolean controlloDisponibilita(int codiceProdotto, int quantita) throws RemoteException;
   public void aggiornamentoQuantita(int quantita, int codiceProdotto, String username) throws RemoteException;

} 
