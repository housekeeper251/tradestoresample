package com.ourcompany.tradestore;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ourcompany.tradestore.dto.TradeExpiryRequestDTO;
import com.ourcompany.tradestore.dto.TradeExpiryResponseDTO;
import com.ourcompany.tradestore.dto.TradeMasterDTO;
import com.ourcompany.tradestore.dto.TradeTransactionDTO;
import com.ourcompany.tradestore.dto.TradeTransactionKeyDTO;
import com.ourcompany.tradestore.dto.TradeTransactionResponseDTO;
import com.ourcompany.tradestore.service.TradeExpiryService;
import com.ourcompany.tradestore.service.TradeTransactionService;
import com.ourcompany.tradestore.util.DateUtil;



public class TradeTransactionServiceTest {

	private TradeTransactionService tradeTransactionService = new TradeTransactionService();
	private TradeExpiryService tradeExpiryService = new TradeExpiryService();
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	
	@Test
	public void testForBlankMandatoryParams() {
		 exceptionRule.expect(RuntimeException.class);
		 exceptionRule.expectMessage("tradeTransactionDTO is mandatory");
		TradeTransactionResponseDTO response = tradeTransactionService.performTradeTransaction(null);
	}

	@Test
	public void testForBlankMandatoryParamsKey() {

		 exceptionRule.expect(RuntimeException.class);
		 exceptionRule.expectMessage("Key is mandatory");

		TradeTransactionDTO tradeTransactionDTO = new TradeTransactionDTO();
		TradeTransactionResponseDTO response = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);
	
	}

	@Test
	public void testForTodaysMaturityDate() {
		// sad test case

		 exceptionRule.expect(RuntimeException.class);
		 exceptionRule.expectMessage("Maturity date should be greater than todays date");

		
		TradeTransactionDTO tradeTransactionDTO = new TradeTransactionDTO();
		TradeTransactionKeyDTO  tradeTransactionKeyDTO = new TradeTransactionKeyDTO();
		tradeTransactionKeyDTO.setTradeId("TXN_TODAY_MAT_DATE");
		tradeTransactionKeyDTO.setTradeVersion(1);
		tradeTransactionDTO.setKey(tradeTransactionKeyDTO);
		
		tradeTransactionDTO.setMaturityDate(DateUtil.fetchTodaysDateWithoutTime());
		
		TradeTransactionResponseDTO response = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);
	}

	
	@Test
	public void testForOlderMaturityDate() {
		// sad test case

		 exceptionRule.expect(RuntimeException.class);
		 exceptionRule.expectMessage("Maturity date should be greater than todays date");

		
		TradeTransactionDTO tradeTransactionDTO = new TradeTransactionDTO();
		TradeTransactionKeyDTO  tradeTransactionKeyDTO = new TradeTransactionKeyDTO();
		tradeTransactionKeyDTO.setTradeId("TXN_OLD_MAT_DATE");
		tradeTransactionKeyDTO.setTradeVersion(1);
		tradeTransactionDTO.setKey(tradeTransactionKeyDTO);
		
		tradeTransactionDTO.setMaturityDate(DateUtil.getDate(-2));
		
		TradeTransactionResponseDTO response = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);
	}
	
	@Test
	public void testForOldVersion() {

		
		exceptionRule.expect(RuntimeException.class);
		exceptionRule.expectMessage("Input TradeVersion should be greater than or equal to latest trade version");

		
		long timeInLong = new Date().getTime();
		// sad test case
		TradeTransactionDTO tradeTransactionDTO = new TradeTransactionDTO();
		TradeTransactionKeyDTO  tradeTransactionKeyDTO = new TradeTransactionKeyDTO();
		tradeTransactionKeyDTO.setTradeId("TXN_OLD_VERSION"+ timeInLong);
		tradeTransactionKeyDTO.setTradeVersion(1);
		tradeTransactionDTO.setKey(tradeTransactionKeyDTO);
		
		tradeTransactionDTO.setMaturityDate(DateUtil.getDate(20));
		tradeTransactionDTO.setCounterPartyId("CP_1");
		tradeTransactionDTO.setBookingId("BK_ID");
		
		
		TradeTransactionResponseDTO responsePositive = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);

		tradeTransactionKeyDTO.setTradeVersion(2);
		tradeTransactionDTO.setCounterPartyId("CP_2");
		tradeTransactionDTO.setBookingId("BK_ID_2");

		TradeTransactionResponseDTO response = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);

		tradeTransactionKeyDTO.setTradeVersion(1);
		tradeTransactionDTO.setCounterPartyId("CP_3");
		tradeTransactionDTO.setBookingId("BK_ID_3");

		TradeTransactionResponseDTO response2 = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);
		
		
		
	}

	
	
	@Test
	public void testForValidParameters() {
		
		long timeInLong = new Date().getTime();
		// happy test case
		TradeTransactionDTO tradeTransactionDTO = new TradeTransactionDTO();
		TradeTransactionKeyDTO  tradeTransactionKeyDTO = new TradeTransactionKeyDTO();
		tradeTransactionKeyDTO.setTradeId("TXN_VALID_"+ timeInLong);
		tradeTransactionKeyDTO.setTradeVersion(1);
		tradeTransactionDTO.setKey(tradeTransactionKeyDTO);
		
		tradeTransactionDTO.setMaturityDate(DateUtil.getDate(20));
		tradeTransactionDTO.setCounterPartyId("CP_1");
		tradeTransactionDTO.setBookingId("BK_ID");
		
		TradeTransactionResponseDTO responsePositive = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);
		
		
	}

	
	
	
	@Test
	public void testForValidUpdateExistingTrade() {
		
		long timeInLong = new Date().getTime();
		TradeTransactionDTO tradeTransactionDTO = new TradeTransactionDTO();
		TradeTransactionKeyDTO  tradeTransactionKeyDTO = new TradeTransactionKeyDTO();
		tradeTransactionKeyDTO.setTradeId("TXN_VALIDEXISTING_"+ timeInLong);
		tradeTransactionKeyDTO.setTradeVersion(1);
		tradeTransactionDTO.setKey(tradeTransactionKeyDTO);
		
		tradeTransactionDTO.setMaturityDate(DateUtil.getDate(20));
		tradeTransactionDTO.setCounterPartyId("CP_1");
		tradeTransactionDTO.setBookingId("BK_ID");
		
		TradeTransactionResponseDTO responsePositive1 = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);

		
		tradeTransactionDTO.setMaturityDate(DateUtil.getDate(30));
		tradeTransactionDTO.setCounterPartyId("CP_2");
		tradeTransactionDTO.setBookingId("BK_ID_2");

		TradeTransactionResponseDTO responsePositive2 = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);
		
		
		TradeTransactionDTO tradeTransactionInquiryDTO = tradeTransactionService.fetchTradeTransactionDTO(tradeTransactionDTO.getKey().getTradeId(), tradeTransactionDTO.getKey().getTradeVersion());

		Assert.assertEquals(tradeTransactionInquiryDTO.getBookingId(),"BK_ID_2");
	//	Assert.assertEquals(tradeTransactionInquiryDTO.getMaturityDate(),DateUtil.getDate(30));
		
	}

	
	@Test
	public void testForValidMultipleTrade() {
		
		long timeInLong = new Date().getTime();
		// Happy test case
		TradeTransactionDTO tradeTransactionDTO = new TradeTransactionDTO();
		TradeTransactionKeyDTO  tradeTransactionKeyDTO = new TradeTransactionKeyDTO();
		tradeTransactionKeyDTO.setTradeId("TXN_VALIDMULTIPLE_"+ timeInLong);
		tradeTransactionKeyDTO.setTradeVersion(1);
		tradeTransactionDTO.setKey(tradeTransactionKeyDTO);
		tradeTransactionDTO.setMaturityDate(DateUtil.getDate(20));
		tradeTransactionDTO.setCounterPartyId("CP_1");
		tradeTransactionDTO.setBookingId("BK_ID");
		
		TradeTransactionResponseDTO responsePositive = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);

		tradeTransactionKeyDTO.setTradeVersion(2);
		tradeTransactionDTO.setKey(tradeTransactionKeyDTO);
		tradeTransactionDTO.setMaturityDate(DateUtil.getDate(20));
		tradeTransactionDTO.setCounterPartyId("CP_2");
		tradeTransactionDTO.setBookingId("BK_ID_NEW");
		
		TradeTransactionResponseDTO responsePositive2 = tradeTransactionService.performTradeTransaction(tradeTransactionDTO);
		
		TradeMasterDTO tradeMasterDTO = tradeTransactionService.fetchTradeMaster(tradeTransactionDTO.getKey().getTradeId());
		
		Assert.assertEquals(tradeMasterDTO.getLatestTradeVersion(),2);
				
		
	}



	

	
	
}
