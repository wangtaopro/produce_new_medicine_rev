package edu.trade.quartz;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.annotation.Resource;

import edu.trade.business.impl.CheckAccountDetailAction;
import edu.trade.business.impl.CheckCardDetailAction;
import edu.trade.business.impl.ModifyCustomerInfoAction;
import edu.trade.entity.CardEntity;
import edu.trade.entity.CustomerEntity;
import edu.trade.entity.Header;
import edu.trade.entity.TransEntity;
import edu.trade.mapper.MedicinePublicMapper;
import edu.trade.util.BatchNoUtil;
import edu.trade.util.DateUtil;
import edu.trade.util.PropertiesUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import MedData.CertifyInt;
import MedData.CheckReplyInt;
import MedOper.CheckRemainingImpNew;

import com.alibaba.fastjson.JSONObject;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.entity.BasicEntity;

/**
 * 批处理类
 */
public class BatchTask{
	
	@Resource
	private ModifyCustomerInfoAction modifyCustomerInfoAction;
	
	@Resource
	private CheckAccountDetailAction checkAccountDetailAction;
	
	@Resource
	private CheckCardDetailAction checkCardDetailAction;
	
	@Resource
	private MedicinePublicMapper medicinePublicMapper;
	
	private static Log log = LogFactory.getLog(BatchTask.class);
	
	private	String[] thirdPartyProp = PropertiesUtil.getResource("THIRDPARTY_INFO");
	
	private static final String CERTIF_YCODE = "e770b8390cade9c0b79de53e8007758a" ;
	private static final String PASSWORD = "300015" ;
	
	/** 日对账-明细 **/
	public void checkDailyAccount() throws Exception{
		log.info("开始处理明细对账！");
		
		for (int i = 0; i < thirdPartyProp.length; i++) {
			Body body = resembleBody(i,"CDA");
			
			/** 设值对账时间 **/
			BasicEntity basicEntity = body.getBasicEntity();
			basicEntity.setBuyDate(new DateUtil().getEarlyDate(new Date()));
			body.setBasicEntity(basicEntity);
			
			ReturnEntity returnEntity = checkAccountDetailAction.process(body);
			log.info("明细对账结束，结果为"+JSONObject.toJSONString(returnEntity));
		}
	}
	

	/** 日对账-卡 **/
	public void checkDailyCard() throws Exception{
		log.info("开始处理卡对账！");
		
		for (int i = 0; i < thirdPartyProp.length; i++) {
			Body body = resembleBody(i,"CDC");
			
			/** 设值发卡时间 **/
			CardEntity cardEntity = new CardEntity();
			cardEntity.setProvideDate(new DateUtil().getPreDate(new Date()));
			body.setCardEntity(cardEntity);
			
			ReturnEntity returnEntity = checkCardDetailAction.process(body);
			log.info("卡对账结束，结果为"+JSONObject.toJSONString(returnEntity));
		}
	}
	
	/** 修改客户信息 **/
	public void changeUserInfo() throws Exception{
		log.info("修改客户信息！");
		
		for (int i = 0; i < thirdPartyProp.length; i++) {
			Body body = resembleBody(i,"CIC");
			
			/** 设值修改客户信息时间 **/
			CardEntity cardEntity = new CardEntity();
			cardEntity.setProvideDate(new DateUtil().getPreDate(new Date()));
			body.setCardEntity(cardEntity);
			
			ReturnEntity returnEntity = modifyCustomerInfoAction.process(body);
			log.info("修改客户信息，结果为"+JSONObject.toJSONString(returnEntity));
		}
	}
	
	/**检查链接池状态**/
	public void queryConnection()throws Exception{
		log.info("检查链接池！");
		medicinePublicMapper.selectKeyByIp("checkConnection");
		
	}
	
	/** 检查查询余额数据库连接 **/
	public void queryCheckConnection()throws Exception{
		log.info("检查jdbc连接！");
		/** 获取卡号和密码 **/
		CertifyInt certifyInt = new CertifyInt();
		certifyInt.setMedCardNo("checkBalance");
		certifyInt.setCardPassword(PASSWORD);
		
		/** 获取机器码 **/
		String certifyCode = CERTIF_YCODE;
		
		/**  查询余额**/
		CheckRemainingImpNew checkRemainingImpNew = new CheckRemainingImpNew();
		CheckReplyInt tCheckReplyInt = new CheckReplyInt();
		tCheckReplyInt = checkRemainingImpNew.CheckRemaining(certifyCode, certifyInt);
		log.info(tCheckReplyInt.getRestMoney());
	}
	
	/**
	 * 生成body 
	 */
	private static Body getBody(String ip,Integer port,String checkBit,String tradeType,String thirdParty){
		
		Body body = new Body();
		Header header = null;
		
		/** 第三方信息 **/
		TransEntity transEntity = new TransEntity();
		transEntity.setThirdPartyIp(ip);
		transEntity.setThirdPartyPort(port);
		
		/** 组装header **/
		try {
			header = new Header(
					InetAddress.getLocalHost().getHostAddress(),
					new BatchNoUtil().getBatchNo(),
					tradeType,
					checkBit,
					new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())
				);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取本地IP失败！");
		}
		
		/** 发给第三方，没有精确到药店 **/
		BasicEntity basicEntity = new BasicEntity();
		basicEntity.setMedShopCode("TK_MEDICINE");
		basicEntity.setCounterNo("TK_COUNTER");
		
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setThirdParty(thirdParty);
		
		body.setCustomerEntity(customerEntity);
		body.setBasicEntity(basicEntity);
		body.setTransEntity(transEntity);
		body.setHeader(header);
		
		return body;
	}
	
	/** 组织数据 **/
	private Body resembleBody(int i,String tradeType) {
		
		String[] split = thirdPartyProp[i].split("-");
		
		String ip = split[0] ;
		Integer port = Integer.parseInt(split[1]);
//		String key = split[2];
		String checkBit = split[3];
		String thirdParty = split[4];
		
		/** key从数据库获取 **/
		
		/** 校验位加密 **/
//		checkBit = EncryptUtil.toString(EncryptUtil.encryptStr(key.getBytes(), checkBit.getBytes()));
		
		Body body = getBody(ip, port, checkBit, tradeType,thirdParty);
		return body;
	}
}
