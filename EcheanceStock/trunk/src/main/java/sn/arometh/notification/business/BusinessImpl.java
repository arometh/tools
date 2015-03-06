package sn.arometh.notification.business;


import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

import sn.arometh.notification.entity.Location;
import sn.arometh.notification.entity.Product;
import sn.arometh.notification.entity.Quant;
import sn.arometh.notification.entity.Stock;
import sn.arometh.notification.enumeration.EnumLocation;
import sn.arometh.notification.enumeration.EnumProduct;
import sn.arometh.notification.enumeration.EnumQuant;
import sn.arometh.notification.enumeration.EnumStock;
import sn.arometh.notification.odoo.Odoo;
import sn.arometh.notification.odoo.OdooDomain;
import sn.arometh.notification.odoo.OdooRecord;
import sn.arometh.notification.odoo.OdooRecordSet;

public class BusinessImpl implements Business {	
	private static Logger logger = Logger.getLogger(BusinessImpl.class);		
	
	private Odoo odoo;
	
	/** Constructeur 
	 * @throws XmlRpcException 
	 * @throws MalformedURLException */
	public BusinessImpl() throws MalformedURLException, XmlRpcException {
	    odoo = new Odoo(VAR_NOTIFICATION_REST_MACHINE, VAR_NOTIFICATION_REST_DATABASE, VAR_NOTIFICATION_REST_USER, VAR_NOTIFICATION_REST_PASSWORD);
	 }

    /**
     * @return the odoo
     */
    public Odoo getOdoo() {
        return odoo;
    }

    /**
     * @param pOdoo the odoo to set
     */
    public void setOdoo(Odoo pOdoo) {
        odoo = pOdoo;
    }
	
    /**
     * Recupere la liste des produits du stock
     * @return
     */
    @SuppressWarnings("unused")
    private List<Product> getListProduct(){
        List<Product> produits = null;
        Object[] productIds = odoo.search("product.product");
        OdooRecordSet resultProducts = odoo.readRecords("product.product", productIds, new String[] { "name_template", "categ_id" });

        if(null != resultProducts){
            produits = new ArrayList<Product>();            
            Product produit;
            Iterator<OdooRecord> lineRecordIteratorProduct = resultProducts.iterator();
            while (lineRecordIteratorProduct.hasNext()) {
                OdooRecord lineRecordProduct = lineRecordIteratorProduct.next();
                HashMap<String, Object> mapLineProduct = lineRecordProduct.getRecord();
                produit = new Product();
                for (Map.Entry<String, Object> entryLineProduct : mapLineProduct.entrySet()){                    
                    if(EnumProduct.PRODUCT_ID.getValue().equals(entryLineProduct.getKey())){
                        produit.setId((Integer)entryLineProduct.getValue());
                    }else if(EnumProduct.PRODUCT_NAME.getValue().equals(entryLineProduct.getKey())){
                        produit.setName((String)entryLineProduct.getValue());
                    }else if(EnumProduct.PRODUCT_VARIANTE.getValue().equals(entryLineProduct.getKey())){
                        produit.setVariante((String) entryLineProduct.getValue());
                    }
                }    
                produits.add(produit);
            }
        }
        
        
        return produits;
    }
    
    /**
     * Recupére la liste des stock
     * @param LOCATIONID
     * @param LOCATIONDESTID
     * @return
     */
    @SuppressWarnings("unused")
    private List<Stock> getListStock(Integer LOCATIONID, Integer LOCATIONDESTID) {
    	List<Stock> stocks = null;
    	OdooDomain domain = new OdooDomain();
    	if(null != LOCATIONID){
    		domain.add("location_id", LOCATIONID);
    	}
    	if(null != LOCATIONDESTID){
    		domain.add("location_dest_id", LOCATIONDESTID);
    	}
    	
    	Object[] stockMoveIds = odoo.search("stock.move", domain);
		OdooRecordSet stocksMove = odoo.readRecords("stock.move", stockMoveIds, new String[] { "id","origin", "product_id", "product_qty", "product_uos","location_id", "location_dest_id" });

        Stock stock;
        if(null != stocksMove){
        	stocks = new ArrayList<Stock>();
	        Iterator<OdooRecord> lineRecordIteratorStock = stocksMove.iterator();
	        
	        while(lineRecordIteratorStock.hasNext()){
		        OdooRecord lineRecordStock = lineRecordIteratorStock.next();
	            HashMap<String, Object> mapLineStock = lineRecordStock.getRecord();
	            stock = new Stock();
	            for (Map.Entry<String, Object> entryLineStock : mapLineStock.entrySet()){ 
	            	if(EnumStock.STOCK_ID.getValue().equals(entryLineStock.getKey())){
	            		stock.setId((Integer) entryLineStock.getValue());
	            	}else if(EnumStock.STOCK_LOCATION_DEST_ID.getValue().equals(entryLineStock.getKey())){
	            		try {
	            			stock.setEmplacementDestination(getLocationByID((Integer)entryLineStock.getValue()));
                        }catch(ClassCastException e){
                        	stock.setEmplacementDestination(null);
                        }
	            	}else if(EnumStock.STOCK_LOCATION_ID.getValue().equals(entryLineStock.getKey())) {
	            		try {
	            			stock.setEmplacementSource(getLocationByID((Integer)entryLineStock.getValue()));
                        }catch(ClassCastException e){
                        	stock.setEmplacementSource(null);
                        }	            	    
	            	}else if(EnumStock.STOCK_PRODUCT_ID.getValue().equals(entryLineStock.getKey())){
	            		stock.setProduct(getProductByID((Integer)entryLineStock.getValue()));
                    }else if(EnumStock.STOCK_PRODUCT_QTY.getValue().equals(entryLineStock.getKey())){
                        stock.setQuantityStock((Double)entryLineStock.getValue());
                    }
	            }  
	            stocks.add(stock);
	        }
        }
    	return stocks;
    }
    
    /**
     * Recupére le produit pas ID 
     * @param pProductID
     * @return
     */
    private Product getProductByID(Integer pProductID) {
        OdooDomain domain = new OdooDomain();
        domain.add("id",pProductID);
        Object[] productIds = odoo.search("product.product", domain);
        OdooRecordSet resultProducts = odoo.readRecords("product.product", productIds, new String[] { "id" , "name_template", "categ_id" });
        
        Product produit = null;        
        if(null != resultProducts){                       
            Iterator<OdooRecord> lineRecordIteratorProduct = resultProducts.iterator();
            while (lineRecordIteratorProduct.hasNext()) {
                OdooRecord lineRecordProduct = lineRecordIteratorProduct.next();
                HashMap<String, Object> mapLineProduct = lineRecordProduct.getRecord();
                produit = new Product();
                for (Map.Entry<String, Object> entryLineProduct : mapLineProduct.entrySet()){                    
                    if(EnumProduct.PRODUCT_ID.getValue().equals(entryLineProduct.getKey())){
                        produit.setId((Integer)entryLineProduct.getValue());
                    }else if(EnumProduct.PRODUCT_NAME.getValue().equals(entryLineProduct.getKey())){
                        produit.setName((String)entryLineProduct.getValue());
                    }else if(EnumProduct.PRODUCT_VARIANTE.getValue().equals(entryLineProduct.getKey())){
                        produit.setVariante((String) entryLineProduct.getValue());
                    }
                }    
            }
        }
        
        return produit;
    }

    /**
     * Recupére les emplacements en fonction de l'ID de l'emplacement
     * @param pLocationID
     * @return
     */
    private Location getLocationByID(Integer pLocationID) {
        OdooDomain domain = new OdooDomain();
        domain.add("id", pLocationID);
        Object[] locationIds = odoo.search("stock.location", domain);
        OdooRecordSet resultLocations = odoo.readRecords("stock.location", locationIds, new String[] { "id", "name","location_id", "complete_name", "removal_strategy_id" });
        
        Location location = null;        
        if(null != resultLocations){                       
            Iterator<OdooRecord> lineRecordIteratorLocation = resultLocations.iterator();
            while (lineRecordIteratorLocation.hasNext()) {
                OdooRecord lineRecordLocation = lineRecordIteratorLocation.next();
                HashMap<String, Object> mapLineLocation = lineRecordLocation.getRecord();
                location = new Location();
                for (Map.Entry<String, Object> entryLineLocation : mapLineLocation.entrySet()){   
                   if(EnumLocation.LOCATION_ID.getValue().equals(entryLineLocation.getKey())){
                        location.setId((Integer)entryLineLocation.getValue());
                    }else if(EnumLocation.LOCATION_NAME.getValue().equals(entryLineLocation.getKey())){
                        location.setName((String)entryLineLocation.getValue());
                    }else if(EnumLocation.LOCATION_PARENT.getValue().equals(entryLineLocation.getKey())){
                        try {
                            location.setParentLocation(getLocationByID((Integer)entryLineLocation.getValue()));
                        }catch(ClassCastException e){
                            location.setParentLocation(null);
                            logger.warn("Erreur lot id IN  quant => " + location.getId() + ", emplacement parent => " + entryLineLocation.getValue(), e);
                        }
                    }else if(EnumLocation.LOCATION_COMPLETE_NAME.getValue().equals(entryLineLocation.getKey())){
                        location.setCompleteName((String)entryLineLocation.getValue());
                    }
                }   
            }
        }
        return location;
    }
        
    /**
     * Recuperation des emplacements dont l'id parent est pLocationID
     * @param pLocationID
     * @return
     */
    private List<Location> getLocationByIDParent(Integer pLocationID) {
        OdooDomain domain = new OdooDomain();
        domain.add("location_id", pLocationID);
        //domain.add("location_id", 12);
        Object[] locationIds = odoo.search("stock.location", domain);
        OdooRecordSet resultLocations = odoo.readRecords("stock.location", locationIds, new String[] { "id", "name","location_id", "complete_name", "removal_strategy_id" });
        
        Location location = null;
        List<Location> listLocation = null;
        if(null != resultLocations){ 
        	listLocation = new ArrayList<Location>();
            Iterator<OdooRecord> lineRecordIteratorLocation = resultLocations.iterator();
            while (lineRecordIteratorLocation.hasNext()) {
                OdooRecord lineRecordLocation = lineRecordIteratorLocation.next();
                HashMap<String, Object> mapLineLocation = lineRecordLocation.getRecord();
                location = new Location();
                for (Map.Entry<String, Object> entryLineLocation : mapLineLocation.entrySet()){   
                   if(EnumLocation.LOCATION_ID.getValue().equals(entryLineLocation.getKey())){
                        location.setId((Integer)entryLineLocation.getValue());
                    }else if(EnumLocation.LOCATION_NAME.getValue().equals(entryLineLocation.getKey())){
                        location.setName((String)entryLineLocation.getValue());
                    }else if(EnumLocation.LOCATION_PARENT.getValue().equals(entryLineLocation.getKey())){
                        try {
                            location.setParentLocation(getLocationByID((Integer)entryLineLocation.getValue()));
                        }catch(ClassCastException e){
                            location.setParentLocation(null);
                            logger.warn("Erreur lot id IN  quant => " + location.getId() + ", emplacement parent => " + entryLineLocation.getValue(), e);
                        }
                    }else if(EnumLocation.LOCATION_COMPLETE_NAME.getValue().equals(entryLineLocation.getKey())){
                        location.setCompleteName((String)entryLineLocation.getValue());
                    }
                }
                listLocation.add(location);
            }
        }
        return listLocation;
    }
    
    /**
     * Recuperation des quantités de stock par emplacement
     * @param pLocationID
     * @return
     */
    private List<Quant> getStockQuant(Integer pLocationID){
        List<Quant> ListStockQuant = null;
        OdooDomain domain = new OdooDomain();
        if(null != pLocationID){
            domain.add("location_id", pLocationID);
        }
        
        Object[] stockQuantIds = odoo.search(EnumQuant.MODEL.getValue(), domain);
        OdooRecordSet stocksQuant = odoo.readRecords(EnumQuant.MODEL.getValue(), stockQuantIds, new String[] { "id","product_id", "qty", "location_id", "lot_id","in_date" });
        
        Quant quant;
        if(null != stocksQuant){
            ListStockQuant = new ArrayList<Quant>();
            Iterator<OdooRecord> lineRecordIteratorQuant = stocksQuant.iterator();
            
            while(lineRecordIteratorQuant.hasNext()){
                OdooRecord lineRecordQuant = lineRecordIteratorQuant.next();
                HashMap<String, Object> mapLineQuant = lineRecordQuant.getRecord();
                quant = new Quant();
                for (Map.Entry<String, Object> entryLineQuant : mapLineQuant.entrySet()){ 
                    if(EnumQuant.QUANT_ID.getValue().equals(entryLineQuant.getKey())){
                        quant.setId((Integer) entryLineQuant.getValue());
                    }else if(EnumQuant.QUANT_LOCATION_ID.getValue().equals(entryLineQuant.getKey())) {
                        try {
                            quant.setEmplacement(getLocationByID((Integer)entryLineQuant.getValue()));
                        }catch(ClassCastException e){
                            quant.setEmplacement(null);
                            logger.warn("Erreur lot id IN  quant => " + quant.getId() + ", emplacement => " + entryLineQuant.getValue(), e);
                        }                       
                    }else if(EnumQuant.QUANT_PRODUCT_ID.getValue().equals(entryLineQuant.getKey())){
                        quant.setProduct(getProductByID((Integer)entryLineQuant.getValue()));
                    }else if(EnumQuant.QUANT_PRODUCT_QTY.getValue().equals(entryLineQuant.getKey())){
                        try {
                        quant.setQuantite((Double)entryLineQuant.getValue());
                        }catch(ClassCastException e){
                            quant.setEmplacement(null);
                            logger.warn("Erreur lot id IN  quant => " + quant.getId() + ", quantite => " + entryLineQuant.getValue(), e);
                        }
                    }else if(EnumQuant.QUANT_LOT_ID.getValue().equals(entryLineQuant.getKey())){
                        try{
                            quant.setLotID((Integer) entryLineQuant.getValue());
                        }catch(ClassCastException e){
                            quant.setLotID(null);
                            logger.warn("Erreur lot id IN  quant => " + quant.getId() + ", lot id => " + entryLineQuant.getValue(), e);
                        }
                    }else if(EnumQuant.QUANT_IN_DATE.getValue().equals(entryLineQuant.getKey())){
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            quant.setInDate(df.parse((String) entryLineQuant.getValue()));
                        } catch (ParseException e) {
                            logger.warn("Erreur date IN stock quant => " + quant.getId() + ", date in => " + entryLineQuant.getValue(), e);
                        }
                    }
                }  
                ListStockQuant.add(quant);
            }
        }
        
        return ListStockQuant;
    }
    
    /**
     * Recuperation des quantités de stock par emplacement parent
     * @param pLocationID
     * @return
     */
    private List<Quant> getParentStockQuant(Integer pLocationID){
        List<Quant> ListStockQuant = null;
        
        List<Location> locationChildren = getLocationByIDParent(pLocationID);
        
        if(null != locationChildren){
        	ListStockQuant = new ArrayList<Quant>();
	        for (Location location : locationChildren) {
	        	ListStockQuant.addAll(getStockQuant(location.getId()));
			}
        }
             
        return ListStockQuant;
    }
    
    /**
     * Recuperation des quantités de stock par emplacement parent et par produit
     * @param pLocationID
     * @return
     */
    private Map<Integer, Product> getQuantByProductInStock(Integer pLocationID) {
    	Map<Integer, Product> quantProduct = null;
		//On recupere tout le stock
		List<Quant> stockQuant = getParentStockQuant(pLocationID);
		stockQuant.addAll(getStockQuant(pLocationID));
		
		if(!stockQuant.isEmpty()){
			quantProduct = new HashMap<Integer, Product>();
			for (Quant quant : stockQuant) {
				if(null != quantProduct.get(quant.getProduct().getId())){
					quant.getProduct().setQtyOnHand(quantProduct.get(quant.getProduct().getId()).getQtyOnHand() + quant.getQuantite());
				}else {
					quant.getProduct().setQtyOnHand(quant.getQuantite());					
				}
				quantProduct.put(quant.getProduct().getId(),quant.getProduct());
			}
		}
    	return quantProduct;
    }
    
    /**
     * Retourne les produits par emplacements
     * @return
     */
    @SuppressWarnings("unused")
    private Map<Location, Product> getProductByLocationStock() {
        Map<Location, Product> productLocation = null;
        List<Quant> stockQuant = getParentStockQuant(VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_STOCK);
        stockQuant.addAll(getStockQuant(VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_STOCK));
        
        Location emplacement;
        Product product;
        if(null != stockQuant && !stockQuant.isEmpty()){
            productLocation = new HashMap<Location, Product>();
            for (Quant quant : stockQuant) {
                product = quant.getProduct();
                emplacement = quant.getEmplacement();
                if(null != productLocation.get(emplacement)){
                    product.setQtyOnHand(product.getQtyOnHand() + quant.getQuantite());
                    productLocation.put(emplacement, product);
                } else {
                    product.setQtyOnHand(quant.getQuantite());
                    productLocation.put(emplacement, product);
                }
            }
        }
        return productLocation;
    }
    
    /**
     * Retourne le produit par emplacements
     * @param pProduct
     * @return
     */
    private Map<Location, Product> getProductByLocationStock(Product pProduct) {
        Map<Location, Product> productLocation = null;
        List<Quant> stockQuant = getParentStockQuant(VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_STOCK);
        stockQuant.addAll(getStockQuant(VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_STOCK));
        
        Location emplacement;
        Product product;
        if(null != stockQuant && !stockQuant.isEmpty()){
            productLocation = new HashMap<Location, Product>();
            for (Quant quant : stockQuant) {
                product = quant.getProduct();
                if(null != pProduct && pProduct.equals(product)){                
                    emplacement = quant.getEmplacement();
                    if(null != productLocation.get(emplacement)){
                        product.setQtyOnHand(product.getQtyOnHand() + quant.getQuantite());
                        productLocation.put(emplacement, product);
                    } else {
                        product.setQtyOnHand(quant.getQuantite());
                        productLocation.put(emplacement, product);
                    }
                }
            }
        }
        return productLocation;
    }
    
    public static void main(String[] args) throws MalformedURLException, XmlRpcException {
    	BusinessImpl buss = new BusinessImpl();
        
        //System.out.println(buss.getLocationByID(19));
        /*List<Product> produits = buss.getListProduct();
        for (Product product : produits) {
            System.out.println(product);
        }*/
        /*List<Stock> stock = buss.getListStock(null,12);
        for (Stock stock2 : stock) {
            System.out.println(stock2);
        }*/
      //System.out.println(buss.getProductByID(2));
    	/*List<Quant> stock = buss.getStockQuant(12);
        for (Quant stock2 : stock) {
            System.out.println(stock2);
        }*/
    	/*List<Quant> stockQuant = buss.getParentStockQuant(VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_STOCK);
        stockQuant.addAll(buss.getStockQuant(VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_STOCK));
        for (Quant quant : stockQuant) {
            System.out.println(quant);
        }
        buss.getProductAlertQuantStock(); */  
    	Map<Location, Product> quants = buss.getProductByLocationStock(new Product(2, "produit 1"));
    	for (Map.Entry<Location, Product> e : quants.entrySet()){ 
    	    System.out.println(e.getKey().getName() + " ==> " + e.getValue());
    	}
    }

	@Override
	/**
     * @see sn.arometh.notification.business.Business#getProductAlertQuantStock()
     * {@inheritDoc}
     */
	public List<Product> getProductAlertQuantStock() {
		List<Product> produitQuant = null;
		Map<Integer, Product> quants = getQuantByProductInStock(VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_STOCK);
		
		if(null != quants && !quants.isEmpty()){
		    produitQuant = new ArrayList<Product>();
    		for (Map.Entry<Integer, Product> entry : quants.entrySet()){
    		    if(entry.getValue().getQtyOnHand() < VAR_NOTIFICATION_BDD_ECHEANCE_QUANTITE){
    		        produitQuant.add(entry.getValue());
    		    }
    		}
		}
		return produitQuant;
	}

	@Override
	/**
     * @see sn.arometh.notification.business.Business#formatListToMessage(List<Product>)
     * {@inheritDoc}
     */
	public String formatListToMessage(List<Product> pProduct){
	    String messageFormat = null;
	    
	    if(null != pProduct && !pProduct.isEmpty()){
	        messageFormat = "";
	        Map<Location, Product> emplacementByProduct;
	        for (Product product : pProduct) {
	            emplacementByProduct = getProductByLocationStock(product);
	            messageFormat += "Produit [" + product.getName() + "] ==> Quantite [" + product.getQtyOnHand() + "] \n";
	            messageFormat += "#######################################################\n";
                messageFormat += "      Quantite => " + product.getQtyOnHand() + " \n";
                messageFormat += "#######################################################\n";
                if(null != emplacementByProduct && !emplacementByProduct.isEmpty()){
                    for (Map.Entry<Location, Product> entry : emplacementByProduct.entrySet()) {
                        messageFormat += "          Emplacement [" + entry.getKey().getName() + "] ==> Quantite [" + entry.getValue().getQtyOnHand() + "]";
                    }                    
                }
                messageFormat += "---------------------------------------------------------------------------------------------------------------------------------------\n\n"; 
            }
	    }
	    
	    return messageFormat;
	}
	
	@Override
	/**
     * @see sn.arometh.notification.business.Business#getProductAlertOutOfDate()
     * {@inheritDoc}
     */
	public List<Product> getProductAlertOutOfDate() {
		// TODO Auto-generated method stub
		return null;
	}

}
