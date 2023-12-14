package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreAccessiInterfaccia extends Remote 
{  
   String controllaFormatoCredenziali(String username, String password) throws RemoteException;
   String ricercaCredenziali(String username, String password) throws RemoteException;
   HashMap<String, Object> ricercaUtente(String username) throws RemoteException;
   boolean verificaDuplicato(String nuovoUsername) throws RemoteException;
   int verifica(String vecchioUsername, String nuovoUsername) throws RemoteException;
   void cambio(String vecchioUsername, String nuovoUsername) throws RemoteException;
   void mostraErrore(int val);
   void messaggio(String messaggio);
} 




