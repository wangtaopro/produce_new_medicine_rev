package edu.trade.entity;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * 头文件
 */
public class Header implements Serializable{
	
	private static final long serialVersionUID = 7923474759216295950L;
	
	/** 对方IP **/
	private String ip ;
	/** 批次号 **/
	private String batchNo;
	/** 交易类型 **/
	private String tradeType;
	/** 校验位 **/
	private String checkBit;
	/** 交易时间 **/
	private String timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
	
	public String getCheckBit() {
		return checkBit;
	}
	public void setCheckBit(String checkBit) {
		this.checkBit = checkBit;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	public Header() {
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public Header(String ip, String batchNo, String tradeType, String checkBit,String timestamp) {
		this.ip = ip;
		this.batchNo = batchNo;
		this.tradeType = tradeType;
		this.checkBit = checkBit;
		this.timestamp = timestamp;
	}
	
}
