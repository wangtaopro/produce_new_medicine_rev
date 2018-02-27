package edu.trade.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import edu.trade.entity.CardEntity;
import edu.trade.entity.ConfigEntity;
import edu.trade.entity.ConfigInfoEntity;
import edu.trade.entity.OtherEntity;
import edu.trade.util.TradeTypeUtil;
import edu.trade.util.ValidateUtil;
import org.springframework.stereotype.Service;

import edu.trade.dto.Body;
import edu.trade.entity.BasicEntity;
import edu.trade.mapper.MedicinePublicMapper;
import edu.trade.service.MedicinePublicService;

@Service("medicinePublicService")
public class MedicinePublicServiceImpl implements MedicinePublicService{

	@Resource
	private MedicinePublicMapper medicinePublicMapper; 
	
	/** 存主表 **/
	public boolean saveMain(Body body) throws Exception {
		try{
			/** 1.归一化请求参数 **/
			if(body.getBasicEntity() == null || 
					body.getBasicEntity().getBatchCount() == null || 
					body.getBasicEntity().getTransSumVolume() == null){
				
				BasicEntity basicEntity = null;
				if(body.getBasicEntity() == null){
					basicEntity = new BasicEntity();
				}else {
					basicEntity = body.getBasicEntity();
				}
				
				basicEntity.setBatchCount(0);
				basicEntity.setTransSumVolume(0.0);
				body.setBasicEntity(basicEntity);
				
			}
			
			/** 2.为operator设值 **/
			if(TradeTypeUtil.isEbaRequestType(body.getHeader().getTradeType())){
				
				body.getOtherEntity().setOperator("EBA");
				
				BasicEntity basicEntity = new BasicEntity();
				basicEntity.setMedShopCode("EBA_SHOP_CODE");
				basicEntity.setCounterNo("EBA_COUNTER_NO");
				
				body.setBasicEntity(basicEntity);
				
			} else {
				body.getOtherEntity().setOperator(body.getHeader().getIp());
			}
			
			/** 3.映射批次号，系统之内的批次号全部为内部批次号 **/
			OtherEntity otherEntity = new OtherEntity();
			
			/** 4.重新生成时间 **/
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String format = simpleDateFormat.format(new Date());
			Date valiableDate = simpleDateFormat.parse(format);
			
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm:ss");
			String valiableTime = simpleDateFormat1.format(new Date());
			
			otherEntity.setMakeDate(valiableDate);
			otherEntity.setMakeTime(valiableTime);
			otherEntity.setModifyDate(valiableDate);
			otherEntity.setModifyTime(valiableTime);
			
			medicinePublicMapper.addMedTpaMain(body);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("发送信息异常："+e.getMessage());
		}
	}

	/** 修改主表状态 **/
	public boolean modifyMainState(Body body) throws Exception {
		try {
			medicinePublicMapper.modifyMainState(body);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("返回信息异常："+e.getMessage());
		}
	}

	/** 实时交易完整性校验 **/
	public String checkIntegrality(Body body) throws Exception {
		
		/** 卡号不得为空 **/
		if (body.getBasicEntity().getMedCardNo() == null || 
				"".equals(body.getBasicEntity().getMedCardNo())) {
			return "1";
		}

		/** 校验卡号是否为第三方发卡 **/
		String isMedCardNoTrue = medicinePublicMapper.queryIsMedCardNoTrue(body.getBasicEntity().getMedCardNo());
		if("0".equals(isMedCardNoTrue)){
			return "22";
		}
		
		/** 终端号不得为空 **/
		if(body.getBasicEntity().getContNo() == null || 
				"".equals(body.getBasicEntity().getContNo())){
			return "2";
		}
		
		/** 总价不得为空 **/
		if(body.getBasicEntity().getSumPrice() == null || 
				"".equals(body.getBasicEntity().getSumPrice())){
			return "3";
		}
		
		/** 校验购药渠道是否为空 **/
		if(body.getBasicEntity().getBuyChnl() == null || 
				"".equals(body.getBasicEntity().getBrowserType())
			){
			return "5";
		}
		
		/** 当购药渠道为网购时，必传字段校验 **/
		if("1".equals(body.getBasicEntity().getBuyChnl())){
			/** 寄送地址必传 **/
			if (body.getBasicEntity().getAddress() == null || 
					"".equals(body.getBasicEntity().getAddress())) {
				return "6";
			}
			
			/** 收件人必传 **/
			if(body.getBasicEntity().getAddressee() == null || 
					"".equals(body.getBasicEntity().getAddressee())){
				return "7";
			}
			
			/** 联系方式必传 **/
			if (body.getBasicEntity().getMobile() == null || 
					"".equals(body.getBasicEntity().getMobile())) {
				return "8";
			}
			
			/** 联系方式不符合格式 **/
			if(body.getBasicEntity().getMobile() == null || 
					"".equals(body.getBasicEntity().getMobile()) || 
						!ValidateUtil.isMobile(body.getBasicEntity().getMobile())){
				return "4";
			}
			
			if( "RTD".equals(body.getHeader().getTradeType()) ){
				/** 客户端IP必传 **/
				if(body.getHeader().getIp() == null || 
						"".equals(body.getHeader().getIp())){
					return "9";
				}
				
				/** 浏览器类型必传 **/
				if (body.getBasicEntity().getBrowserType() == null || "".equals(body.getBasicEntity().getBrowserType())) {
					return "10";
				}
			}
		}
		
		/** 校验总价与单价是否相符 **/
		if( "RTD".equals(body.getHeader().getTradeType()) ){
			Double sum = 0.0;
			for (int i = 0; i < body.getRealTimeTradeEntityList().length; i++) {
				sum += (body.getRealTimeTradeEntityList()[i].getUnitPrice() * body.getRealTimeTradeEntityList()[i].getQuantity());
			}
			if(sum.equals(body.getBasicEntity().getSumPrice())){
				
			} else {
				return "11";
			}
		}
		
		/** 商户号不得为空 **/
		if (body.getBasicEntity().getMedShopCode() == null || 
				"".equals(body.getBasicEntity().getMedShopCode())) {
			return "12";
		}
		
		/** 校验药店是否维护 **/
		String countDrugShop = medicinePublicMapper.queryIsDrugShopRight(body.getBasicEntity().getMedShopCode());
		if("0".equals(countDrugShop)){
			return "13";
		}
		
		/** 校验药店是否在黑名单中 **/
		String countBlackList = medicinePublicMapper.queryIsBlackList(body.getBasicEntity().getMedShopCode());
		if(!"0".equals(countBlackList)){
			return "14";
		}

		/** 终端号不得为空 **/
		if(body.getBasicEntity().getCounterNo() == null || 
				"".equals(body.getBasicEntity().getCounterNo())){
			return "15";
		}
		
		/** 校验终端号是否维护 **/
		String countDrugCounter = medicinePublicMapper.queryIsDrugConterRight(body.getBasicEntity().getCounterNo());
		if("0".equals(countDrugCounter)){
			return "16";
		}
		
		/** 商户名称不得为空 **/
		if (body.getBasicEntity().getMedShopName() == null || 
				"".equals(body.getBasicEntity().getMedShopName())) {
			return "17";
		}
		
		/** 终端名称不得为空 **/
		if(body.getBasicEntity().getCounterName() == null || 
				"".equals(body.getBasicEntity().getCounterName())){
			return "18";
		}
		
		/** 省市县不得为空 **/
		if(body.getBasicEntity().getProvince() == null 			|| 
				"".equals(body.getBasicEntity().getProvince()) 	||
				body.getBasicEntity().getCity() == null 		||
				"".equals(body.getBasicEntity().getCity()) 		||
				body.getBasicEntity().getCounty() == null		||
				"".equals(body.getBasicEntity().getCounty())
			){
			return "19";
		}
		
		/** 购药日期时间不为空 **/
		if(body.getBasicEntity().getBuyDate() == null || 
				"".equals(body.getBasicEntity().getBuyDate().toString()) ||
				body.getBasicEntity().getBuyTime() == null ||
				"".equals(body.getBasicEntity().getBuyTime())
			){
			return "20";
		}
		
		/** 药店校验正确 **/
		return "21";
	}

	/** 补卡换卡取得旧卡信息 **/
	public Body getOldCardInfo(Body body) throws Exception {
		List<CardEntity> cardEntityList  = medicinePublicMapper.selectLCMedCardUserInfo(
								body.getCardEntity().getOldMedCardNo());
		if(cardEntityList.size() != 1){
			return null;
		} else {
			CardEntity cardEntity = cardEntityList.get(0);
			body.setCardEntity(cardEntity);
			return body;
		}
	}

	/** 查询校验位 **/
	public String queryCheckBit(String checkBit) throws Exception {
		return  medicinePublicMapper.queryCheckBit(checkBit);
	}
	
	/** 通过公司名称缩写获取配置信息 **/
	public ConfigEntity selectConfigByName(ConfigInfoEntity configInfoEntity) {
		return medicinePublicMapper.selectConfigByName(configInfoEntity);
	}
	
	/** 通过Ip获取key **/
	public String selectKeyByIp(String ip) {
		return medicinePublicMapper.selectKeyByIp(ip);
	}
	
	/** 补卡换卡完整性校验 **/
	public String checkModifyCard(Body body) throws Exception{
		
		/** 校验此卡是否为请求方所发卡 **/
		
		if("".equals(body.getCardEntity().getThirdParty())
			|| body.getCardEntity().getThirdParty() == null){
			return "[sinosoft-----A001------此卡发放方与请求方不符!]";
		}
		
		/** 修改卡是否为保费卡 **/
		String flag = medicinePublicMapper.selectIsModifyCard(body.getCardEntity().getOldMedCardNo());
		if(!"1".equals(flag)){
			return "[sinosoft-----A002-----此卡已失效无法进行补卡退卡]";
		}
		
		return "0";
	}
	
}










































