package sn.arometh.notification.api.rest;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;














import sn.arometh.notification.commons.ConstantFunctionnals;
import sn.arometh.notification.commons.PropertiesUtils;
import sn.arometh.notification.entity.Product;
import sn.arometh.notification.entity.QuerySearch;

public class OdooImpl implements Odoo, ConstantFunctionnals {
	private XmlRpcClient models;
	
	public OdooImpl() throws XmlRpcException, MalformedURLException {
		models = new XmlRpcClient() {{
		    setConfig(new XmlRpcClientConfigImpl() {{
		        setServerURL(new URL("http", "localhost", 8069, "/xmlrpc/object"));
		    }});
		    }};
	}
      
	
	@Override
	public List<Object> searchEntity(QuerySearch pQuery) throws XmlRpcException {
		List<Object> produit = Arrays.asList((Object[])models.execute(VAR_NOTIFICATION_REST_EXECUTE_KW, Arrays.asList(
			    VAR_NOTIFICATION_REST_DATABASE, VAR_NOTIFICATION_REST_USER, VAR_NOTIFICATION_REST_PASSWORD,
			    "product.product", "search_read",
			    Arrays.asList(Arrays.asList(
			        Arrays.asList("active", "=", true))),
			    new HashMap() {{
			        put("fields", Arrays.asList("name_template"));
			        put("limit", 15);
			    }}
			)));
		return null;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public static void main(String[] args) throws Exception {
		final XmlRpcClient client = new XmlRpcClient();

		final String url = "http://localhost:8069/xmlrpc/common",
	              db = "arometh1",
	        username = "admin",
	        password = "azeqsdwxc",
	        url1 = "http://localhost:8069/xmlrpc/object";
		
		final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
		common_config.setServerURL(new URL(url1));
		common_config.setServerURL(new URL("http", "localhost", 8069, "/xmlrpc/common"));
		client.execute(common_config, "version", Arrays.asList());
	
		int uid = (Integer) client.execute(
			    common_config, "authenticate", Arrays.asList(
			        db, username, password, Arrays.asList()));
		System.out.println("url => " + url + ", db => " + db + ", username => " + username + ", password => " + password + ", uid => " + uid);
		
		final XmlRpcClient models = new XmlRpcClient() {{
		    setConfig(new XmlRpcClientConfigImpl() {{
		        setServerURL(new URL("http", "localhost", 8069, "/xmlrpc/object"));
		    }});
		}};
		models.execute("execute_kw", Arrays.asList(
		    db, uid, password,
		    "res.partner", "check_access_rights",
		    Arrays.asList("read"),
		    new HashMap() {{ put("raise_exception", false); }}
		));
		
		List<Object> produit = Arrays.asList((Object[])models.execute("execute_kw", Arrays.asList(
			    db, uid, password,
			    "product.template", "search_read",
			    Arrays.asList(Arrays.asList()),
			    new HashMap() {{
			        put("fields", Arrays.asList("name_template"));
			    }}
			)));
		
		//System.out.println(produit);
		
		List<Object> stock = Arrays.asList((Object[])models.execute("execute_kw", Arrays.asList(
			    db, uid, password,
			    "stock.move", "search_read",
			    Arrays.asList(Arrays.asList()),
			    new HashMap() {{
			        put("fields", Arrays.asList("product_qty","location_id","location_dest_id","product_id"));
			        put("limit", 150);
			    }}
			)));
		
		OdooImpl oo = new OdooImpl();
		for (Object object : produit) {
		    oo.record = (HashMap<String, Object>) object;
		    oo.convertRelated();
		}
		System.out.println(oo.record);
		
		List<Object> quant = Arrays.asList((Object[])models.execute("execute_kw", Arrays.asList(
			    db, uid, password,
			    "stock.quant", "search_read",
			    Arrays.asList(Arrays.asList(
			        Arrays.asList("company_id", "=", 1))),
			    new HashMap() {{
			        put("fields", Arrays.asList("qty","product_id"));
			        put("limit", 150);
			    }}
			)));
		//System.out.println(quant);
		
		Object [] ob = (Object[])models.execute("execute_kw", Arrays.asList(
		                                                     db, uid, password,
		                                                     "res.partner", "search_read",
		                                                     Arrays.asList(Arrays.asList(
		                                                         Arrays.asList("is_company", "=", true),
		                                                         Arrays.asList("customer", "=", true))),
		                                                     new HashMap() {{
		                                                         put("fields", Arrays.asList("name", "country_id", "comment"));
		                                                         put("limit", 5);
		                                                     }}
		                                                 ));
		
		//System.out.println(Arrays.toString(ob));
	}
	private HashMap<String, Object> record;
	private void convertRelated() {
        // needed to avoid ConcurrentModificationException when adding the new
        // _NAME key
        HashMap<String, Object> converted = new HashMap<String, Object>();

        Set<String> keys = record.keySet();
        Iterator<String> i = keys.iterator();
        while (i.hasNext()) {
            String key = i.next();

            // let's see if we got an array
            try {
                Object[] o = (Object[]) record.get(key);
                if (o.length > 1) {
                    // found one: add explicit values
                    converted.put(key, o[0]); // will overwrite value
                    converted.put(key + "_NAME", o[1]);
                }
            } catch (ClassCastException e) {
                // nothing to do. isn't a related record after all
            }

        }

        // combine with new values
        record.putAll(converted);
    }

}
