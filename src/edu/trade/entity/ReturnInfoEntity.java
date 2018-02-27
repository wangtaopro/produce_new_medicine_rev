package edu.trade.entity;

import java.io.Serializable;

/**
 * @author wangtao
 * 部分成功返回结果集
 * 用途：(为以下接口提供返回集)
 *  1.发卡接口
 *  2.明细批量上传接口
 *  3.客户信息变更接口
 *  4.补卡换卡接口
 *  5.实时扣款接口（带明细与不带明细）
 *  6.账户余额查询接口
 * 	7.交易结果查询接口
 * 	8.信息收集接口
 * 	9.日对账接口（卡对账与明细对账）
 */
public class ReturnInfoEntity implements Serializable,Comparable<ReturnInfoEntity>{
	
	private static final long serialVersionUID = 7997401096695087051L;
	
	/** 批次号 **/
	private String batchNo;
	
	/** 卡号 **/
	private String medCarNo;
	
	/** 个单号 **/
	private String contNo;
	
	/** 失败信息 **/
	private String msg;
	
	/** 顺序号，为发卡设计 **/
	private String no ;
	
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public ReturnInfoEntity() {
	}

	public ReturnInfoEntity(String batchNo, String medCarNo, String contNo,
			String msg,String no) {
		this.batchNo = batchNo;
		this.medCarNo = medCarNo;
		this.contNo = contNo;
		this.msg = msg;
		this.no = no;
	}

	public String getMedCarNo() {
		return medCarNo;
	}

	public void setMedCarNo(String medCarNo) {
		this.medCarNo = medCarNo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getContNo() {
		return contNo;
	}

	public void setContNo(String contNo) {
		this.contNo = contNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public int compareTo(ReturnInfoEntity o) {
		if(batchNo.equals(o.getBatchNo())){
			return 1;
		}
		return 0;
	}

}
