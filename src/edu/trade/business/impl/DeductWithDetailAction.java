package edu.trade.business.impl;

import javax.annotation.Resource;

import edu.trade.entity.OtherEntity;
import edu.trade.util.BatchNoUtil;
import edu.trade.util.CommonUtil;
import org.springframework.stereotype.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import edu.trade.business.MedicineAction;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.entity.BasicEntity;
import edu.trade.mina.ServerHandler;
import edu.trade.service.MedicinePublicService;
import edu.trade.service.MedicineTradeService;

/**
 * 实时扣款
 */
@Controller("deductWithDetailAction")
public class DeductWithDetailAction extends MedicineAction{

	private static final long serialVersionUID = 6594300497798174991L;
	
	@Resource
	private MedicineTradeService medicineTradeService ;
	@Resource
	private MedicinePublicService medicinePublicService ;
	
	
	private final static Log log = LogFactory.getLog(ServerHandler.class);
	@Override
	public ReturnEntity deal(Body body) {
		ReturnEntity returnEntity = new ReturnEntity();
		if("RTD".equals( body.getHeader().getTradeType().trim())){
			if(checkBody(body)){
				returnEntity = dealRTD(body);
			} else {
				returnEntity = CommonUtil.getReturnEntity("未通过合法性校验", "0" , body.getHeader());
			}
		} else if ("RLD".equals( body.getHeader().getTradeType().trim())) {
		}
		return returnEntity;
	}

	public ReturnEntity deal(Body body , String sessionIP) {
		ReturnEntity returnEntity = new ReturnEntity();
		if("RTD".equals( body.getHeader().getTradeType().trim())){
			if(checkBody(body)){
				returnEntity = dealRTD(body);
			} else {
				returnEntity = CommonUtil.getReturnEntity("未通过合法性校验", "0" , body.getHeader());
			}
		} else if ("RLD".equals( body.getHeader().getTradeType().trim())) {
		}
		return returnEntity;
	}
	
	
	public ReturnEntity dealRTD(Body body) {
		Body completeBody = new Body();
		completeBody = prepareBody(body);
		boolean flag = true;
		ReturnEntity returnEntity = new ReturnEntity();
		if(flag){
			try {
				returnEntity = medicineTradeService.deduct(completeBody);
			} catch (Exception e) {
				e.printStackTrace();
				returnEntity = CommonUtil.getReturnEntity("存主表失败", "0" , body.getHeader());
			}
		} else {
			returnEntity = CommonUtil.getReturnEntity("存主表失败", "0" , body.getHeader());
		}
		return returnEntity;
	}
	
	public Body prepareBody(Body body) {
		Body completeBody = new Body();
		completeBody.setHeader(body.getHeader());
		BasicEntity basicEntity = new BasicEntity();
		basicEntity = body.getBasicEntity();
		
		/** 仁和没有报销金额,赋值为0 ,后期按照其他第三方需要进行修改 **/
		basicEntity.setSumClmPrice(0.0);
		
		
		completeBody.setBasicEntity(basicEntity);
		completeBody.setRealTimeTradeEntityList(body.getRealTimeTradeEntityList());
		OtherEntity otherEntity = new OtherEntity();
		otherEntity.setOperator("tk");
		otherEntity.setLogSerialNo(new BatchNoUtil().getSerialNo());
		otherEntity.setState("2");
		completeBody.setOtherEntity(otherEntity);
		return completeBody;
	}
	
	/**
	 * 校验数据合法性

	 * @param body
	 * @return
	 */
	public boolean checkBody(Body body){
		String flag = "0";
		try {
			log.info("初始化状态" + flag);
			flag = medicinePublicService.checkIntegrality(body);
			log.info("校验后状态" + flag);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(flag == "21" || "21".equals(flag)){
			log.info("校验后状态" + flag);
			return true;
		} else {
			return false;
		}
	}
	
}









































