package sn.arometh.notification.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sn.arometh.notification.commons.ConstantFunctionnals;

public class NotifStockJob extends QuartzJobBean implements StatefulJob, ConstantFunctionnals{

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}
	/*private static Logger logger = Logger.getLogger(NotifStockJob.class);	
	private SAEBusiness saeCleannerBusiness;	
	private CleannerBean cleannerBean;
	private static int NB_JOUR_PURGE = 30;
	private static int NB_JOUR_CLEAN = 30;
	
	public NotifStockJob() {
		logger.debug("############ Constructeur: Creation d'un nouveau bean de Clean.");
		try {			
			NB_JOUR_CLEAN = Integer.parseInt(SAEProperties.getPropertiesValues("sae.delai.retention.clean"));
			NB_JOUR_PURGE = Integer.parseInt(SAEProperties.getPropertiesValues("sae.delai.retention.purge"));
		} catch (Exception e) {
			logger.error("Erreur, ", e);
		}
		
		saeCleannerBusiness = new SAEBusiness(false);
		cleannerBean = new CleannerBean();
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			logger.info("Scheduler de purge -> " + cleannerBean.getNameScheduler());
			
			// quand on met les documents en stutut 11 (a purger) la date d'update change
			// donc le nb jours pour supprimer les anciens documents est NB_JOUR_CLEAN - NB_JOUR_PURGE
			
			if(! saeCleannerBusiness.purgeDocuments(NB_JOUR_CLEAN - NB_JOUR_PURGE)){
				logger.error("################# Erreur lors du nettoyage de la base. Suppression des documents dont le statut est à 11");
			}else {
				logger.info("################# les documents ayant le statut 11 ont ete supprimes.");
			}		
		}catch(Exception e){
			logger.error("############# Erreur de convertion de la date du jour",e);
		}
		
	}

	public SAEBusiness getSaeCleannerBusiness() {
		return saeCleannerBusiness;
	}

	public void setSaeCleannerBusiness(SAEBusiness saeCleannerBusiness) {
		this.saeCleannerBusiness = saeCleannerBusiness;
	}
	
	public CleannerBean getCleannerBean() {
		return cleannerBean;
	}

	public void setCleannerBean(CleannerBean cleannerBean) {
		this.cleannerBean = cleannerBean;
	}	*/
}
