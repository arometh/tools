package sn.arometh.notification.entity;

public class Location {
	
	/** id location */
	private int id;
	
	/** nom location */
	private String name;
	
	/** location parent */
	private int parentLocation;
	
	/** nom complet location */
	private String completeName;
	
	/** Usage */
	private String Usage;
	
	/** actif */
	private boolean active;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
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
	 * @return the parentLocation
	 */
	public int getParentLocation() {
		return parentLocation;
	}

	/**
	 * @param parentLocation the parentLocation to set
	 */
	public void setParentLocation(int parentLocation) {
		this.parentLocation = parentLocation;
	}

	/**
	 * @return the completeName
	 */
	public String getCompleteName() {
		return completeName;
	}

	/**
	 * @param completeName the completeName to set
	 */
	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

	/**
	 * @return the usage
	 */
	public String getUsage() {
		return Usage;
	}

	/**
	 * @param usage the usage to set
	 */
	public void setUsage(String usage) {
		Usage = usage;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
