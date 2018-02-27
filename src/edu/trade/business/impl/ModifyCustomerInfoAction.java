package edu.trade.business.impl;

import javax.annotation.Resource;

import edu.trade.util.CommonUtil;
import org.springframework.stereotype.Controller;

import edu.trade.business.MedicineAction;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicineBatchService;

/**
 * 客户信息变更
 */
@Controller("modifyCustomerInfoAction")
public class ModifyCustomerInfoAction extends MedicineAction{

	private static final long serialVersionUID = 1088643031663319608L;
	
	@Resource
	private MedicineBatchService medicineBatchService;


	@Override
	public ReturnEntity deal(Body body) {
		ReturnEntity returnEntity = new ReturnEntity();
		try{
			returnEntity = medicineBatchService.uploadUserInfo(body);
			
		}catch(Exception e){
			e.printStackTrace();
			return CommonUtil.getReturnEntity("客户信息变更失败！","0",body.getHeader());
		}
		return returnEntity;
	}

}
