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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * OpenERP Read result record
 * 
 * @author Marco Dieckhoff, BREMSKERL-REIBBELAGWERKE Emmerling GmbH & Co. KG
 */
public class OdooRecord {

	final String XMLRPCDATEFORMAT = "yyyy-MM-dd HH:mm:ss";

	private HashMap<String, Object> record;
	private HashMap<String, Object> original;

	/**
	 * create new "empty" OpenERP result record
	 */
	public OdooRecord() {
		record = new HashMap<String, Object>();
		original = new HashMap<String, Object>();
	}

	/**
	 * create new prefilled OpenERP result record (for copy purposes)
	 * 
	 * @param rc
	 *            Record dataset
	 * @param orig
	 *            Original dataset
	 */
	protected OdooRecord(HashMap<String, Object> rc,
			HashMap<String, Object> orig) {
		record = rc;
		original = orig;
	}

	/**
	 * create new OpenERP result record
	 * 
	 * @param readresult
	 *            data set part derived from OpenERP.read
	 */
	@SuppressWarnings("unchecked")
	// did try catch ClassCastException
	public OdooRecord(Object readresult) {
		record = new HashMap<String, Object>();

		// try converting to HashMap
		if (readresult != null) {
			try {
				record = (HashMap<String, Object>) readresult;
				convertRelated();
			} catch (ClassCastException e) {
				// nothing to do. record stays null.
				// TODO maybe throw an exception? there will be data missing
				// from the results...
			}
		}

		original = (HashMap<String, Object>) record.clone();
	}

	/**
	 * copy this object
	 * 
	 * Records will be clones, keep that in mind regarding attached objects
	 */
	public OdooRecord copy() {

		@SuppressWarnings("unchecked")
		HashMap<String, Object> newR = (HashMap<String, Object>) this.record
				.clone();

		@SuppressWarnings("unchecked")
		HashMap<String, Object> newO = (HashMap<String, Object>) this.original
				.clone();

		OdooRecord newRecord = new OdooRecord(newR, newO);

		return newRecord;
	}

	/**
	 * @deprecated replaced by copy, just here for compatibility reasons. 
	 * Did not do real cloning() anyway :) Now returns a copy().
	 * @see copy() function 
	 */
	public Object clone() throws CloneNotSupportedException {
		return this.copy();
	}
	
	/**
	 * converts the related fields (id, name) to overwritten id and _NAME field
	 */
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

	/**
	 * @return string representation of the internal HashMap storage
	 */
	public String toString() {
		if (record == null)
			return null;
		return record.toString();
	}

	/**
	 * @return the internal HashMap storage
	 */
	public HashMap<String, Object> toHashMap() {
		return record;
	}

	/**
	 * returns if the key is available
	 * 
	 * @param key
	 * @return true if key is in this record (value may still be empty, though)
	 */
	public boolean containsKey(String key) {
		if (record == null)
			return false;
		return record.containsKey(key);
	}

	/**
	 * returns a specific key from the internal hashMapStorage
	 * 
	 * @param key
	 * @return value object
	 */
	public Object get(String key) {
		if (record == null)
			return null;
		return record.get(key);
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for the key, the old value is
	 * replaced.
	 * 
	 * @param key
	 *            - key with which the specified value is to be associated
	 * @param value
	 *            - value to be associated with the specified key
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key. (A null return can also indicate that the map
	 *         previously associated null with key.)
	 */
	public Object put(String key, Object value) {
		return record.put(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for the key, the old value is
	 * replaced.
	 * <p>
	 * Timestamp values are converted into format yyyy-mm-dd hh:ii:ss.
	 * 
	 * @param key
	 *            - key with which the specified value is to be associated
	 * @param ts
	 *            - value to be associated with the specified key as timestamp
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key. (A null return can also indicate that the map
	 *         previously associated null with key.)
	 */
	public Object put(String key, Timestamp ts) {
		if (ts == null)
			return record.put(key, null);
		SimpleDateFormat sdf = new SimpleDateFormat(XMLRPCDATEFORMAT);
		java.util.Date dt = new java.util.Date(ts.getTime());
		return record.put(key, sdf.format(dt));
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for the key, the old value is
	 * replaced.
	 * <p>
	 * Date values are converted into format yyyy-mm-dd hh:ii:ss.
	 * 
	 * @param key
	 *            - key with which the specified value is to be associated
	 * @param dt
	 *            - value to be associated with the specified key as date
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key. (A null return can also indicate that the map
	 *         previously associated null with key.)
	 */
	public Object put(String key, java.sql.Date dt) {
		if (dt == null)
			return record.put(key, null);
		SimpleDateFormat sdf = new SimpleDateFormat(XMLRPCDATEFORMAT);
		java.util.Date udt = new java.util.Date(dt.getTime());
		return record.put(key, sdf.format(udt));
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for the key, the old value is
	 * replaced.
	 * <p>
	 * Date values are converted into format yyyy-mm-dd hh:ii:ss.
	 * 
	 * @param key
	 *            - key with which the specified value is to be associated
	 * @param dt
	 *            - value to be associated with the specified key as date
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key. (A null return can also indicate that the map
	 *         previously associated null with key.)
	 */
	public Object put(String key, java.util.Date dt) {
		if (dt == null)
			return record.put(key, null);
		SimpleDateFormat sdf = new SimpleDateFormat(XMLRPCDATEFORMAT);
		return record.put(key, sdf.format(dt));
	}

	/**
	 * returns the list of keys available in this record
	 * 
	 * @return the list of keys available in this record
	 */
	public Set<String> keySet() {
		return record.keySet();
	}

	/**
	 * adds an OpenERPRecord as substring as relation.keynames
	 * 
	 * @param string
	 *            relation. name to use
	 * @param insert
	 *            record to add
	 */
	public void relate(String relation, OdooRecord insert) {
		if (relation == null || insert == null)
			return;

		Set<String> keys = insert.keySet();
		Iterator<String> iKeys = keys.iterator();
		while (iKeys.hasNext()) {
			String key = iKeys.next();
			record.put(relation + "." + key, insert.get(key));
		}
	}

	/**
	 * adds an OpenERPRecord as substring as relation_SET for one2many/many2many
	 * relationships
	 * 
	 * @param relation
	 * @param subset
	 *            OpenERPRecordSet to insert
	 */
	public void relate(String relation, OdooRecordSet subset) {
		record.put(relation + "_SET", subset);
	}

	/**
	 * gets the requested key value as String
	 * 
	 * @param key
	 *            - key for value to return
	 * @param errorreplacement
	 *            - replace fault/non-String values with this (e.g. "")
	 * @return String or error replacement
	 */
	public String getString(String key, String errorreplacement) {
		String value = errorreplacement;
		try {
			value = (String) record.get(key);
		} catch (ClassCastException ccE) {
			value = errorreplacement;
		}
		return value;
	}

	/**
	 * gets the requested key value as String
	 * 
	 * @param key
	 *            - key for value to return
	 * @return String or null
	 */
	public String getString(String key) {
		return getString(key, null);
	}

	/**
	 * gets the requested key value as Integer
	 * 
	 * @param key
	 *            - key for value to return
	 * @param errorreplacement
	 *            - replace fault/non-Integer values with this (e.g. 0 or
	 *            Integer.MIN_VALUE)
	 * @return Integer or error replacement
	 */
	public Integer getInteger(String key, Integer errorreplacement) {
		Integer value = errorreplacement;
		try {
			value = (Integer) record.get(key);
		} catch (ClassCastException ccE) {
			value = errorreplacement;
		}
		return value;
	}

	/**
	 * gets the requested key value as Integer
	 * 
	 * @param key
	 *            - key for value to return
	 * @return Integer or null
	 */
	public Integer getInteger(String key) {
		return getInteger(key, null);
	}

	/**
	 * gets the requested key value as Date
	 * 
	 * @param key
	 *            - key for value to return
	 * @param errorreplacement
	 *            - replace fault/non-Date values with this (e.g. new Date())
	 * @return Date or error replacement
	 */
	public Date getDate(String key, Date errorreplacement) {
		Date value = errorreplacement;
		try {
			value = (Date) record.get(key);
		} catch (ClassCastException ccE) {
			value = errorreplacement;
		}
		return value;
	}

	/**
	 * gets the requested key value as Date
	 * 
	 * @param key
	 *            - key for value to return
	 * @return Date or null
	 */
	public Date getDate(String key) {
		return getDate(key, null);
	}

	/**
	 * gets the requested key value as Number
	 * 
	 * @param key
	 *            - key for value to return
	 * @param errorreplacement
	 *            - replace fault/non-Number values with this (e.g. 0 or
	 *            Integer.MIN_VALUE)
	 * @return Number or error replacement
	 */
	public Number getNumber(String key, Number errorreplacement) {
		Number value = errorreplacement;
		try {
			value = (Number) record.get(key);
		} catch (ClassCastException ccE) {
			value = errorreplacement;
		}

		return value;
	}

	/**
	 * gets the requested key value as Number
	 * 
	 * @param key
	 *            - key for value to return
	 * @return Number
	 */
	public Number getNumber(String key) {
		return getNumber(key, null);
	}

	/**
	 * gets the requested key value as Double
	 * 
	 * @param key
	 *            - key for value to return
	 * @param errorreplacement
	 *            - replace fault/non-Double values with this (e.g. 0.0 or
	 *            Double.MIN_VALUE)
	 * @return Double or error replacement
	 */
	public Double getDouble(String key, Double errorreplacement) {
		Double value = errorreplacement;
		try {
			value = (Double) record.get(key);
		} catch (ClassCastException ccE) {
			value = errorreplacement;
		}
		return value;
	}

	/**
	 * gets the requested key value as Double
	 * 
	 * @param key
	 *            - key for value to return
	 * @return Double or null
	 */
	public Double getDouble(String key) {
		return getDouble(key, null);
	}

	/**
	 * gets the requested key value as Boolean
	 * 
	 * @param key
	 *            - key for value to return
	 * @param errorreplacement
	 *            - replace fault/non-Double values with this (null, true or
	 *            false)
	 * @return Boolean or error replacement
	 */
	public Boolean getBoolean(String key, Boolean errorreplacement) {
		Boolean value = errorreplacement;
		try {
			value = (Boolean) record.get(key);
		} catch (ClassCastException ccE) {
			value = errorreplacement;
		}
		return value;
	}

	/**
	 * gets the requested key value as Boolean
	 * 
	 * @param key
	 *            - key for value to return
	 * @return Boolean or null
	 */
	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	/**
	 * coalesce returns the first not-null statement. In this simple case, it'll
	 * return the second statement if the first is null.
	 * 
	 * @param o1
	 *            Object/entity 1
	 * @param o2
	 *            Object/entity 2
	 * @return (o1 != null) ? o1 : o2;
	 */
	public <E> E coalesce(E o1, E o2) {
		return (o1 != null) ? o1 : o2;
	}

	public HashMap<String, Object> toCreate() {
		HashMap<String, Object> c = new HashMap<String, Object>();

		Set<String> keys = record.keySet();
		Iterator<String> iKeys = keys.iterator();
		while (iKeys.hasNext()) {
			String key = iKeys.next();

			// continue when encountering additional information and related
			// sets
			if (key.endsWith("_NAME"))
				continue;
			if (key.endsWith("_SET"))
				continue;
			if (key.contains("."))
				continue;

			// do not use NULL values, simply submit nothing instead
			if (record.get(key) == null)
				continue;

			c.put(key, record.get(key));
		}

		return c;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> toUpdate() {
		HashMap<String, Object> u = new HashMap<String, Object>();

		if (original.keySet().size() == 0) {
			// never loaded from OpenERP: this OpenERPRecord was created for the
			// purpose of updating only

			// remove id, if present: we don't need this for update purposes
			record.remove("id");

			u = (HashMap<String, Object>) record.clone();

		} else {

			Set<String> keys = original.keySet();
			Iterator<String> iKeys = keys.iterator();
			while (iKeys.hasNext()) {
				String key = iKeys.next();

				if (key.equals("id"))
					continue;

				Object o = original.get(key);
				Object r = record.get(key);

				if (!o.equals(r))
					u.put(key, r);
			}
		}

		return u;
	}

}
