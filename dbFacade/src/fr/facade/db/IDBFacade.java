package fr.facade.db;

import java.io.Serializable;
import java.util.List;

public interface IDBFacade {
	
	public boolean create(Serializable o);
	public List<Serializable> read(Serializable o);
	public boolean update(Serializable o);
	public boolean delete(Serializable o);
	
}
