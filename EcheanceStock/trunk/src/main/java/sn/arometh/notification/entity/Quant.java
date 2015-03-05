package sn.arometh.notification.entity;

import java.util.Date;

public class Quant extends Entity {
	
	/** Produit */
	private Product product;
	
	/** emplacement */
	private Location emplacement;
	
	/** Quantité en stock */
	private Double quantite;
	
	/** lot ID */
	private Integer lotID;
	
	/** date de creation */
	private Date inDate;

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return the emplacement
	 */
	public Location getEmplacement() {
		return emplacement;
	}

	/**
	 * @param emplacement the emplacement to set
	 */
	public void setEmplacement(Location emplacement) {
		this.emplacement = emplacement;
	}

	/**
	 * @return the quantite
	 */
	public Double getQuantite() {
		return quantite;
	}

	/**
	 * @param quantite the quantite to set
	 */
	public void setQuantite(Double quantite) {
		this.quantite = quantite;
	}

	/**
	 * @return the lotID
	 */
	public Integer getLotID() {
		return lotID;
	}

	/**
	 * @param lotID the lotID to set
	 */
	public void setLotID(Integer lotID) {
		this.lotID = lotID;
	}

	/**
	 * @return the inDate
	 */
	public Date getInDate() {
		return inDate;
	}

	/**
	 * @param inDate the inDate to set
	 */
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	
	@Override
	public String toString() {
	    return "Quant : [ ID => " + this.getId() + ", " + this.getProduct() + ", Quantité => " + this.getQuantite() + ", Date In => " + this.getInDate() + ", lot ID => " + this.getLotID() + "]";
	}
}
