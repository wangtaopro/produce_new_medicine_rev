package edu.trade.mapper;

import java.util.List;

import edu.trade.dto.Body;
import edu.trade.entity.BasicEntity;
import edu.trade.entity.CardEntity;

public interface MedicineBatchMapper {
	
	/** 
	 * 存新药房业务表
	 */
	public void insertCustomer(Body body) throws Exception;

	/** 
	 * 查询卡信息
	 */
	public List<CardEntity> queryCardDetail(Body body) throws Exception;

	/**
	 * 明细对账查询当天交易额

	 */
	public List<BasicEntity> selectLCMedBuyMain(Body body) throws Exception;
	
	/**
	 * 明细对账查询当天交易额 B表

	 */
	public List<BasicEntity> selectLCMedBuyMainB(Body body) throws Exception;

	/**
	 * 明细上传插入Med_TpaMain表

	 */
	public void insertMedTpaMain(Body body) throws Exception;

	/**
	 * 明细上传插入LCMedBuyMain表

	 */
	public void insertLCMedBuyMain(Body body) throws Exception;

	/**
	 * 明细上传插入LCMedBuyDetail表

	 */
	public void insertLCMedBuyDetail(Body body) throws Exception;

	/**
	 * 明细上传插入LCMedAddressInfoLog表

	 */
	public void insertLCMedAddressInfoLog(Body body) throws Exception;

	/**
	 * 明细上传时修改LCMedBuyMain表

	 */
	public void updateLCMedBuyMain(Body body) throws Exception;
	
	/**
	 * 明细上传修改Med_TpaMain
	 */
	public void updateMedTpaMain(Body body) throws Exception;


	/**
	 * 查询客户变更信息
	 */
	public List<Body> queryCustomer(Body body) throws Exception;
}
