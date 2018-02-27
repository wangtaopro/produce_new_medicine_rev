package edu.trade.entity;

import java.io.Serializable;

/**
 * @author wangtao
 * 查询辅助实体
 */
public class QueryEntity implements Serializable{
	
	private static final long serialVersionUID = -2556668660337720400L;
	
	/** 卡号 **/
	private String medCardNo ;
	/** 个单号 **/
	private String contNo;
	/** 批次号 **/
	private String batchNo;
	
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public QueryEntity() {
	}
	
	public String getMedCardNo() {
		return medCardNo;
	}
	public void setMedCardNo(String medCardNo) {
		this.medCardNo = medCardNo;
	}
	public String getContNo() {
		return contNo;
	}
	public void setContNo(String contNo) {
		this.contNo = contNo;
	}

}
