package sn.arometh.notification.enumeration;

public enum EnumQuantFieldName {
    QUANT_ID("id"), QUANT_PRODUCT_NAME_STOCK("product_id_NAME"), QUANT_LOCATION_ID("location_id")
    ,QUANT_LOCATION_NAME("location_id_NAME"),QUANT_PRODUCT_ID("product_id"), QUANT_PRODUCT_NAME("product_id_NAME"), 
    QUANT_PRODUCT_QTY("qty"), QUANT_LOT_ID("lot_id"), QUANT_IN_DATE("in_date") ;

    String fieldName;

    private EnumQuantFieldName(String pFieldName) {
        fieldName = pFieldName;
    }

    public String getValue() {
        return fieldName;
    }
}
