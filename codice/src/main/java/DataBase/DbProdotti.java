package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement; 
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.ArrayList;

public class DbProdotti implements DbProdottiInterfaccia
{
	public DbProdotti()
	{
	}
	
	public ArrayList<HashMap<String, Object>> query(String comandoSql)
	{
		Connection conn;
		Statement stmt;
		ResultSet rs;
		ArrayList<HashMap<String, Object>> list = null;
		int columns;
		HashMap<String, Object> row;
		ResultSetMetaData md;

		System.out.println("DbProdotti: " + comandoSql + "\n");

		try 
		{
           		conn = DriverManager.getConnection("jdbc:sqlite:dati/DbProdotti.db");		
	    		stmt = conn.createStatement();
    			rs = stmt.executeQuery(comandoSql);
			md = rs.getMetaData();
			columns = md.getColumnCount();
			list = new ArrayList<HashMap<String, Object>>();
			while (rs.next())
			{
				row = new HashMap<String, Object>(columns);
				for(int i=1; i<=columns; ++i)
					row.put(md.getColumnName(i), rs.getObject(i));
				list.add(row);
			}
			rs.close();
			stmt.close();
			conn.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		return list;
	}

	public void update(String comandoSql)

	{
		Connection conn;
		Statement stmt;

		System.out.println("DbProdotti: " + comandoSql + "\n");

		try
		{
			conn = DriverManager.getConnection("jdbc:sqlite:dati/DbProdotti.db");
			stmt = conn.createStatement();
			stmt.executeUpdate(comandoSql);
			stmt.close();
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<HashMap<String, Object>> ricercaProdotto(String titolo, String autore, String editore,
            String anno, String tipo) {
        ArrayList<HashMap<String, Object>> risultati = null;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:dati/DbProdotti.db")) {
            String comandoSql = "SELECT * FROM Prodotto WHERE 1=1 ";

            if (titolo != null && !titolo.isEmpty())
                comandoSql += "AND Titolo LIKE ? ";
            if (autore != null && !autore.isEmpty())
                comandoSql += "AND Autore LIKE ? ";
            if (editore != null && !editore.isEmpty())
                comandoSql += "AND Editore LIKE ? ";
            if (anno != null && !anno.isEmpty())
                comandoSql += "AND Anno = ? ";
            if (tipo != null && !tipo.isEmpty())
                comandoSql += "AND Tipo LIKE ? ";
            /* qui metti gli altri */
            
            System.out.println("DbProdotti.ricercaProdotto: " + comandoSql + "\n");

            try (PreparedStatement pstmt = conn.prepareStatement(comandoSql)) {
                int numeroColonna = 1;
              

                if (titolo != null && !titolo.isEmpty())
                    pstmt.setString(numeroColonna++, "%" + titolo + "%");  
                if (autore != null && !autore.isEmpty())
                    pstmt.setString(numeroColonna++, "%" + autore + "%");
                if (editore != null && !editore.isEmpty())
                    pstmt.setString(numeroColonna++, "%" + editore + "%");
                if (anno != null && !anno.isEmpty())
                    pstmt.setString(numeroColonna++, anno );
                if (tipo != null && !tipo.isEmpty())
                    pstmt.setString(numeroColonna++, "%" + tipo + "%");
                
                /**
                 * SELECT * FROM Prodotti WHERE 1=1 AND anno = 2020
                 * 
                 * ResultSet rs = {
                 *    'il mondo di oggi','MJornad','MG','2020','boh'
                 *    'coccodrillo','MJornad','MG','2020','boh'
                 * }
                 */
                

                try (ResultSet rs = pstmt.executeQuery()) {
                    int colonne = rs.getMetaData().getColumnCount();
                    risultati = new ArrayList<HashMap<String, Object>>();

                    while (rs.next()) {
                        HashMap<String, Object> riga = new HashMap<>();

                        for (int i = 1; i <= colonne; i++) {
                        	//<'titolo':'il mondo di oggi'>
                            riga.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                        }

                        risultati.add(riga);
                        
                        /*   cerco per anno 2020 e ho trovato 2 record:
                         *   list = {
                         *       (<'titolo':'il mondo di oggi'>,<'autore': 'MJornad'>,<'editore':'MG'>,<'anno':'2020'>,<'tipo':'boh'>),
                         *       (<'titolo':'coccodrillo'>,<'autore': 'MJornad'>,<'editore':'MG'>,<'anno':'2020'>,<'tipo':'boh'>),
                         *   
                         *   }
                         * 
                         */
                    }
                }
                
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return risultati;
    }

}
