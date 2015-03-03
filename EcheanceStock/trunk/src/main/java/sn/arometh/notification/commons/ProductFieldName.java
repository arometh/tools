package sn.arometh.notification.commons;

public enum ProductFieldName {
    PRODUCT_ID("id"), PRODUCT_NAME("name_template"), PRODUCT_VARIANTE("variante");

    String fieldName;

    private ProductFieldName(String pFieldName) {
        fieldName = pFieldName;
    }

    public String getValue() {
        return fieldName;
    }
}
