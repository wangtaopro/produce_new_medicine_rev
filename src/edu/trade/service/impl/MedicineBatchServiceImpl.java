package edu.trade.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;


import edu.trade.business.impl.CheckAccountDetailAction;
import edu.trade.service.MedicineBatchService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.entity.BasicEntity;
import edu.trade.entity.CardEntity;
import edu.trade.entity.CustomerEntity;
import edu.trade.entity.OtherEntity;
import edu.trade.entity.RealTimeTradeEntity;
import edu.trade.entity.TransEntity;
import edu.trade.mapper.MedicineBatchMapper;
import edu.trade.mapper.MedicinePublicMapper;
import edu.trade.util.BatchNoUtil;
import edu.trade.util.ClientUtil;
import edu.trade.util.CommonUtil;
import edu.trade.util.DateUtil;
import edu.trade.util.PackFTPUtil;

@Service("medicineBatchService")
public class MedicineBatchServiceImpl extends MedicineServiceImpl implements MedicineBatchService {
	
	private Log log = LogFactory.getLog(CheckAccountDetailAction.class);
	
	@Resource
	private MedicineBatchMapper medicineBatchMapper;
	@Resource
	private MedicinePublicMapper medicinePublicMapper;


	/**
	 * 1.客户信息变更
	 */
	public ReturnEntity uploadUserInfo(Body body)throws Exception{
		ReturnEntity returnEntity = new ReturnEntity();
		
		/** 判断传入数据是否为空 **/
		if(body == null){
			return CommonUtil.getReturnEntity("传入body为空！","0",body.getHeader());
		}
		
		try{
			/**查询做过保全的客户信息**/
			List<Body> bodyList = new ArrayList<Body>();
			bodyList = medicineBatchMapper.queryCustomer(body);
			
			/** 判断是否查询到数据**/
			if(bodyList.size() == 0 || bodyList == null){
				return CommonUtil.getReturnEntity("当天没有客户信息变更！", "1", body.getHeader());
			}
			
			/**将获取到的发卡信息放入Body**/
			Body newBody = new Body();
			CustomerEntity[] customerEntitys = new CustomerEntity[bodyList.size()];
			for(int i = 0;i < bodyList.size();i++){
				customerEntitys[i] = bodyList.get(i).getCustomerEntity();
			}
			newBody.setCustomerEntityList(customerEntitys);
			newBody.setHeader(body.getHeader());
			newBody.setTransEntity(body.getTransEntity());
			
			/**生成xml文件,生成压缩包并上传ftp服务器**/
			String path = PackFTPUtil.uploadToFTP(newBody, null);

			/**同时存表Med_TpaDetail_business**/
			for(int i = 1;i <= customerEntitys.length;i++){
				CustomerEntity customerEntity = customerEntitys[i-1]; 
				customerEntity.setNo(String.valueOf(i));
				newBody.setCustomerEntity(customerEntity);
				newBody.setHeader(body.getHeader());
				newBody.setOtherEntity(body.getOtherEntity());
				medicineBatchMapper.insertCustomer(newBody);
			}
			
			/** 将路径发送给第三方 **/
			TransEntity transEntity = new TransEntity();
			Body oldBody = new Body();
			transEntity.setUrl(path);
			transEntity.setThirdPartyPort(body.getTransEntity().getThirdPartyPort());
			transEntity.setThirdPartyIp(body.getTransEntity().getThirdPartyIp());
			oldBody.setTransEntity(transEntity);
			oldBody.setHeader(newBody.getHeader());
			
			/** 请求第三方,从配置文件获取第三方IP与端口,通过客户端访问第三方 **/
			 String result = ClientUtil.request(oldBody);
			
			/** 获取返回结果 **/
			returnEntity = JSON.parseObject(result,ReturnEntity.class);
			
		}catch(RuntimeException ee){
			ee.printStackTrace();
			return CommonUtil.getReturnEntity("客户信息变更异常！", "0",returnEntity.getHeader());
		}
		return returnEntity;
	}

	/**
	 * 2.日对账(明细对账)
	 */
	public Body checkAccoutInfo(Body body) throws Exception{
		if( "CDA".equals(body.getHeader().getTradeType()) ){
			try {
				/** C表数据 **/
				List<BasicEntity> basicEntities = medicineBatchMapper.selectLCMedBuyMain(body);
				for(BasicEntity sonBasicEntity : basicEntities){
					sonBasicEntity.setMedState("0");
				}
				
				/** B表数据 **/
				List<BasicEntity> basicEntitiesB = medicineBatchMapper.selectLCMedBuyMainB(body);
				for(BasicEntity sonBasicEntity : basicEntitiesB){
					sonBasicEntity.setMedState("1");
				}
				
				basicEntities.addAll(basicEntitiesB);
				
				Body totalBody = new Body();
				
				/** 如果没有数据只组装basicEntity **/
				if (basicEntities.size() == 0) {
					BasicEntity basicEntity = new BasicEntity();
					basicEntity.setTransSumVolume(0.0);
					basicEntity.setBatchCount(0);
					basicEntity.setBuyDate(body.getBasicEntity().getBuyDate());
					totalBody.setBasicEntity(basicEntity);
					log.debug(JSONObject.toJSONString(totalBody,true));
					return totalBody;
				} 
				
				/**有数据组装数组**/
				BasicEntity[] basicEntityList = new BasicEntity[basicEntities.size()]; 
				BasicEntity basicEntity = new BasicEntity();
				Double transSumVolume = 0.0;
				Integer batchCount = 0;
				/** 拆分查询结果存入一个Body中 **/
				for (int i = 0 ; i < basicEntityList.length ; i++) {
					basicEntityList[i] = basicEntities.get(i);
					transSumVolume += basicEntities.get(i).getSumPrice();
					batchCount += 1;
				}
				BigDecimal bigDecimal = new BigDecimal(transSumVolume);
				transSumVolume = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				basicEntity.setTransSumVolume(transSumVolume);
				basicEntity.setBatchCount(batchCount);
				basicEntity.setBuyDate(body.getBasicEntity().getBuyDate());
				totalBody.setBasicEntity(basicEntity);
				totalBody.setBasicEntityList(basicEntityList);
				log.debug(JSONObject.toJSONString(totalBody,true));
				return totalBody;
			} catch (Exception e) {
				return null;
			}

		} else if ( "CDC".equals(body.getHeader().getTradeType()) ) {
			
		} else {
			
		}
		return null;
	}

	/**
	 * 3.收集手机信息
	 */
	public ReturnEntity collectMobile(Body body) throws Exception {
		return null;
	}

	/**
	 * 4.上传交易明细
	 */
	public ReturnEntity uploadTradeDetail(Body body) throws Exception {
		
		/** 1.校验批次数量 **/
		int length = body.getBasicEntityList().length;
		if(!Integer.valueOf(length).equals(body.getBasicEntity().getBatchCount())){
			return CommonUtil.getReturnEntity("批次数量错误!", "0", body.getHeader());
		}
		
		
		/** 2.校验批次号是否存在 **/
		String batchNo = "";
		for(int i = 0; i < body.getBasicEntityList().length; i++){
			BasicEntity basicEntity = body.getBasicEntityList()[i];
			try {
				batchNo = medicinePublicMapper.selectBatchNoFromMedTpaMain(basicEntity.getForeignBatchNo());
				if(batchNo == null || "".equals(batchNo)){
					return CommonUtil.getReturnEntity("无批次号", "0", body.getHeader());
				} else {
					String taiKangBatchNo = new String(batchNo);
					basicEntity.setTaiKangBatchNo(taiKangBatchNo);
				}
			} catch (Exception e) {
				return CommonUtil.getReturnEntity("无批次号", "0", body.getHeader());
			}
		}
		
		/** 3.校验sumOfMoney **/
		BasicEntity[] basicEntities = body.getBasicEntityList();
		Double sumPriceOutDouble = 0.0;
		for (int i = 0; i < basicEntities.length; i++) {
			RealTimeTradeEntity[] realTimeTradeEntities = basicEntities[i].getRealTimeTradeEntityList();
			Double sumPrice = 0.0;
			for (int j = 0; j < realTimeTradeEntities.length; j++) {
				sumPrice += realTimeTradeEntities[j].getUnitPrice()*realTimeTradeEntities[j].getQuantity();
				BigDecimal bigDecimal = new BigDecimal(sumPrice);
				sumPrice = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				sumPriceOutDouble += sumPrice;
			}
			try {
				String taiKangBatchNo = basicEntities[i].getTaiKangBatchNo();
				Double  basicEntitySumPrice = basicEntities[i].getSumPrice();
				
				Double sumPayPrice = 
					medicinePublicMapper.selectSumPayPriceFromLCMedBuyMain(taiKangBatchNo);
				if(!basicEntitySumPrice.equals(sumPrice)){
					return CommonUtil.
						getReturnEntity("批次号为" + taiKangBatchNo + "的总价与单价之和不一致", "0", body.getHeader());
				}
				
				if(!sumPayPrice.equals(sumPrice)){
					return CommonUtil.getReturnEntity(
							"批次号为" + taiKangBatchNo + "的总价与数据库不一致", "0", body.getHeader());
				}
			} catch (Exception e) {
				return CommonUtil.getReturnEntity("查询总价失败", "0", body.getHeader());
			}
		}
		if (!sumPriceOutDouble.equals(body.getBasicEntity().getTradeVolume())) {
			return CommonUtil.getReturnEntity("报文中的总价与明细总价不相等", "0", body.getHeader());
		}
		
		/**4.存表**/
		try {
			DateUtil dateUtil = new DateUtil();
			for (int i = 0; i < basicEntities.length; i++) {
				
				/** 准备BasicEntity数据 **/
				BasicEntity basicEntity = new BasicEntity();
				BeanUtils.copyProperties(basicEntity, body.getBasicEntity());
				BeanUtils.copyProperties(basicEntity, basicEntities[i]);
				Body newBody = new Body();
				newBody.setBasicEntity(basicEntity);
				
				/** 准备OtherEntity数据 **/
				OtherEntity otherEntity = new OtherEntity();
				otherEntity.setMakeDate(new Date());
				otherEntity.setModifyDate(new Date());
				otherEntity.setMakeTime(dateUtil.getTime());
				otherEntity.setModifyTime(dateUtil.getTime());
				otherEntity.setLogSerialNo(new BatchNoUtil().getSerialNo());
				otherEntity.setState("1");
				newBody.setOtherEntity(otherEntity);
				
				medicineBatchMapper.insertLCMedAddressInfoLog(newBody);
				RealTimeTradeEntity[] realTimeTradeEntities = basicEntities[i].getRealTimeTradeEntityList();
				for (int j = 0; j < realTimeTradeEntities.length; j++) {
					RealTimeTradeEntity realTimeTradeEntity = new RealTimeTradeEntity();
					BeanUtils.copyProperties(realTimeTradeEntity, realTimeTradeEntities[j]);
					newBody.setRealTimeTradeEntity(realTimeTradeEntity);
					medicineBatchMapper.insertLCMedBuyDetail(newBody);
				}
			}
		} catch (Exception e) {
			CommonUtil.getReturnEntity("存表失败", "0", body.getHeader());
		}
		
		
		return CommonUtil.getReturnEntity("上传明细成功", "1", body.getHeader());
	}
	
	/**
	 * 5.日对账(卡对账)
	 */
	public ReturnEntity checkCardInfo(Body body) throws Exception{
		ReturnEntity returnEntity = new ReturnEntity();
		
		/** 判断传入数据是否为空 **/
		if(body == null){
			return CommonUtil.getReturnEntity("传入body为空！","0",body.getHeader());
		}
		
		try{
			/** 查询当天的发卡信息 LCMedCardUserInfo **/
			List<CardEntity> cList = new ArrayList<CardEntity>();
			cList = medicineBatchMapper.queryCardDetail(body);

			/** 判断是否查询到数据**/
			if(cList.size() == 0 || cList == null){
				return CommonUtil.getReturnEntity("当天没有发卡信息！", "1", body.getHeader());
			}
			
			/** 将获取到的发卡信息放入Body **/
			Body newBody = new Body(); 
			CardEntity cardEntity = new CardEntity();
			cardEntity.setCardCount(cList.size());
			CardEntity[] cardEntities = new CardEntity[cList.size()];
			cardEntities = cList.toArray(cardEntities);
			
			newBody.setCardEntityList(cardEntities);
			newBody.setCardEntity(cardEntity);
			newBody.setHeader(body.getHeader());
			newBody.setTransEntity(body.getTransEntity());
			
			/**复杂对象转XML,生成压缩包,上传ftp服务器并将地址发给第三方**/
			String path = PackFTPUtil.uploadToFTP(newBody, null);
			
			/** 将路径发送给第三方 **/
			TransEntity transEntity = new TransEntity();
			Body oldBody = new Body();
			transEntity.setUrl(path);
			transEntity.setThirdPartyIp(body.getTransEntity().getThirdPartyIp());
			transEntity.setThirdPartyPort(body.getTransEntity().getThirdPartyPort());
			oldBody.setTransEntity(transEntity);
			oldBody.setHeader(newBody.getHeader());
			
			/** 请求第三方,从配置文件获取第三方IP与端口,通过客户端访问第三方 **/
			String result = ClientUtil.request(oldBody);

			/** 获取返回结果 **/
			returnEntity = JSON.parseObject(result,ReturnEntity.class);
			
		}catch(Exception e){
			e.printStackTrace();
			return CommonUtil.getReturnEntity("卡对账交易异常！","0",body.getHeader());
		}
		return returnEntity;
	}


}
