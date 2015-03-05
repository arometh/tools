package sn.arometh.notification.business;

import java.util.List;

//import sn.arometh.notification.entity.Location;
import sn.arometh.notification.entity.Product;
//import sn.arometh.notification.entity.Stock;
public interface Business {
    
    /***
     * Retourne l'ensemble des produits de la base
     * @return
     */
    //public List<Product> getListProduct();
    
    /**
     * Retourne un produit 
     * @param productID
     * @return
     */
    //public Product getProductByID(Integer productID);
    
    /**
     * Reoutne l'ensemble des produits du stock
     * @return
     */
    //public List<Stock> getListStock();
    
    /**
     * Retourne un emplacement
     * @param locationID
     * @return
     */
    //public Location getLocationByID(Integer locationID);
    
	/**
	 * Retourne l'ensemble des produits dont la quantité est inferieure a la quantite minimal du stock
	 * @return
	 */
	public List<Product> getProductAlertQuantStock();
	
	/**
	 * Retourne l'ensemble des produits dont la date est fixée dans le stock approche de la date du jour
	 * @return
	 */
	public List<Product> getProductAlertOutOfDate();
}
