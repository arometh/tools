package sn.arometh.notification.dao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sn.arometh.notification.commons.ConstantFunctionnals;
import sn.arometh.notification.commons.PropertiesUtils;
import sn.arometh.notification.commons.SendMail;
import sn.arometh.notification.entity.Stock;
import sn.arometh.notification.entity.StockOutOfDate;
import sn.arometh.notification.entity.StockQuant;

public class DAOImpl implements DAO, ConstantFunctionnals{
	private static Logger logger = Logger.getLogger(DAOImpl.class);
	private Connection con;
	private boolean isTest;

	public DAOImpl(boolean isTest) {
		this.isTest = isTest;
	}

	public Connection getCon() {
		if(con == null){
			Context ctx;
			try {
				ctx = new InitialContext();
				DataSource source = (DataSource)ctx.lookup("java:comp/env/jdbc/saeDs");
				
				if(isTest){
					source = (DataSource)ctx.lookup("java:comp/env/jdbc/saeDsTest");
				}
				logger.debug("### source => " + source);
				setCon(source.getConnection());
				logger.debug("### source => " + con);
			} catch (Exception e) {
				logger.error("Erreur de connexion a la datasource.", e);
				
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));		
				SendMail.sendMail("Erreur lors de la connexion a la base SAE: \n\n\n"+ sw.toString());		
			}	
		}
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public Connection getConnectionDatasource() throws NamingException, SQLException {
		Context ctx = new InitialContext();
		DataSource source = (DataSource)ctx.lookup("java:comp/env/jdbc/saeDs");
		
		if (isTest) {
			source = (DataSource)ctx.lookup("java:comp/env/jdbc/saeDsTest");
		}
		
		logger.info("### source => " + source);
		Connection connection = source.getConnection();
		logger.info("### source => " + connection);
		return connection;
	}

	@Override
	public Connection getConnection(String pMachine, String pBase, String pPort, String pUser, String pPassword) throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(
				"jdbc:postgresql://" + pMachine + ":" + pPort + "/" + pBase + "", pUser,
				pPassword);
		
	}
	/**
	 * Recupere les stocks dont la quantie a atteint une limite
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> rechercheStockAlert(boolean isQuantite) {
		List<T> stockLimite = null;
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			//TODO à completer
			String query = "";
			if(isQuantite){
				//TODO Query pour recuperer les stocks par quantite
				stockLimite= (List<T>) new ArrayList<StockQuant>();
				query = "";
			}else {
				//TODO Query pour recuperer les stocks par date d'expiration
				stockLimite= (List<T>) new ArrayList<StockOutOfDate>();
				query = "";
			}
			select = getCon().prepareStatement(query);
						
			rs = select.executeQuery();
			
			loadStock(rs);
		}catch(Exception e) {
			logger.error("Erreur : ", e);
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur lors de la vacation de l'export, \n\n\n"+ sw.toString());		
			
		} finally {	
			if (rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null){
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return stockLimite;
	}
	
	/**
	 * 
	 * @param pResultSet
	 * @return
	 * @throws SQLException
	 */
	private <T> List<T>  loadStock(ResultSet pResultSet) throws SQLException {
		List<T>  stockLimite= (List<T>) new ArrayList<StockQuant>();
		Stock stock;
		while (pResultSet.next()) {	
			stock = new Stock();
			//TODO remplir les champs lies au stock
			stockLimite.add((T) stock);
		}
		return stockLimite;
	}
/*	
	public List<DocsExport> rechercheDocExportParStatusLimit(int limit, int status){
		List<DocsExport> retour =  new ArrayList<DocsExport>();
		String query = "SELECT numero_id FROM DOCS_EXPORT with (nolock) WHERE status=?";
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			select = getCon().prepareStatement(query);
			select.setInt(1, status);
			select.setMaxRows(limit);			
			rs = select.executeQuery();
			
			logger.info( "########################### limit des documents a récupérer = "+limit);
			
			int i = 0;
			b: while (rs.next()) {	
				if (i< limit){
					String numero_id = rs.getString("numero_id");				
					retour.add(rechercheDocExportParNumId(numero_id));
					i++;
				}else {
					break b;
				}
				
			}
		} catch(Exception e) {
			logger.error("Erreur : ", e);
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur lors de la vacation de l'export, \n\n\n"+ sw.toString());		
			
		} finally {	
			if (rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null){
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return retour;
	}
		
	public List<DocsExport> rechercheDocExportParStatus(int status){
		List<DocsExport> retour =  new ArrayList<DocsExport>();
		String query = "SELECT numero_id FROM DOCS_EXPORT with (nolock) WHERE status=?";
		PreparedStatement select = null;		
		ResultSet rs = null;		
		try {
			select = getCon().prepareStatement(query);
			select.setInt(1, status);
			rs = select.executeQuery();
			while (rs.next()) {
				String numero_id = rs.getString("numero_id");
				retour.add(rechercheDocExportParNumId(numero_id));								
			}
		}catch(Exception e){
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			
			logger.error("Erreur : ", e);
		}finally{	
			if (rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null){
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}						
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return retour;
	}
	
	*//**
	 * recherche des documents export sans les pieces jointes qui sont lourdes a charger
	 * 
	 * @param status
	 * @return
	 *//*
	public List<DocsExport> rechercheDocExportParStatusSansPiencesJointes(int status){
		List<DocsExport> retour =  new ArrayList<DocsExport>();
		String query = "SELECT  numero_id, date_creation, date_update, prestation, status, lot_id " +
				       "FROM DOCS_EXPORT with (nolock) WHERE status=?";
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			select = getCon().prepareStatement(query);
			select.setInt(1, status);
			rs = select.executeQuery();
			while (rs.next()) {
				retour.add(new DocsExport(rs.getString("numero_id"), rs.getTimestamp("date_creation"), rs.getTimestamp("date_update"), rs.getInt("prestation"), null, null , rs.getInt("status"), rs.getString("lot_id")));
			}
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			logger.error("Erreur : ", e);
		} finally {	
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return retour;
	}
	
	public List<DocsExport> rechercheDocExportParPrestation(int limit, int prestation){
		List<DocsExport> retour =  new ArrayList<DocsExport>();
		String query = "SELECT * FROM DOCS_EXPORT with (nolock) WHERE prestation=?";
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			select = getCon().prepareStatement(query);
			select.setInt(1, prestation);
			select.setMaxRows(limit);
			rs = select.executeQuery();
			
			while (rs.next()) {
				retour.add(new DocsExport(rs.getString("numero_id"), rs.getTimestamp("date_creation"), rs.getTimestamp("date_update"), rs.getInt("prestation"), rs.getBytes("metadata"), rs.getBytes("piece_jointe"), rs.getInt("status"), rs.getString("lot_id")));
				
			}
		}catch(Exception e){
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			
			logger.error("Erreur : ", e);
		}finally{	
			if (rs != null)
			{
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return retour;
	}
	
	public int rechercheNBDocExportParLotId(String lotId) {
		String query = "SELECT count(numero_id) FROM DOCS_EXPORT with (nolock) WHERE lot_id=?";
		PreparedStatement select = null;
		ResultSet rs = null;
		int resultat = 0;
		try {
			select = getCon().prepareStatement(query);
			select.setString(1, lotId);			
			rs = select.executeQuery();
			if (rs.next()) {
				resultat = rs.getInt(1);
			}			             
			return resultat;
		} catch(Exception e) {			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			
			logger.error("Erreur : ", e);
			return resultat;
		} finally {	
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
	}
	
	*//**
	 * Retourne les numeros id des documents dont le lot est lotId
	 * @param limit
	 * @param lotId
	 * @return
	 *//*
	public List<String> rechercheNumIdsParLotId(int limit, String lotId) {
		List<String> retour =  new ArrayList<String>();
		String query = "SELECT numero_id FROM DOCS_EXPORT with (nolock) WHERE lot_id=?";
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			select = getCon().prepareStatement(query);
			select.setString(1, lotId);
			select.setMaxRows(limit);
			rs = select.executeQuery();
			
			while (rs.next()) {
				retour.add(rs.getString("numero_id"));
			}
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());			
			logger.error("Erreur : ", e);
			return null;
		} finally {	
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return retour;
	}
	
	public List<DocsExport> rechercheDocExportParLotId(int limit, String lotId){
		List<DocsExport> retour =  new ArrayList<DocsExport>();
		String query = "SELECT * FROM DOCS_EXPORT with (nolock) WHERE lot_id=?";
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			select = getCon().prepareStatement(query);
			select.setString(1, lotId);
			select.setMaxRows(limit);
			rs = select.executeQuery();
			
			while (rs.next()) {
				retour.add(new DocsExport(rs.getString("numero_id"), rs.getTimestamp("date_creation"), rs.getTimestamp("date_update"), rs.getInt("prestation"), rs.getBytes("metadata"), rs.getBytes("piece_jointe"), rs.getInt("status"), rs.getString("lot_id")));
			}
			//logger.info("######### query => " + query + ", lotId => " + lotId + ", i => " + i);
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			logger.error("Erreur : ", e);
			return null;
		} finally {	
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return retour;
	}
	
	public DocsExport rechercheDocExportParNumId(String numId) {
		DocsExport retour =  null;
		String query = "SELECT * FROM DOCS_EXPORT with (nolock) WHERE numero_id=?";
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			select = getCon().prepareStatement(query);
			select.setString(1, numId);
			rs = select.executeQuery();
			while (rs.next()) {
				retour = new DocsExport(rs.getString("numero_id"), rs.getTimestamp("date_creation"), rs.getTimestamp("date_update"), rs.getInt("prestation"), rs.getBytes("metadata"), rs.getBytes("piece_jointe"), rs.getInt("status"), rs.getString("lot_id"));
				logger.info("################# recherche document trouve ==> " + retour.getNumId());
			}			
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			logger.error("Erreur : ", e);
		} finally {	
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return retour;
	}
	
	public DocsExport rechercheDocExport(DocsExport doc){
		DocsExport retour =  null;
		String query = "SELECT * FROM DOCS_EXPORT with (nolock) WHERE numero_id=?";
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			//Connection conn = getConnectionDatasource();
			select = getCon().prepareStatement(query);
			select.setString(1, doc.getNumId());
			rs = select.executeQuery();
			while (rs.next()) {
				retour = new DocsExport(rs.getString("numero_id"), rs.getTimestamp("date_creation"), rs.getTimestamp("date_update"), rs.getInt("prestation"), rs.getBytes("metadata"), rs.getBytes("piece_jointe"), rs.getInt("status"), rs.getString("lot_id"));
			}
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			logger.error("Erreur : ", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return retour;
	}
	
	public boolean suppression(DocsExport doc){
		String query = "DELETE FROM DOCS_EXPORT WHERE numero_id = ?";
		PreparedStatement update = null;
		try {
			update = getCon().prepareStatement(query);
			update.setString(1, doc.getNumId());
			update.executeUpdate();
			update.close();
			logger.info("## Suppression du document OK. " + doc.toString());
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			logger.error("Erreur : ", e);
			return false;
		} finally {			
			if (update != null) {
				try {
					update.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture preparedStatement.", e);
				}
				update = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return true;
	}
	
	*//**
	 * suppresion des documents par prestation ou pas statut
	 * 
	 * @param prestationStatus
	 * @param isPrestation a true si on veut supprimer des documents par prestation 
	 * @return
	 *//*
	public boolean suppressionParStatusOuPrestation(int prestationStatus, boolean isPrestation){		
		String query;
		if (isPrestation) {
			query = "DELETE FROM DOCS_EXPORT WHERE prestation = ?";
		} else {
			query = "DELETE FROM DOCS_EXPORT WHERE status = ?";
		}
		PreparedStatement update = null;
		try {
			update = getCon().prepareStatement(query);
			update.setInt(1, prestationStatus);
			update.executeUpdate();
			update.close();
			if (isPrestation) {
				logger.debug("## Suppression du document OK. prestation " + prestationStatus);
			} else {
				logger.debug("## Suppression du document OK. status " + prestationStatus);
			}
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			logger.error("Erreur : ", e);
			return false;
		} finally {			
			if (update != null) {
				try {
					update.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture preparedStatement.", e);
				}
				update = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return true;
	}
	
	public int recupereTousLesDocAExport(){
		String query = "SELECT count(*) as nbLines FROM DOCS_EXPORT with (nolock) WHERE status IN (?,?,?)";
		PreparedStatement select = null;
		ResultSet rs = null;
		int nbLines = 0;		
		try {
			select = getCon().prepareStatement(query);
			select.setInt(1, STATUS_A_EXPORTER);
			select.setInt(2, STATUS_A_REEXPORTER);
			select.setInt(3, STATUS_SIMPLIFIER);			
			rs = select.executeQuery();
			if(rs.next())
				nbLines = rs.getInt("nbLines");
			
			return nbLines;
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			logger.error("Erreur : ", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return -1;
	}
	
	public int recupereTousLesDocExport(int[] status) {
		String query = "SELECT count(*) as nbLines FROM DOCS_EXPORT with (nolock) ";
		if (status.length > 0) {
			query += " WHERE status IN (?";
			for(int i = 0; i < status.length; i++)
				query += " ,?";
			query += ")";
		}
		PreparedStatement select = null;
		ResultSet rs = null;
		int nbLines = 0;
		
		try {
			select = getCon().prepareStatement(query);
			if(status.length > 0){
				for(int j=0; j < status.length; j++)
					select.setInt(j+1, status[j]);
			}
			rs = select.executeQuery();
			rs.next();
			nbLines = rs.getInt("nbLines");
			return nbLines;
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			logger.error("Erreur : ", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return -1;
	}

	public Counter rechercheCounterParName(String name){
		Counter retour =  null;
		String query = "SELECT * FROM Counter with (nolock) WHERE name=?";
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			select = getCon().prepareStatement(query);
			select.setString(1, name);
			rs = select.executeQuery();
			while (rs.next()) {
				retour = new Counter();
				retour.setName(rs.getString("name"));
				retour.setDate(rs.getString("date"));
				retour.setNumber(rs.getDouble("number"));
			}
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());
			logger.error("Erreur : ", e);
		} finally {	
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du resultSet ##", e);
				}
				rs = null;
			}
			if (select != null) {
				try {
					select.close();
				} catch (SQLException e) {
					logger.error("## Erreur lors de la femeture du statement ##", e);
				}
				select = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return retour;
	}
	
	public boolean insertionCounter(Counter counter){
		PreparedStatement insert = null;
		String query = "INSERT INTO Counter (name,date,number) VALUES (?,?,?)";
		try {
			insert = getCon().prepareStatement(query);
			insert.setString(1, counter.getName());
			insert.setString(2, counter.getDate());
			insert.setDouble(3, counter.getNumber());
			insert.execute();
			logger.debug("## Insertion du counter OK. ==> " + counter.getName());
		}catch(Exception e){			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());			
			logger.error("Erreur sur document : " + counter.getName(), e);
			return false;
		}finally{			
			if (insert != null) {
				try {
					insert.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture preparedStatement.", e);
				}
				insert = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return true;
	}
	
	public boolean modificationCounter(Counter counter){
		String query = "UPDATE Counter SET date = ?,number = ? where name = ?";
		PreparedStatement update = null;
		try {
			update = getCon().prepareStatement(query);			
			update.setString(1, counter.getDate());
			update.setDouble(2, counter.getNumber());
			update.setString(3, counter.getName());
			update.executeUpdate();
			logger.debug("## Mise a jour du document OK. ==> " + counter.getName());
		}catch(Exception e){			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur DAO SAE: \n\n\n"+ sw.toString());			
			logger.error("Erreur : ", e);
		}finally{			
			if (update != null) {
				try {
					update.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture preparedStatement.", e);
				}
				update = null;
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Erreur fermeture connexion.", e);
				}
				con = null;
			}
		}
		return true;
	}	*/
}
