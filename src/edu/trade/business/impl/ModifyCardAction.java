package edu.trade.business.impl;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import edu.trade.entity.CardEntity;
import edu.trade.entity.OtherEntity;
import edu.trade.util.CommonUtil;
import edu.trade.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import edu.trade.business.MedicineAction;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.mina.ServerHandler;
import edu.trade.service.MedicinePublicService;
import edu.trade.service.MedicineTradeService;

/**
 * 补卡换卡信息
 */
@Controller("modifyCardAction")
public class ModifyCardAction extends MedicineAction{

	private static final long serialVersionUID = -9056143570396930967L;

	
	@Resource
	private MedicineTradeService medicineTradeService ;
	@Resource
	private MedicinePublicService medicinePublicService ;
	
	private final static Log log = LogFactory.getLog(ServerHandler.class);

	
	/**
	 * 实现补卡换卡交易
	 */
	@Override
	public ReturnEntity deal(Body body) {
		ReturnEntity returnEntity = new ReturnEntity();
		if(body == null || body.getCardEntityList().length <= 0){
			returnEntity = CommonUtil.getReturnEntity("传入Body为空!","0",body.getHeader());
			return returnEntity;
		}
		return dealBody(body);
	
	}
	
	public ReturnEntity dealBody(Body body) {
		/** 准备数据 **/
		ArrayList<Body> bodyList = new ArrayList<Body>();
		for (int i = 0; i < body.getCardEntityList().length; i++) {
			/**拆分Body，方便处理**/
			Body sonBody = new Body();
			CardEntity cardEntity = body.getCardEntityList()[i];
			sonBody.setCardEntity(cardEntity);
			sonBody.setHeader(body.getHeader());
			/** 取得旧卡信息以便存入新卡 **/
			try {
				sonBody = medicinePublicService.getOldCardInfo(sonBody);
				
				sonBody.getCardEntity().setProvideDate(cardEntity.getProvideDate());
				sonBody.getCardEntity().setProvideTime(cardEntity.getProvideTime());
				sonBody.getCardEntity().setLostDate(cardEntity.getLostDate());
				sonBody.getCardEntity().setLostTime(cardEntity.getLostTime());
				sonBody.getCardEntity().setNewMedCardNo(body.getCardEntityList()[i].getNewMedCardNo());
				sonBody.getCardEntity().setOldCardState("2");  //2-代表作废
				sonBody.getCardEntity().setCardState("1");

				OtherEntity otherEntity = new OtherEntity();
				otherEntity.setMakeDate(new DateUtil().getToDayDate(new Date()));
				otherEntity.setModifyDate(new DateUtil().getToDayDate(new Date()));
				otherEntity.setMakeTime(new DateUtil().getTime());
				
				sonBody.setOtherEntity(otherEntity);
				String msg = medicinePublicService.checkModifyCard(sonBody);
				if ("0".equals(msg)) {
					bodyList.add(sonBody);
				} else {
					log.error("卡号为"+ sonBody.getCardEntity().getOldMedCardNo() + "的信息的问题为" + msg);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		
		/** 补换卡业务逻辑 **/
		int flag = 0;
		if (bodyList.size() <= 0) {
			return CommonUtil.getReturnEntity("数据为空", "0", body.getHeader());
		}
		ReturnEntity returnEntity = new ReturnEntity();
		
		
		for (int i = 0; i < bodyList.size(); i++) {
			Body sonBody = bodyList.get(i);
			try {
				returnEntity = medicineTradeService.changeCard(sonBody);
				if ("1".equals(returnEntity.getResultCode())) {
					flag++;
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		if (flag == 0) {
			returnEntity = CommonUtil.getReturnEntity("全部请求失败", "0" , body.getHeader());
		} else if (flag == bodyList.size()) {
			returnEntity = CommonUtil.getReturnEntity("全部请求成功", "1" , body.getHeader());
		} else {
			returnEntity = CommonUtil.getReturnEntity("部分请求成功", "2" , body.getHeader());
		}
		return returnEntity;
	}
	
	

}
