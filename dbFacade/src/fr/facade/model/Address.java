package fr.facade.model;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class Address implements Serializable{

	private Integer id 				= null;
	private Integer streetNumber 	= null;
	private String streetName	= null;
	private String city			= null;
	private String zipCode		= null;
	private String country		= null;
	private Date createdOn 		= null;
	private Date updatedOn 		= null;
	
	public Address() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(Integer streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", streetNumber=" + streetNumber + ", streetName=" + streetName + ", city=" + city
				+ ", zipCode=" + zipCode + ", country=" + country + ", createdOn=" + createdOn + ", updatedOn="
				+ updatedOn + "]";
	}
	
}
