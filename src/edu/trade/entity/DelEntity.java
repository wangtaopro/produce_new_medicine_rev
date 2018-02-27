package edu.trade.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 删除批次实体类


 * @author itw_wuzp
 * @date 20151127
 */
public class DelEntity implements Serializable{
	
	private static final long serialVersionUID = -7235855503539786562L;
	
	/** 批次号 **/
	private String batchNo;
	/** 金额 **/
	private Double sumOfMoney;
	/** 交易日期 **/
	private Date tradeDate;
	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Double getSumOfMoney() {
		return sumOfMoney;
	}
	public void setSumOfMoney(Double sumOfMoney) {
		this.sumOfMoney = sumOfMoney;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	
}
