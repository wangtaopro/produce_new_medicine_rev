package edu.trade.business.impl;

import javax.annotation.Resource;

import edu.trade.util.CommonUtil;
import org.springframework.stereotype.Controller;

import edu.trade.business.MedicineAction;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicineBatchService;

/**
 * 日对账（卡信息对账）
 */
@Controller("checkCardDetailAction")
public class CheckCardDetailAction extends MedicineAction{

	private static final long serialVersionUID = -7776003007786573830L;
	
	@Resource
	private MedicineBatchService medicineBatchService;

	@Override
	public ReturnEntity deal(Body body) {
		ReturnEntity returnEntity = new ReturnEntity();
		try{
			
			returnEntity = medicineBatchService.checkCardInfo(body);
			
		}catch(Exception e){
			e.printStackTrace();
			return CommonUtil.getReturnEntity("卡对账交易失败！","0",body.getHeader());
		}
		return returnEntity;
	}
	
}
