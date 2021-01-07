package com.ourcompany.tradestore.service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.RuntimeErrorException;
import javax.persistence.EntityManager;

import com.ourcompany.tradestore.dto.TradeMasterDTO;
import com.ourcompany.tradestore.dto.TradeTransactionDTO;
import com.ourcompany.tradestore.dto.TradeTransactionKeyDTO;
import com.ourcompany.tradestore.dto.TradeTransactionResponseDTO;
import com.ourcompany.tradestore.entity.TradeMasterEntity;
import com.ourcompany.tradestore.entity.TradeTransactionEntity;
import com.ourcompany.tradestore.entity.TradeTransactionEntityKey;
import com.ourcompany.tradestore.util.DatabaseConnectionUtil;
import com.ourcompany.tradestore.util.DateUtil;

/**
 * This is service class is used for performing trade transaction.
 * 
 * @author LENOVO
 *
 */
public class TradeTransactionService {

	private static Logger logger = Logger.getLogger(TradeTransactionService.class.getName());

	/**
	 * This method is used to perform trade transaction. It will perform following
	 * steps
	 * <ol>
	 * <li>Validate maturity date- Trade maturity date should be greater than todays
	 * date
	 * <li>Validate Trade version id- Version id in the input should be greater or
	 * equal than latest version id for corresponding trade id. System is storing
	 * latest version in {@link TradeMasterEntity}. Check against this number.
	 * <li>If the record not found in TradeMaster then directly create
	 * {@link TradeMasterEntity} and {@link TradeTransactionDTO}.
	 * </ol>
	 * 
	 * @param tradeTransactionDTO
	 * @return
	 */
	public TradeTransactionResponseDTO performTradeTransaction(TradeTransactionDTO tradeTransactionDTO) {
		if (logger.isLoggable(Level.FINE)) {
			logger.log(Level.FINE, "Start of performTradeTransaction");
		}

		validateMandatoryInputField(tradeTransactionDTO);

		EntityManager entityManager = null;
		
		TradeTransactionResponseDTO tradeTransactionResponseDTO = null;
		try {
			validateMaturityDateOfTrade(tradeTransactionDTO);

			
			entityManager = DatabaseConnectionUtil.getInstance().openDatabaseConnection();

			// get TradeMasterRecord
			TradeMasterEntity tradeMaster = getTradeMaster(entityManager, tradeTransactionDTO.getKey().getTradeId());

			// 
			
			boolean isTradeAlreadyExist = false;
			
			if(tradeMaster!=null) {
				isTradeAlreadyExist = true;
				validateVersion(entityManager, tradeTransactionDTO.getKey().getTradeVersion(),tradeMaster.getLatestTradeVersion());
			}
			
			if(!isTradeAlreadyExist) {
				createTradeMaster(entityManager,tradeTransactionDTO);
				createTradeTransaction(entityManager,tradeTransactionDTO);
			}
			
			if(isTradeAlreadyExist) {  
				if(tradeTransactionDTO.getKey().getTradeVersion() == tradeMaster.getLatestTradeVersion()) {
					updateExistingTradeTransaction(entityManager,tradeTransactionDTO);
				}
				else if(tradeTransactionDTO.getKey().getTradeVersion() > tradeMaster.getLatestTradeVersion()) {
					updateTradeMasterWithUpdatedVersion(entityManager,tradeMaster,tradeTransactionDTO);
					createTradeTransaction(entityManager,tradeTransactionDTO);
				}
				else {
					throw new RuntimeException("Old Version !!. Un rechable block");
				}
			}
			
			tradeTransactionResponseDTO = prepareTradeTrannsactionResponse(tradeTransactionDTO);
			
			DatabaseConnectionUtil.getInstance().commit(entityManager);
			
			if (logger.isLoggable(Level.FINE)) {
				logger.log(Level.FINE, "End of performTradeTransaction");
			}

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
			if(entityManager!=null ) {
				DatabaseConnectionUtil.getInstance().close(entityManager);
			}
		}
		

		return tradeTransactionResponseDTO;
	}

	
	private void enrichTradeTransactionRequest(TradeTransactionDTO tradeTransactionDTO) {
		// TODO Auto-generated method stub
		
		if(tradeTransactionDTO.getCreationDate()==null) {
			tradeTransactionDTO.setCreationDate(DateUtil.fetchTodaysDateWithoutTime());
		}
		
	}


	private TradeTransactionResponseDTO prepareTradeTrannsactionResponse(TradeTransactionDTO tradeTransactionDTO) {
		TradeTransactionResponseDTO responseDTO = new TradeTransactionResponseDTO();
		responseDTO.setTradeTransactionDTO(tradeTransactionDTO);
		return responseDTO;
	}


	private TradeMasterEntity updateTradeMasterWithUpdatedVersion(EntityManager entityManager, TradeMasterEntity tradeMaster, TradeTransactionDTO tradeTransactionDTO) {
		if(tradeMaster!=null) {
			tradeMaster.setLatestTradeVersion(tradeTransactionDTO.getKey().getTradeVersion());
			entityManager.persist(tradeMaster);
		}
		
		return tradeMaster;
	}


	private TradeTransactionEntity updateExistingTradeTransaction(EntityManager entityManager, TradeTransactionDTO tradeTransactionDTO) {
		TradeTransactionEntityKey key = new TradeTransactionEntityKey();
		key.setTradeId(tradeTransactionDTO.getKey().getTradeId());
		key.setTradeVersion(tradeTransactionDTO.getKey().getTradeVersion());

		TradeTransactionEntity entity = entityManager.find(TradeTransactionEntity.class, key);

		if(entity==null) {
			throw new RuntimeException("Invalid update. Not expected");
		}
		
		
		if(entity!=null) {
			if("Y".equals(entity.getExpiryStatus())){
				throw new RuntimeException("Trade is expired");
			}
			
			entity.setBookingId(tradeTransactionDTO.getBookingId());
			entity.setCounterPartyId(tradeTransactionDTO.getCounterPartyId());
			entity.setCreationDate(DateUtil.fetchTodaysDateWithoutTime());
			entity.setMaturityDate(tradeTransactionDTO.getMaturityDate());
			entity.setExpiryStatus("N");
			entityManager.persist(entity);
			
			
			// TODO -- Move old record in TradeTransactionEntityHistory. for Auditing requirements if any
		}
		return entity;
		
	}


	private TradeTransactionEntity createTradeTransaction(EntityManager entityManager, TradeTransactionDTO tradeTransactionDTO) {
		TradeTransactionEntity entity = new TradeTransactionEntity();
		TradeTransactionEntityKey key = new TradeTransactionEntityKey();
		key.setTradeId(tradeTransactionDTO.getKey().getTradeId());
		key.setTradeVersion(tradeTransactionDTO.getKey().getTradeVersion());
		entity.setKey(key);
		
		entity.setBookingId(tradeTransactionDTO.getBookingId());
		entity.setCounterPartyId(tradeTransactionDTO.getCounterPartyId());
		entity.setCreationDate(DateUtil.fetchTodaysDateWithoutTime());
		entity.setMaturityDate(tradeTransactionDTO.getMaturityDate());
		entity.setExpiryStatus("N");
		entityManager.persist(entity);
		
		return entity;
		
	}


	private TradeMasterEntity createTradeMaster(EntityManager entityManager, TradeTransactionDTO tradeTransactionDTO) {
		
		TradeMasterEntity tradeMasterEntity = new TradeMasterEntity();
		tradeMasterEntity.setTradeId(tradeTransactionDTO.getKey().getTradeId());
		if(tradeTransactionDTO.getKey().getTradeVersion() !=0) {
			logger.log(Level.SEVERE, "Initial version should be 1");
		}
		
		tradeMasterEntity.setLatestTradeVersion(tradeTransactionDTO.getKey().getTradeVersion());
		
		entityManager.persist(tradeMasterEntity);
		return tradeMasterEntity; 
	}


	private void validateVersion(EntityManager entityManager, int inputTradeVersion, int latestTradeVersion) {
		if(inputTradeVersion < latestTradeVersion) {
			throw new RuntimeException("Input TradeVersion should be greater than or equal to latest trade version");
		}
		
	}

	private TradeMasterEntity getTradeMaster(EntityManager entityManager, String tradeId) {

		TradeMasterEntity tradeMaster = entityManager.find(TradeMasterEntity.class, tradeId);

		return tradeMaster;
	}

	private void validateMandatoryInputField(TradeTransactionDTO tradeTransactionDTO) {
		if (tradeTransactionDTO == null) {
			throw new RuntimeException("tradeTransactionDTO is mandatory");
		}

		if (tradeTransactionDTO.getKey() == null) {
			throw new RuntimeException("Key is mandatory");
		}

		if (tradeTransactionDTO.getKey().getTradeId() == null) {
			throw new RuntimeException("Key.Id is mandatory");
		}

		if (tradeTransactionDTO.getKey().getTradeVersion() == 0) {
			throw new RuntimeException("version is mandatory");
		}

		if (tradeTransactionDTO.getMaturityDate() == null) {
			throw new RuntimeException("Maturity date is mandatory");
		}

	}

	private void validateMaturityDateOfTrade(TradeTransactionDTO tradeTransactionDTO) {
		Date inputMaturityDateWithoutTimeStamp = tradeTransactionDTO.getMaturityDate();
		Date todaysDate = DateUtil.fetchTodaysDateWithoutTime();
		
		if (inputMaturityDateWithoutTimeStamp.before(DateUtil.fetchTodaysDateWithoutTime()) || inputMaturityDateWithoutTimeStamp.equals(todaysDate)) {
			throw new RuntimeException("Maturity date should be greater than todays date");
		}

	}
	
	
	public TradeTransactionDTO fetchTradeTransactionDTO(String tradeId , int tradeVersion) {

		EntityManager entityManager = null;
		
		TradeTransactionDTO tradeTransactionDTO = null;
		try {
			entityManager = DatabaseConnectionUtil.getInstance().openDatabaseConnection();

			TradeTransactionEntityKey key = new TradeTransactionEntityKey();
			key.setTradeId(tradeId);
			key.setTradeVersion(tradeVersion);

			TradeTransactionEntity entity = entityManager.find(TradeTransactionEntity.class, key);

			if(entity==null) {
				throw new RuntimeException("Trade Transaction not found");
			}
			
			tradeTransactionDTO = new TradeTransactionDTO();
			TradeTransactionKeyDTO keyDTO = new TradeTransactionKeyDTO();
			keyDTO.setTradeId(tradeId);
			keyDTO.setTradeVersion(tradeVersion);
			
				
				tradeTransactionDTO.setBookingId(entity.getBookingId());
				tradeTransactionDTO.setCounterPartyId(entity.getCounterPartyId());
				tradeTransactionDTO.setCreationDate(entity.getCreationDate());
				tradeTransactionDTO.setMaturityDate(entity.getMaturityDate());
				tradeTransactionDTO.setExpiryStatus(entity.getExpiryStatus());

			
			
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
			if(entityManager!=null ) {
				DatabaseConnectionUtil.getInstance().close(entityManager);
			}
		}

		
		return tradeTransactionDTO;	
	}
	
	public TradeMasterDTO fetchTradeMaster(String tradeId) {
		EntityManager entityManager = null;
		
		TradeMasterDTO tradeMasterDTO = null;
		try {
			entityManager = DatabaseConnectionUtil.getInstance().openDatabaseConnection();


			TradeMasterEntity entity = entityManager.find(TradeMasterEntity.class, tradeId);

			if(entity==null) {
				throw new RuntimeException("Trade Master not found");
			}
			
			tradeMasterDTO = new TradeMasterDTO();
			
			tradeMasterDTO.setTradeId(tradeId);
			tradeMasterDTO.setLatestTradeVersion(entity.getLatestTradeVersion());
			
			
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
			if(entityManager!=null ) {
				DatabaseConnectionUtil.getInstance().close(entityManager);
			}
		}
		return tradeMasterDTO;	
	}


}
