package sn.arometh.notification.business;

import java.util.List;

import sn.arometh.notification.entity.Location;
import sn.arometh.notification.entity.Product;
import sn.arometh.notification.entity.Stock;
public interface Business {
    
    /***
     * Retourne l'ensemble des produits de la base
     * @return
     */
    public List<Product> getListProduct();
    
    /**
     * Retourne un produit 
     * @param productID
     * @return
     */
    public Product getProductByID(Integer productID);
    
    /**
     * Reoutne l'ensemble des produits du stock
     * @return
     */
    public List<Stock> getListStock();
    
    /**
     * Retourne un emplacement
     * @param locationID
     * @return
     */
    public Location getLocationByID(Integer locationID);
    
}
