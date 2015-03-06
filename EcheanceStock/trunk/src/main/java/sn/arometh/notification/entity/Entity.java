package sn.arometh.notification.entity;

public class Entity {
	
	/** ID de l'entity */
	private Integer id;
	
	/** Nom de l'entity */
	private String name;

	/** Model de l'entity */
	private String model;
	
	/** fied search */
	private String[] field;
	
	public Entity() {
		super();
	}
	
	public Entity (Integer pID, String pName) {
		id = pID;
		name = pName;
	}
	
	public Entity(Integer pID, String pModel, String[] pField) {
	    id= pID;
	    model = pModel;
	    field = pField;
	}

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
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param pModel the model to set
     */
    public void setModel(String pModel) {
        model = pModel;
    }

    /**
     * @return the field
     */
    public String[] getField() {
        return field;
    }

    /**
     * @param pField the field to set
     */
    public void setField(String[] pField) {
        field = pField;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if(pObj instanceof Entity){
            Entity entity = (Entity) pObj;
            if(this.getId().equals(entity.getId())){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
