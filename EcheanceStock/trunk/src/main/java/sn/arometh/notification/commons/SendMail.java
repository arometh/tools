package sn.arometh.notification.commons;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import org.openl.util.Log;

/**
 * Envoyer un email
 */
public class SendMail {	
	private static final String smtpHost = Properties.getPropertiesValues("mail.sae.serveur.smtp"); 
	private static final String notificateur = Properties.getPropertiesValues("mail.sae.notificateur");
	private static final String to = Properties.getPropertiesValues("mail.sae.liste.extelia");
	private static final String liste_extelia = Properties.getPropertiesValues("mail.sae.liste.extelia");
	
	/**
	 * 
	 * @param sujet
	 * @param messageMail
	 * @param to
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendMail(String sujet, String messageMail, String listeDestinataires []) throws AddressException, MessagingException {
		Properties props = new Properties();
	    props.put("mail.smtp.host", smtpHost);
	    props.put("mail.smtp.auth", "true");
	 
	    Session session = Session.getDefaultInstance(props);
	    session.setDebug(true);
	 
	    MimeMessage message = new MimeMessage(session);   
	    message.setFrom(new InternetAddress(notificateur));
	    for (String to : listeDestinataires) {
	    	 message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		}	   
	    message.setSubject(sujet);
	    message.setText(messageMail);
	 
	    Transport tr = session.getTransport("smtp");
	    tr.connect(smtpHost, "", "");
	    message.saveChanges();
	 
	    tr.sendMessage(message,message.getAllRecipients());
	    tr.close();	 
	}
	
	/**
	 * 
	 * @param sujet
	 * @param messageMail
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendMail(String sujet, String messageMail) throws AddressException, MessagingException {
		Properties props = new Properties();
	    props.put("mail.smtp.host", smtpHost);
	    props.put("mail.smtp.auth", "true");
	 
	    Session session = Session.getDefaultInstance(props);
	    session.setDebug(true);
	 
	    MimeMessage message = new MimeMessage(session);   
	    message.setFrom(new InternetAddress(notificateur));
	    String[] destinataires = to.split(";");
	    InternetAddress[] adresses = new InternetAddress[destinataires.length];
	    int i = 0;
	    for(String email : destinataires){
	    	adresses[i] = new InternetAddress(email);		    	
	    	i++;	    	
	    }
	   
	    message.addRecipients(Message.RecipientType.TO, adresses);
	    message.setSubject(sujet);
	    message.setText(getMailMessageEntete() + messageMail);
	 
	    Transport tr = session.getTransport("smtp");
	    tr.connect(smtpHost, "", "");
	    message.saveChanges();
	 
	    tr.sendMessage(message,message.getAllRecipients());
	    tr.close();
	 
	}
		
	public static void sendMail(String message) {		
		String sujet = "Notification Docapost-BPO : Anomalie remontée";
		
		message = "Bonjour," +
				  "\n" +
				  "Merci de ne pas repondre à ce message. Vous l'avez reçu suite à l'erreur ci-dessous : " +
				  "\n\n" +
				  message +			
				  "\n\nCordialement\n";
		
		String[] destinatairesExtelia = liste_extelia.split(";");
		try {
			sendMail(sujet, message, destinatairesExtelia);
		}catch(Exception e){
			Log.error("Erreur.", e);
		}
	}
	
	/**
	 * 
	 * @param raisonDetaille
	 * @return
	 */
	public static String getMailMessageEntete(){
		return "Bonjour, \n\n" +
				"Ce mail est un envoi automatique de la plate-forme de traitement de dématérialisation des courriers et fax GDF-SUEZ par Docapost-BPO. " +
				"Nous vous prions de ne pas y répondre." +
				"\n\n" +
				"Ce mail vous est adressé pour les raisons suivantes : \n";
	}
	
	public static void main(String[] args) throws AddressException, MessagingException {
		String smtpHost = "smtpmrs.prod.extelia.fr";
		String to = "belaid.irmeche3@extelia.fr";
		String to1 = "olivier.bontemps3@extelia.fr";
		Properties props = new Properties();
	    props.put("mail.smtp.host", smtpHost);
	    props.put("mail.smtp.auth", "true");
	 
	    String sujet ="test";
	    String messageMail = "merde";
	    
	    Session session = Session.getDefaultInstance(props);
	    session.setDebug(true);
	 
	    MimeMessage message = new MimeMessage(session);   
	    message.setFrom(new InternetAddress(notificateur));
	    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to1));
	    message.setSubject(sujet);
	    message.setText(messageMail);
	 
	    Transport tr = session.getTransport("smtp");
	    tr.connect(smtpHost, "", "");
	    message.saveChanges();
	 
	    // tr.send(message);
	   // Genere l'erreur. Avec l authentification, oblige d utiliser sendMessage meme pour une seule adresse..
	 
	    tr.sendMessage(message,message.getAllRecipients());
	    tr.close();	    
	}
}