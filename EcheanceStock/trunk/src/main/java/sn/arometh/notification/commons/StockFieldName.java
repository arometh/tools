package sn.arometh.notification.commons;

public enum StockFieldName {
    STOCK_ID("id"), STOCK_PRODUCT_NAME_STOCK("product_id_NAME"), STOCK_PRODUCT_UOS("product_uos"), STOCK_LOCATION_ID("location_id"),STOCK_LOCATION_DEST_ID("location_dest_id")
    ,STOCK_LOCATION_NAME("location_id_NAME"),STOCK_LOCATION_DEST_NAME("location_id_NAME"), STOCK_PRODUCT_ID("product_id"), STOCK_PRODUCT_NAME("product_id_NAME"), STOCK_PRODUCT_QTY("product_qty"), STOCK_ORIGIN("origin") ;

    String fieldName;

    private StockFieldName(String pFieldName) {
        fieldName = pFieldName;
    }

    public String getValue() {
        return fieldName;
    }
}
