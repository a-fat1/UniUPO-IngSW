package Elaborazione;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import DataBase.*;

public class GestoreNotifiche implements GestoreNotificheInterfaccia {
    private Registry registry;
    private DbNotificheInterfaccia dbNotifiche;

    public GestoreNotifiche(String host) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(host, 1098);
        dbNotifiche = (DbNotificheInterfaccia) registry.lookup("dbNotifiche");
    }

    public GestoreNotifiche(DbNotifiche d1) // per testing
    {
        dbNotifiche = d1;
    }

    public String generaTestoNotificaProdotto(HashMap<String, Object> prodotto) throws RemoteException {
		String tipo = prodotto.get("tipo").toString();
		String autore = prodotto.get("autore").toString();
		String titolo = prodotto.get("titolo").toString();

		return "Nuovo " + tipo + ": " + autore + ", " + titolo + ".";
    }

    public String generaTestoNotificaOrdine(HashMap<String, Object> ordine) throws RemoteException {
		String username = ordine.get("username").toString();

        return "Nuovo Ordine: effettuato da " + username + ".";
    }

    public String generaTestoNotificaAvviso() throws RemoteException {
		return "Avviso: ";
    }

    public String generaTestoNotificaUtente(HashMap<String, Object> utente) throws RemoteException {
        String nome = utente.get("nome").toString();
		String cognome = utente.get("cognome").toString();

        return "Nuovo Cliente: " + nome + " " + cognome + ".";
    }

    public String verificaCorrettezzaDati(String data, String ora, String testoNotifica) throws RemoteException {
        String esitoVerifica = "errore ";
        int lenTxt = testoNotifica.length();

        // controllo formato data
        try {
            LocalDate.parse(data, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return esitoVerifica + "formato data";
        }
        // controllo formato ora
        if (!ora.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            return esitoVerifica + "formato ora";
        }
        // controllo correttezza data
        else if (LocalDate.parse(data).isBefore(LocalDate.now())) {
            return esitoVerifica + "data";
        }
        // controllo lunghezza testo
        else if (lenTxt == 0) {
            return esitoVerifica + "testo notifica";
        } else {
            return "ok";
        }
    }

    public void inserimentoNotifica(HashMap<String, Object> dataPubblicazione, HashMap<String, Object> dataScadenza, String testoNotifica, String tipoUtente) throws RemoteException {
        String comandoSql = "INSERT INTO Notifica (\"" + dataPubblicazione.get("data") + dataPubblicazione.get("ora") + "\", \"" + dataScadenza.get("data") + dataScadenza.get("ora") + "\", \"" + testoNotifica + "\", \"" + tipoUtente + "\");";

        dbNotifiche.update(comandoSql);
    }

}
