package fr.facade.test;

import java.util.List;
import java.io.Serializable;

import java.sql.Date;

import fr.facade.db.DBFacade;
import fr.facade.db.DBManager;
import fr.facade.model.Address;

/** 
 * @author Dimitri Dean DARSEYNE (D3), 
 * Published by Short-Circuit under Creative Commons (CC) Licensing: 
 * Authorship/Paternity, NO Commercial Use, NO Derivative
 * Please check for more informations:
 * http://creativecommons.org/licenses/by-nc-nd/2.0/
 *
 * Auteur Dimitri Dean DARSEYNE (D3),
 * Publié par Short-Circuit sous license Creative Commons (CC):
 * Paternité, PAS d'Utilisation Commerciale, pas de Dérivés/Modifications
 * Pour plus d'informations, se rendre sur:
 * http://creativecommons.org/licenses/by-nc-nd/2.0/fr/ 
 * 
 * @since Short-Circuit 1999
 */


public class Main
{
	//Connecteur à la base de donnée
	public DBManager 				dbManager;
	
	public static void main(String args[])
	{		
		//Main newAppli			= new Main();
		DBManager dm	= new DBManager();
		dm.dbConnect();
		DBFacade facade = new DBFacade(dm);
		
		Address a = new Address();
		a.setStreetNumber(122);
		a.setStreetName("qwerty aswqed azerty");
		a.setCity("city2");
		a.setZipCode("22345");
		a.setCountry("country2");
		a.setCreatedOn(new Date(System.currentTimeMillis()));
		a.setUpdatedOn(new Date(System.currentTimeMillis()));
		
		System.out.print("create Object : ");
		System.out.println(facade.create(a) ? "SUCCESS" :"FAIL");

		System.out.println("\n\nread Object : ");
		List<Serializable> list = facade.read(a);
		for(Serializable s : list) {
			System.out.println(s.toString() + "\n");
		}
		
		System.out.print("\nupdate Object : \n");
		((Address) list.get(0)).setStreetNumber(123);
		((Address) list.get(0)).setStreetName("zxwerty qwerty aswqed azerty");
		((Address) list.get(0)).setCity("cityQ");
		
		System.out.println(((Address) list.get(0)).toString());
		System.out.println(facade.update(list.get(0)) ? "SUCCESS" :"FAIL");
	
		System.out.print("delete Object : ");
		System.out.println(facade.delete(list.get(0)) ? "SUCCESS" :"FAIL");
		dm.dbDisconnect();
	}
	
		
}
