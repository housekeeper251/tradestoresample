package com.ourcompany.tradestore.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * This is Trade Transaction entity class. This entity will store every trade transaction.
 * Every trade transaction is uniquely represented by id and version.
 * 
 *  
 * @author LENOVO
 *
 */
public class TradeTransactionDTO implements Serializable {
	
	private TradeTransactionKeyDTO key;
	
	private String counterPartyId;
	
	private String bookingId;

	private Date maturityDate;
	
	private Date creationDate;
	
	private String expiryStatus;

	
	public TradeTransactionKeyDTO getKey() {
		return key;
	}

	public void setKey(TradeTransactionKeyDTO key) {
		this.key = key;
	}

	public String getCounterPartyId() {
		return counterPartyId;
	}

	public void setCounterPartyId(String counterPartyId) {
		this.counterPartyId = counterPartyId;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getExpiryStatus() {
		return expiryStatus;
	}

	public void setExpiryStatus(String expiryStatus) {
		this.expiryStatus = expiryStatus;
	}

	
	
	

}
