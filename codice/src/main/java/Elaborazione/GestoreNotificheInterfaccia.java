package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreNotificheInterfaccia extends Remote 
{
    ArrayList<HashMap<String, Object>> ricercaNotifiche(String tipoUtente, String myDateObj) throws RemoteException;
}


