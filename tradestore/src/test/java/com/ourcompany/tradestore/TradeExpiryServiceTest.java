package com.ourcompany.tradestore;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ourcompany.tradestore.dto.TradeExpiryRequestDTO;
import com.ourcompany.tradestore.dto.TradeExpiryResponseDTO;
import com.ourcompany.tradestore.dto.TradeTransactionDTO;
import com.ourcompany.tradestore.dto.TradeTransactionKeyDTO;
import com.ourcompany.tradestore.service.TradeExpiryService;
import com.ourcompany.tradestore.service.TradeTransactionService;
import com.ourcompany.tradestore.util.DateUtil;

import mockit.Mocked;

public class TradeExpiryServiceTest {

	private TradeTransactionService tradeTransactionService = new TradeTransactionService();
	private TradeExpiryService tradeExpiryService = new TradeExpiryService();
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	
	
	@Test
	public void testForExpiryTrade() {
		
		long timeInLong = new Date().getTime();
		TradeTransactionDTO tradeTransactionDTO = new TradeTransactionDTO();
		TradeTransactionKeyDTO  tradeTransactionKeyDTO = new TradeTransactionKeyDTO();
		tradeTransactionKeyDTO.setTradeId("TXN_EXPIRYTRADE_"+ timeInLong);
		tradeTransactionKeyDTO.setTradeVersion(1);
		tradeTransactionDTO.setKey(tradeTransactionKeyDTO);
		tradeTransactionDTO.setMaturityDate(DateUtil.fetchTodaysDateWithoutTime());
		tradeTransactionDTO.setCounterPartyId("CP_1");
		tradeTransactionDTO.setBookingId("BK_ID");
		//tradeTransactionDTO.setCreationDate(DateUtil.getDate(-10));

		// TODO : Use Jmock library in execution
		
		// Use JMock library to by pass validation for maturity date processing , We are passing invalid maturity date here. This is just to test. 
		//TradeTransactionResponseDTO responsePositive = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);

		// Call 
		
		TradeExpiryRequestDTO tradeExpiryRequestDTO = new TradeExpiryRequestDTO();
		tradeExpiryRequestDTO.setCurrentEffectiveDate(DateUtil.fetchTodaysDateWithoutTime());

		TradeExpiryResponseDTO tradeExpiryResponseDTO = tradeExpiryService.performExpiryProcessing(tradeExpiryRequestDTO);
		
		Assert.assertNotNull(tradeExpiryResponseDTO);
		
		
		// Call
		
		//TradeTransactionDTO inquiryDTO = tradeTransactionService.fetchTradeTransactionDTO(tradeTransactionDTO.getKey().getTradeId(), tradeTransactionDTO.getKey().getTradeVersion());
		
		//Assert.assertEquals(inquiryDTO.getExpiryStatus(), "N");
	}
}
