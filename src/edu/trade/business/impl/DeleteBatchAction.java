package edu.trade.business.impl;

import javax.annotation.Resource;

import edu.trade.business.MedicineAction;
import edu.trade.util.CommonUtil;
import org.springframework.stereotype.Controller;

import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicineTradeService;

/**
 * 删除批次
 * @author itw_wuzp
 * @date 20151125
 */

@Controller("deleteBatchAction")
public class DeleteBatchAction extends MedicineAction {

	private static final long serialVersionUID = 8728156382099299498L;
	
	@Resource
	private MedicineTradeService medicineTradeService;
	
	@Override
	public ReturnEntity deal(Body body) {
		ReturnEntity returnEntity = new ReturnEntity();
		try{
			
			returnEntity = medicineTradeService.deleteBatch(body);
			
		}catch(Exception e){
			e.printStackTrace();
			return CommonUtil.getReturnEntity("批次删除失败！","0",body.getHeader());
		}
		return returnEntity;
	}

}
