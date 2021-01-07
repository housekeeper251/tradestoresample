package com.ourcompany.tradestore.entity;

import java.io.Serializable;

/**
 * This class stores aggregated information about trade which is useful while performing transaction.
 * <br> For ex
 * <ol>
 * <li> Store latest version of every trade- This is useful for version validation. 
 * </ol>
 * @author LENOVO
 *
 */
public class TradeMasterEntity implements Serializable {

	private String tradeId;
	
	private int latestTradeVersion;

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public int getLatestTradeVersion() {
		return latestTradeVersion;
	}

	public void setLatestTradeVersion(int latestTradeVersion) {
		this.latestTradeVersion = latestTradeVersion;
	}
	
}
