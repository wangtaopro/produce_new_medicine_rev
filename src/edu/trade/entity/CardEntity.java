package edu.trade.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 卡业务实体

 */
public class CardEntity implements Serializable{
	
	private static final long serialVersionUID = -8241230477597095014L;
	
	/** 为反序列添加 **/
	public CardEntity() {
	}
	
	/** 旧卡号 **/
	private String oldMedCardNo;
	/** 个单号 **/
	private String contNo;
	/** 新卡号 **/
	private String newMedCardNo;
	/** 客户号 **/
	private String insuredNo;
	/** 保单号 **/
	private String grpContNo;
	/** 姓名 **/
	private String name;
	/** 密码 **/
	private String password;
	/** 状态 **/
	private String cardState;
	/** 旧卡状态 **/
	private String oldCardState;
	/** 补卡批次 **/
	private String provideNo;
	/** 备注 **/
	private String memo;
	/** 个人积分 **/
	private Double bonus;
	/** 管理机构 **/
	private String manageCom;
	/** 操作员 **/
	private String operator;
	/** 可用公共账户限额 **/
	private Double maxPubLimit;
	/** 发卡日期 **/
	private Date provideDate;
	/** 发卡时间 **/
	private String provideTime;
	/** 丢卡日期 **/
	private Date lostDate;
	/** 丢卡时间 **/
	private String lostTime;
	/** 卡数量 **/
	private Integer cardCount;
	/** 卡号 **/
	private String medCardNo;
	/** 第三方发卡机构 **/
	private String thirdParty;
	/** 联系方式 **/
	private String phone;
	/** 身份证号码 **/
	private String idNo;
	
	public String getOldMedCardNo() {
		return oldMedCardNo;
	}

	public void setOldMedCardNo(String oldMedCardNo) {
		this.oldMedCardNo = oldMedCardNo;
	}

	public String getContNo() {
		return contNo;
	}

	public void setContNo(String contNo) {
		this.contNo = contNo;
	}

	public String getNewMedCardNo() {
		return newMedCardNo;
	}

	public void setNewMedCardNo(String newMedCardNo) {
		this.newMedCardNo = newMedCardNo;
	}

	public String getInsuredNo() {
		return insuredNo;
	}

	public void setInsuredNo(String insuredNo) {
		this.insuredNo = insuredNo;
	}

	public String getGrpContNo() {
		return grpContNo;
	}

	public void setGrpContNo(String grpContNo) {
		this.grpContNo = grpContNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCardState() {
		return cardState;
	}

	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	public String getProvideNo() {
		return provideNo;
	}

	public void setProvideNo(String provideNo) {
		this.provideNo = provideNo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public String getManageCom() {
		return manageCom;
	}

	public void setManageCom(String manageCom) {
		this.manageCom = manageCom;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Double getMaxPubLimit() {
		return maxPubLimit;
	}

	public void setMaxPubLimit(Double maxPubLimit) {
		this.maxPubLimit = maxPubLimit;
	}



	public String getProvideTime() {
		return provideTime;
	}

	public void setProvideTime(String provideTime) {
		this.provideTime = provideTime;
	}

	

	public Date getProvideDate() {
		return provideDate;
	}

	public void setProvideDate(Date provideDate) {
		this.provideDate = provideDate;
	}

	public Date getLostDate() {
		return lostDate;
	}

	public void setLostDate(Date lostDate) {
		this.lostDate = lostDate;
	}

	public String getLostTime() {
		return lostTime;
	}

	public void setLostTime(String lostTime) {
		this.lostTime = lostTime;
	}

	public String getOldCardState() {
		return oldCardState;
	}

	public void setOldCardState(String oldCardState) {
		this.oldCardState = oldCardState;
	}

	public Integer getCardCount() {
		return cardCount;
	}

	public void setCardCount(Integer cardCount) {
		this.cardCount = cardCount;
	}

	public String getMedCardNo() {
		return medCardNo;
	}

	public void setMedCardNo(String medCardNo) {
		this.medCardNo = medCardNo;
	}

	public String getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}


}
