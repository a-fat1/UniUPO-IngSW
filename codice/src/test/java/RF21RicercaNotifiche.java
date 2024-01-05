/*import static org.junit.Assert.*;

import DataBase.DbNotifiche;
import org.junit.Test;
import Elaborazione.GestoreNotifiche;


public class RF21RicercaNotifiche {
    final private DbNotifiche dbNotifiche = new DbNotifiche();
    final private GestoreNotifiche gestoreNotifiche = new GestoreNotifiche(dbNotifiche);

    @Test
    public void testcontrolloParametriCorretti() {
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("01-01-2020", "01-01-2021"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("01-01-2020", "01-01-2020"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("01-01-2020", "01-02-2020"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("01-01-2020", "02-01-2020"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("01-01-2020", "02-02-2020"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("01-01-2020", "02-02-2021"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("01-01-2020", "01-02-2021"));
        assertEquals("Date corrette!", gestoreNotifiche.controlloParametri("01-01-2020", "02-01-2021"));
    }

    @Test
    public void testcontrolloParametriErrati() {
        try {
            gestoreNotifiche.controlloParametri(null, "01-01-2021");
            fail("Non è stata lanciata l'eccezione IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Manca una data!", e.getMessage());
        }
        try {
            gestoreNotifiche.controlloParametri("01-01-2020", null);
            fail("Non è stata lanciata l'eccezione IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Manca una data!", e.getMessage());
        }
        try {
            gestoreNotifiche.controlloParametri(null, null);
            fail("Non è stata lanciata l'eccezione IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Manca una data!", e.getMessage());
        }
        try {
            gestoreNotifiche.controlloParametri("01-01-2021", "01-01-2020");
            fail("Non è stata lanciata l'eccezione IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Date non valide!", e.getMessage());
        }
        try {
            gestoreNotifiche.controlloParametri("03-01-2020", "01-01-2020");
            fail("Non è stata lanciata l'eccezione IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Date non valide!", e.getMessage());
        }
        try{
            gestoreNotifiche.controlloParametri("2020/01/03", "01-01-2020");
            fail("Non è stata lanciata l'eccezione IllegalArgumentException");
        } catch (IllegalArgumentException e){
            assertEquals("Formato data non valido!", e.getMessage());
        }
    }

}*/
