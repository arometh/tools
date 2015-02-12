package sn.arometh.notification.entity;

import java.io.Serializable;

public class Stock implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String idStock;
	private float quantityStock;
	private String dateExpirationStock;
	private String nameStock;
	private String emplacement;
	
	
	public String getIdStock() {
		return idStock;
	}
	public void setIdStock(String idStock) {
		this.idStock = idStock;
	}
	public float getQuantityStock() {
		return quantityStock;
	}
	public void setQuantityStock(float quantityStock) {
		this.quantityStock = quantityStock;
	}
	public String getDateExpirationStock() {
		return dateExpirationStock;
	}
	public void setDateExpirationStock(String dateExpirationStock) {
		this.dateExpirationStock = dateExpirationStock;
	}
	public String getNameStock() {
		return nameStock;
	}
	public void setNameStock(String nameStock) {
		this.nameStock = nameStock;
	}
	public String getEmplacement() {
		return emplacement;
	}
	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}
	
	
	
}
