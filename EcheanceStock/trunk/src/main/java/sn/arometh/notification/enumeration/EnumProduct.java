package sn.arometh.notification.enumeration;

public enum EnumProduct {
    PRODUCT_ID("id"), PRODUCT_NAME("name_template"), PRODUCT_VARIANTE("variante");

    String fieldName;

    private EnumProduct(String pFieldName) {
        fieldName = pFieldName;
    }

    public String getValue() {
        return fieldName;
    }
}
