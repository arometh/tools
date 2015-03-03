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
import sn.arometh.notification.entity.Product;
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
        Object[] result_ids = odoo.search("product.product");
        OdooRecordSet results = odoo.readRecords("product.product", result_ids, new String[] { "name_template", "categ_id" });

        //Vector<Object> category_ids = results.getFieldContents("categ_id");
        //OdooRecordSet categories = odoo.readRecords("product.category", category_ids, new String[] { "name", "sequence", "type" });

        //results.relate("categ_id", categories);
        
        if(null != results){
            produits = new ArrayList<Product>();            
            Product produit;
            Iterator<OdooRecord> j = results.iterator();
            while (j.hasNext()) {
                OdooRecord n = j.next();
                HashMap<String, Object> map = n.getRecord();
                produit = new Product();
                for (Map.Entry<String, Object> e : map.entrySet()){                    
                    if(ProductFieldName.PRODUCT_ID.getValue().equals(e.getKey())){
                        produit.setId((Integer)e.getValue());
                    }else if(ProductFieldName.PRODUCT_NAME.getValue().equals(e.getKey())){
                        produit.setName((String)e.getValue());
                    }else if(ProductFieldName.PRODUCT_VARIANTE.getValue().equals(e.getKey())){
                        produit.setVariante((String) e.getValue());
                    }
                }    
                produits.add(produit);
            }
        }
        
        
        return produits;
    }
    
    public static void main(String[] args) throws MalformedURLException, XmlRpcException {
        Business buss = new BusinessImpl();
        
        List<Product> produits = buss.getListProduct();
        for (Product product : produits) {
            System.out.println(product);
        }
    }
}
