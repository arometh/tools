package sn.arometh.notification.business;

import java.util.List;

import sn.arometh.notification.entity.Product;
public interface Business {
    
    /***
     * Retourne l'ensemble des produits de la base
     * @return
     */
    public List<Product> getListProduct();
}
