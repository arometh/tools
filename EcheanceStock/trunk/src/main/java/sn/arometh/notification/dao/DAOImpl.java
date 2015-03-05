package sn.arometh.notification.dao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sn.arometh.notification.commons.ConstantFunctionnals;
import sn.arometh.notification.entity.Stock;

public class DAOImpl implements DAO, ConstantFunctionnals{
	private static Logger logger = Logger.getLogger(DAOImpl.class);
	
	private String quantiteStock;
	private String echeancePeriode;
	private String[] whIDStocks;
	private String[] whIDSuppliers;
	private String[] whIDCustomer;
	
	/** connection*/
	private Connection con;
	
	public DAOImpl(boolean isTest) throws ClassNotFoundException, SQLException {
		if(isTest){
		    Class.forName("org.postgresql.Driver");
            con =  DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/arometh", "openpg",
                "openpgpwd");
            quantiteStock = "5";
            echeancePeriode = "10";
		}else {
		    quantiteStock = VAR_NOTIFICATION_BDD_ECHEANCE_QUANTITE;
		    echeancePeriode = VAR_NOTIFICATION_BDD_ECHEANCE_PERIODE;
		    //whIDStocks = VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_STOCK.split(";");
		    //whIDSuppliers = VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_SUPPLIER.split(";");
		    //whIDCustomer = VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_CUSTOMER.split(";");		    
		}
	}

	
	/**
	 * connection via datasource
	 * @return
	 */
	public Connection getCon() {
		if(con == null){
			Context ctx;
			try {
				ctx = new InitialContext();
				DataSource source = (DataSource)ctx.lookup("java:comp/env/jdbc/saeDs");
				
				logger.debug("### source => " + source);
				setCon(source.getConnection());
				logger.debug("### source => " + con);
			} catch (Exception e) {
				logger.error("Erreur de connexion a la datasource.", e);
				
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));		
				//SendMail.sendMail("Erreur lors de la connexion a la base SAE: \n\n\n"+ sw.toString());		
			}	
		}
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	/**
	 * Connection via datasource
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public Connection getConnectionDatasource() throws NamingException, SQLException {
		Context ctx = new InitialContext();
		DataSource source = (DataSource)ctx.lookup("java:comp/env/jdbc/echeanceNotificationDS");
		
		
		logger.info("### source => " + source);
		Connection connection = source.getConnection();
		logger.info("### source => " + connection);
		return connection;
	}

	/**
	 * Connection à la base de donnees
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
	    if(con == null) {
	        Class.forName("org.postgresql.Driver");
		    con =  DriverManager.getConnection(
				"jdbc:postgresql://" + VAR_NOTIFICATION_BDD_MACHINE + ":" + VAR_NOTIFICATION_BDD_PORT + "/" + VAR_NOTIFICATION_BDD_BDD + "", VAR_NOTIFICATION_BDD_USER,
				VAR_NOTIFICATION_BDD_PASSWORD);
	    }
	    return con;
	    		
	}
	/**
	 * Recupere les stocks dont la quantie a atteint une limite
	 * @return
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<Stock> searchStockAlert() {
		List<Stock> stockLimite = null;
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			//TODO à verifier
			stockLimite= new ArrayList<Stock>();
	
			select = getConnection().prepareStatement(VAR_NOTIFICATION_BDD_QUERY_STOCK_ECHEANCE_ALERTE);
			select.setInt(1, Integer.parseInt(quantiteStock));
			select.setInt(2, Integer.parseInt(echeancePeriode));  
			rs = select.executeQuery();
			
			Stock stock;
			while (rs.next()) {	
				stock = new Stock();
				//stock.setNomProduit(rs.getString("nom_produit"));
				//stock.setEmplacement(rs.getString("emplacement"));
				//stock.setQuantityStock(rs.getString("quantite"));
				stock.setDateExpirationStock(rs.getString("date_stock"));
				stockLimite.add(stock);
			}
		}catch(Exception e) {
			logger.error("Erreur : ", e);						
		} finally {	
			if (rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
					e.printStackTrace();
				}
				rs = null;
			}
			if (select != null){
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
					e.printStackTrace();
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
					e.printStackTrace();
				}
				con = null;
			}
		}
		return stockLimite;
	}


    /**
     * @return the quantiteStock
     */
    public String getQuantiteStock() {
        return quantiteStock;
    }


    /**
     * @param pQuantiteStock the quantiteStock to set
     */
    public void setQuantiteStock(String pQuantiteStock) {
        quantiteStock = pQuantiteStock;
    }


    /**
     * @return the echeanceDate
     */
    public String getEcheanceDate() {
        return echeancePeriode;
    }


    /**
     * @param pEcheanceDate the echeanceDate to set
     */
    public void setEcheanceDate(String pEcheanceDate) {
        echeancePeriode = pEcheanceDate;
    }
		
	
}
