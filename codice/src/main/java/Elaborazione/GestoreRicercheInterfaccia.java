package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreRicercheInterfaccia extends Remote 
{

	boolean controllaParametri(String titolo, String autore, String editore, String anno, String tipo);

	ArrayList<HashMap<String, Object>> ricercaProdotto(String titolo, String autore, String editore, String anno, String tipo) throws RemoteException;  
} 
