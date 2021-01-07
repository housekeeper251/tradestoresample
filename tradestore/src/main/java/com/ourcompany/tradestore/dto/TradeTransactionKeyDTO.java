package com.ourcompany.tradestore.dto;

/**
 * This is entity key class for {@link TradeTransactionDTO}.
 * <br> Trade transaction entity is uniquely represented by id and version. 
 * @author LENOVO
 *
 */
public class TradeTransactionKeyDTO {
	
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
