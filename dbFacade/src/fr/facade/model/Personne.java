package fr.facade.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Personne implements Serializable {

	private Integer id;
	private String nom;
	
	public Personne(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() {
		return "Personne [id=" + id + ", nom=" + nom + "]";
	}
	
}