package edu.trade.entity;

import java.io.Serializable;

/**
 * 客户实体
 */
public class CustomerEntity implements Serializable{
	
	private static final long serialVersionUID = -3465335150695400142L;
	
	/** 投保人名称 **/
	private String name ;
	/** 身份证号 **/
	private String IDNo ;
	/** 个单号 **/
	private String contNo ;
	/** 投保单位名称 **/
	private String grpName ;
	/** 每条数据的序号 **/
	private String no ;
	/** 证件类型 **/
	private String IDType ;
	/** 公司名称 **/
	private String comName;
	/** 手机号码 **/
	private String mobile;
	/** 医疗卡号 **/
	private String medCardNo;
	/** 医疗卡号 **/
	private String thirdParty;
	/** 团单号 **/
	private String grpContNo;
	/** 被保人号码 **/
	private String insuredNo ;
	/** 机构编码 **/
	private String comCode ;
	
	public String getGrpContNo() {
		return grpContNo;
	}
	public void setGrpContNo(String grpContNo) {
		this.grpContNo = grpContNo;
	}
	public String getMedCardNo() {
		return medCardNo;
	}
	public void setMedCardNo(String medCardNo) {
		this.medCardNo = medCardNo;
	}
	public CustomerEntity() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIDNo() {
		return IDNo;
	}
	public void setIDNo(String no) {
		IDNo = no;
	}
	public String getContNo() {
		return contNo;
	}
	public void setContNo(String contNo) {
		this.contNo = contNo;
	}
	public String getGrpName() {
		return grpName;
	}
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}
	
	public String getIDType() {
		return IDType;
	}

	public void setIDType(String type) {
		IDType = type;
	}

	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getThirdParty() {
		return thirdParty;
	}
	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}
	public String getInsuredNo() {
		return insuredNo;
	}
	public void setInsuredNo(String insuredNo) {
		this.insuredNo = insuredNo;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	
	
}
