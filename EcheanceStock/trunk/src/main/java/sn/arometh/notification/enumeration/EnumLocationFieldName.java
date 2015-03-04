package sn.arometh.notification.enumeration;

public enum EnumLocationFieldName {
    LOCATION_ID("id"), LOCATION_PARENT("location_id"), LOCATION_NAME("location_id_NAME"), LOCATION_COMPLETE_NAME("complete_name");

    String fieldName;

    private EnumLocationFieldName(String pFieldName) {
        fieldName = pFieldName;
    }

    public String getValue() {
        return fieldName;
    }
}
