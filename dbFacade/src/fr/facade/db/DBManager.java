package fr.facade.db;

import java.sql.*;

import fr.facade.config.DBConfig;

public class DBManager implements DBConfig {

	// Objects spEcifiques aux differents appels vers les bases (locales ou distantes).
	public Connection myConnect;
	public Statement myState;
	public ResultSet myResultSet;

	// Objects de Meta-Information sur la Database connecte, et sur la requete effectuee.
	public DatabaseMetaData myDbMetaData;

//	public String arrayContent[][];
//	public String arrayHeader[];
	public String strConnectURL;

	public DBManager() {}
	
	public void dbConnect()
	{
		try
		{	
			//1ere etape: Chargement de la classe de driver, responsable - par contrat d'interfaces - de la connection vers le SGBD
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("Driver Loaded...");
			
			//2eme etape: Definition de l'URL de connection
 			strConnectURL				= "jdbc:mysql://"+ DB_HOST+":" + DB_PORT + "/" + DB_NAME;
 			System.out.println(strConnectURL);
			
 			//3eme etape: Creation de l'object de connection
 			myConnect 					= DriverManager.getConnection(strConnectURL, DB_USER, DB_PWD);//login & password
			System.out.println("Connection to database etablished...");
			
			//Option: Acces a un jeu de meta information sur la base avec laquelle on dialogue.
			myDbMetaData 				= myConnect.getMetaData();

			System.out.println("DbManager: dbConnect: show DataBase MetaData:");
			System.out.println("DbManager: dbConnect: productName=" 	+ myDbMetaData.getDatabaseProductName());
			System.out.println("DbManager: dbConnect: productVersion=" 	+ myDbMetaData.getDatabaseProductVersion());
			//etc... de nombreuses autres info sont disponibles
			
			//4eme etape: creation d'une instruction/formule, socle pour executer des requetes
			myState						= myConnect.createStatement();

		}
		catch (ClassNotFoundException e) 	{System.out.println("dbConnect ClassNotFoundException: " + e.toString()); e.printStackTrace();}	
		catch (SQLException e) 				{System.out.println("dbConnect SQLException: " + e.toString()); e.printStackTrace();}	
		catch (Exception e) 					{System.out.println("dbConnect Exception: " + e.toString()); 	e.printStackTrace();}	
	}
	
	public ResultSet doQuery(String query)
	{
		try{
			return myState.executeQuery(query);
		}
		catch (Exception e)
		{
			System.out.println(e.getCause() + ": " + e.getMessage());
		}
		return null;
	}
	
	public boolean doUpdate(String query)
	{
		try{
			return myState.execute(query);
		}
		catch (Exception e)
		{
			System.out.println(e.getCause() + ": " + e.getMessage());
		}
		return false;
	}
	
	public boolean doDelete (String query)
	{
		try{
			return myState.execute(query);
		}
		catch (Exception e)
		{
			System.out.println(e.getCause() + ": " + e.getMessage());
		}
		return false;
	}
	
	public void dbDisconnect()
	{
		try {
			myState.close();
		}
		catch (java.sql.SQLException e)	{
			System.out.println("dbDisconnect: close statement: " + e.toString());
		}
		catch (Exception e)	{
			System.out.println("dbDisconnect: close statement: " + e.toString());
		}		
		
		try {
			myConnect.close();
		}
		catch (java.sql.SQLException e)	{
			System.out.println("dbDisconnect: close statement: " + e.toString());
		}
		catch (Exception e)	{
			System.out.println("dbDisconnect: close connection: " + e.toString());
		}		
	}
	
	//////////////////
	//MISC
	/////////////////
	
	public static String escapeQuote(String strIn)
	{	
		if (strIn == null)
			return "";
		
		String strOut		= "";
		int strLength		= strIn.length();
				
		for (int i = 0; i != strLength; i++)
			strOut			+= (strIn.substring(i, i + 1).equalsIgnoreCase("'"))? "''" : strIn.substring(i, i + 1);
		
		return strOut;
	}	

	
}
