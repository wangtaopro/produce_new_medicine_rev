package edu.trade.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *  购药清单明细表

 *  @author itw_wuzp
 */
public class MedBuyDetailEntity implements Serializable{

	private static final long serialVersionUID = 1214911940510060027L;

	/** 序列号 **/
	private String serialNo;
	/** 购药批次号 **/
	private String batchNo;
	/** 药品流水号 **/
	private String seqNo;
	/** 医疗卡号 **/
	private String medCardNo;
	/** 药品名称 **/
	private String medName;
	/** 药品分类1 **/
	private String medType1;
	/** 药品分类2 **/
	private String medType2;
	/** 药品分类3 **/
	private String medType3;
	/** 药品数量 **/
	private double quanity;
	/** 药品单价 **/
	private double unitPrice;
	/**药品总金额  **/
	private double sumPrice;
	/** 可报销标示 **/
	private String bxFlag;
	/** 生成日期 **/
	private Date makeDate;
	/** 生成时间 **/
	private String makeTime;
	/** 药店代码 **/
	private String medShopCode;
	/** 交易柜台号 **/
	private String conterNo;
	/** 备注 **/
	private String memo;
	/** 备用字段1 **/
	private String standByFlag1;
	/** 备用字段2 **/
	private String standByFlag2;
	/** 备用字段3 **/
	private String standByFlag3;
	/** 备用字段4 **/
	private String standByFlag4;
	/** 备用字段5 **/
	private String standByFlag5;
	/** 备用字段6 **/
	private String standByFlag6;
	/** 备用字段7 **/
	private String standByFlag7;
	
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
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getMedCardNo() {
		return medCardNo;
	}
	public void setMedCardNo(String medCardNo) {
		this.medCardNo = medCardNo;
	}
	public String getMedName() {
		return medName;
	}
	public void setMedName(String medName) {
		this.medName = medName;
	}
	public String getMedType1() {
		return medType1;
	}
	public void setMedType1(String medType1) {
		this.medType1 = medType1;
	}
	public String getMedType2() {
		return medType2;
	}
	public void setMedType2(String medType2) {
		this.medType2 = medType2;
	}
	public String getMedType3() {
		return medType3;
	}
	public void setMedType3(String medType3) {
		this.medType3 = medType3;
	}
	public double getQuanity() {
		return quanity;
	}
	public void setQuanity(double quanity) {
		this.quanity = quanity;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public double getSumPrice() {
		return sumPrice;
	}
	public void setSumPrice(double sumPrice) {
		this.sumPrice = sumPrice;
	}
	public String getBxFlag() {
		return bxFlag;
	}
	public void setBxFlag(String bxFlag) {
		this.bxFlag = bxFlag;
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
	public String getMedShopCode() {
		return medShopCode;
	}
	public void setMedShopCode(String medShopCode) {
		this.medShopCode = medShopCode;
	}
	public String getConterNo() {
		return conterNo;
	}
	public void setConterNo(String conterNo) {
		this.conterNo = conterNo;
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
	public String getStandByFlag3() {
		return standByFlag3;
	}
	public void setStandByFlag3(String standByFlag3) {
		this.standByFlag3 = standByFlag3;
	}
	public String getStandByFlag4() {
		return standByFlag4;
	}
	public void setStandByFlag4(String standByFlag4) {
		this.standByFlag4 = standByFlag4;
	}
	public String getStandByFlag5() {
		return standByFlag5;
	}
	public void setStandByFlag5(String standByFlag5) {
		this.standByFlag5 = standByFlag5;
	}
	public String getStandByFlag6() {
		return standByFlag6;
	}
	public void setStandByFlag6(String standByFlag6) {
		this.standByFlag6 = standByFlag6;
	}
	public String getStandByFlag7() {
		return standByFlag7;
	}
	public void setStandByFlag7(String standByFlag7) {
		this.standByFlag7 = standByFlag7;
	}
	
}
