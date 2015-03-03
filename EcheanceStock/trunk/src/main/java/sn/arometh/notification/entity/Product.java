package sn.arometh.notification.entity;

public class Product {
	
	/** ID du produit */
	private Integer id;
	
	/** Nom du produit */
	private String name;
	
	/** quantité disponible du produit en stock */
	private Integer quantStock;
	
	/** Quantité virtuel disponible en stock */
	private Integer quantVirtual;
	
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
	 * @return the quantStock
	 */
	public Integer getQuantStock() {
		return quantStock;
	}

	/**
	 * @param quantStock the quantStock to set
	 */
	public void setQuantStock(Integer quantStock) {
		this.quantStock = quantStock;
	}

	/**
	 * @return the quantVirtual
	 */
	public Integer getQuantVirtual() {
		return quantVirtual;
	}

	/**
	 * @param quantVirtual the quantVirtual to set
	 */
	public void setQuantVirtual(Integer quantVirtual) {
		this.quantVirtual = quantVirtual;
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
	
	
}
