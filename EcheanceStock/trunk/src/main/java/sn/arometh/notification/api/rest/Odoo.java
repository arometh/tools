package sn.arometh.notification.api.rest;

import java.util.List;

import org.apache.xmlrpc.XmlRpcException;

import sn.arometh.notification.entity.Product;
import sn.arometh.notification.entity.QuerySearch;

public interface Odoo {

	/**
	 * Retourne la liste d'entity
	 * @return
	 * @throws XmlRpcException 
	 */
	public List<Object> searchEntity(QuerySearch pQuery) throws XmlRpcException;
	
}
