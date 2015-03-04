package sn.arometh.notification.entity;

public class Entity {
	
	/** ID du produit */
	private Integer id;
	
	/** Nom du produit */
	private String name;

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
	
	public Entity() {
		
	}
	
	public Entity (Integer pID, String pName) {
		id = pID;
		name = pName;
	}
	
}
