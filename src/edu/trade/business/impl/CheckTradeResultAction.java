package edu.trade.business.impl;

import javax.annotation.Resource;

import edu.trade.util.CommonUtil;
import org.springframework.stereotype.Controller;

import edu.trade.business.MedicineAction;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicineQueryService;

/**
 * 交易结果查询
 */
@Controller("checkTradeResultAction")
public class CheckTradeResultAction extends MedicineAction{

	@Resource
	private MedicineQueryService medicineQueryService;
	
	private static final long serialVersionUID = 3036054236347011315L;

	@Override
	public ReturnEntity deal(Body body) {
		
		ReturnEntity queryTradeResult = null;
		
		try {
			queryTradeResult = medicineQueryService.queryTradeResult(body);
			return queryTradeResult;
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity(e.getMessage(),"0",body.getHeader());
		}
	}

	
}
