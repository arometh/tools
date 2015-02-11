package sn.arometh.notification.commons;

import java.util.ResourceBundle;

/**
 * @author Ansoumane Camara
 * Classe de récupération des properties
 */
public class Properties {
      private static ResourceBundle resourceSAE;
      private static String properties = "sae";
      
      public Properties(String properties){
    	  Properties.setProperties(properties);
      }
            
      static {
    	  resourceSAE = ResourceBundle.getBundle(getProperties());
      }

      /**
       * Récupere la valeur de la clé passée comme paramétre
       * @param key
       * @return 
       */
      public static String  getPropertiesValues(String key) {
            return resourceSAE.getString(key).trim();
      }

	public static void setProperties(String properties) {
		Properties.properties = properties;
	}

	public static String getProperties() {
		return properties;
	}

}