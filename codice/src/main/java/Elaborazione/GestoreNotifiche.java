package Elaborazione;

import java.time.LocalDate;
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
    private DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public GestoreNotifiche(String host) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(host, 1098);
        dbNotifiche = (DbNotificheInterfaccia) registry.lookup("dbNotifiche");
    }

    public GestoreNotifiche(DbNotifiche d1) // per testing
    {
        dbNotifiche = d1;
    }

    public String generaTestoNotificaProdotto(HashMap<String, Object> prodotto) throws RemoteException {
	// RF04
        //genera il testo della notifica nel caso di un nuovo prodotto
        String tipo = prodotto.get("tipo").toString();
        String autore = prodotto.get("autore").toString();
        String titolo = prodotto.get("titolo").toString();

        return "Nuovo " + tipo + ": " + autore + ", " + titolo + ".";
    }

    public String generaTestoNotificaOrdine(HashMap<String, Object> ordine) throws RemoteException {
	// RF04
        //genera il testo della notifica nel caso di un nuovo ordine
        String username = ordine.get("username").toString();

        return "Nuovo Ordine: effettuato da " + username + ".";
    }

    public String generaTestoNotificaAvviso() throws RemoteException {
	// RF04
        //genera il testo della notifica nel caso di un nuovo avviso
        return "Avviso: ";
    }

    public String generaTestoNotificaUtente(HashMap<String, Object> utente) throws RemoteException {
	// RF04
        //genera il testo della notifica nel caso di un nuovo utente
        String nome = utente.get("nome").toString();
        String cognome = utente.get("cognome").toString();

        return "Nuovo Cliente: " + nome + " " + cognome + ".";
    }

    public String verificaCorrettezzaDati(String data, String ora, String testoNotifica) throws RemoteException {
	// RF04
        //metodo utilizzato per verificare che i dati in input (data, ora e testo della notifica) siano corretti e in linea con i formati
        String esitoVerifica = "errore ";

        // controllo formato data
        try {
            LocalDate.parse(data, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return esitoVerifica + "formato data";
        }
        // controllo formato ora
        if (!ora.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            return esitoVerifica + "formato ora";
        }
        // controllo correttezza data
        else if (LocalDate.parse(data).isBefore(LocalDate.now())) {
            return esitoVerifica + "data";
        }
        // controllo lunghezza testo
        else if (testoNotifica.isEmpty()) {
            return esitoVerifica + "testo notifica";
        } else {
            return "ok";
        }
    }

    public void inserimentoNotifica(HashMap<String, String> dataPubblicazione, HashMap<String, String> dataScadenza, String testoNotifica, String tipoUtente) throws RemoteException {
	// RF04
        //metodo utilizzato per l'inserimento di una notifica nel Database
        String comandoSql = "INSERT INTO Notifica (dataPubblicazione, dataScadenza, testo, tipoUtente) values (\"" +
                dataPubblicazione.get("data") + " " +
                dataPubblicazione.get("ora") + "\", \"" +
                dataScadenza.get("data") + " " +
                dataScadenza.get("ora") + "\", \"" +
                testoNotifica + "\", \"" +
                tipoUtente + "\");";

        dbNotifiche.update(comandoSql);
    }

    public String controlloParametri(String dataPubblicazione, String dataScadenza) {
        //RF21 - RicercaNotifiche
        //Colombo Giacomo, Riccardo Caviggia

        if (dataPubblicazione == null || dataScadenza == null || dataPubblicazione.isEmpty() || dataScadenza.isEmpty())
            return "Manca una data!";
        else {
            try {
                LocalDate pubblicazione = LocalDate.parse(dataPubblicazione, FORMATO_DATA);
                LocalDate scadenza = LocalDate.parse(dataScadenza, FORMATO_DATA);

                if (pubblicazione.isAfter(scadenza))
                    return "Date non valide!";
                else return "Date corrette!";
            } catch (DateTimeParseException e) {
                return "Formato data non valido!";
            }
        }
    }


    public ArrayList<HashMap<String, Object>> cercaNotifiche(String dataPubblicazione, String dataScadenza, String tipoUtente) {
        //RF21 - RicercaNotifiche
        //Colombo Giacomo, Riccardo Caviggia

        String comandoSql;
        ArrayList<HashMap<String, Object>> notifica = null;

        comandoSql = "SELECT * FROM Notifica WHERE dataPubblicazione BETWEEN \""  +dataPubblicazione + " 00:00:00\" AND \"" + dataPubblicazione + " 23:59:59\" AND dataScadenza BETWEEN \"" + dataScadenza + " 00:00:00 \" AND \"" + dataScadenza + " 23:59:59 \" AND tipoUtente = \"" + tipoUtente + "\" ;";

        try {
            notifica = dbNotifiche.query(comandoSql);
        } catch (RemoteException e) {
            System.err.println("Errore remoto: ");
            e.printStackTrace();
        }
        return notifica;
    }


    public ArrayList<HashMap<String, Object>> ricercaNotifiche(String tipoUtente, String myDateObj) throws RemoteException{
	// RF01
        String comandoSql;
        ArrayList<HashMap<String,Object>> notifica=null;
        comandoSql="SELECT * FROM Notifica WHERE(tipoUtente='"+tipoUtente+"' OR tipoUtente='tutti')"+" AND dataScadenza>='"+myDateObj+"'";
        try{
            notifica=dbNotifiche.query(comandoSql);
        }
        catch (Exception e){
            System.err.println(e.toString());
            e.printStackTrace();
        }
        return notifica;
    }
}



