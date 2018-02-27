package edu.trade.service;

import MedData.CheckReplyInt;

import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;

public interface MedicineQueryService extends MedicineService{
	
	/**
	 * 1.查询账户余额 
	 */
	ReturnEntity queryAccountBalance(Body body)throws Exception ;
	
	/**
 	 * 2.查询交易结果
	 */
	ReturnEntity queryTradeResult(Body body) throws Exception;
	
	/**
	 * 3.查询余额 
	 */
	CheckReplyInt getCheckReplyInt(Body body)throws RuntimeException;

}
