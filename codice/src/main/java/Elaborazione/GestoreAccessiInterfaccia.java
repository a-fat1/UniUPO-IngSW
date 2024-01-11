package Elaborazione;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.HashMap;

public interface GestoreAccessiInterfaccia extends Remote
{
   String controllaFormatoCredenziali(String username, String password) throws RemoteException;
   String ricercaCredenziali(String username, String password) throws RemoteException;
   HashMap<String, Object> ricercaUtente(String username) throws RemoteException;
   public void bloccoUtente(String username) throws RemoteException;
   public void sbloccoUtente(String username) throws RemoteException;

   int verificaCredenziali(String password, String passwordAttuale) throws RemoteException;
   int controlloNuovaPassword(String nuovaPassword) throws RemoteException;
   void AggiornaPassword(String username, String nuovaPassword) throws RemoteException;

   boolean verificaDuplicato(String nuovoUsername) throws RemoteException;

   int verifica(String vecchioUsername, String nuovoUsername) throws RemoteException;


   
   //RF02
   int controlloFormatoNomeCognome(String nome, String cognome)throws RemoteException;
   void promptSalvaAccount(String nome, String cognome) throws RemoteException;
   void richiestaAttivazioneAccount(String nome, String cognome, String tipoUtente) throws RemoteException;
   void aggiuntaCredenziali(String username) throws RemoteException;

   //RF23
   boolean verificaDuplicato(String nuovoUsername) throws RemoteException;
   int verifica(String vecchioUsername, String nuovoUsername) throws RemoteException;
   void cambio(String vecchioUsername, String nuovoUsername) throws RemoteException;

   /**
    * RF24: AggiornamentoDomicilio
    * Autore: Mondelli e Reci
    */
   int controllaFormatoDomicilio(String via, String numero, String cap, String localita) throws RemoteException;
   void promptSalvaDomicilio(String username, String via, String civico, String cap, String localita) throws RemoteException;
   String[] promptRecuperaDomicilio(String username) throws RemoteException;

}
