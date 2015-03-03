package sn.arometh.notification.business;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import sn.arometh.notification.commons.ConstantFunctionnals;
import sn.arometh.notification.dao.DAOImpl;
import sn.arometh.notification.entity.Stock;

public class Business implements ConstantFunctionnals {	
	private static Logger logger = Logger.getLogger(Business.class);		
	private DAOImpl dao ;
	
	/** Constructeur */
	public Business(boolean isTest) throws ClassNotFoundException, SQLException {
		dao = new DAOImpl(isTest);
	}
	
	/**
	 * retour un string contenant les colonnes des stocks et entete séparé par des ;
	 * @return
	 */
	public String rechercherStockAlert() {
	  //Nom Produit;Emplacement;Quantite;Date d'expiration
	   String retourFormatStock = null;
	   try {
    	   List<Stock> stockAlert = dao.searchStockAlert();
    	   if(null != stockAlert && !stockAlert.isEmpty()){
    	       retourFormatStock = VAR_NOTIFICATION_BUSINESS_ENTETE_MESSAGE_STOCK_ALERT;
        	   for (Stock stock : stockAlert) {	           	       
        	       //retourFormatStock += System.getProperty("line.separator") + stock.getNomProduit() + ";" + stock.getEmplacement() + ";" + stock.getQuantityStock() + ";" + stock.getDateExpirationStock(); 
        	   }
    	   }else {
    	       retourFormatStock += System.getProperty("line.separator") + VAR_NOTIFICATION_BUSINESS_RAS_STOCK_MESSAGE_ALERT;
    	   }
	   }catch(Exception e) {
	       logger.error("Error", e);
	   }
	   return retourFormatStock; 	    	   
	}
	
	public static void main(String[] args) {
        try {
            Business business = new Business(true);
            System.out.println(business.rechercherStockAlert());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
