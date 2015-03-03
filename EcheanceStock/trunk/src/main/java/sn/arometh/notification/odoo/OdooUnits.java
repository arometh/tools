package sn.arometh.notification.odoo;

/**
 * OpenERP Interfaces for Java
 * 
 * Copyright (c) 2010+ BREMSKERL-REIBBELAGWERKE Emmerling GmbH & Co. KG
 * Author: Marco Dieckhoff, marco.dieckhoff@bremskerl.de
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.HashMap;

import sn.arometh.notification.odoo.models.ProductUOM;

/**
 * OpenERP Unit conversions
 * 
 * @author Marco Dieckhoff, BREMSKERL-REIBBELAGWERKE Emmerling GmbH & Co. KG
 */
public class OdooUnits {

	// database connection
	Odoo openerp;

	/**
	 * cache for product.uom, by id
	 */
	HashMap<Integer, ProductUOM> cache;

	/**
	 * cache for reference product.uom (base unit), by category_id
	 */
	HashMap<Integer, ProductUOM> baseunits;

	/**
	 * create a new instance
	 * 
	 * @param o
	 *            OpenERP database connection
	 */
	public OdooUnits(Odoo o) {
		openerp = o;
		cache = new HashMap<Integer, ProductUOM>();
		baseunits = new HashMap<Integer, ProductUOM>();
	}

	/**
	 * return known UoM by id
	 * @param id
	 * @return
	 */
	public ProductUOM getProductUoM(Integer id) {
		if (cache.containsKey(id))
			return cache.get(id);
		ProductUOM p = new ProductUOM(openerp, id);
		cache.put(id, p);
		return p;
	}

	/**
	 * search for a named UoM
	 * 
	 * @param category
	 *            Category ID to search in
	 * @param name
	 *            Name to search for
	 * @return product_uom found (expecting max. one)
	 */
	public ProductUOM searchUoM(Integer category, String name) {
		OdooDomain domain = new OdooDomain();
		domain.add("category_id", category);
		domain.add("name", "=", name);

		Object[] ids = openerp.search("product.uom", domain);
		if (ids == null)
			return null;

		Integer id = null;
		if (ids.length > 0)
			id = (Integer) ids[0];

		if (id == null)
			return null;

		return getProductUoM(id);
	}

	/**
	 * gets the reference (base) UoM for a category
	 * 
	 * @param category
	 *            Category ID to search the reference UoM in
	 * @return Reference UoM for the given category
	 */
	public ProductUOM getBaseUoM_byCategory(Integer category) {
		// use caching
		if (baseunits.containsKey(category))
			return baseunits.get(category);

		OdooDomain domain = new OdooDomain();
		domain.add("category_id", category);
		domain.add("uom_type", "=", "reference");
		Object[] ids = openerp.search("product.uom", domain);

		if (ids == null)
			return null;

		Integer id = null;
		if (ids.length > 0)
			id = (Integer) ids[0];

		if (id == null)
			return null;

		ProductUOM p = new ProductUOM(openerp, id);
		baseunits.put(id, p);
		return p;
	}

	/**
	 * gets the reference (base) UoM for this units category
	 * 
	 * @param uom
	 *            Unit (product_uom) to get Base unit of
	 * @return Base unit of uom
	 */
	public ProductUOM getBaseUoM(ProductUOM uom) {
		return getBaseUoM_byCategory(uom.getCategory_id());
	}

	/**
	 * gets the reference (base) UoM for this units category
	 * 
	 * @param uom
	 *            Unit (Integer) to get Base unit of
	 * @return Base unit of uom
	 */
	public ProductUOM getBaseUoM(Integer uomid) {
		ProductUOM uom = new ProductUOM(openerp, uomid);
		return getBaseUoM(uom);
	}

	/**
	 * convert between two known UoMs
	 * 
	 * @param from
	 *            source UoM
	 * @param value
	 *            value to convert
	 * @param to
	 *            target UoM
	 * @return converted value
	 * @throws Exception
	 *             if UoM categories are incompatible
	 */
	public Double convert(ProductUOM from, Double value, ProductUOM to) throws Exception {
		if ((from == null) || (to == null))
			return null;
		return from.convert(value, to);
	}
	
	/**
	 * convert between two known UoMs
	 * 
	 * @param uom_from
	 *            source UoM id
	 * @param value
	 *            value to convert
	 * @param uom_to
	 *            target UoM id
	 * @return converted value
	 * @throws Exception
	 *             if UoM categories are incompatible
	 */
	public Double convert(Integer uom_from, Double value, Integer uom_to) throws Exception {
		ProductUOM from = getProductUoM(uom_from);
		ProductUOM to = getProductUoM(uom_to);
		return convert(from, value, to);
	}

	/**
	 * convert between two known UoMs
	 * 
	 * @param uom_from
	 *            source UoM id
	 * @param value
	 *            value to convert
	 * @param uom_to
	 *            target UoM
	 * @return converted value
	 * @throws Exception
	 *             if UoM categories are incompatible
	 */
	public Double convert(Integer uom_from, Double value, ProductUOM to) throws Exception {
		ProductUOM from = getProductUoM(uom_from);
		return convert(from, value, to);
	}

	/**
	 * convert between two known UoMs
	 * 
	 * @param uom_from
	 *            source UoM
	 * @param value
	 *            value to convert
	 * @param uom_to
	 *            target UoM id
	 * @return converted value
	 * @throws Exception
	 *             if UoM categories are incompatible
	 */
	public Double convert(ProductUOM from, Double value, Integer uom_to) throws Exception {
		ProductUOM to = getProductUoM(uom_to);
		return convert(from, value, to);
	}


	/**
	 * convert between a known and a named UoMs
	 * 
	 * @param uom_from
	 *            source UoM ID
	 * @param value
	 *            value to convert
	 * @param uom_to
	 *            target UoM Name
	 * @return converted value
	 * @throws Exception
	 *             if UoM categories are incompatible
	 */
	public Double convert(ProductUOM from, Double value, String uom_to) {
		if (from == null)
			return null;

		ProductUOM p = searchUoM(from.getCategory_id(), uom_to);
		if (p == null)
			return null;

		// can't happen to have an incompatible conversion, as I'm searching in
		// the same category!
		try {
			return convert(from, value, p.getId());
		} catch (Exception e) {
			// should never happen
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * convert between a known and a named UoMs
	 * 
	 * @param uom_from
	 *            source UoM ID
	 * @param value
	 *            value to convert
	 * @param uom_to
	 *            target UoM Name
	 * @return converted value
	 * @throws Exception
	 *             if UoM categories are incompatible
	 */
	public Double convert(Integer uom_from, Double value, String uom_to) {
		ProductUOM from = getProductUoM(uom_from);
		return convert(from, value, uom_to);
	}
	

	/**
	 * convert to reference / base unit
	 * 
	 * @param from
	 *            Source UoM
	 * @param value
	 *            Value to convert
	 * @return converted value
	 */
	public Double convertToBase(ProductUOM from, Double value) {
		if (from == null)
			return null;

		ProductUOM to = getBaseUoM(from);
		if (to == null)
			return null;

		// can't happen to have an incompatible conversion, as I'm searching in
		// the same category!
		try {
			return from.convert(value, to);
		} catch (Exception e) {
			// should never happen
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * convert to reference / base unit
	 * 
	 * @param uom_from
	 *            Source UoM id
	 * @param value
	 *            Value to convert
	 * @return converted value
	 */
	public Double convertToBase(Integer uom_from, Double value) {
		ProductUOM from = getProductUoM(uom_from);
		return convertToBase(from, value);
	}

}
