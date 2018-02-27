package edu.trade.service;

import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;

public interface MedicineBatchService extends MedicineService{

	/**
	 *  1.客户信息变更 
	 */
	ReturnEntity uploadUserInfo(Body body)throws Exception;
	
	/**
	 *  2.批量上传交易
	 */
	ReturnEntity uploadTradeDetail(Body body)throws Exception;
	
	/**
	 *  3.收集手机号码
	 */
	ReturnEntity collectMobile(Body body)throws Exception;
	
	/**
	 *  4.日对账(明细对账)
	 */
	Body checkAccoutInfo(Body body) throws Exception;

	/**
	 * 5.日对账(卡对账)
	 */
	ReturnEntity checkCardInfo(Body body) throws Exception;
	
}
