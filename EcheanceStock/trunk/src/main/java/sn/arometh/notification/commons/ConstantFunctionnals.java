package sn.arometh.notification.commons;

public interface ConstantFunctionnals {
	static final Integer QUANTITE_MINIMUM_STOCK = Integer.parseInt(PropertiesUtils.getPropertiesValues(""));
	static final Integer LIMITE_OUT_OF_DATE_STOCK = Integer.parseInt(PropertiesUtils.getPropertiesValues(""));
	static final String USER = "";
	
}
