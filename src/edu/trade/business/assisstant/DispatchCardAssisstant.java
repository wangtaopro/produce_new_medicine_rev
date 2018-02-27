package edu.trade.business.assisstant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import edu.trade.entity.CustomerEntity;
import edu.trade.entity.Header;
import edu.trade.entity.OtherEntity;
import edu.trade.entity.ReturnInfoEntity;
import edu.trade.util.ClientUtil;
import edu.trade.util.CommonUtil;
import edu.trade.util.DateUtil;
import edu.trade.util.ValidateUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicineTradeService;


/**
 * 发卡业务辅助类
 */
@Controller
public class DispatchCardAssisstant {
	
	@Resource
	private MedicineTradeService medicineTradeService;
	
	private static Log log = LogFactory.getLog(DispatchCardAssisstant.class);
	
	/** 保存EBA数据,返回不合规范数据 **/
	public ReturnEntity saveEBAData(Body body)  {
		
		if(body == null)
			throw new NullPointerException("传入数据为空！");
		
		List<ReturnInfoEntity> returnInfoEntitys = new ArrayList<ReturnInfoEntity>();
		List<CustomerEntity> customerEntitys = new ArrayList<CustomerEntity>();
		ReturnEntity returnEntity = new ReturnEntity();
		
		if(body.getCustomerEntityList() == null || body.getHeader() == null)
			return CommonUtil.getReturnEntity("客户信息为空！", "0", body.getHeader());
		
		CustomerEntity customerEntity = body.getCustomerEntity();
		
		for(CustomerEntity ce : body.getCustomerEntityList()){
			
			try {
				if("".equals(ce.getIDType()))
					throw new RuntimeException("证件类型不能为空！");
				
				if("0".equals(ce.getIDType())){
					
					/** 校验身份证 **/
					String IDNo = ValidateUtil.desensIdentityNo(ce.getIDNo());
					if(ce.getIDNo().length() != IDNo.length()){
						throw new RuntimeException("身份证格式有误！");
					} else{
						ce.setIDNo(IDNo);
					}
				}
				else{
					/** 为空校验 **/
					if("".equals(ce.getIDNo()) || ce.getIDNo()==null ){
						throw new RuntimeException("证件号码不能为空！");
					}
				}
				
				/** 手机号码为空校验 **/
				if("".equals(ce.getMobile()) || ce.getMobile() == null){
					throw new RuntimeException("手机号码不能为空！");
				}
				
				/** 手机号码格式校验 **/
				if(!ValidateUtil.isMobile(ce.getMobile())){
					throw new RuntimeException("手机号码格式有误！");
				}
				
				/** 组装属性 **/
				BeanUtils.copyProperty(ce, "grpName", customerEntity.getGrpName());
				BeanUtils.copyProperty(ce, "comName", customerEntity.getComName());
				BeanUtils.copyProperty(ce, "grpContNo", customerEntity.getGrpContNo());
				
				body.setCustomerEntity(ce);
				
				try {
					/** 保存数据 **/
					body.getOtherEntity().setBatchNo("");
					if(body.getOtherEntity() == null || body.getOtherEntity().getBatchNo()==null || "".equals(body.getOtherEntity().getBatchNo())){
						body.getOtherEntity().setBatchNo(body.getHeader().getBatchNo());
					}
				
					body.getOtherEntity().setMakeDate(new DateUtil().getToDayDate(new Date()));
					body.getOtherEntity().setModifyDate(new DateUtil().getToDayDate(new Date()));
					medicineTradeService.saveCardD(body);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage()+" "+JSONObject.toJSONString(ce));
				}
				
				ce.setComName(null);
				ce.setGrpContNo(null);
				ce.setGrpName(null);
				customerEntitys.add(ce);
				
			} catch (Exception e) {
				e.printStackTrace();
				returnInfoEntitys.add(new ReturnInfoEntity(body.getHeader().getBatchNo(), null, ce.getContNo(),e.getMessage(),ce.getNo()));
			}
		}
		
		body.setCustomerEntity(customerEntity);
		
		/** 剔除不合格数据 **/
		CustomerEntity[] customerEntities = new CustomerEntity[customerEntitys.size()];
		customerEntitys.toArray(customerEntities);
		body.setCustomerEntityList(customerEntities);
		
		log.info("正确发卡数据为[The Data To Dispatch Card Without Syntex Errors]:"+JSONObject.toJSONString(body));
		
		if(returnInfoEntitys.isEmpty()){
			return null;
		}
		
		/** 将不合格的数据返回 **/
		returnEntity.setHeader(body.getHeader());
		returnEntity.setReturnInfoEntitys(returnInfoEntitys.toArray(new ReturnInfoEntity[returnInfoEntitys.size()]));
		return returnEntity;
	}
	
	/**
	 * 在信息上传至服务器时通知第三方数据已经上传至服务器
	 * @throws Exception
	 */
	public String noticeThirdParty(Body body) throws Exception {

		/** 测试过后该句为核心代码 **/
		return ClientUtil.request(body);
		
	}
	
	/**
	 * 存已发卡用户信息
	 */
	public ReturnEntity saveAndUpdateCard(Body body){
		
		List<ReturnInfoEntity> returnInfoEntitys = new ArrayList<ReturnInfoEntity>();
		ReturnEntity returnEntity = new ReturnEntity();
		
		if(body.getCustomerEntityList() == null){
			return CommonUtil.getReturnEntity("第三方返回发卡数据为空！", "0", body.getHeader());
		}
		
		for(CustomerEntity ce : body.getCustomerEntityList()){
			
			try {
				
				/** 1.检测重复（LCMedCardUserInfo） **/
				body.setCustomerEntity(ce);
				
				OtherEntity otherEntity = new OtherEntity();
				otherEntity.setOperator("operator");
				body.setOtherEntity(otherEntity);
				
				boolean flag = medicineTradeService.findCard(ce);
				
				if(flag){
					throw new RuntimeException("该卡已经发放！");
				}
				
				/** 2.更新状态（Med_TpaDetail_Business） 卡号、卡状态、结果，成败原因 **/
				OtherEntity otherEntity2 = CommonUtil.toggleState();
				otherEntity2.setOperator(body.getOtherEntity().getOperator());
				
				body.setOtherEntity(otherEntity2);
				medicineTradeService.updateCard(body);
				
				String contno = body.getCustomerEntity().getContNo();
				CustomerEntity customerEntity1 = medicineTradeService.selectCustomerEntityByContno(contno);
				
				body.getCustomerEntity().setGrpContNo(customerEntity1.getGrpContNo());
				body.getCustomerEntity().setIDNo(customerEntity1.getIDNo());
				body.getCustomerEntity().setName(customerEntity1.getName());
				body.getCustomerEntity().setInsuredNo(customerEntity1.getInsuredNo());
				
				String grpContNo = customerEntity1.getGrpContNo();
				byte[] bytes = grpContNo.getBytes();
				String comCode = new String(bytes,2,4);
				body.getCustomerEntity().setComCode(comCode);
				
				/** 3.插入数据（LCMedCardUserInfo）**/
				medicineTradeService.saveCardE(body);
				
			} catch (Exception e) {
				e.printStackTrace();
				returnInfoEntitys.add(new ReturnInfoEntity(body.getHeader().getBatchNo(), body.getCustomerEntity().getMedCardNo(), ce.getContNo(),e.getMessage(),ce.getNo()));
			}
		}
		returnEntity.setHeader(body.getHeader());
		returnEntity.setReturnInfoEntitys(returnInfoEntitys.toArray(new ReturnInfoEntity[returnInfoEntitys.size()]));
		return returnEntity;
		
	}
	
	/** 
	 * 保存数据测试 | 上传服务器测试
	 * @throws Exception 
	 */
	@Test
	public void test() throws Exception{
		
		/*Body body = new Body();
		CustomerEntity customerEntity = new CustomerEntity();
		
		customerEntity.setComName("comName");
		customerEntity.setContNo("contNo");
		customerEntity.setGrpName("grpName");
		customerEntity.setIDNo("110101198001010053");
		customerEntity.setIDType("1");
		customerEntity.setMobile("18910101111");
		customerEntity.setName("test");
		customerEntity.setNo("1");
		
		Header header = new Header();
		header.setBatchNo("batchNo");
		header.setIp("ip");
		header.setTradeType("ACC");
		
		CustomerEntity[] customerEntityList = new CustomerEntity[1];
		customerEntityList[0] = customerEntity;
		
		body.setHeader(header);
		body.setCustomerEntityList(customerEntityList);
		
		ReturnEntity returnEntity = new DispatchCardAssisstant().saveEBAData(body);
		System.out.println(returnEntity);
		
		DispatchCardAssisstant dispatchCardAssisstant = new DispatchCardAssisstant();
		dispatchCardAssisstant.uploadToFTP(body,null);*/
		
		/*String path = "/";
		String fileName = "ACC_TK_1447911736535.xml";
		PackFTPUtil.dealData(path, fileName, (String)PropertiesUtil.getProperties(PROPERTY).get("LOCALSITE"));*/
		
		String url = "/ACC_TK_1447923632893.zip";
		
		ReturnEntity returnEntity = new ReturnEntity();
		Header header = new Header();
		header.setBatchNo("batchNo");
		header.setIp("ip");
		header.setTradeType("ACC");
		returnEntity.setHeader(header);
		
		ReturnInfoEntity returnInfoEntity = new ReturnInfoEntity();
		returnInfoEntity.setMsg(url);
		
		ReturnInfoEntity[] returnInfoEntitys = new ReturnInfoEntity[1];
		returnInfoEntitys[0] = returnInfoEntity;
		
		returnEntity.setReturnInfoEntitys(returnInfoEntitys);
		
		/*File file = downloadFromFTP(JSONObject.toJSONString(returnEntity));
		System.out.println(file);*/
		
	}
	
}
