package sn.arometh.notification.job;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

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
        
        Context initCtx;
        try {
            initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            Session session = (Session) envCtx.lookup("mail/Session");

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("contact@arometh.com"));
            InternetAddress to[] = new InternetAddress[1];
            to[0] = new InternetAddress("ansoumane.camara@externe.sacem.fr");
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject("test");
            message.setContent("ceci est un test", "text/plain");
            Transport.send(message);
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("ERROR parse exception => ", e);
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            logger.error("ERROR parse exception => ", e);
            e.printStackTrace();
        } catch (MessagingException e) {
            logger.error("ERROR parse exception => ", e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        //Gestion des quantités de produits 
        /*List<Product> productAlert = bussiness.getProductAlertQuantStock();
        if(null != productAlert && !productAlert.isEmpty()){
            sendMailAlert(productAlert, CONSTANT_INFO_EMAIL_QUANTITE);
        }
        
        //Gestion des dates d'échéance
        List<Stock> productAlertDateOf;
        try {
            productAlertDateOf = bussiness.getProductAlertOutOfDate();
            if(productAlertDateOf != null && !productAlertDateOf.isEmpty()){
                sendMailAlertStock(productAlertDateOf, CONSTANT_INFO_EMAIL_DATEOF);
            }
        } catch (ParseException e) {
            logger.error("ERROR parse exception => ", e);
            e.printStackTrace();
        }*/
        
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
	
	/**
     * Envoie un mail d'alerte en formattant le message
     * @param pProduct
     */
    private void sendMailAlertStock(List<Stock> pStock, String pInfo) {
        try {
            if(null != pStock && !pStock.isEmpty()) {
                
                String messageMail = VAR_NOTIFICATION_CONFIGURATION_EMAIL_SUJET_NOTIFICATION_ENTETE_MESSAGE
                                        + bussiness.formatListToMessageStock(pStock);
                SendMail.sendMail(VAR_NOTIFICATION_CONFIGURATION_EMAIL_SUJET_NOTIFICATION_DATEECHEANCE,messageMail,VAR_NOTIFICATION_CONFIGURATION_EMAIL_LISTE_ENVOIE);
                logger.info("Envoie de mail d'alerte [" + pInfo + "] concernant " + pStock.size() + " stock");
            }else {
                
            }
        } catch (MessagingException e) {
            logger.error("Error envoie de mail alerte", e);
            e.printStackTrace();
        }
    }
}
