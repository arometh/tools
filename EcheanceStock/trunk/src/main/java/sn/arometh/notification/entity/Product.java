package sn.arometh.notification.entity;

public class Product extends Entity {
	
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
	@Override
	public String toString() {	    
	    return "Produit : [ ID => " + this.getId() + ", NOM => " + this.getName() + ", VARIANTE => " + this.getVariante() + " ]";
	}
}
