package edu.trade.service;

import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.entity.CustomerEntity;

public interface MedicineTradeService extends MedicineService {

	/**
	 *  1.发卡
	 */
	ReturnEntity dispatchCard(Body body) throws Exception;
	
	/**
	 *  2.实时扣款 
	 */
	ReturnEntity deduct(Body body) throws Exception;
	
	/**
	 *  3.换卡补卡 
	 */
	
	ReturnEntity changeCard(Body body) throws Exception;
	/**
	 * 保存卡信息交易细节
	 */
	boolean saveCardD(Body body) throws Exception;
	
	/**
	 * 保存卡实体
	 */
	boolean saveCardE(Body body) throws Exception;
	
	/**
	 * 更新客户实体
	 */
	boolean updateCustomerE(Body body) throws Exception;

	/**
	 *  查找卡
	 */
	boolean findCard(CustomerEntity ce) throws Exception;

	/**
	 * 更新发卡交易表
	 */
	void updateCard(Body body) throws Exception;
	
	/**
	 *  4. 删除批次
	 */
	ReturnEntity deleteBatch(Body body) throws Exception;
	
	/**
	 * 查找团体保单号
	 */
	
	CustomerEntity selectCustomerEntityByContno(String contno) throws Exception;
}
