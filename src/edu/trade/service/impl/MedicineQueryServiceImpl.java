package edu.trade.service.impl;

import javax.annotation.Resource;

import edu.trade.util.CommonUtil;
import org.springframework.stereotype.Service;

import MedData.CertifyInt;
import MedData.CheckReplyInt;
import MedOper.CheckRemainingImpNew;

import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.mapper.MedicineQueryMapper;
import edu.trade.service.MedicineQueryService;

@Service("medicineQueryService")
public class MedicineQueryServiceImpl extends MedicineServiceImpl implements MedicineQueryService{

	@Resource
	private MedicineQueryMapper medicineQueryMapper;
	private static final String CERTIF_YCODE = "e770b8390cade9c0b79de53e8007758a" ;
	private static final String PASSWORD = "300015" ;
	
	/**
	 * 查询账户余额
	 */
	public ReturnEntity queryAccountBalance(Body body) throws Exception{
		ReturnEntity returnEntity = new ReturnEntity();
		if(body == null){
			return CommonUtil.getReturnEntity("传入body为空！","0",body.getHeader());
		}else{
			try{
				/** 1.1校验与获取余额 **/
				CheckReplyInt mCheckReplyInt = getCheckReplyInt(body);
				
				/** TKFlag , 0-数据查询成功,1-数据查询失败,2-认证校验失败，3-流水号校验失败**/
				if("0".equals(mCheckReplyInt.getTKFlag())){
					/** 获取余额 **/
					String balance = String.valueOf(mCheckReplyInt.getRestMoney());
					returnEntity = CommonUtil.getReturnEntity(balance,"1", body.getHeader());
				}
				else if("1".equals(mCheckReplyInt.getTKFlag())){
					returnEntity = CommonUtil.getReturnEntity("数据查询失败", "0",body.getHeader());
				}
				else if("2".equals(mCheckReplyInt.getTKFlag())){
					returnEntity = CommonUtil.getReturnEntity("认证校验失败", "0",body.getHeader());
				}
				else if("3".equals(mCheckReplyInt.getTKFlag())){
					returnEntity = CommonUtil.getReturnEntity("流水号校验失败", "0",body.getHeader());
				}
				
			} catch(Exception e){
				e.printStackTrace();
				throw new Exception("查询异常！");
			
			}
		}
		return returnEntity;
	}
	
	
	/**
	 * 1.1校验与获取余额


	 */
	public CheckReplyInt getCheckReplyInt(Body body)throws RuntimeException{
		
		/** 获取卡号和密码 **/
		CertifyInt certifyInt = new CertifyInt();
		certifyInt.setMedCardNo(body.getQueryEntity().getMedCardNo());
		certifyInt.setCardPassword(PASSWORD);
		
		/** 获取机器码 **/
		String certifyCode = CERTIF_YCODE;
		
		/**  查询余额**/
		CheckRemainingImpNew checkRemainingImpNew = new CheckRemainingImpNew();
		CheckReplyInt tCheckReplyInt = new CheckReplyInt();
		tCheckReplyInt = 	checkRemainingImpNew.CheckRemaining(certifyCode, certifyInt);
		
		return tCheckReplyInt;
	}

	/**
	 * 查询交易结果
	 */
	public ReturnEntity queryTradeResult(Body body) throws Exception{
		ReturnEntity returnEntity = new ReturnEntity();

		/** 判断传入数据是否为空 **/
		if(body == null){
			return CommonUtil.getReturnEntity("传入body为空！","0",body.getHeader());
		}

		try {
			
			/**  查询交易状态 **/
			String state = medicineQueryMapper.queryTradeResult(body);
			
			/** 判断是否查询为空 **/
			if("".equals(state) || state == null){
				
				return CommonUtil.getReturnEntity("未查询到结果！", "0", body.getHeader()); 
			}
			
			/** 判断查询结果的状态，根据不同状态而判定查询返回的结果 **/
			if("1".equals(state)){
				
				return CommonUtil.getReturnEntity("交易成功！","1", body.getHeader());
			}else if("0".equals(state)){
				
				return CommonUtil.getReturnEntity("交易失败！", "0", body.getHeader()); 
			}else if("2".equals(state)){
				
				return CommonUtil.getReturnEntity("交易正在处理中...", "2", body.getHeader()); 
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询异常！");
		}

		return returnEntity;
	}

}
