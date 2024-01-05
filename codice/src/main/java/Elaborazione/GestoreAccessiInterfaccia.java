package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;

public interface GestoreAccessiInterfaccia extends Remote 
{  
   String controllaFormatoCredenziali(String username, String password) throws RemoteException;
   String ricercaCredenziali(String username, String password) throws RemoteException;
   HashMap<String, Object> ricercaUtente(String username) throws RemoteException;
   int verificaCredenziali(String password, String passwordAttuale) throws RemoteException;
   int controlloNuovaPassword(String nuovaPassword) throws RemoteException;
   void AggiornaPassword(String username, String nuovaPassword) throws RemoteException;
   
   //RF02
   int controlloFormatoNomeCognome(String nome, String cognome)throws RemoteException;
   void promptSalvaAccount(String nome, String cognome) throws RemoteException;
   void richiestaAttivazioneAccount(String nome, String cognome, String tipoUtente) throws RemoteException;
   void aggiuntaCredenziali(String username) throws RemoteException;
} 
