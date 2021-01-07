package com.ourcompany.tradestore.dto;

import java.io.Serializable;

/**
 * Response class for trade transaction.
 * @author LENOVO
 *
 */
public class TradeTransactionResponseDTO implements Serializable {

	private TradeTransactionDTO tradeTransactionDTO;
	

	public TradeTransactionDTO getTradeTransactionDTO() {
		return tradeTransactionDTO;
	}

	public void setTradeTransactionDTO(TradeTransactionDTO tradeTransactionDTO) {
		this.tradeTransactionDTO = tradeTransactionDTO;
	}
	
}
