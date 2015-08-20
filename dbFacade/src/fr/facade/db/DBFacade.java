package fr.facade.db;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class DBFacade implements IDBFacade {

	private DBFactory dbf;
	private DBManager myDbManager;

	public DBFacade(DBManager dbm) {
		setMyDbManager(dbm);
	}

	@Override
	public boolean create(Serializable object) {
		Class<?> objectClass 			= object.getClass();
		List<Object> properties 		= this.getObjectProperties(object, true);
		List<Object> values 			= this.getObjectValues(object, properties);
		
		if (properties == null || values == null)
			return (false);

		String tableName 		= objectClass.getName().substring(objectClass.getName().lastIndexOf(".") + 1).toLowerCase();
		tableName 				= tableName.substring(0, 1).toUpperCase().concat(tableName.substring(1, tableName.length()));
		String propertiesStr 	= this.makeQueryString(properties, true);
		String valuesStr 		= this.makeQueryString(values, false);
		
		// Getting query
		String query = "INSERT INTO `" + tableName + "` " + "(" + propertiesStr + ") " + "VALUES (" + valuesStr + ")";
//
		Logger.getAnonymousLogger().info(query);

		// Executing query
		try {
			this.myDbManager.doUpdate(query);
			return (true);
		} catch (Exception e) {
			Logger.getLogger("Epimarket").warning("MySQL error : " + e);
		}
		return (false);
	}

	
	@Override
	public List<Serializable> read(Serializable object) {
		Class<?> objectClass 	= object.getClass();
		String  tableName 		= objectClass.getName().
									substring(objectClass.getName().lastIndexOf(".") + 1).toLowerCase();
		String  query = "SELECT * FROM `" + tableName + "`";
        
        try {
            return (this.getObjectFromResultSet(objectClass, this.myDbManager.doQuery(query)));
        }
        catch (Exception e)
        {
                 Logger.getLogger("Epimarket").warning("Invoke error : " + e);
        }
        return (null);

	}

	@Override
	public boolean update(Serializable object) {
		Class<?> objectClass 			= object.getClass();
		List<Object> properties 		= this.getObjectProperties(object, false);
		List<Object> values 			= this.getObjectValues(object, properties);
		
		if (properties == null || values == null)
			return (false);

		String tableName 		= objectClass.getName().substring(objectClass.getName().lastIndexOf(".") + 1).toLowerCase();

		// Getting query
		String query = "UPDATE `" + tableName + "` SET" ; 
		int j = 0;
		int k = 0;
		String value = " `";
		while (j < properties.size() && k <values.size()) {
			 value += properties.get(j) + "` = '" + values.get(k) + "', `";
			j++;
			k++;
		}
		value = value.substring(0, value.length() - ", `".length());
		
		query += value +" WHERE "+ properties.get(0) + " = " + values.get(0);
		
		System.out.println("update query = " + query);
		
		Logger.getAnonymousLogger().info(query);
		
		// Executing query
		try {
			this.myDbManager.doUpdate(query);
			return (true);
		} catch (Exception e) {
			Logger.getLogger("Epimarket").warning("MySQL error : " + e);
		}
		return false;

	}

	@Override
	public boolean delete(Serializable object) {
		
		Class<?> objectClass 			= object.getClass();
		List<Object> properties 		= this.getObjectProperties(object, false);
		List<Object> values 			= this.getObjectValues(object, properties);

		if (properties == null || values == null)
			return (false);

		String tableName 		= objectClass.getName().substring(objectClass.getName().lastIndexOf(".") + 1).toLowerCase();
		System.out.println(object.toString());
		// Getting query
		String query = "DELETE FROM `" + tableName + "`" + " WHERE `" + properties.get(0) + "` = " + values.get(0);
		System.out.println("delete query = " + query);
		// Executing query
		try {
			this.myDbManager.doDelete(query);
			return (true);
		} catch (Exception e) {
			Logger.getLogger("Epimarket").warning("MySQL error : " + e);
		}
		return (false);
	}

	public void setMyDbManager(DBManager myDbManager) {
		this.myDbManager = myDbManager;
	}

	
	private List<Object> getObjectProperties(Serializable object, boolean skipFirst) {
		int propertiesCnt = object.getClass().getDeclaredFields().length - ((skipFirst) ? 1 : 0);
		Object[] properties = new Object[propertiesCnt];
		int cnt = 0;
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (!skipFirst || cnt != 0)
				properties[cnt - ((skipFirst) ? 1 : 0)] = field.getName();
			++cnt;
		}
		return (Arrays.asList(properties));
	}

	private List<Object> getObjectValues(Serializable object, List<Object> properties) {
		ArrayList<Object> values = new ArrayList<Object>();
		for (Object property : properties) {
			String prop = property.toString();
			try {
				values.add(object.getClass().getMethod(
						"get" + prop.substring(0, 1).toUpperCase() + prop.substring(1, prop.length()))
						.invoke(object));
			} catch (Exception e) {
				Logger.getLogger("Epimarket").warning("Property " + property
						+ " could not be retrieved. Object not synchronized in database. " + e.toString());
				return (null);
			}
		}
		return (values);
	}

	private String makeQueryString(List<Object> values, boolean withBackquotes) {
		StringBuilder propertiesStr = new StringBuilder();
		String quote = (withBackquotes) ? "`" : "\'";
		int cnt = 0;
		for (Object value : values) {
			if (cnt != 0)
				propertiesStr.append(quote + ", " + quote);
			propertiesStr.append(value);
			++cnt;
		}
		return (quote + propertiesStr.toString() + quote);
	}

	private List<Serializable> getObjectFromResultSet(Class<?> className, ResultSet resultSet)
			throws Exception {
		
		List<Serializable>	list= new ArrayList<Serializable>();
		while (resultSet.next()) //incremente aussi l'index pour la lecture des donnees
		{
			Object result 						  = className.newInstance();
			ResultSetMetaData myResultSetMetaData = resultSet.getMetaData();
			int nbrColumn						  = myResultSetMetaData.getColumnCount();

			for (int cnt = 1; cnt <= nbrColumn; ++cnt) {
				String columnName 		= myResultSetMetaData.getColumnName(cnt);
				Class<?> columnClass 	= Class.forName(myResultSetMetaData.getColumnClassName(cnt));
                Class<?>[] args =  {columnClass};
                try {
                	
                	className.
						getMethod("set" +
									columnName.substring(0, 1).toUpperCase() +
									columnName.substring(1, columnName.length())   
								, args).
						invoke(result, resultSet.getObject(cnt));
				} catch (NoSuchMethodException e) {
					Logger.getLogger("Epimarket").warning("Invoke error : " + e);
				}
			}
			result.toString();
			list.add((Serializable) result);
		}
		return (list);
	}

}
