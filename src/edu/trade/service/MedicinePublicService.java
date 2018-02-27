package edu.trade.service;

import edu.trade.dto.Body;
import edu.trade.entity.ConfigEntity;
import edu.trade.entity.ConfigInfoEntity;

public interface MedicinePublicService extends MedicineService{
	
	/** 存主表 **/
	public boolean saveMain(Body body) throws Exception;
	
	/** 修改主表状态 **/
	public boolean modifyMainState(Body body) throws Exception;
	
	/** 查询校验位 **/
	public String queryCheckBit(String checkBit) throws Exception;
	
	/** 通过公司名称缩写获取配置信息 **/
	public ConfigEntity selectConfigByName(ConfigInfoEntity configInfoEntity);
	
	/** 通过Ip获取key **/
	public String selectKeyByIp(String ip);	
	
	/** 实时交易完整性校验 **/
	public String checkIntegrality(Body body) throws Exception;
	
	/** 补卡换卡取得旧卡信息 **/
	public Body getOldCardInfo(Body body) throws Exception;
	
	/** 补卡换卡完整性校验 **/
	public String checkModifyCard(Body body) throws Exception;
	
}
