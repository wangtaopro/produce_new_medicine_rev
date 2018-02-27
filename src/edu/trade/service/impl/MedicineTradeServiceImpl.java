package edu.trade.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import edu.trade.business.assisstant.DispatchCardAssisstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import MedData.CertifyInt;
import MedData.CheckReplyInt;
import MedOper.CheckRemainingImpNew;

import com.alibaba.fastjson.JSONObject;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.entity.BasicEntity;
import edu.trade.entity.CustomerEntity;
import edu.trade.entity.Header;
import edu.trade.entity.MedBuyDetailEntity;
import edu.trade.entity.MedBuyMainEntity;
import edu.trade.entity.RealTimeTradeEntity;
import edu.trade.entity.ReturnInfoEntity;
import edu.trade.mapper.MedicinePublicMapper;
import edu.trade.mapper.MedicineTradeMapper;
import edu.trade.service.MedicineTradeService;
import edu.trade.util.CommonUtil;
import edu.trade.util.CopyUtil;
import edu.trade.util.DateUtil;
import edu.trade.util.PackFTPUtil;
import edu.trade.util.XmlUtil;

@Service("medicineTradeService")
public class MedicineTradeServiceImpl extends MedicineServiceImpl implements MedicineTradeService{
	
	private static Log log = LogFactory.getLog(MedicineTradeServiceImpl.class);
	
	private static final String CERTIF_YCODE = "e770b8390cade9c0b79de53e8007758a" ;
	private static final String PASSWORD = "300015" ;
	
	@Resource
	private MedicineTradeMapper medicineTradeMapper;
	
	@Resource
	private MedicinePublicMapper medicinePublicMapper;
	
	private static final int PARAM = 50000;
	
	@Resource
	private DispatchCardAssisstant dispatchCardAssisstant;
	
	
	/**
	 * 补卡换卡
	 */
	public ReturnEntity changeCard(Body body) throws Exception{
		try {

			/** 存Med_TpaDetail_Business表 **/
			medicineTradeMapper.addMedTpaDetailBusiness(body);

			/** 更新旧表状态 **/
			medicineTradeMapper.updateLCMedCardUserInfo(body);
			
			/** 存新表 **/
			medicineTradeMapper.addLCMedCardUserInfo(body);
			return CommonUtil.getReturnEntity("存表成功", "1" , body.getHeader());
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException("补卡换卡插入数据出错");
		}

	}
	

	/**
	 * 实时扣费
	 */
	public ReturnEntity deduct(Body body) throws Exception{
		
		/** 事务处理 * */
		try {
			ReturnEntity tReturnEntity = new ReturnEntity();
			
			if (body == null) {
				/** 判断body是否为空，若为空返回错误消息 * */
				tReturnEntity = CommonUtil.getReturnEntity("传入值为空","0", body.getHeader());
				return tReturnEntity;
			} else {
				/** 带明细的实时交易 * */
				if ("RTD".equals(body.getHeader().getTradeType())) {
					
					Double sumClmPrice = 0.0;
					for (int i = 0; i < body.getRealTimeTradeEntityList().length; i++) {
						RealTimeTradeEntity realTimeTradeEntity = body.getRealTimeTradeEntityList()[i];
						if("1".equals(realTimeTradeEntity.getBxFlag())){
							sumClmPrice += realTimeTradeEntity.getUnitPrice() * realTimeTradeEntity.getQuantity();
						}
					}
					BigDecimal bigDecimalb = new BigDecimal(sumClmPrice);
					sumClmPrice = bigDecimalb.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					
					CertifyInt certifyInt = new CertifyInt();
					certifyInt.setMedCardNo(body.getBasicEntity().getMedCardNo());
					certifyInt.setCardPassword(PASSWORD);
					
					/** 获取机器码 **/
					String certifyCode = CERTIF_YCODE;
					
					/**  查询余额**/
					CheckRemainingImpNew checkRemainingImpNew = new CheckRemainingImpNew();
					CheckReplyInt tCheckReplyInt = new CheckReplyInt();
					tCheckReplyInt = checkRemainingImpNew.CheckRemaining(certifyCode, certifyInt);
					
					
					
					if((sumClmPrice-tCheckReplyInt.getRestMoney()) > 0.0){
						log.info("====账户余额为"+tCheckReplyInt.getRestMoney() + ", 消费总金额为"+ sumClmPrice + ",两者之差为"+(tCheckReplyInt.getRestMoney()-sumClmPrice));
						return CommonUtil.getReturnEntity("实时交易失败，消费金额大于账户余额！账户余额为"+tCheckReplyInt.getRestMoney() + "元", "0", body.getHeader());
					}
					
					Double restMoney = tCheckReplyInt.getRestMoney() - sumClmPrice;
					BigDecimal bigDecimalrest = new BigDecimal(restMoney);
					restMoney = bigDecimalrest.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					log.info("====账户余额为"+tCheckReplyInt.getRestMoney() + ", 消费总金额为"+ sumClmPrice + ",两者之差为"+restMoney);
					/** 拆分body为sonBody，以ArrayList线性表方式存储，并传递到mybatis批量操作 * */
					Body sonBody = new Body();
					if ("1".equals(body.getBasicEntity().getBuyChnl())) {
						medicineTradeMapper.addLCMedAddressInfoLog(body);
					} else {
						medicineTradeMapper.insertLCMedAddressInfoLog(body);
					}
					
					/** 存lcmedbatchinfo表 **/
					medicineTradeMapper.insertLCMedBatchInfo(body);
					
					for (int i = 0; i < body.getRealTimeTradeEntityList().length; i++) {
						sonBody = new Body();
							
						sonBody.setHeader((Header) CopyUtil.deeplyCopy(body.getHeader()));
						sonBody.setBasicEntity((BasicEntity) CopyUtil.deeplyCopy(body.getBasicEntity()));
						sonBody.setRealTimeTradeEntity((RealTimeTradeEntity) CopyUtil.deeplyCopy(body.getRealTimeTradeEntityList()[i]));
						Double sumPriceDouble = sonBody.getRealTimeTradeEntity().getQuantity()* sonBody.getRealTimeTradeEntity().getUnitPrice();
						BigDecimal bigDecimal = new BigDecimal(sumPriceDouble);
						sumPriceDouble = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						sonBody.getRealTimeTradeEntity().setSumPrice(sumPriceDouble);
						sonBody.setOtherEntity(body.getOtherEntity());
						
						medicineTradeMapper.addLCMedBuyDetail(sonBody);
						
						if (i == 0) {
							medicineTradeMapper.addLCMedBuyMain(sonBody);
						} else {
							boolean flag = false;
							for (int j = i - 1; j >= 0; j--) {
								if (sonBody.getRealTimeTradeEntity().getMedType1().equals(
										body.getRealTimeTradeEntityList()[j].getMedType1())) {
									flag = true;
									break;
								}
							}
							if (flag == false) {
								medicineTradeMapper.addLCMedBuyMain(sonBody);
							}
						}
					}
					
					/** 交易后修改lcmedbuymain表 * */
					List<BasicEntity> basicEntityList = medicinePublicMapper.selectSumPriceByMedType1(body.getHeader().getBatchNo());
					if (basicEntityList == null && basicEntityList.size() <= 0) {
						return CommonUtil.getReturnEntity("修改表数据失败", "0", body.getHeader());
					}
					for (int i = 0; i < basicEntityList.size(); i++) {
						BasicEntity basicEntity = basicEntityList.get(i);
						medicineTradeMapper.updateLCMedBuyMain(basicEntity);
					}

					body.getOtherEntity().setMsg("实时交易成功");
					body.getOtherEntity().setState("1");
					tReturnEntity = CommonUtil.getReturnEntity("实时交易成功,账户余额为"+restMoney+"元", "1", body.getHeader());
					return tReturnEntity;
				}
				/** 不带明细的实时交易 * */
				else {
					Double sumPrice = body.getBasicEntity().getSumPrice();
					BigDecimal bigDecimalb = new BigDecimal(sumPrice);
					sumPrice = bigDecimalb.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					
					CertifyInt certifyInt = new CertifyInt();
					certifyInt.setMedCardNo(body.getBasicEntity().getMedCardNo());
					certifyInt.setCardPassword(PASSWORD);
					
					/** 获取机器码 **/
					String certifyCode = CERTIF_YCODE;
					
					/**  查询余额**/
					CheckRemainingImpNew checkRemainingImpNew = new CheckRemainingImpNew();
					CheckReplyInt tCheckReplyInt = new CheckReplyInt();
					tCheckReplyInt = checkRemainingImpNew.CheckRemaining(certifyCode, certifyInt);
					
					if((sumPrice-tCheckReplyInt.getRestMoney()) > 0.0){
						return CommonUtil.getReturnEntity("消费金额大于账户余额！余额为"+tCheckReplyInt.getRestMoney()+"元", "0", body.getHeader());
					}
					
					Double restMoney = tCheckReplyInt.getRestMoney() - sumPrice;
					BigDecimal bigDecimalrest = new BigDecimal(restMoney);
					restMoney = bigDecimalrest.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					
					/** 存lcmedbatchinfo表 **/
					medicineTradeMapper.insertLCMedBatchInfo(body);
					medicineTradeMapper.addLCMedBuyMain(body);
					if ("1".equals(body.getBasicEntity().getBuyChnl())) {
						medicineTradeMapper.addLCMedAddressInfoLog(body);
					} else {
						medicineTradeMapper.insertLCMedAddressInfoLog(body);
					}
					medicineTradeMapper.insertLCMedBuyDetail(body);
					body.getOtherEntity().setMsg("实时交易成功,账户余额为"+restMoney+"元");
					body.getOtherEntity().setState("1");
					tReturnEntity = CommonUtil.getReturnEntity("实时交易成功,账户余额为"+restMoney+"元", "1", body.getHeader());
					return tReturnEntity;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			/** 抛出异常，spring确定其插入失败，事物回滚 **/
			throw new RuntimeException("实时交易插入数据出错");
		} 
	}
	
	
	/**
	 * 发卡
	 */
	public ReturnEntity dispatchCard(Body body) throws Exception{
		
		/** 1.解析从EBA获取的数据，注意将序号落地 **/
		if(body == null || body.getCustomerEntityList() == null)
			return CommonUtil.getReturnEntity("发卡业务-传入信息为空！", "0", null);
		
		/** 待发卡客户总条数 **/
		int sumCard = body.getCustomerEntityList().length;
		
		
		/** 2.检验数据并存表Med_TpaDetail_Business，将不合格数据从原数据中剔除 **/
		ReturnEntity returnEntity = dispatchCardAssisstant.saveEBAData(body);
		
		
		/** 3.检测数据量 **/
		if(body.getCustomerEntityList() == null){
			return CommonUtil.getReturnEntity("没有符合格式的数据，请核对后再次上传！", "0", body.getHeader());
		}
		
		if(body.getCustomerEntityList().length > PARAM){
			log.debug("数据量超过"+PARAM+"条，请注意！");
		}
		
		
		/** 4.上传服务器,并组装body对象 **/
		String fileName = "";
		try {
			fileName = PackFTPUtil.uploadToFTP(body,returnEntity);
		} catch (Exception e) {
			return CommonUtil.getReturnEntity(e.getMessage(), "0", body.getHeader());
		}
		
		/** 设置上传文件路径 **/
		body.getTransEntity().setUrl(fileName);
		
		
		/** 5.通知第三方数据已传至服务器，阻塞等待返回结果 **/
		String thirdPartyResult = dispatchCardAssisstant.noticeThirdParty(body);
		if("".equals(thirdPartyResult) || thirdPartyResult == null){
			return CommonUtil.getReturnEntity("与第三方交互异常！", "0", body.getHeader());
		}
		
		/** 6.解密获取信息 **/
		String url = "";
		ReturnEntity returnEntity2 = null;
		
		try {
			
			/** 从前台获取发卡信息 **/
			returnEntity2 = JSONObject.parseObject(thirdPartyResult,ReturnEntity.class);
			url = returnEntity2.getReturnInfoEntitys()[0].getMsg();
			
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity("解析文件内容出错", "0", body.getHeader());
		}
		
		if(url == null || "".equals(url)){
			return CommonUtil.getReturnEntity("指定文件路径为空", "0", body.getHeader());
		}
		
		if(!url.toLowerCase().endsWith("zip")){
			return CommonUtil.getReturnEntity(url, "0", body.getHeader());
		}
		
		/** 7.从服务器下载发卡文件 **/
		log.info("接收信息为[Received Info is ]:"+JSONObject.toJSONString(returnEntity2));
		File downloadFromFTP = PackFTPUtil.downloadFromFTP(url, returnEntity2.getHeader());
		if(downloadFromFTP == null){
			return CommonUtil.getReturnEntity("服务器没有指定文件！", "0", body.getHeader());
		}
		
		
		/** 8.将文件转换成对象，药店返回的文件没有Header，需要重新赋值**/
		Header header = body.getHeader();
	    body = new XmlUtil<Body>().file2Object(downloadFromFTP);
		body.setHeader(header);
		
		
		/** 9.将第三方返回结果存表；1.检测重复，2.更新状态，3.插入数据 **/
		ReturnEntity failReturnEntity = dispatchCardAssisstant.saveAndUpdateCard(body);
		
		
		/** 10.将格式不合适数据与存表失败的数据组装返回，这里的代码需要重构 **/
		Set<ReturnInfoEntity> aslistAll = new LinkedHashSet<ReturnInfoEntity>();
		if(failReturnEntity != null){
			ReturnInfoEntity[] returnInfoEntitys = failReturnEntity.getReturnInfoEntitys();
			List<ReturnInfoEntity> asList = Arrays.asList(returnInfoEntitys);
			aslistAll.addAll(asList);
		}
		if(returnEntity != null){
			ReturnInfoEntity[] returnInfoEntitys2 = returnEntity.getReturnInfoEntitys();
			List<ReturnInfoEntity> asList2 = Arrays.asList(returnInfoEntitys2);
			aslistAll.addAll(asList2);
		}
		
		
		/** 11.失败记录条数，设置resultCode值 **/
		int failLength = failReturnEntity.getReturnInfoEntitys().length;
		String resultCode = "";
		
		if (failLength == 0){
			resultCode = "1";
		} else {
			if(sumCard - failLength == 0){
				resultCode = "0";
			}
			else {
				resultCode = "2";
			}
		}
		
		if("".equals(resultCode) || resultCode == null)
			resultCode = "0";
		
		return resembleReturnEntity(body, failReturnEntity, aslistAll,resultCode);
		
	}
	
	/** 组装返回实体 **/
	private ReturnEntity resembleReturnEntity(Body body, ReturnEntity failReturnEntity,
			Set<ReturnInfoEntity> aslistAll,String resuleCode) {
		failReturnEntity.setResultCode(resuleCode);
		failReturnEntity.setReturnInfoEntitys(aslistAll.toArray(new ReturnInfoEntity[aslistAll.size()]));
		failReturnEntity.setHeader(body.getHeader());
		return failReturnEntity; 
	}
	
	/**
	 * 保存卡信息
	 */
	public boolean saveCardD(Body body) throws Exception {
		medicineTradeMapper.saveCardD(body);
		return true;
	}
	
	/**
	 * 保存卡实体
	 */
	public boolean saveCardE(Body body) throws Exception {
		medicineTradeMapper.saveCardE(body);
		return true;
	}

	/**
	 * 更新客户实体
	 */
	public boolean updateCustomerE(Body body) throws Exception {
		medicineTradeMapper.updateCustomerE(body);
		return true;
	}

	/** 
	 * 查找卡

	 */
	public boolean findCard(CustomerEntity ce) throws Exception {
		String result = medicineTradeMapper.findCard(ce);
		if("0".equals(result)){
			return false;
		}
		return true;
	}

	/**
	 * 更新卡交易表
	 */
	public void updateCard(Body body) throws Exception {
		medicineTradeMapper.updateCard(body);
	}
	

	/**
	 *  删除批次
	 */
	public ReturnEntity deleteBatch(Body body) throws Exception{
		
		MedBuyMainEntity medBuyMainEntity = new MedBuyMainEntity();
		MedBuyDetailEntity medBuyDetailEntity = new MedBuyDetailEntity();

		/**  校验传入数据是否为空 **/
		if(body == null){
			return CommonUtil.getReturnEntity("传入body为空！", "0", body.getHeader());
		}
		
		try{
			/** 1.验证批次号是否存在 **/
			String batchNo = medicineTradeMapper.queryBatchNo(body);
			if("0".equals(batchNo)){
				return CommonUtil.getReturnEntity("批次号不存在！", "0", body.getHeader());
			}

			/** 2.查询LCmedbuymain中批次记录  **/
			String isExist = medicineTradeMapper.isExists(body);
			if("0".equals(isExist)){
				/** 不存在批次号，设置返回 **/
				return CommonUtil.getReturnEntity("批次记录不存在-main！", "0", body.getHeader());
			}

			/** 3.查询LCmedbuydetail中批次纪录 **/
			String isBe = medicineTradeMapper.isBe(body);
			if("0".equals(isBe)){
				/** 不存在批次号，设置返回 **/
				return CommonUtil.getReturnEntity("批次记录不存在-detail！", "0", body.getHeader());
			}

			/** 4.校验批次是否允许删除；（遵从原逻辑） **/
			String clmflag = medicineTradeMapper.queryVerify(body);
			
			/**  日起格式转换 **/
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			/**  4.1校验第三方金额是否与数据库匹配 **/
			Double sumPrice = medicineTradeMapper.querySumPrice(body);
			if(sumPrice - body.getDelEntity().getSumOfMoney() != 0){
				return CommonUtil.getReturnEntity("药品金额与实际金额不匹配！", "0",body.getHeader());
			}
			
			if("0".equals(clmflag)){
				/** 5.将查询出来的记录插入到LBMedbuymain中 **/
				List<MedBuyMainEntity> medList = new ArrayList<MedBuyMainEntity>();
				medList = medicineTradeMapper.queryMedMain(body);
				
				for(int j = 0;j<medList.size();j++){
					medBuyMainEntity = medList.get(j);
					
					/**  5.1自动生成序列号**/
					String serialNo= UUID.randomUUID().toString().replace("-","");
					medBuyMainEntity.setSerialNo(serialNo);
					medBuyMainEntity.setMakeDate(sdf.parse(new DateUtil().getDate()));
					medBuyMainEntity.setMakeTime(new DateUtil().getTime());

					/**  5.2执行插入语句 **/
					medicineTradeMapper.addBackups(medBuyMainEntity);
				}

				/** 6.将查询出来的记录插入到LBMedbuydetail中 **/
				List<MedBuyDetailEntity> mList = new ArrayList<MedBuyDetailEntity>();
				mList = medicineTradeMapper.queryMedDetail(body);

				for(int i = 0;i<mList.size();i++){
					medBuyDetailEntity = mList.get(i);
					
					/**  6.1自动生成序列号**/
					String serialNoOne= UUID.randomUUID().toString().replace("-","");
					medBuyDetailEntity.setSerialNo(serialNoOne);
					medBuyDetailEntity.setMakeDate(sdf.parse(new DateUtil().getDate()));
					medBuyDetailEntity.setMakeTime(new DateUtil().getTime());
					
					/** 6.2执行插入语句 **/
					medicineTradeMapper.addBack(medBuyDetailEntity);
				}

				/** 7.删除LCMedBuyMain，LCMedBuyDetail表中查询出的记录 **/
				medicineTradeMapper.deleteRecord(body);
				medicineTradeMapper.removeRecord(body);

				return CommonUtil.getReturnEntity("删除成功！", "1",body.getHeader());
			}else{
				return CommonUtil.getReturnEntity("该批次不允许删除！", "0", body.getHeader());
			}

		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("删除批次异常！");
		}
	}

	public CustomerEntity selectCustomerEntityByContno(String contno) throws Exception {
		return medicinePublicMapper.selectCustomerEntityByContno(contno);
	}
	
}
