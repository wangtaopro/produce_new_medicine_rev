package edu.trade.mapper;

import edu.trade.dto.Body;

public interface MedicineQueryMapper {

	/** 
	 * 查询交易结果 
	 */
	public String queryTradeResult(Body body) throws Exception;

	/** 
	 * 查询账户余额
	 */
	public String queryBalance(Body body) throws Exception;
}
