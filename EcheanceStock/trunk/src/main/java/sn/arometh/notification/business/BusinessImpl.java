package sn.arometh.notification.business;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

import sn.arometh.notification.commons.ConstantFunctionnals;
import sn.arometh.notification.entity.Entity;
import sn.arometh.notification.entity.Location;
import sn.arometh.notification.entity.Product;
import sn.arometh.notification.entity.Stock;
import sn.arometh.notification.enumeration.EnumLocationFieldName;
import sn.arometh.notification.enumeration.EnumProductFieldName;
import sn.arometh.notification.enumeration.EnumStockFieldName;
import sn.arometh.notification.odoo.Odoo;
import sn.arometh.notification.odoo.OdooDomain;
import sn.arometh.notification.odoo.OdooRecord;
import sn.arometh.notification.odoo.OdooRecordSet;

public class BusinessImpl implements Business,ConstantFunctionnals {	
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
	
    @Override
    /**
     * @see sn.arometh.notification.business.Business#getListProduct()
     * {@inheritDoc}
     */
    public List<Product> getListProduct(){
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
                    if(EnumProductFieldName.PRODUCT_ID.getValue().equals(entryLineProduct.getKey())){
                        produit.setId((Integer)entryLineProduct.getValue());
                    }else if(EnumProductFieldName.PRODUCT_NAME.getValue().equals(entryLineProduct.getKey())){
                        produit.setName((String)entryLineProduct.getValue());
                    }else if(EnumProductFieldName.PRODUCT_VARIANTE.getValue().equals(entryLineProduct.getKey())){
                        produit.setVariante((String) entryLineProduct.getValue());
                    }
                }    
                produits.add(produit);
            }
        }
        
        
        return produits;
    }
    
    @Override
    /**
     * @see sn.arometh.notification.business.Business#getListStock()
     * {@inheritDoc}
     */
    public List<Stock> getListStock() {
    	List<Stock> stocks = null;
    	Object[] stockMoveIds = odoo.search("stock.move");
		OdooRecordSet stocksMove = odoo.readRecords("stock.move", stockMoveIds, new String[] { "id","origin", "product_id", "product_qty", "product_uos","location_id", "location_dest_id" });

		Vector<Object> produitIds = stocksMove.getFieldContents("produit_id");
        OdooRecordSet products = odoo.readRecords("product.template", produitIds, new String[] { "name_template","product_id"});
        
        if(null != products && null!= stocksMove){
        	stocksMove.relate("product_id", products);
        }
        
        Product produit;
        Stock stock;
        if(null != stocksMove){
        	stocks = new ArrayList<Stock>();
	        Iterator<OdooRecord> lineRecordIteratorStock = stocksMove.iterator();
	        
	        while(lineRecordIteratorStock.hasNext()){
		        OdooRecord lineRecordStock = lineRecordIteratorStock.next();
	            HashMap<String, Object> mapLineStock = lineRecordStock.getRecord();
	            produit = new Product();
	            stock = new Stock();
	            for (Map.Entry<String, Object> entryLineStock : mapLineStock.entrySet()){ 
	            	if(EnumStockFieldName.STOCK_ID.getValue().equals(entryLineStock.getKey())){
	            		stock.setId((Integer) entryLineStock.getValue());
	            	}else if(EnumStockFieldName.STOCK_LOCATION_DEST_ID.getValue().equals(entryLineStock.getKey())){
	            	    stock.setEmplacementDestination(getLocationByID((Integer)entryLineStock.getValue()));
	            	}else if(EnumStockFieldName.STOCK_LOCATION_ID.getValue().equals(entryLineStock.getKey())) {
	            	    stock.setEmplacementSource(getLocationByID((Integer)entryLineStock.getValue()));
	            	}else if(EnumStockFieldName.STOCK_PRODUCT_ID.getValue().equals(entryLineStock.getKey())){
                        stock.setProduct(getProductByID((Integer)entryLineStock.getValue()));
                    }else if(EnumStockFieldName.STOCK_PRODUCT_QTY.getValue().equals(entryLineStock.getKey())){
                        System.out.println("err => " + entryLineStock.getValue());
                        stock.setQuantityStock((Double)entryLineStock.getValue());
                    }
	            	//System.out.println(entryLineStock.getKey() + "  : " + entryLineStock.getValue());
	            }  
	            stock.setProduct(produit);
	            stocks.add(stock);
	        }
        }
    	return stocks;
    }
    
    @Override
    /**
     * @see sn.arometh.notification.business.Business#getProductByID(java.lang.Integer)
     * {@inheritDoc}
     */
    public Product getProductByID(Integer pProductID) {
        OdooDomain domain = new OdooDomain();
        domain.add("product_id",pProductID);
        Object[] productIds = odoo.search("product.product", domain);
        OdooRecordSet resultProducts = odoo.readRecords("product.product", productIds, new String[] { "name_template", "categ_id" });
        
        Product produit = null;        
        if(null != resultProducts){                       
            Iterator<OdooRecord> lineRecordIteratorProduct = resultProducts.iterator();
            while (lineRecordIteratorProduct.hasNext()) {
                OdooRecord lineRecordProduct = lineRecordIteratorProduct.next();
                HashMap<String, Object> mapLineProduct = lineRecordProduct.getRecord();
                produit = new Product();
                for (Map.Entry<String, Object> entryLineProduct : mapLineProduct.entrySet()){                    
                    if(EnumProductFieldName.PRODUCT_ID.getValue().equals(entryLineProduct.getKey())){
                        produit.setId((Integer)entryLineProduct.getValue());
                    }else if(EnumProductFieldName.PRODUCT_NAME.getValue().equals(entryLineProduct.getKey())){
                        produit.setName((String)entryLineProduct.getValue());
                    }else if(EnumProductFieldName.PRODUCT_VARIANTE.getValue().equals(entryLineProduct.getKey())){
                        produit.setVariante((String) entryLineProduct.getValue());
                    }
                }    
            }
        }
        
        return produit;
    }

    @Override
    /**
     * @see sn.arometh.notification.business.Business#getLocationByID(java.lang.Integer)
     * {@inheritDoc}
     */
    public Location getLocationByID(Integer pLocationID) {
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
                    System.out.println(entryLineLocation.getKey() + "  : " + entryLineLocation.getValue());
                   if(EnumLocationFieldName.LOCATION_ID.getValue().equals(entryLineLocation.getKey())){
                        location.setId((Integer)entryLineLocation.getValue());
                    }else if(EnumLocationFieldName.LOCATION_NAME.getValue().equals(entryLineLocation.getKey())){
                        location.setName((String)entryLineLocation.getValue());
                    }else if(EnumLocationFieldName.LOCATION_PARENT.getValue().equals(entryLineLocation.getKey())){
                        try {
                            location.setParentLocation(getLocationByID((Integer)entryLineLocation.getValue()));
                        }catch(ClassCastException e){
                            location.setParentLocation(null);
                        }
                    }else if(EnumLocationFieldName.LOCATION_COMPLETE_NAME.getValue().equals(entryLineLocation.getKey())){
                        location.setCompleteName((String)entryLineLocation.getValue());
                    }
                }   
            }
        }
        return location;
    }
    
    /**
     * 
     * @param entity
     * @return
     */
    /*private Entity getEntityById(Entity entity){
        OdooDomain domain = new OdooDomain();
        domain.add(entity.getField()[0], entity.getId());
        
        Object[] entityIds = odoo.search(entity.getModel());
        OdooRecordSet resultEntitys = odoo.readRecords(entity.getModel(), entityIds, entity.getField());
        
        if(null != resultEntitys){
            Iterator<OdooRecord> lineRecordIteratorEntity = resultEntitys.iterator();
            //if(entity instanceof Product){
                Product produit; 
                Stock stock;
                Location location;
                while (lineRecordIteratorEntity.hasNext()) {
                    OdooRecord lineRecordEntity = lineRecordIteratorEntity.next();
                    HashMap<String, Object> mapLineEntity = lineRecordEntity.getRecord();
                    produit = new Product();
                    stock = new Stock();
                    
                    for (Map.Entry<String, Object> entryLineEntity : mapLineEntity.entrySet()){  
                        if(entity instanceof Product){
                            if(ProductFieldName.PRODUCT_ID.getValue().equals(entryLineEntity.getKey())){
                                produit.setId((Integer)entryLineEntity.getValue());
                            }else if(ProductFieldName.PRODUCT_NAME.getValue().equals(entryLineEntity.getKey())){
                                produit.setName((String)entryLineEntity.getValue());
                            }else if(ProductFieldName.PRODUCT_VARIANTE.getValue().equals(entryLineEntity.getKey())){
                                produit.setVariante((String) entryLineEntity.getValue());
                            }
                        }else {
                            if(StockFieldName.STOCK_ID.getValue().equals(entryLineEntity.getKey())){
                                stock.setId((Integer) entryLineEntity.getValue());
                            }else if(StockFieldName.STOCK_LOCATION_DEST_ID.getValue().equals(entryLineEntity.getKey())){
                                //stock.setEmplacementDestination(getEntityById(new En));
                            }
                        }
                    }
                    return produit;
                 }
        }
    	return null;
    }*/
    public static void main(String[] args) throws MalformedURLException, XmlRpcException {
        Business buss = new BusinessImpl();
        
        //buss.getLocationByID(12);
        /*List<Product> produits = buss.getListProduct();
        for (Product product : produits) {
            System.out.println(product);
        }*/
        List<Stock> stock = buss.getListStock();
        for (Stock stock2 : stock) {
            System.out.println(stock2);
        }
    }

}
