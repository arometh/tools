package sn.arometh.notification.entity;

public class Location extends Entity {
	
	/** location parent */
	private Location parentLocation;
	
	/** nom complet location */
	private String completeName;
	
	/** Usage */
	private String Usage;
	
	/** actif */
	private boolean active;
	
	public Location() {
		super();
	}

	public Location (Integer pID, String pName) {
		super(pID, pName);
	}
	/**
	 * @return the parentLocation
	 */
	public Location getParentLocation() {
		return parentLocation;
	}

	/**
	 * @param parentLocation the parentLocation to set
	 */
	public void setParentLocation(Location parentLocation) {
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
	
	@Override
    public String toString() {      
        return "Location : [ ID => " + this.getId() + ", NOM => " + this.getName() + ", COMPLETE NAME : " + this.getCompleteName() + ", PARENT LOCATION [ " + this.getParentLocation() + "]";
    }
}
