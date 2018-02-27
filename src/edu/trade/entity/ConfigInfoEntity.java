package edu.trade.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 配置辅助实体，对应litpaInfo表
 */
public class ConfigInfoEntity implements Serializable{

	private static final long serialVersionUID = 2930353274101348073L;
	
	private String tpaCode ;
	private String name;
	private String shortName ;
	private String toGetHerFlag ;
	private String manageCom;
	private String linkMan;
	private String phone ;
	private String mobile ;
	private String address;
	private String postAlCode;
	private String bankCode ;
	private String bankName ;
	private String bankAccNo;
	private String standByFlag1;
	private String standByFlag2;
	private String standByFlag3;
	private String operator;
	private Date makeDate;
	private String makeTime ;
	private Date modifyDate;
	private String modifyTime;
	
	public String getTpaCode() {
		return tpaCode;
	}
	public void setTpaCode(String tpaCode) {
		this.tpaCode = tpaCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getToGetHerFlag() {
		return toGetHerFlag;
	}
	public void setToGetHerFlag(String toGetHerFlag) {
		this.toGetHerFlag = toGetHerFlag;
	}
	public String getManageCom() {
		return manageCom;
	}
	public void setManageCom(String manageCom) {
		this.manageCom = manageCom;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostAlCode() {
		return postAlCode;
	}
	public void setPostAlCode(String postAlCode) {
		this.postAlCode = postAlCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccNo() {
		return bankAccNo;
	}
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
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
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
}
