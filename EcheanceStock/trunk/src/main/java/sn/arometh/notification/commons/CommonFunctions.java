package sn.arometh.notification.commons;

import java.util.Date;

public class CommonFunctions {
	
	public static String buildQueries(String[] pArray){
		return "";
	}
	
	/**
	 * Cette méthode permet de calculer le nombre de jours entre deux dates
	 * @param date1
	 * @param date2
	 * @return le nombre de jours entre la date date1 et la date date2
	 */
	public static long getNbJour(Date date1, Date date2){
	    long dateDiff = (long)(Math.abs(date2.getTime() - date1.getTime())) / (1000l * 60 * 60 * 24);
	    if(date1.before(date2)){
	        dateDiff = - dateDiff;
	    }
		return dateDiff;
	}
}
