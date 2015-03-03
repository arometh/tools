package sn.arometh.notification.commons;

public interface ConstantFunctionnals {
    
	//Configuration du batch de notification 
    /** variable permettant de controler l'envoie de mail*/
    static final String VAR_NOTIFICATION_CONFIGURATION_STOP_MAIL = "echeance.notification.bloque.envoi.email";
    
    //Envoie des mails
    static final String VAR_NOTIFICATION_CONFIGURATION_EMAIL_SMTP_HOST = Properties.getPropertiesValues("echeance.notification.email.serveur.smtp"); 
    static final String VAR_NOTIFICATION_CONFIGURATION_EMAIL_NOTIFICATEUR = Properties.getPropertiesValues("echeance.notification.email.notificateur");
    static final String VAR_NOTIFICATION_CONFIGURATION_EMAIL_TO = Properties.getPropertiesValues("echeance.notification.email.to.destinataire");
    static final String VAR_NOTIFICATION_CONFIGURATION_EMAIL_LISTE_ENVOIE = Properties.getPropertiesValues("echeance.notification.email.liste.destinataire");
    static final String VAR_NOTIFICATION_CONFIGURATION_EMAIL_SUJET_NOTIFICATION = Properties.getPropertiesValues("echeance.notification.email.sujet.notification");
    static final String VAR_NOTIFICATION_CONFIGURATION_EMAIL_SUJET_NOTIFICATION_RAS = Properties.getPropertiesValues("echeance.notification.email.sujet.notification.ras");
    
  //variable contenant les id des emplacements dans le stock
    static final String VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_WH = Properties.getPropertiesValues("echeance.notification.id.entrepot.wh");
    static final String VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_STOCK = Properties.getPropertiesValues("echeance.notification.id.entrepot.stock");
    static final String VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_CUSTOMER = Properties.getPropertiesValues("echeance.notification.id.entrepot.customer");
    static final String VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_SUPPLIER = Properties.getPropertiesValues("echeance.notification.id.entrepot.supplier");
    static final String VAR_NOTIFICATION_CONFIGURATION_ID_EMPLACEMENT_PROCUREMENT = Properties.getPropertiesValues("echeance.notification.id.entrepot.procurement");
    
    //Variable d'accès au service REST
    final static String VAR_NOTIFICATION_REST_EXECUTE_KW = "execute_kw";
    static final String VAR_NOTIFICATION_REST_URL = Properties.getPropertiesValues("echeance.notification.rest.url");
    static final String VAR_NOTIFICATION_REST_DATABASE = Properties.getPropertiesValues("echeance.notification.rest.db");
    static final String VAR_NOTIFICATION_REST_USER = Properties.getPropertiesValues("echeance.notification.rest.username");
    static final String VAR_NOTIFICATION_REST_PASSWORD = Properties.getPropertiesValues("echeance.notification.rest.password");
    
    //Variable d'accès à la base de données
    /** variable qui stocke le nom de la machine */
    static final String VAR_NOTIFICATION_BDD_MACHINE = Properties.getPropertiesValues("echeance.notification.machine");
    /** variable qui stocke le nom de l utilisateur */
    static final String VAR_NOTIFICATION_BDD_USER = Properties.getPropertiesValues("echeance.notification.user");
    /** variable qui stocke le port */
    static final String VAR_NOTIFICATION_BDD_PORT = Properties.getPropertiesValues("echeance.notification.port");
    /** variable qui stocke le mot de passe */
    static final String VAR_NOTIFICATION_BDD_PASSWORD = Properties.getPropertiesValues("echeance.notification.password");
    /** variable qui stocke la base de données */
    static final String VAR_NOTIFICATION_BDD_BDD = Properties.getPropertiesValues("echeance.notification.bdd");
    
    static final String VAR_NOTIFICATION_BDD_QUERY_STOCK_ECHEANCE_ALERTE = "SELECT produit.name_template as nom_produit, loc.complete_name as emplacement, quant.qty as quantite, quant.in_date as date_stock  FROM stock_quant quant "
                                                        + " INNER JOIN product_product produit ON  produit.id = quant.product_id "
                                                        + " INNER JOIN stock_location loc ON loc.id = quant.location_id "
                                                        + " WHERE quant.qty < ? "  
                                                        + " OR DATE_PART('day', current_timestamp - quant.in_date::timestamp) < ?";
    
    //Variable qui stocke les variables d'échéance
    /** variable qui stocke la quantite de stock en dessous de laquelle on envoie une alerte */
    static String  VAR_NOTIFICATION_BDD_ECHEANCE_QUANTITE = Properties.getPropertiesValues("echeance.notification.quantite");
    
    /** variable qui stocke la periode  en dessous de laquelle on envoie une alerte */
    static final String VAR_NOTIFICATION_BDD_ECHEANCE_PERIODE = Properties.getPropertiesValues("echeance.notification.periode");
	
    //Message retourne dans les notifications
    /** Entete du message retourne dans le mail de notification */    
    static final String VAR_NOTIFICATION_BUSINESS_ENTETE_MESSAGE_STOCK_ALERT = "echeance.notitication.entete.message.email";
    /** si pas stock on renvoi ce message à la place */
    static final String VAR_NOTIFICATION_BUSINESS_RAS_STOCK_MESSAGE_ALERT = "echeance.notitication.ras.stock.message";
        
}
