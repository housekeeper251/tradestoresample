package com.ourcompany.tradestore.entity;

import java.io.Serializable;

/**
 * This is entity key class for {@link TradeTransactionEntity}.
 * <br> Trade transaction entity is uniquely represented by id and version. 
 * @author LENOVO
 *
 */
public class TradeTransactionEntityKey implements Serializable{
	
	private String tradeId;
	
	private int tradeVersion;
	

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public int getTradeVersion() {
		return tradeVersion;
	}

	public void setTradeVersion(int tradeVersion) {
		this.tradeVersion = tradeVersion;
	}

}
