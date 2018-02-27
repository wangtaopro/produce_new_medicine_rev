package edu.trade.entity;

import java.io.Serializable;
import java.util.Date;

import edu.trade.util.BatchNoUtil;
import edu.trade.util.DateUtil;

public class OtherEntity implements Serializable{

	private static final long serialVersionUID = -3368193405185013244L;

	/** 操作员 **/
	private String operator;
	
	/** 生成日期 **/
	private Date makeDate = new Date();
	
	/** 生成时间 **/
	private String makeTime = new DateUtil().getTime();
	
	/** 修改日期 **/
	private Date modifyDate = new Date();
	
	/** 修改时间 **/
	private String modifyTime = new DateUtil().getTime();
	
	/** 接受json串 **/
	private String acceptMessage;
	
	/** 发送json串 **/
	private String sendMessage;
	
	/** 日志序列号 **/
	private String logSerialNo;
	
	/** 交易状态 **/
	private String state;
	
	/** 交易信息 **/
	private String msg;
	
	/** 第三方批次号 **/
	private String foreignBatchNo;
	
	/** 生成批次号 **/
	private String batchNo = new BatchNoUtil().getBatchNo();
	
	/** 第三方编码 **/
	private String thirdParty;
	
	public String getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getForeignBatchNo() {
		return foreignBatchNo;
	}

	public void setForeignBatchNo(String foreignBatchNo) {
		this.foreignBatchNo = foreignBatchNo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAcceptMessage() {
		return acceptMessage;
	}

	public void setAcceptMessage(String acceptMessage) {
		this.acceptMessage = acceptMessage;
	}

	public String getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}


	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	

	public String getMakeTime() {
		makeTime = new DateUtil().getTime();
		return makeTime;
	}

	public void setMakeTime(String makeTime) {
		this.makeTime = makeTime;
	}

	

	
	public Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyTime() {
		modifyTime = new DateUtil().getTime();
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getLogSerialNo() {
		return logSerialNo;
	}

	public void setLogSerialNo(String logSerialNo) {
		this.logSerialNo = logSerialNo;
	}

}
