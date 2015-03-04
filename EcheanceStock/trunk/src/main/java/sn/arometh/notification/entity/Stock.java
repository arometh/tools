package sn.arometh.notification.entity;

import java.io.Serializable;

public class Stock extends Entity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** Produit */
	private Product product;
	
	
	/** emplacement du produit dans le stock */
	private Location emplacement;
	
	/** quantite du stock */
	private String quantityStock;
	
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
    public Location getEmplacement() {
        return emplacement;
    }
    /**
     * @param pEmplacement the emplacement to set
     */
    public void setEmplacement(Location pEmplacement) {
        emplacement = pEmplacement;
    }
    /**
     * @return the quantityStock
     */
    public String getQuantityStock() {
        return quantityStock;
    }
    /**
     * @param pQuantityStock the quantityStock to set
     */
    public void setQuantityStock(String pQuantityStock) {
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
	
	
	
}
