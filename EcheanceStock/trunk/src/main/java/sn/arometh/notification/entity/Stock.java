package sn.arometh.notification.entity;

import java.io.Serializable;

public class Stock extends Entity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** Produit */
	private Product product;
	
	
	/** emplacement du produit dans le stock */
	private Location emplacementSource;
	
	/** emplacement du produit dans le stock */
    private Location emplacementDestination;
    
	/** quantite du stock */
	private Double quantityStock;
	
	/** date d'expiration du stock */
	private String dateExpirationStock;
	
	/**
     * @return the nomProduit
     */
    public Product getProduct() {
        return product;
    }
    /**
     * @param pNomProduit the nomProduit to set
     */
    public void setProduct(Product pProduct) {
        product = pProduct;
    }
    /**
     * @return the emplacement
     */
    public Location getEmplacementSource() {
        return emplacementSource;
    }
    /**
     * @param pEmplacement the emplacement to set
     */
    public void setEmplacementSource(Location pEmplacementSource) {
        emplacementSource = pEmplacementSource;
    }
        
    /**
     * @return the emplacementDestination
     */
    public Location getEmplacementDestination() {
        return emplacementDestination;
    }
    /**
     * @param pEmplacementDestination the emplacementDestination to set
     */
    public void setEmplacementDestination(Location pEmplacementDestination) {
        emplacementDestination = pEmplacementDestination;
    }
    /**
     * @return the quantityStock
     */
    public Double getQuantityStock() {
        return quantityStock;
    }
    /**
     * @param pQuantityStock the quantityStock to set
     */
    public void setQuantityStock(Double pQuantityStock) {
        quantityStock = pQuantityStock;
    }
    /**
     * @return the dateExpirationStock
     */
    public String getDateExpirationStock() {
        return dateExpirationStock;
    }
    /**
     * @param pDateExpirationStock the dateExpirationStock to set
     */
    public void setDateExpirationStock(String pDateExpirationStock) {
        dateExpirationStock = pDateExpirationStock;
    }
	
	@Override
	public String toString() {
	    return "Stock : [ID => " + this.getId() + ", NOM => " + this.getName() + ", " + this.getProduct() + ", Date expiration Stock => " + this.getDateExpirationStock() + ", QUANTITE => " + this.getQuantityStock() + ", EMPLACEMENT SOURCE => " + this.getEmplacementSource() + ", EMPLACEMENT DESTINATION => " + this.getEmplacementDestination() + "]";
	}
	
}
