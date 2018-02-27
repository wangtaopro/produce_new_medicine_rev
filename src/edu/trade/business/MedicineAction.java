package edu.trade.business;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;

import edu.trade.entity.OtherEntity;
import edu.trade.util.CommonUtil;
import edu.trade.util.TradeTypeUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicinePublicService;

/**
 * 业务基类
 */
public abstract class MedicineAction implements Serializable{

	@Resource
	private MedicinePublicService medicinePublicService;
	
	private Logger logger = Logger.getLogger(MedicineAction.class);
	
	/** 处理主逻辑-第三方请求时调用 **/
	public ReturnEntity deal(Body body){
		return null;
	}
	
	/** 存主表 **/
	public boolean saveMain(Body body) throws RuntimeException {
		try {
			
			boolean flag = medicinePublicService.saveMain(body);

			if(flag){
				body.getHeader().setBatchNo(body.getOtherEntity().getBatchNo());
				body.getOtherEntity().setBatchNo(null);
			}
			
			return flag ; 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** 修改主表状态 **/
	public boolean modifyMainState(Body body) throws RuntimeException{
		
		try {
			return medicinePublicService.modifyMainState(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/** 主方法 **/
	public ReturnEntity process(Body body){
		
		ReturnEntity returnEntity = new ReturnEntity();
		
		try {
			
			/** 给接收数据存值 **/
			setProperty(body,"2","acceptMessage", body);
			
			/** 存主表 **/
			if(!saveMain(body)){
				logger.info("该条数据有误，详情为："+JSONObject.toJSONString(body));
				return CommonUtil.getReturnEntity("存取信息失败！","0", body.getHeader());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity("交易前中断，请核对交易数据！","0", body.getHeader());
		}
		
		try{
			if(!TradeTypeUtil.isRequestType(body.getHeader().getTradeType())
				&& !TradeTypeUtil.isEbaRequestType(body.getHeader().getTradeType())
				&& !TradeTypeUtil.isResponseType(body.getHeader().getTradeType()
			)){
				return CommonUtil.getReturnEntity("交易前中断，请求类型与请求方不一致！","0", body.getHeader());
			}
			
			/** 核心业务处理 **/
			returnEntity = deal(body);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			try {
				/** 失败存表 **/
				if(returnEntity == null){
					returnEntity.setResultCode("0");
				}
				
				setProperty(body,returnEntity.getResultCode(),"sendMessage", returnEntity);
			} catch(Exception e1) {
				e1.printStackTrace();
				logger.info("修改表状态失败！");
			}
			return CommonUtil.getReturnEntity(e.getMessage(), "0", body.getHeader());
		}
		
		try{
			/** 给发出数据存值 **/
			setProperty(body,returnEntity.getResultCode(),"sendMessage", returnEntity);
			
			/** 回写表状态 **/
			if(!modifyMainState(body)){
				logger.info("回写表状态失败！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改表状态异常");
			return CommonUtil.getReturnEntity("修改表状态异常", "0", body.getHeader());
		}
		
		return returnEntity;
	}
	
	/** 简单设值 **/
	private void setProperty(Body body,String state,String msgDir, Object obj)
			throws IllegalAccessException, InvocationTargetException {
		OtherEntity entity = new OtherEntity();
		BeanUtils.setProperty(entity, msgDir, JSONObject.toJSONString(obj));
		BeanUtils.setProperty(entity, "state", state);
		BeanUtils.setProperty(body, "otherEntity",entity);
	}
	
}
