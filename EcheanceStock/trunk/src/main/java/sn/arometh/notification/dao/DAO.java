package sn.arometh.notification.dao;


import java.util.List;

public interface DAO {

	/**
	 * Fait une recherche dans la base de données
	 * @param <T>
	 * @return
	 */
	public <T> List<T> searchStockAlert();
	
}
