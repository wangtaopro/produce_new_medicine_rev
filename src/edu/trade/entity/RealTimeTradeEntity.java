package edu.trade.entity;

import java.io.Serializable;

/**
 * 实时交易辅助实体
 */
public class RealTimeTradeEntity implements Serializable{

	private static final long serialVersionUID = 4027123946076619930L;
	
	/** 序列号 **/
	private String seqNo ;
	/** 药材名称 **/
	private String medName ;
	/** 药品类型1 **/
	private String medType1 ;
	/** 药品类型2 **/
	private String medType2 ;
	/** 药品类型3 **/
	private String medType3 ;
	/** 购药数量 **/
	private Integer quantity ;
	/** 药品单价 **/
	private Double unitPrice ;
	/** 可报销标志 **/
	private String bxFlag ;
	/** 单价*数量 **/
	private Double sumPrice;
	
	public RealTimeTradeEntity() {
	}
	
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getBxFlag() {
		return bxFlag;
	}
	public void setBxFlag(String bxFlag) {
		this.bxFlag = bxFlag;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}
	
}
