package sn.arometh.notification.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Lob;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@Table(name="DOCS_EXPORT")
@NamedQueries( {
	@NamedQuery(name = "DOCALL.find", query = "select d from DocsExport d"),
	@NamedQuery(name = "DOCBYID.find", query = "select d from DocsExport d where d.numId = :numId"),
	@NamedQuery(name = "DOCBYSTATUS.find", query = "select d from DocsExport d where d.status = :statusVal"),
	@NamedQuery(name = "DOCBYLOTID.find", query = "select d from DocsExport d where d.lotId = :lotIdVal"),
	@NamedQuery(name = "DOCBYPRESTA.find", query = "select d from DocsExport d where d.prestation = :prestation")
} )
public class StockQuant implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	//@Column(name="numero_id",length = 14,nullable = false, unique = true)
	private String idStock;
	private float quantityStock;
	private String dateExpirationStock;
	
	
	public String getIdStock() {
		return idStock;
	}
	public void setIdStock(String idStock) {
		this.idStock = idStock;
	}
	public float getQuantityStock() {
		return quantityStock;
	}
	public void setQuantityStock(float quantityStock) {
		this.quantityStock = quantityStock;
	}
	public String getDateExpirationStock() {
		return dateExpirationStock;
	}
	public void setDateExpirationStock(String dateExpirationStock) {
		this.dateExpirationStock = dateExpirationStock;
	}
	
	
	
}
