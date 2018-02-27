package edu.trade.dto;

import java.io.Serializable;

import edu.trade.entity.Header;
import edu.trade.entity.ReturnInfoEntity;

/**
 * 返回实体
 */
public class ReturnEntity implements Serializable,Comparable<ReturnEntity>{

	private static final long serialVersionUID = -663698664493870874L;
	
	/** 返回类型 **/
	private String resultCode;
	/** 返回原因数组 **/
	private ReturnInfoEntity[] returnInfoEntitys ;
	/** 返回头 **/
	private Header header;
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public ReturnEntity() {
	}
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public ReturnInfoEntity[] getReturnInfoEntitys() {
		return returnInfoEntitys;
	}

	public void setReturnInfoEntitys(ReturnInfoEntity[] returnInfoEntitys) {
		this.returnInfoEntitys = returnInfoEntitys;
	}

	public int compareTo(ReturnEntity o) {
		if(header.getBatchNo().equals(o.getHeader().getBatchNo())){
			return 1;
		}
		return 0;
	}
}
