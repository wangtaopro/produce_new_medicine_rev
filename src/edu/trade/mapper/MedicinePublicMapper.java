package edu.trade.mapper;

import java.util.List;

import edu.trade.dto.Body;
import edu.trade.entity.BasicEntity;
import edu.trade.entity.CardEntity;
import edu.trade.entity.ConfigEntity;
import edu.trade.entity.ConfigInfoEntity;
import edu.trade.entity.CustomerEntity;

/**
 * 公共sql
 */
public interface MedicinePublicMapper {
	
	/** 
	 * 权限校验 
	 */
	public Integer queryIsIpRight(String ip) throws Exception;
	
	/** 
	 * 药店权限 
	 */
	public String queryIsDrugShopRight(String medShopCode) throws Exception;
	
	/**
	 * 卡号校验
	 */
	public String queryIsMedCardNoTrue(String medCardNo) throws Exception;
	
	/** 
	 * 黑名单检测

	 */
	public String queryIsBlackList(String medShopCode) throws Exception;
	
	/**
	 * 终端权限
	 */
	public String queryIsDrugConterRight(String counterNo) throws Exception;
	
	/** 
	 * 存主表 
	 */
	public void addMedTpaMain(Body body) throws Exception;
	
	/** 
	 * 回写主表状态 
	 */
	public void modifyMainState(Body body) throws Exception;
	
	/** 
	 * 查询旧卡信息 
	 */
	public List<CardEntity> selectLCMedCardUserInfo(String oldMedCardNo) throws Exception;
	
	/** 
	 * 计算当天交易量

	 */
	public Integer selectLMedTpaMain(String date) throws Exception;
	
	/** 
	 * 通过第三方批次号获得泰康批次号 
	 */
	public String selectBatchNoFromMedTpaMain(String foreignBatchNo) throws Exception;
	
	/** 
	 * 查询当前批次的总额
	 */
	public Double selectSumPayPriceFromLCMedBuyMain(String batchNo) throws Exception;
	
	/** 
	 * 查询校验位 
	 */
	public String queryCheckBit(String checkBit) throws Exception;

	/** 
	 * 通过公司名称缩写获取配置信息
	 */
	public ConfigEntity selectConfigByName(ConfigInfoEntity configInfoEntity);
	
	/** 
	 * 通过Ip获取key
	 */
	public String selectKeyByIp(String ip);
	
	/**
	 * 获取lcmedbuydetail表的medtype1的总钱数

	 */
	public List<BasicEntity> selectSumPriceByMedType1(String batchno);
	
	/**
	 * 通过个单号查找团体保单号
	 */
	
	public CustomerEntity selectCustomerEntityByContno(String contno) throws Exception;
	
	/**
	 * 查看当前保单
	 */
	public String selectIsModifyCard(String medCardNo) throws Exception;
	
	
}
