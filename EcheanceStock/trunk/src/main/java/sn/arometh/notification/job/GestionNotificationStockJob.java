package sn.arometh.notification.job;

import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sn.arometh.notification.bean.GestionNotificationStockBean;
import sn.arometh.notification.business.Business;
import sn.arometh.notification.commons.ConstantFunctionnals;
import sn.arometh.notification.commons.SendMail;

public class GestionNotificationStockJob extends QuartzJobBean implements StatefulJob, ConstantFunctionnals{

    private static Logger logger = Logger.getLogger(GestionNotificationStockJob.class);
                                                    
    private Business bussiness;
    
    private GestionNotificationStockBean gestionNotificationBean;
    
    
    public GestionNotificationStockJob() {
    	logger.debug("############ Constructeur: Creation d'un nouveau bean pour la gestion des Notification.");
    	gestionNotificationBean = new GestionNotificationStockBean();
    	try {
			bussiness = new Business(false);
		} catch (ClassNotFoundException e) {
			logger.error("ERROR", e);
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error("ERROR", e);
			e.printStackTrace();
		}
	}

	@Override
    protected void executeInternal(JobExecutionContext pArg0) throws JobExecutionException {
        logger.debug("############ Execution du scheduler de gestion des notifications => " + gestionNotificationBean.getNameScheduler());
        String stockNotification = bussiness.rechercherStockAlert();        
        if(Boolean.getBoolean(VAR_NOTIFICATION_CONFIGURATION_STOP_MAIL)){
           //envoie de mail
            try {
                if(null != stockNotification){
                    SendMail.sendMail(VAR_NOTIFICATION_CONFIGURATION_EMAIL_SUJET_NOTIFICATION,stockNotification,VAR_NOTIFICATION_CONFIGURATION_EMAIL_LISTE_ENVOIE);
                    logger.info(stockNotification);
                }else {
                    SendMail.sendMail(VAR_NOTIFICATION_CONFIGURATION_EMAIL_SUJET_NOTIFICATION_RAS,VAR_NOTIFICATION_BUSINESS_RAS_STOCK_MESSAGE_ALERT,VAR_NOTIFICATION_CONFIGURATION_EMAIL_LISTE_ENVOIE);
                    logger.info(VAR_NOTIFICATION_BUSINESS_RAS_STOCK_MESSAGE_ALERT);
                }
                } catch (AddressException e) {
                logger.error("Error", e);
                e.printStackTrace();
            } catch (MessagingException e) {
                logger.error("Error", e);
                e.printStackTrace();
            }
        }
    }

}
