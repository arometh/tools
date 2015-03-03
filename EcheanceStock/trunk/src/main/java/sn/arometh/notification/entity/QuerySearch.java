package sn.arometh.notification.entity;

import java.util.List;
import java.util.Map;

public class QuerySearch {
	/** objet recherche */
	private String object;
	
	/** methode rerecherche */
	private String method;
	
	/** query de recherche */
	private List<String> query;
	
	/** field recherche */
	private Map<String, List<String>> result;

	/**
	 * @return the object
	 */
	public String getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(String object) {
		this.object = object;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the query
	 */
	public List<String> getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(List<String> query) {
		this.query = query;
	}

	/**
	 * @return the result
	 */
	public Map<String, List<String>> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Map<String, List<String>> result) {
		this.result = result;
	}

	
}
