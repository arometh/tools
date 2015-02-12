package sn.arometh.notification.commons;

import java.util.ResourceBundle;

/**
 * @author Ansoumane Camara
 * Classe de r�cup�ration des properties
 */
public class PropertiesUtils {
      private static ResourceBundle resourceSAE;
      private static String properties = "sae";
      
      public PropertiesUtils(String properties){
    	  PropertiesUtils.setProperties(properties);
      }
            
      static {
    	  resourceSAE = ResourceBundle.getBundle(getProperties());
      }

      /**
       * R�cupere la valeur de la cl� pass�e comme param�tre
       * @param key
       * @return 
       */
      public static String  getPropertiesValues(String key) {
            return resourceSAE.getString(key).trim();
      }

	public static void setProperties(String properties) {
		PropertiesUtils.properties = properties;
	}

	public static String getProperties() {
		return properties;
	}

}