package sn.arometh.notification.entity;

public class Product extends Entity {
	
	/** Quantite en stock du produit en temps reel */
	private Double qtyOnHand;
	
	/** variante du produit */
	private String variante;

	/**
	 * @return the variante
	 */
	public String getVariante() {
		return variante;
	}

	/**
	 * @param variante the variante to set
	 */
	public void setVariante(String variante) {
		this.variante = variante;
	}
	
	/**
	 * constructeur
	 */
	public Product() {
	}
	
	/**
	 * Constructeur
	 * @param pID
	 * @param pName
	 */
	public Product(Integer pID, String pName) {
		super(pID, pName);
	}
	
	/**
	 * Constructeur
	 * @param pID
	 * @param pName
	 * @param pVariante
	 */
	public Product(Integer pID, String pName, String pVariante) {
		super(pID, pName);
		variante = pVariante;
	}
	
	
	/**
	 * @return the qtyOnHand
	 */
	public Double getQtyOnHand() {
		return qtyOnHand;
	}

	/**
	 * @param qtyOnHand the qtyOnHand to set
	 */
	public void setQtyOnHand(Double qtyOnHand) {
		this.qtyOnHand = qtyOnHand;
	}

	@Override
	public String toString() {	    
	    return "Produit : [ ID => " + this.getId() + ", NOM => " + this.getName() + ", Quanity On Hand => " + this.getQtyOnHand()+ ", VARIANTE => " + this.getVariante() + " ]";
	}
}
