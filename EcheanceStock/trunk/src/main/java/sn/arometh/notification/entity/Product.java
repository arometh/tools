package sn.arometh.notification.entity;

public class Product {
	
	/** ID du produit */
	private Integer id;
	
	/** Nom du produit */
	private String name;
	
	/** variante du produit */
	private String variante;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the variante
	 */
	public String getVariante() {
		return variante;
	}

	/**
	 * @param variante the variante to set
	 */
	public void setVariante(String variante) {
		this.variante = variante;
	}
	
	@Override
	public String toString() {	    
	    return "Produit : [ ID => " + this.getId() + ", NOM => " + this.getName() + ", VARIANTE => " + this.getVariante() + " ]";
	}
}
