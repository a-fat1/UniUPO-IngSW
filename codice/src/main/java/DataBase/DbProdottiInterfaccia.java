package DataBase;

import java.rmi.Remote; 
import java.rmi.RemoteException;  

import java.util.HashMap;
import java.util.ArrayList;

public interface DbProdottiInterfaccia extends Remote 
{  
   ArrayList<HashMap<String, Object>> query(String comandoSql) throws RemoteException;
   void update(String comandoSql) throws RemoteException;
   
   
   public ArrayList<HashMap<String, Object>> ricercaProdotto(String titolo, String autore, String editore,
           String anno, String tipo);
} 
