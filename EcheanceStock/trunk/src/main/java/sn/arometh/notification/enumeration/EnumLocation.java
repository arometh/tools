package sn.arometh.notification.enumeration;

public enum EnumLocation {
    LOCATION_ID("id"), LOCATION_PARENT("location_id"), LOCATION_NAME("location_id_NAME"), 
    LOCATION_COMPLETE_NAME("complete_name"), MODEL("stock.location"), REMOVAL_STRATEGY_ID("removal_strategy_id");

    String fieldName;

    private EnumLocation(String pFieldName) {
        fieldName = pFieldName;
    }

    public String getValue() {
        return fieldName;
    }
}
