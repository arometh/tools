package sn.arometh.notification.business;

import java.util.List;
import sn.arometh.notification.commons.ConstantFunctionnals;
import sn.arometh.notification.entity.Product;

public interface Business extends  ConstantFunctionnals {
    
	/**
	 * Retourne l'ensemble des produits dont la quantité est inferieure a la quantite minimal du stock
	 * @return
	 */
	public List<Product> getProductAlertQuantStock();
	
	/**
	 * Retourne un message formatté à partir d'une liste de produit
	 * 
	 * @param pProduct
	 * @return
	 */
	public String formatListToMessage(List<Product> pProduct);
	
	/**
	 * Retourne l'ensemble des produits dont la date est fixée dans le stock approche de la date du jour
	 * @return
	 */
	public List<Product> getProductAlertOutOfDate();
		
}
