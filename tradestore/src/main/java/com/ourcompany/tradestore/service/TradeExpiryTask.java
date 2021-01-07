package com.ourcompany.tradestore.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ourcompany.tradestore.dto.TradeExpiryRequestDTO;
import com.ourcompany.tradestore.dto.TradeExpiryResponseDTO;
import com.ourcompany.tradestore.util.DateUtil;

public class TradeExpiryTask extends TimerTask {

	private static Logger logger = Logger.getLogger(TradeExpiryTask.class.getName());

	
	private static TradeExpiryTask singletonInstance ;
	
	private static TradeExpiryTask fetchSingletonInstance() {
		if(singletonInstance==null) {
			synchronized (TradeExpiryService.class) {
				if(singletonInstance==null) {
					singletonInstance = new TradeExpiryTask();
				}
			}
		}
		
		return singletonInstance;
	}
	
	
	public void schedulePoller() {
		Date date2pm = new java.util.Date();
		date2pm.setHours(14);
		date2pm.setMinutes(0);

		Timer timer = new Timer();
		timer.schedule(this ,date2pm, 86400000);
	}
	
	@Override
	public void run() {
		
		logger.log(Level.SEVERE, "Expiry Poller started ");
		
		Date todaysDate = DateUtil.fetchTodaysDateWithoutTime();
		
		TradeExpiryRequestDTO requestDTO= new TradeExpiryRequestDTO();
		requestDTO.setCurrentEffectiveDate(todaysDate);

		TradeExpiryResponseDTO tradeExpiryResponseDTO = new TradeExpiryService().performExpiryProcessing(requestDTO);
		
		logger.log(Level.SEVERE, "Expiry Poller finished ");
		
		
	}

}
