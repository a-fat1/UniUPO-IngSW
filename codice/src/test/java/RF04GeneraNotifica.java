import static org.junit.Assert.*;

import DataBase.DbNotifiche;
import Elaborazione.GestoreNotifiche;
import org.junit.Test;
import java.rmi.RemoteException;

import DataBase.DbUtenti;
import Elaborazione.GestoreAccessi;

public class RF04GeneraNotifica {

    DbNotifiche dbNotifiche = new DbNotifiche();
    GestoreNotifiche gestoreNotifiche = new GestoreNotifiche(dbNotifiche);

    @Test
    public void testVerificaCorrettezzaDati() {

    }

}