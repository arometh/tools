package sn.arometh.notification.business;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import fr.extelia.gdf.sae.commons.ConstantFunctionnals;
import fr.extelia.gdf.sae.commons.SendMail;
import fr.extelia.gdf.sae.commons.Tools;
import fr.extelia.gdf.sae.dao.DAO;
import fr.extelia.gdf.sae.entity.DocsExport;

//@Transactional(readOnly=true)
public class Business {	
	private static Logger logger = Logger.getLogger(Business.class);		
	private DAOImpl dao ;
	
	public Business(boolean isTest) {
		dao = new DAOImpl(isTest);
	}
	
	/**
	 * Permet de créer dans la base de données l’entrée correspondante au couple <méta-données (metadata), pdf (images)>
		<br> -	Attribution d’un numId.
		<br> -	Extraction de la prestation de metadata
		<br> -	Date de création et de mise à jour égales à la date courante.
		<br> -	Mise du status à 1.
		<br> -	Peuplement du clob avec metadata.
		<br> -	Peuplement du blob avec images.
	 * @param doc
	 * @return
	 */
	public boolean createDocument(DocsExport doc){		
		try {
			return dao.insertion(doc);	
		}catch(Exception e){
			logger.error("######## Erreur creation document " + doc.toString(), e);
			return false;
		}
	}

	/**
	 * Cherche le documents matchant à l'id de document.
	 * @param numId
	 * @return
	 */
	public DocsExport getDocumentByNumId(String numId){		
		try {
			logger.info("###################### numID ==> " + numId);
			return dao.rechercheDocExportParNumId(numId);
		}catch(Exception e){
			logger.error("Erreur : ", e);
			return null;
		}		
	}
	
	/**
	 * Cherche le ou les documents matchant la liste des identifiants.
	 * @param numIds
	 * @return
	 */
	public List<DocsExport> getDocumentsByNumIds(List<String> numIds) {
		List<DocsExport> docs = new ArrayList<DocsExport>();
		for(String numId : numIds){
			docs.add(dao.rechercheDocExportParNumId(numId));
		}
		return docs;
	}
	
	/**
	 * Retourne les documents dont le status est status
	 * sans les pieces jointes qui sont lourdes a charger
	 * 
	 * @param status
	 * @return DocsExport[]
	 */
	public List<DocsExport> getDocumentByStatus(int status) {
		try {
			List<DocsExport> docs = dao.rechercheDocExportParStatus(status);
			return docs;
		}catch(Exception e){
			logger.error("Erreur : ", e);
			return null;
		}
	}
	
	
	/**
	 * Retourne les documents dont le status est status
	 * @param status
	 * @return DocsExport[]
	 */
	public List<DocsExport> getDocumentByStatusSansPiecesJointes(int status) {
		try {
			List<DocsExport> docs = dao.rechercheDocExportParStatusSansPiencesJointes(status);
			return docs;
		}catch(Exception e){
			logger.error("Erreur : ", e);
			return null;
		}
	}
	
	/**
	 * Cherche le ou les documents matchant la liste des status
	 * @param status
	 * @return la liste des documents correspondant à la liste des prestations status.
	 */
	public List<DocsExport> getDocumentByStatus(int[] statuts, int limit) {
		List<DocsExport> retour = new ArrayList<DocsExport>();
		try {
			int nbResultatsMax = limit;
			b: for(int statut : statuts){
				if(limit == 0){
					retour.addAll(getDocumentByStatus(statut));
				}else{
					retour.addAll(dao.rechercheDocExportParStatusLimit(nbResultatsMax, statut));
					
					// le nombre de resultat a ajouter a la liste pour atteindre la limite est:
					nbResultatsMax = limit - retour.size();
					
					// sortir de cette boucle si on atteint le nombre limite de résultat
					if(retour.size() >= limit){
						break b;
					}
				}
			}
		}catch(Exception e){
			logger.error("Erreur : ", e);
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));		
			SendMail.sendMail("Erreur lors de la vacation de l'export, \n\n\n"+ sw.toString());			
			
		}
		return retour;
	}
	/**
	 * Cherche le ou les documents matchant la liste des status
	 * @param status
	 * @return la liste des documents correspondant à la liste des prestations status.
	 */
	public List<DocsExport> getDocumentByStatus(int status, int limit) {
		try {
			List<DocsExport> docs = dao.rechercheDocExportParStatusLimit(limit, status);
			return docs;
		}catch(Exception e){
			logger.error("Erreur : ", e);
			return null;
		}
	}	
	/**
	 * Retourne les documents dont la prestation est prestation
	 * @param prestation
	 * @return
	 */
	public List<DocsExport> getDocumentsByPrestation(int prestation) {
		try {
			List<DocsExport> docs = dao.rechercheDocExportParPrestation(0, prestation);
			return docs;
		}catch(Exception e){
			logger.error("Erreur : ", e);
			return null;
		}
	}
	
	/**
	 * Retourne les documents dont la prestation est prestation
	 * @param prestation
	 * @return
	 */
	public List<DocsExport> getDocumentsByLotId(String lotId) {
		try {
			List<DocsExport> docs = dao.rechercheDocExportParLotId(0, lotId);
			return docs;
		}catch(Exception e){
			logger.error("Erreur : ", e);
			return null;
		}
	}
	/**
	 * 
	 * @param lotId
	 * @return
	 */
	public int getNBDocumentByLotId(String lotId){
		//return getDocumentsByLotId(lotId).size();
		try {
			return dao.rechercheNBDocExportParLotId(lotId);
		} catch(Exception e) {
			logger.error("Erreur : ", e);
			return -1;
		}
	}
	
	/**
	 * Retourne les numeros de documents dont le lot est lotId
	 * 
	 * @param lotId identifiant du lot
	 * @return liste des identifiants du lot
	 */
	public List<String> getNumIdsByLotId(String lotId) {
		try {
			List<String> docs = dao.rechercheNumIdsParLotId(0, lotId);
			return docs;
		} catch(Exception e) {
			logger.error("Erreur : ", e);
			return null;
		}
	}
	
	/**
	 * Permet de mettre à jour un document apres changement de son status.
	 * 
	 * @param doc
	 * @param setPJ a mettre a true si on souhaite mettre a jour les pieces jointes , pdf et xml
	 * @return
	 */
	public boolean updateDocumentAndCahngeStatus(DocsExport doc, boolean setPJ) {
		boolean retour = false;		
		try {
			//DocsExport doc = dao.rechercheDocExportParNumId(numId);
			logger.info("############### document => " + doc.getNumId() + ", status avant => " + doc.getStatus());
			switch(doc.getStatus()){			
				case ConstantFunctionnals.STATUS_A_EXPORTER:
					doc.setStatus(ConstantFunctionnals.STATUS_EXPORTER);				
					break;
					
				case ConstantFunctionnals.STATUS_A_REEXPORTER:
					doc.setStatus(ConstantFunctionnals.STATUS_REEXPORTER);
					break;
					
				case ConstantFunctionnals.STATUS_A_SIMPLIFIER:
					doc.setStatus(ConstantFunctionnals.STATUS_SIMPLIFIER);
					break;	
										
				case ConstantFunctionnals.STATUS_SIMPLIFIER:
					doc.setStatus(ConstantFunctionnals.STATUS_REEXPORTER);
					break;
					
				case ConstantFunctionnals.STATUS_ERROR:
					doc.setStatus(ConstantFunctionnals.STATUS_A_EXPORTER);
					break;	
					
				/*	
				case ConstantFunctionnals.STATUS_A_REVIDEOCODER:
					doc.setStatus(ConstantFunctionnals.STATUS_REVIDEOCODER);
					break;
					
				case ConstantFunctionnals.STATUS_REVIDEOCODER:
					doc.setStatus(ConstantFunctionnals.STATUS_REEXPORTER);
					break;
				*/
										
				case ConstantFunctionnals.STATUS_EXPORTER:
					break;
				case ConstantFunctionnals.STATUS_REEXPORTER:
					break;
				case ConstantFunctionnals.STATUS_INTEGRER :
					break;	
				case ConstantFunctionnals.STATUS_A_PURGER :
					break;	
				default:
					logger.warn("ATTENTION lot " + doc.getNumId() + " probleme eventuel a investiguer.");
					break;
			}
			
			retour = dao.miseAJour(doc, setPJ);
			logger.info("############### document => " + doc.getNumId() + ", status apres update => " + doc.getStatus());
		}catch(Exception e){			
			logger.error("Erreur : ", e);
		}
		return retour;		
	}
	
	/**
	 *  Permet de mettre à jour un document sans changer le statut status.
	 *  
	 * @param doc
	 * @param setPJ a mettre a true si on souhaite mettre a jour les pieces jointes , pdf et xml
	 * @return
	 */
	public boolean updateDocument(DocsExport doc, boolean setPJ) {
		boolean retour = false;		
		try {
			retour = dao.miseAJour(doc, setPJ);			
		}catch(Exception e){			
			logger.error("Erreur : ", e);
		}
		return retour;		
	}
	
	/**
	 * 
	 * @param numId
	 * @param status
	 * @return
	 */
	public boolean updateDocumentStatusByNumId(String numId, int status) {
		boolean retour = false;		
		try {
			retour = dao.miseAJourStatusByNumId(numId, status);
		} catch(Exception e) {			
			logger.error("Erreur : ", e);
		}
		return retour;	
	}
				
	/**	 
	 * 
	 * @param docs
	 * @param lot_id
	 * @param status
	 * @return
	 */
	public boolean updateOtherDocuments(List<DocsExport> docs, String lot_id, int status) {
		boolean retour = false;		
		try {
			retour = dao.miseAJourStatusOther(docs, lot_id, status);			
		}catch(Exception e){			
			logger.error("Erreur : ", e);
		}
		return retour;		
	}
	
	
	public HashMap<String, String> getOtherDocuments(List<DocsExport> docs, String lot_id) {		
		try {						
			return dao.getOther(docs, lot_id);			
		}catch(Exception e){			
			logger.error("Erreur : ", e);
			return null;
		}			
	}
	
	public boolean deleteDocExport(DocsExport doc){
		try {
			return dao.suppression(doc);
		}catch(Exception e){
			logger.error("Erreur : ", e);
			return false;
		}
	}
	
	/**
	 * Permet de purger tous les documents ayant le status 11 dans la table DOCS_EXPORT.
	 * @throws ParseException 
	 */
	public boolean purgeDocuments(int NB_JOUR_CLEAN) throws ParseException {
		boolean retour = false;
		try {
			List<DocsExport> docs = getDocumentByStatusSansPiecesJointes(ConstantFunctionnals.STATUS_A_PURGER);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			logger.info("############### nombre de document avec le status 11 -> " + docs.size());
			for(DocsExport doc : docs){
				if(Tools.getNbJour(doc.getDateUpdate(), formatter.parse(formatter.format(new Date()))) >= NB_JOUR_CLEAN){
					dao.suppression(doc);
				}				
			}
			
			retour = true;
		}catch(Exception e){
			logger.error("Erreur : ", e);
		}
		return retour;
	}
		
	public boolean removeDocExport(DocsExport doc){
		return deleteDocExport(doc);
	}
	
	public int getNbDocuments(int[] status){
		return dao.recupereTousLesDocExport(status);
	}
	
	public int getNbDocumentsAExporter(){
		return dao.recupereTousLesDocAExport();
	}	
}
