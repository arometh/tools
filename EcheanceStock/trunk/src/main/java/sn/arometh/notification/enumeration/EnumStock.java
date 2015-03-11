package sn.arometh.notification.enumeration;

public enum EnumStock {
    STOCK_ID("id"), STOCK_PRODUCT_NAME_STOCK("product_id_NAME"), STOCK_PRODUCT_UOS("product_uos"), STOCK_LOCATION_ID("location_id"),STOCK_LOCATION_DEST_ID("location_dest_id")
    ,STOCK_LOCATION_NAME("location_id_NAME"),STOCK_LOCATION_DEST_NAME("location_dest_id_NAME"), STOCK_PRODUCT_ID("product_id"), STOCK_PRODUCT_NAME("product_id_NAME"), 
    STOCK_PRODUCT_QTY("product_qty"), STOCK_ORIGIN("origin"), MODEL("stock.move"), STOCK_OUT_OF_DATE("x_wh_date_stock_out_of_date") ;

    String fieldName;

    private EnumStock(String pFieldName) {
        fieldName = pFieldName;
    }

    public String getValue() {
        return fieldName;
    }
}
