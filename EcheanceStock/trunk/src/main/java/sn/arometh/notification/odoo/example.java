package sn.arometh.notification.odoo;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;

class example {

	Odoo odoo;

	public static void main(String[] args) throws MalformedURLException, XmlRpcException {
		example ex = new example();
		//ex.relate();
		ex.search_and_output();
	}

	public example() throws MalformedURLException, XmlRpcException {
		odoo = new Odoo("localhost", "arometh1", "admin", "azeqsdwxc");
	}

	public void search_and_output() {
		OdooDomain domain = new OdooDomain();
		//domain.add("product_id", 2);

		Object[] result_ids = odoo.search("stock.move", domain);
		OdooRecordSet results = odoo.readRecords("stock.move", result_ids, new String[] { "origin", "product_id", "product_qty", "product_uos","location_id", "location_dest_id" });

		System.out.println(results);

	}

	public void create() {
		HashMap<String, Object> new1 = new HashMap<String, Object>();
		new1.put("origin", "test1");
		new1.put("some_int", new Integer(2));
		new1.put("product_qty", new Double(2.2));
		// drycoded, won't probably work like this (missed neccessary fields?)

		Integer new1_id = odoo.create("stock.move", new1);

		if (new1_id == null) {
			System.err.println("Something went wrong while creating a new stock.move");
		} else {
			System.out.println("created " + new1_id);
		}

	}

	public void relate() {
		Object[] result_ids = odoo.search("product.product");
		OdooRecordSet results = odoo.readRecords("product.product", result_ids, new String[] { "name", "categ_id" });

		Vector<Object> category_ids = results.getFieldContents("categ_id");
		OdooRecordSet categories = odoo.readRecords("product.category", category_ids, new String[] { "name", "sequence", "type" });

		results.relate("categ_id", categories);
		System.out.println("All products with category data " + results);

	}

	public void stockmove_actiondone() {
		// example of a methodCall beside search,read,write,create
		/* Definition:
		   def action_done(self, cr, uid, ids, context=None):
		   Makes the move done and if all moves are done, it will finish the picking.
		   
		   self, cr, uid are provided by the OpenERP class.
		*/

		Vector<Object> params = new Vector<Object>();
		// example on setting 2, 4 and 6 to done:
		params.add(new Integer[] { 2, 4, 6 });

		Object result = odoo.methodCall("stock.move", "action_done", params);

		System.out.println(result);
	}

}
