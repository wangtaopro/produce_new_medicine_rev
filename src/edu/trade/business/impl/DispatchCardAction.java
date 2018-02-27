package edu.trade.business.impl;

import javax.annotation.Resource;

import edu.trade.business.MedicineAction;
import edu.trade.util.CommonUtil;
import org.springframework.stereotype.Controller;

import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicineTradeService;

/**
 * 发卡交易
 */
@Controller("dispatchCardAction")
public class DispatchCardAction extends MedicineAction {

	private static final long serialVersionUID = -8184031573630333362L;
	
	@Resource
	private MedicineTradeService medicineTradeService;
	
	/** 
	 * 发卡逻辑，重点：成功或失败后的应对策略
	 */
	public ReturnEntity deal(Body body) {
		
		try {
			return medicineTradeService.dispatchCard(body);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity("NullPointerException", "0", body.getHeader());
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity(e.getMessage(), "0", body.getHeader());
		}
	}
}
