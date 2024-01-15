import static org.junit.Assert.*;

import DataBase.DbNotifiche;
import org.junit.Test;
import Elaborazione.GestoreNotifiche;


public class RF21RicercaNotifiche {
    final private DbNotifiche dbNotifiche = new DbNotifiche();
    final private GestoreNotifiche gestoreNotifiche = new GestoreNotifiche(dbNotifiche);

    @Test
    public void testcontrolloParametriCorretti() {
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("2020-01-01", "2021-01-01"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("2020-01-01", "2021-01-01"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("2020-01-01", "2020-02-01"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("2020-01-01", "2020-01-02"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("2020-01-01", "2020-02-02"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("2020-01-01", "2021-02-02"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("2020-01-01", "2021-02-01"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("2020-01-01", "2021-01-02"));
    }

    @Test
    public void testcontrolloParametriErrati() {
        assertEquals("Manca una data!", gestoreNotifiche.controlloParametri("2022-05-10", ""));
        assertEquals("Manca una data!", gestoreNotifiche.controlloParametri("", "2020-09-07"));
        assertEquals("Date non valide!", gestoreNotifiche.controlloParametri("2022-05-10", "2021-10-01"));
        assertEquals("Formato data non valido!", gestoreNotifiche.controlloParametri("01-10-2024", "2020-10-20"));
        assertEquals("Formato data non valido!", gestoreNotifiche.controlloParametri("2022/05/10", "2020-10-20"));
        assertEquals("Formato data non valido!", gestoreNotifiche.controlloParametri("aufgbsudb", "2020-10-20"));
    }

}
