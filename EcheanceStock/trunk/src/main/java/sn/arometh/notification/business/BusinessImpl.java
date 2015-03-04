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
import sn.arometh.notification.commons.ProductFieldName;
import sn.arometh.notification.commons.StockFieldName;
import sn.arometh.notification.entity.Entity;
import sn.arometh.notification.entity.Product;
import sn.arometh.notification.entity.Stock;
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
    public List<Product> getListProduct(){
        List<Product> produits = null;
        Object[] productIds = odoo.search("product.product");
        OdooRecordSet resultProducts = odoo.readRecords("product.product", productIds, new String[] { "name_template", "categ_id" });

        //Vector<Object> category_ids = results.getFieldContents("categ_id");
        //OdooRecordSet categories = odoo.readRecords("product.category", category_ids, new String[] { "name", "sequence", "type" });

        //results.relate("categ_id", categories);
        
        if(null != resultProducts){
            produits = new ArrayList<Product>();            
            Product produit;
            Iterator<OdooRecord> lineRecordIteratorProduct = resultProducts.iterator();
            while (lineRecordIteratorProduct.hasNext()) {
                OdooRecord lineRecordProduct = lineRecordIteratorProduct.next();
                HashMap<String, Object> mapLineProduct = lineRecordProduct.getRecord();
                produit = new Product();
                for (Map.Entry<String, Object> entryLineProduct : mapLineProduct.entrySet()){                    
                    if(ProductFieldName.PRODUCT_ID.getValue().equals(entryLineProduct.getKey())){
                        produit.setId((Integer)entryLineProduct.getValue());
                    }else if(ProductFieldName.PRODUCT_NAME.getValue().equals(entryLineProduct.getKey())){
                        produit.setName((String)entryLineProduct.getValue());
                    }else if(ProductFieldName.PRODUCT_VARIANTE.getValue().equals(entryLineProduct.getKey())){
                        produit.setVariante((String) entryLineProduct.getValue());
                    }
                }    
                produits.add(produit);
            }
        }
        
        
        return produits;
    }
    
    @Override
    public List<Stock> getListStock() {
    	List<Stock> stocks = null;
    	Object[] stockMoveIds = odoo.search("stock.move");
		OdooRecordSet stocksMove = odoo.readRecords("stock.move", stockMoveIds, new String[] { "id","origin", "product_id", "product_qty", "product_uos","location_id", "location_dest_id" });

		Vector<Object> produitIds = stocksMove.getFieldContents("produit_id");
        OdooRecordSet products = odoo.readRecords("product.template", produitIds, new String[] { "name_template","product_id"});
        
        if(null != products && null!= stocksMove){
        	stocksMove.relate("product_id", products);
        }
        
        Vector<Object> locationIds = stocksMove.getFieldContents("location_id");
        OdooRecordSet locations = odoo.readRecords("stock.location", produitIds, new String[] { "id","name","location_id","complete_name","active"});
        
        if(null != products && null!= stocksMove){
        	stocksMove.relate("location_id", products);
        	stocksMove.relate("location_dest_id", products);
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
	            	if(StockFieldName.STOCK_ID.getValue().equals(entryLineStock.getKey())){
	            		stock.setId((Integer) entryLineStock.getValue());
	            	}else if(StockFieldName.STOCK_LOCATION_DEST_ID.getValue().equals(entryLineStock.getKey())){
	            		//stock.set
	            	}
	                /*if(ProductFieldName.PRODUCT_ID.getValue().equals(entryLineStock.getKey())){
	                    produit.setId((Integer)entryLineStock.getValue());
	                }else if(ProductFieldName.PRODUCT_NAME.getValue().equals(entryLineStock.getKey())){
	                    produit.setName((String)entryLineStock.getValue());
	                }else if(ProductFieldName.PRODUCT_VARIANTE.getValue().equals(entryLineStock.getKey())){
	                    produit.setVariante((String) entryLineStock.getValue());
	                }*/
	            	System.out.println(entryLineStock.getKey() + "  : " + entryLineStock.getValue());
	            }  
	            stock.setProduct(produit);
	            stocks.add(stock);
	        }
        }
    	return stocks;
    }
    
    private Map<Integer, Entity> getEntityById(Entity entity){
    	
    	return null;
    }
    public static void main(String[] args) throws MalformedURLException, XmlRpcException {
        Business buss = new BusinessImpl();
        
        buss.getListStock();
        /*List<Product> produits = buss.getListProduct();
        for (Product product : produits) {
            System.out.println(product);
        }*/
    }
}
