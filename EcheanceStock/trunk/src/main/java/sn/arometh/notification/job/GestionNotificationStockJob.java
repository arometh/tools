package sn.arometh.notification.job;
import java.net.MalformedURLException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sn.arometh.notification.bean.GestionNotificationStockBean;
import sn.arometh.notification.business.BusinessImpl;
import sn.arometh.notification.commons.ConstantFunctionnals;
import sn.arometh.notification.commons.SendMail;
import sn.arometh.notification.entity.Product;

public class GestionNotificationStockJob extends QuartzJobBean implements StatefulJob, ConstantFunctionnals{

    private static Logger logger = Logger.getLogger(GestionNotificationStockJob.class);
                                                    
    private BusinessImpl bussiness;
    
    private GestionNotificationStockBean gestionNotificationBean;
    
    
    public GestionNotificationStockJob() {
    	logger.debug("############ Constructeur: Creation d'un nouveau bean pour la gestion des Notification.");
    	gestionNotificationBean = new GestionNotificationStockBean();
    	try {
            bussiness = new BusinessImpl();
        } catch (MalformedURLException e) {
            logger.error("ERROR URL MAL FORMEE", e);
            e.printStackTrace();
        } catch (XmlRpcException e) {
            logger.error("ERROR XML RPC EXCEPTION", e);
            e.printStackTrace();
        }
	}

	@Override
    protected void executeInternal(JobExecutionContext pArg0) throws JobExecutionException {
        logger.debug("############ Execution du scheduler de gestion des notifications => " + gestionNotificationBean.getNameScheduler());
        
        //Gestion des produits 
        List<Product> productAlert = bussiness.getProductAlertQuantStock();
        try {
            if(null != productAlert && !productAlert.isEmpty()) {
                String messageMail = "Rapport de stock insuffisant";
                SendMail.sendMail(VAR_NOTIFICATION_CONFIGURATION_EMAIL_SUJET_NOTIFICATION_QUANTITE,messageMail,VAR_NOTIFICATION_CONFIGURATION_EMAIL_LISTE_ENVOIE);
            }
        } catch (MessagingException e) {
            logger.error("Error", e);
            e.printStackTrace();
        }
        /*String stockNotification = bussiness.rechercherStockAlert();        
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
        }*/
    }

}
