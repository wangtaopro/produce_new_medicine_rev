package edu.trade.mapper;

import java.util.List;

import edu.trade.dto.Body;
import edu.trade.entity.BasicEntity;
import edu.trade.entity.MedBuyDetailEntity;
import edu.trade.entity.MedBuyMainEntity;
import edu.trade.entity.CustomerEntity;

public interface MedicineTradeMapper {

	/**
	 * 向LCMedBuyMain插入数据
	 */
	public void addLCMedBuyMain(Body body) throws Exception;

	/**
	 * 向LCMedBuyDetail插入数据
	 */
	public void addLCMedBuyDetail(Body body) throws Exception;
	
	/**
	 * 向LCMedBuyDetail插入数据
	 */
	public void insertLCMedBuyDetail(Body body) throws Exception;

	/**
	 * 向LCMedAddressInfoLog插入数据
	 */
	public void addLCMedAddressInfoLog(Body body) throws Exception;
	
	/**
	 * 向LCMedAddressInfoLog插入数据
	 */
	public void insertLCMedAddressInfoLog(Body body) throws Exception;

	/**
	 * 向MedTpaDetailBusiness插入数据
	 */
	public void addMedTpaDetailBusiness(Body body) throws Exception;

	/**
	 * 更改LCMedCardUserInfo的状态

	 */
	public void updateLCMedCardUserInfo(Body body) throws Exception;

	/**
	 * 向LCMedCardUserInfo插入数据
	 */
	public void addLCMedCardUserInfo(Body body) throws Exception;

	/**
	 * 存卡交易细节
	 */
	public void saveCardD(Body body) throws Exception;

	/**
	 * 存卡实体
	 */
	public void saveCardE(Body body) throws Exception;

	/**
	 * 更新客户实体
	 */
	public void updateCustomerE(Body body) throws Exception;

	/**
	 * 按用户信息存查询卡

	 */
	public String findCard(CustomerEntity ce) throws Exception;

	/**
	 * 更新卡交易表
	 */
	public void updateCard(Body body) throws Exception;

	/**
	 * 查询批次号

	 */
	public String queryBatchNo(Body body) throws Exception;

	/**
	 * 批次号是否存在LCmedbuymain
	 */
	public String isExists(Body body) throws Exception;

	/**
	 * 批次号是否存在LCmedbuydetail
	 */
	public String isBe(Body body) throws Exception;

	/**
	 * 查询LCmedbuymain
	 */
	public List<MedBuyMainEntity> queryMedMain(Body body) throws Exception;

	/**
	 * 查询LCmedbuydetail
	 */
	public List<MedBuyDetailEntity> queryMedDetail(Body body) throws Exception;

	/**
	 * 插入备份表LBmedbuymain
	 */
	public void addBackups(MedBuyMainEntity medBuyMainEntity) throws Exception;

	/**
	 * 插入备份表LBmedbuydetail
	 */
	public void addBack(MedBuyDetailEntity medBuyDetailEntity) throws Exception;

	/**
	 * 删除查询记录LCmedbuymain
	 */
	public void deleteRecord(Body body) throws Exception;

	/**
	 * 删除查询记录LCmedbuydetail
	 */
	public void removeRecord(Body body) throws Exception;

	/**
	 * 查询校验批次是否允许删除
	 */
	public String queryVerify(Body body) throws Exception;
	
	/**
	 * 查询药品总金额 
	 */
	public Double querySumPrice(Body body) throws Exception;
	
	/**
	 * 回写带明细实时交易的交易金额
	 */
	public void updateLCMedBuyMain(BasicEntity basicEntity) throws Exception;
	
	/**
	 * 向LCMedBatchInfo插入数据
	 */
	public void insertLCMedBatchInfo(Body body) throws Exception;

}

































