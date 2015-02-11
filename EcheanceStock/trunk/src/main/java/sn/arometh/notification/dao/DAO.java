package sn.arometh.notification.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

public interface DAO {


	/** Connection via une datasource*/
	public Connection getConnectionDatasource() throws NamingException, SQLException;

	/** 
	 * Recupere une connexion a la base de données
	 * @param pMachine
	 * @param pBase
	 * @param pPort
	 * @param pUser
	 * @param pPassword
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection(String pMachine, String pBase, String pPort, String pUser, String pPassword) throws ClassNotFoundException, SQLException;
	/**
	 * Recupere les stocks dont la quantie a atteint une limite ou la date limite de peremption de l'article est atteint
	 * @param <T>
	 * @return
	 */
	public <T> List<T> rechercheStockAlert(boolean isQuantite);
	
}
