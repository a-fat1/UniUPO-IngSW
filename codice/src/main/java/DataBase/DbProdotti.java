package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
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
	{	// Codetta
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
	{	// Codetta
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
}
