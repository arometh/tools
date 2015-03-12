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
import sn.arometh.notification.commons.ConstantObjects;
import sn.arometh.notification.commons.SendMail;
import sn.arometh.notification.entity.Product;
import sn.arometh.notification.entity.Stock;

public class GestionNotificationStockJob extends QuartzJobBean implements StatefulJob, ConstantFunctionnals, ConstantObjects{

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
        sendMailAlert(productAlert, CONSTANT_INFO_EMAIL_QUANTITE);
        
        //Gestion des dates d'échéance
        List<Stock> productAlertDateOf = bussiness.getProductAlertOutOfDate();
        sendMailAlert(productAlertDateOf, CONSTANT_INFO_EMAIL_DATEOF);
    }

	/**
	 * Envoie un mail d'alerte en formattant le message
	 * @param pProduct
	 */
	private void sendMailAlert(List<Product> pProduct, String pInfo) {
		try {
            if(null != pProduct && !pProduct.isEmpty()) {
            	
                String messageMail = VAR_NOTIFICATION_CONFIGURATION_EMAIL_SUJET_NOTIFICATION_ENTETE_MESSAGE
                						+ bussiness.formatListToMessageProduct(pProduct);
                SendMail.sendMail(VAR_NOTIFICATION_CONFIGURATION_EMAIL_SUJET_NOTIFICATION_QUANTITE,messageMail,VAR_NOTIFICATION_CONFIGURATION_EMAIL_LISTE_ENVOIE);
                logger.info("Envoie de mail d'alerte [" + pInfo + "] concernant " + pProduct.size() + " produits");
            }else {
            	
            }
        } catch (MessagingException e) {
            logger.error("Error envoie de mail alerte", e);
            e.printStackTrace();
        }
	}
}
