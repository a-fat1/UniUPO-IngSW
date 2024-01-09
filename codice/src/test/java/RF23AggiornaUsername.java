//RF23 - Aggiorna Username
//autore: Brivio Marco, Serio Giulia
import static org.junit.Assert.*; 
import org.junit.Test;
import java.rmi.RemoteException;

import DataBase.DbUtenti;
import Elaborazione.GestoreAccessi;

public class RF23AggiornaUsername {
    DbUtenti dbUtenti = new DbUtenti();
    GestoreAccessi gestoreAccessi = new GestoreAccessi(dbUtenti);

    @Test
    public void testVerificaDuplicatoTrue() throws RemoteException{
        assertEquals(false, gestoreAccessi.verificaDuplicato(""));
    }

    @Test
    public void testVerificaDuplicatoFalse() throws RemoteException{
        assertEquals(true, gestoreAccessi.verificaDuplicato("aldo"));
    }

    @Test
    public void testVerifica0() throws RemoteException{
        assertEquals(0, gestoreAccessi.verifica("sirio", "Marco"));
    }

    @Test
    public void testVerifica1() throws RemoteException{
        assertEquals(1, gestoreAccessi.verifica("aldo", "ab"));
    }

    @Test
    public void testVerfica2() throws RemoteException{
        assertEquals(2, gestoreAccessi.verifica("aldo", "aldo"));
    }

    @Test
    public void testVerifica3() throws RemoteException{
        assertEquals(3, gestoreAccessi.verifica("ab", "ab"));
    }
}
