package com.ourcompany.tradestore.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.ourcompany.tradestore.dto.TradeExpiryRequestDTO;
import com.ourcompany.tradestore.dto.TradeExpiryResponseDTO;
import com.ourcompany.tradestore.dto.TradeTransactionDTO;
import com.ourcompany.tradestore.util.DatabaseConnectionUtil;

public class TradeExpiryService {

	private static Logger logger = Logger.getLogger(TradeExpiryService.class.getName());

	/**
	 * This method is responsible to mark expiry for the trade which are matured .
	 * It will perform following steps.
	 * <ol>
	 * <li>Make sure that expiry processing is Not Runnning and not completed for
	 * the date.
	 * <li>Mark the status of expiry process as <b>Running </b>. So that no other
	 * thread will start expiry processing.
	 * <li>Update expiry for all trade from 'N' to 'Y' where maturity date is less
	 * than date specified in the input.
	 * <li>Mark status of expiry process as <b> Completed </b> .
	 * </ol>
	 * 
	 */
	public TradeExpiryResponseDTO performExpiryProcessing(TradeExpiryRequestDTO tradeExpiryRequestDTO) {

		validateExpiryProcessNotRunningForTheDate(tradeExpiryRequestDTO.getCurrentEffectiveDate());
		markExpiryProcessAsRunning(tradeExpiryRequestDTO.getCurrentEffectiveDate());
		int numberOfRowsUpdated =processExpiryForMaturedTrades(tradeExpiryRequestDTO);
		updateExpiryProcessAsCompleted(tradeExpiryRequestDTO.getCurrentEffectiveDate());
		TradeExpiryResponseDTO responseDTO = new TradeExpiryResponseDTO();
		responseDTO.setNumberOfRowsUpdated(numberOfRowsUpdated);

		return responseDTO;
	}

	private void validateExpiryProcessNotRunningForTheDate(Date effectiveDate) {

	}

	private void markExpiryProcessAsRunning(Date effectiveDate) {

	}

	private void updateExpiryProcessAsCompleted(Date effectiveDate) {

	}

	private int processExpiryForMaturedTrades(TradeExpiryRequestDTO tradeExpiryRequestDTO)  {
		EntityManager entityManager = null;
		
		TradeTransactionDTO tradeTransactionDTO = null;
		PreparedStatement statement = null;
		int numberOfRowsThatAreUpdated = 0;
		try {
			entityManager = DatabaseConnectionUtil.getInstance().openDatabaseConnection();
			Connection connection = DatabaseConnectionUtil.getRawSqlConnection(entityManager);
			String sqlQuery = "update trade_transaction " +
			"set expiry_status = 'Y' " + "where maturity_date = ?"  ;
			
			
			statement = connection.prepareStatement(sqlQuery);
			
			//statement.setString(1, "Y");
			statement.setDate(1, new java.sql.Date(tradeExpiryRequestDTO.getCurrentEffectiveDate().getTime()));
			
			
			numberOfRowsThatAreUpdated= statement.executeUpdate();
			
			logger.log(Level.SEVERE, "Number of rows updated="+ numberOfRowsThatAreUpdated);
			
			statement.close();
			
			DatabaseConnectionUtil.getInstance().commit(entityManager);
			
			return numberOfRowsThatAreUpdated;
		} catch (RuntimeException runtimeException) {
			logger.log(Level.SEVERE, "Runtime exception in execution",runtimeException);
			if(entityManager!=null) {
				DatabaseConnectionUtil.getInstance().rollback(entityManager);
			}
			throw runtimeException;
		} catch (Throwable throwableException) {
			logger.log(Level.SEVERE, "Throwable exception in execution",throwableException);
			if(entityManager!=null) {
				DatabaseConnectionUtil.getInstance().rollback(entityManager);
			}
			throw new RuntimeException(throwableException.getMessage());
		}
		finally {
			try {
				if(statement!=null && !statement.isClosed()) {
					statement.close();
				}
			}catch(Throwable e) {
				throw new RuntimeException(e.getMessage());
			}
			if(entityManager!=null ) {
				DatabaseConnectionUtil.getInstance().close(entityManager);
			}
		}
		

	}

}
