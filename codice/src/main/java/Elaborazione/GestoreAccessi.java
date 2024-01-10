package Elaborazione;

import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DataBase.*;

public class GestoreAccessi implements GestoreAccessiInterfaccia
{
	private Registry registry;
	private DbUtentiInterfaccia dbUtenti;

	public GestoreAccessi(String host) throws RemoteException, NotBoundException
	{
		registry = LocateRegistry.getRegistry(host, 1098);
       	 	dbUtenti = (DbUtentiInterfaccia) registry.lookup("dbUtenti");
	}

	public GestoreAccessi(DbUtenti d1) // per testing
	{
		dbUtenti = d1;
	}

	public String controllaFormatoCredenziali(String username, String password)
	{
		//RF00: login
    		//autore: Codetta

		int len1, len2;
		String esitoControllo;

		System.out.println("GestoreAccessi.controllaFormatoCredenziali(" + username + ", " + password + ")\n");

		len1=username.length();
		len2=password.length();

		if (len1==0 && len2==0)
			esitoControllo="erroreCredenziali";
		else
			if (len1==0)
				esitoControllo="erroreUsername";
			else
				if (len2==0)
					esitoControllo="errorePassword";
				else
					esitoControllo="ok";

		return esitoControllo;
	}

	public String ricercaCredenziali(String username, String password) throws RemoteException
	{
		//RF00: login
    		//autore: Codetta

		String comandoSql;
		String esitoRicerca;
		ArrayList<HashMap<String, Object>> credenziali = null;

		System.out.println("GestoreAccessi.ricercaCredenziali(" + username + ", " + password + ")\n");

		comandoSql = "SELECT * FROM Credenziali WHERE username = \"" + username + "\" AND password = \"" + password + "\" ;";
		credenziali=dbUtenti.query(comandoSql);

		if (credenziali.size()==0)
			esitoRicerca="erroreAssente";
		else
			if ((int)credenziali.get(0).get("attivo")==1)
				esitoRicerca="attivo";
			else
				esitoRicerca="erroreDisattivato";

		return esitoRicerca;
	}

	public HashMap<String, Object> ricercaUtente(String username) throws RemoteException
	{
		//RF00: login
    		//autore: Codetta

		String comandoSql, esitoRicerca;
		ArrayList<HashMap<String, Object>> utenti = null;
		HashMap<String, Object> utente=null;

		System.out.println("GestoreAccessi.ricercaUtente(" + username + ")\n");

		comandoSql = "SELECT * FROM Utente WHERE username = \"" + username + "\" ;";
		utenti = dbUtenti.query(comandoSql);

		if (utenti.size() == 1)
			utente = utenti.get(0);

		return utente;
	}

	public void bloccoUtente(String username) throws RemoteException {
		// RF20 BloccaSblocca
		// Autori: 20044037, 20039081

		String comandoSql;
		comandoSql = "UPDATE Credenziali SET attivo= false WHERE Credenziali.username= \"" + username+"\";";
		dbUtenti.update(comandoSql);
  }

	public void sbloccoUtente(String username) throws RemoteException {
		// RF20 BloccaSblocca
		// Autori: 20044037, 20039081

		String comandoSql;
		comandoSql = "UPDATE Credenziali SET attivo= true WHERE Credenziali.username= \"" + username+"\";";
		dbUtenti.update(comandoSql);
	}





	public int verificaCredenziali(String passwordAttuale, String password)throws RemoteException{

		//RF03: Aggiorna password
		//autori: Pietro Balossino, Andrija Jovic

		if(password.equals(passwordAttuale)){
			return 0;  //le password sono uguali
		}
		else {
			return 4;  //le password non coincidono
		}
	}

	public int controlloNuovaPassword (String nuovaPassword)throws RemoteException{

		//RF03: Aggiorna password
		//autori: Pietro Balossino, Andrija Jovic

		int len;
		boolean alpha;
		boolean num;


		len=nuovaPassword.length();
		if(len<6){
			return 1;	//lunghezza password<6
		}
		else {
			alpha=nuovaPassword.matches(".*[a-zA-Z].*");
			if(!alpha){
				return 2;	//la password non contiene una lettera
			}
			else {
				num=nuovaPassword.matches(".*[0-9].*");
				if(!num){
					return 3;	//la password non contiene un numero
				}
				else return 0;	//la password rispetta tutti i criteri
			}
		}
	}

	/**
	 * Controlla se l'utente ha inserito delle stringhe di nome e cognome valide.
	 * @param nome Il nome dell'utente che si sta registrando sul sistema informatico.
	 * @param cognome il cognome dell'utente che si sta registrando sul sistema informatico.
	 * @return ritorna 0 se il formato di nome e cognome sono corretti (senza numeri e con lunghezza >=3) 
	 * e se ambedue non contengono numeri. Altrimenti, viene ritornato 1 se il formato di nome è errato, 
	 * 2 se il formato di cognome è errato.
	 */
	public int controlloFormatoNomeCognome(String nome, String cognome) {
		int len1 = nome.length();
		int len2 = cognome.length();
		boolean bool1 = nome.matches(".*\\d.*");	//controlla se nome oppure cognome contengono un numero
		boolean bool2 = cognome.matches(".*\\d.*");
		
		if(len1>3 && !bool1)
		{
			if(len2>3 && !bool2)	return 0;
			else	return 2;
		}
		else
			return 1;
	}

	/**
	 * Funzione che aggiorna il database creando una nuova entry per l'utente designato da nome e cognome,
	 * con nickname "nome.cognome".
	 * @param nome nome dell'utente.
	 * @param cognome cognome dell'utente.
	 * 
	 * @throws RemoteException
	*/
	public void promptSalvaAccount(String nome, String cognome) throws RemoteException
	{
		dbUtenti.update("INSERT INTO 'main'.'Utente' ('username', 'nome', 'cognome') VALUES (\""+nome+'.'+cognome+"\", \""+nome+"\", \""+cognome+"\");");
	}
	
	/**
	 * Funzione necessaria per attivare l'account, settando il tipo utente dell'utente designato da nome
	 * e cognome, con nickname "nome.cognome".
	 * @param nome nome dell'utente.
	 * @param cognome cognome dell'utente.
	 * @param tipoUtente il tipo con cui l'utente verrà attivato.
	 * 
	 * @throws RemoteException
	*/
	public void richiestaAttivazioneAccount(String nome, String cognome, String tipoUtente) throws RemoteException
	{
		dbUtenti.update("UPDATE Utente SET tipo='"+tipoUtente+"' WHERE username='"+nome+'.'+cognome+"'");
	}
	
	/**
	 * Funzione necessaria per aggiungere delle credenziali da aggiornare per l'utente identificato da 
	 * username.
	 * @param username username dell'utente.
	 * 
	 * @throws RemoteException
	*/
	public void aggiuntaCredenziali(String username) throws RemoteException
	{
		dbUtenti.update("INSERT INTO Credenziali ('password', 'username', 'attivo') VALUES ('', '"+username+"', 1);");
	}

	public void AggiornaPassword(String username, String nuovaPassword) throws RemoteException {

		//RF03: Aggiorna password
		//autori: Pietro Balossino, Andrija Jovic

		String comandoSql;

		System.out.println("GestoreAccessi.AggiornaPassword(\""+ username + "\")");

		comandoSql = "UPDATE credenziali SET password=\"" + nuovaPassword + "\" WHERE username=\"" + username + "\" ;";
		dbUtenti.update(comandoSql);
	}

	/**
	 * Verifica se la stringa contiene solo caratteri dell'alfabeto, inclusi eventuali spazi e apostrofi.
	 *
	 * @param input La stringa da verificare.
	 * @return true se la stringa contiene solo caratteri dell'alfabeto, false altrimenti.
	 *
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	private static boolean controllaValiditaStringaAlfabetica(String input) {
		Pattern pattern = Pattern.compile("^[a-zA-Z'\\s]+$");
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}
	/**
	 * Verifica se la stringa rappresenta un numero intero positivo
	 *
	 * @param input La stringa da verificare.
	 * @return true se la stringa rappresenta un numero intero positivo, false altrimenti.
	 *
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	private static boolean isNumeroInteroPositivo(String input) {
		return input.matches("\\d+") && Integer.parseInt(input) > 0;
	}

	/**
	 * Verifica che la stringa contenga cifre (la prima cifra non può essere '0'), eventualmente seguite da lettere, senza altri numeri dopo le lettere
	 *
	 * @param input La stringa da verificare.
	 * @return true se la stringa rappresenta un civico valido, else altrimenti
	 *
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	private static boolean isValidoNumeroCivico(String input) {
		return input.equalsIgnoreCase("snc") || input.matches("[1-9][0-9]*[A-Za-z]?[A-Za-z]*");
	}

	/**
	 * Controlla il formato dei dati relativi a un domicilio.
	 *
	 * @param via       La via del domicilio.
	 * @param numero    Il numero civico del domicilio.
	 * @param cap       Il codice di postale del domicilio.
	 * @param localita  La località del domicilio.
	 * @return Un intero che rappresenta l'esito del controllo:
	 *         - 0 se tutti i campi sono corretti,
	 *         - 1 se il formato della via è errato,
	 *         - 2 se il formato del CAP è errato,
	 *         - 3 se il formato della località è errato,
	 *         - 4 se il formato del numero civico è errato,
	 *         - 5 se ci sono più campi errati.
	 *
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	public int controllaFormatoDomicilio(String via, String numero, String cap, String localita) {
		int esitoControllo;

		int lenVia = via.length();
		int lenCap = cap.length();
		int lenLocalita = localita.length();

		// controllo se tutti i campi sono corretti
		if (lenVia > 0 && controllaValiditaStringaAlfabetica(via) && isValidoNumeroCivico(numero) && lenCap == 5
				&& isNumeroInteroPositivo(cap) && lenLocalita > 0 && controllaValiditaStringaAlfabetica(localita))
			esitoControllo = 0;

			// controllo formato 'via'
		else if ((lenVia == 0 || !controllaValiditaStringaAlfabetica(via)) && isValidoNumeroCivico(numero) && lenCap == 5
				&& isNumeroInteroPositivo(cap) && lenLocalita > 0 && controllaValiditaStringaAlfabetica(localita))
			esitoControllo = 1;

			// controllo formato 'cap'
		else if (lenVia > 0 && controllaValiditaStringaAlfabetica(via) && isValidoNumeroCivico(numero) && (lenCap != 5
				|| !isNumeroInteroPositivo(cap)) && lenLocalita > 0 && controllaValiditaStringaAlfabetica(localita))
			esitoControllo = 2;

			//controllo formato 'localita'
		else if (lenVia > 0 && controllaValiditaStringaAlfabetica(via) && isValidoNumeroCivico(numero) && lenCap == 5
				&& isNumeroInteroPositivo(cap) && (lenLocalita == 0 || !controllaValiditaStringaAlfabetica(localita)))
			esitoControllo = 3;

			//controllo formato 'numero'
		else if (lenVia > 0 && controllaValiditaStringaAlfabetica(via) && !isValidoNumeroCivico(numero) && lenCap == 5
				&& isNumeroInteroPositivo(cap) && lenLocalita > 0 && controllaValiditaStringaAlfabetica(localita))
			esitoControllo = 4;

			//ci sono più campi errati
		else esitoControllo = 5;

		return esitoControllo;
	}

	/**
	 * Salva il domicilio di un utente nel database.
	 *
	 * @param username Nome utente dell'utente.
	 * @param via Nome della via.
	 * @param civico Numero civico.
	 * @param cap Codice di Avviamento Postale (CAP).
	 * @param localita Nome della località.
	 * @throws RemoteException Lanciata in caso di errore remoto.
	 *
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	public void promptSalvaDomicilio(String username, String via, String civico, String cap, String localita) throws RemoteException {

		DbUtenti dbUtenti = new DbUtenti();

		String selectQuery = "SELECT COUNT() FROM Utente WHERE Username = '" + username + "'";
		// numero di record prima dell'esecuzione del metodo
		int esiste = (int) dbUtenti.query(selectQuery).get(0).get("COUNT()");

		if (esiste != 0)
		{
			String Domicilio = via + "|" + civico + "|" + cap + "|" + localita;
			String updateQuery = "UPDATE Utente SET domicilio = '" + Domicilio + "' WHERE Username = '" + username + "'";
			dbUtenti.update(updateQuery);
		}
	}

	/**
	 * Recupera il domicilio di un utente dal database.
	 *
	 * @param username Nome utente dell'utente.
	 * @return Un array di stringhe contenente via, numero civico, CAP e località.
	 *         Se l'utente non ha un domicilio valido, restituisce un array vuoto.
	 * @throws RemoteException Lanciata in caso di errore remoto.
	 *
	 * RF24: AggiornamentoDomicilio
	 * Autore: Mondelli e Reci
	 */
	public String[] promptRecuperaDomicilio(String username) throws RemoteException {
		DbUtenti dbUtenti = new DbUtenti();

		String selectQuery = "SELECT domicilio FROM Utente WHERE Username = '" + username + "'";
		ArrayList<HashMap<String, Object>> result = dbUtenti.query(selectQuery);

		String domicilio = (String) result.get(0).get("domicilio");

		// Dividi il campo Domicilio in 4 parti
		String[] partiDomicilio = domicilio.split("\\|");

		if (partiDomicilio.length == 4) {
			return partiDomicilio;
		} else {
			return new String[0];
		}
	}
}
