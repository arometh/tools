package sn.arometh.notification.business;

import java.util.List;

import sn.arometh.notification.entity.Product;
import sn.arometh.notification.entity.Stock;
public interface Business {
    
    /***
     * Retourne l'ensemble des produits de la base
     * @return
     */
    public List<Product> getListProduct();
    
    /**
     * Reoutne l'ensemble des produits du stock
     * @return
     */
    public List<Stock> getListStock();
}
