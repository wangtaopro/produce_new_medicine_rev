package edu.trade.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 购药分类汇总表
 * @author itw_wuzp
 */
public class MedBuyMainEntity implements Serializable{

	private static final long serialVersionUID = -347869573025356757L;

	/** 序列号 **/
	private String serialNo;
	/** 购药批次号 **/
	private String batchNo;
	/** 医疗卡号 **/
	private String medCardNo;
	/** 药品分类1 **/
	private String medType1;
	/** 药品消费总金额 **/
	private double sumPayPrice;
	/** 药品报销总金额 **/
	private double sumClmPrice;
	/** 生成日期 **/
	private Date makeDate;
	/** 生成时间 **/
	private String makeTime;
	/** 备注 **/
	private String memo;
	/** 备用字段1 **/
	private String standByFlag1;
	/** 备用字段2 **/
	private String standByFlag2;
	/** 赔案生成标志 **/
	private String clmFlag;
	/** 赔案号 **/
	private String clmNo;
	/** 申请号 **/
	private String orderNo;
	/** 预约号 **/
	private String applyNo;
	/** 消费类别 **/
	private String consumeType;
	/** 扣费类型 **/
	private String deductType;
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getMedCardNo() {
		return medCardNo;
	}
	public void setMedCardNo(String medCardNo) {
		this.medCardNo = medCardNo;
	}
	public String getMedType1() {
		return medType1;
	}
	public void setMedType1(String medType1) {
		this.medType1 = medType1;
	}
	public double getSumPayPrice() {
		return sumPayPrice;
	}
	public void setSumPayPrice(double sumPayPrice) {
		this.sumPayPrice = sumPayPrice;
	}
	public double getSumClmPrice() {
		return sumClmPrice;
	}
	public void setSumClmPrice(double sumClmPrice) {
		this.sumClmPrice = sumClmPrice;
	}
	public Date getMakeDate() {
		return makeDate;
	}
	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}
	public String getMakeTime() {
		return makeTime;
	}
	public void setMakeTime(String makeTime) {
		this.makeTime = makeTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStandByFlag1() {
		return standByFlag1;
	}
	public void setStandByFlag1(String standByFlag1) {
		this.standByFlag1 = standByFlag1;
	}
	public String getStandByFlag2() {
		return standByFlag2;
	}
	public void setStandByFlag2(String standByFlag2) {
		this.standByFlag2 = standByFlag2;
	}
	public String getClmFlag() {
		return clmFlag;
	}
	public void setClmFlag(String clmFlag) {
		this.clmFlag = clmFlag;
	}
	public String getClmNo() {
		return clmNo;
	}
	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getConsumeType() {
		return consumeType;
	}
	public void setConsumeType(String consumeType) {
		this.consumeType = consumeType;
	}
	public String getDeductType() {
		return deductType;
	}
	public void setDeductType(String deductType) {
		this.deductType = deductType;
	}
	
}
