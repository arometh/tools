package sn.arometh.notification.business;

import java.text.ParseException;
import java.util.List;

import sn.arometh.notification.commons.ConstantFunctionnals;
import sn.arometh.notification.entity.Entity;
import sn.arometh.notification.entity.Product;
import sn.arometh.notification.entity.Stock;

public interface Business extends  ConstantFunctionnals {
    
	/**
	 * Retourne l'ensemble des produits dont la quantit� est inferieure a la quantite minimal du stock
	 * @return
	 */
	public List<Product> getProductAlertQuantStock();
	
	/**
	 * Retourne un message formatt� � partir d'une liste de produit
	 * 
	 * @param pProduct
	 * @return
	 */
	public String formatListToMessageProduct(List<Product> pProduct);
	
	/**
	 * Retourne un message formatt� � partir d'une liste de produit
	 * 
	 * @param pProduct
	 * @return
	 */
	public String formatListToMessageStock(List<Stock> pProduct);
	
	/**
	 * Retourne l'ensemble des produits dont la date est fix�e dans le stock approche de la date du jour
	 * @return
	 * @throws ParseException 
	 */
	public List<Stock> getProductAlertOutOfDate() throws ParseException;
		
}
