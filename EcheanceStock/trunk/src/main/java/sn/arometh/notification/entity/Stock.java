package sn.arometh.notification.entity;

import java.io.Serializable;

public class Stock implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** Nom du produit */
	private String nomProduit;
	/** emplacement du produit dans le stock */
	private String emplacement;
	/** quantite du stock */
	private String quantityStock;
	/** date d'expiration du stock */
	private String dateExpirationStock;
    /**
     * @return the nomProduit
     */
    public String getNomProduit() {
        return nomProduit;
    }
    /**
     * @param pNomProduit the nomProduit to set
     */
    public void setNomProduit(String pNomProduit) {
        nomProduit = pNomProduit;
    }
    /**
     * @return the emplacement
     */
    public String getEmplacement() {
        return emplacement;
    }
    /**
     * @param pEmplacement the emplacement to set
     */
    public void setEmplacement(String pEmplacement) {
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
