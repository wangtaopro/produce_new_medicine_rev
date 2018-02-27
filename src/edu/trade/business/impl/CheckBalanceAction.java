package edu.trade.business.impl;

import javax.annotation.Resource;

import edu.trade.util.CommonUtil;
import org.springframework.stereotype.Controller;

import edu.trade.business.MedicineAction;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicineQueryService;

/**
 * 账户余额查询 
 */
@Controller("checkBalanceAction")
public class CheckBalanceAction extends MedicineAction{

	private static final long serialVersionUID = 881454030557530900L;
	
	@Resource
	private MedicineQueryService medicineQueryService;
	
	@Override
	public ReturnEntity deal(Body body) {
		ReturnEntity returnEntity = new ReturnEntity();
		try{
			
			
			returnEntity = medicineQueryService.queryAccountBalance(body);
			return returnEntity;
		}catch(Exception e){
			e.printStackTrace();
			return CommonUtil.getReturnEntity(e.getMessage(),"0",body.getHeader());
		}
		
	}
	
	
	
}
