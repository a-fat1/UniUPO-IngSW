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
   public void bloccoUtente(String username) throws RemoteException;
   public void sbloccoUtente(String username) throws RemoteException;
}
