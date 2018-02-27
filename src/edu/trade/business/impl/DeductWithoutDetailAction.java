package edu.trade.business.impl;

import javax.annotation.Resource;

import edu.trade.entity.OtherEntity;
import edu.trade.entity.RealTimeTradeEntity;
import edu.trade.util.BatchNoUtil;
import edu.trade.util.CommonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import edu.trade.business.MedicineAction;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.entity.BasicEntity;
import edu.trade.mina.ServerHandler;
import edu.trade.service.MedicinePublicService;
import edu.trade.service.MedicineTradeService;

/**
 * 实时扣款（不带交易明细）
 */
@Controller("deductWithoutDetailAction")
public class DeductWithoutDetailAction extends MedicineAction{

	private static final long serialVersionUID = 790546497146658817L;

	@Resource
	private MedicinePublicService medicinePublicService ;
	@Resource
	private MedicineTradeService medicineTradeService ;

	private final static Log log = LogFactory.getLog(ServerHandler.class);
	
	/**
	 * 核心方法，分发任务

	 */
	@Override
	public ReturnEntity deal(Body body) {
		ReturnEntity returnEntity = new ReturnEntity();
		if("RTD".equals(body.getHeader().getTradeType().trim())){
			/** 在DeductWithDetailAction中实现 **/
		} else if ("RLD".equals(body.getHeader().getTradeType().trim())) {
			if(checkBody(body)){
				returnEntity = dealRLD(body);
			} else {
				returnEntity = CommonUtil.getReturnEntity("未通过合法性校验", "0" , body.getHeader());
			}
		}
		return returnEntity;
	}
	
	/**
	 * 不带明细实时交易
	 * @param body
	 * @return
	 */
	public ReturnEntity dealRLD(Body body) {
		Body completeBody = new Body();
		completeBody = prepareBody(body);
		ReturnEntity returnEntity = new ReturnEntity();
		try {
			returnEntity = medicineTradeService.deduct(completeBody);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity("实时交易失败", "0" , body.getHeader());
		}
		return returnEntity;
	}
	
	/**
	 * 准备数据
	 * @param body
	 * @return
	 */
	public Body prepareBody(Body body) {
		Body completeBody = new Body();
		/** 准备header **/
		completeBody.setHeader(body.getHeader());
		
		/**准备basicEntity**/
		BasicEntity basicEntity = new BasicEntity();
		basicEntity = body.getBasicEntity();
		basicEntity.setSumClmPrice(0.0);
		completeBody.setBasicEntity(basicEntity);
		
		/**准备realTimeTradeEntity**/
		RealTimeTradeEntity realTimeTradeEntity = new RealTimeTradeEntity();
		realTimeTradeEntity.setMedType1("RHGK");
		realTimeTradeEntity.setSeqNo("1");
		completeBody.setRealTimeTradeEntity(realTimeTradeEntity);
		
		/** 准备otherEntiy **/
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
			flag = medicinePublicService.checkIntegrality(body);
			log.info(flag);
		} catch (Exception e) {
			return false;
		}
		if("21".equals(flag)){
			return true;
		} else {
			return false;
		}
	}

}
