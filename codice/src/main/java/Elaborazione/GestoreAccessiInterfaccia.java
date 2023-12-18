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
   int controllaFormatoDomicilio(String via, String numero, String cap, String localita) throws RemoteException;
   void promptSalvaDomicilio(String username, String via, String civico, String cap, String localita) throws RemoteException;
} 
