package edu.trade.entity;

import java.io.Serializable;

/**
 * 传输路径辅助实体
 */
public class TransEntity implements Serializable{
	
	private static final long serialVersionUID = -178383524909239480L;
	
	/** 文件传输路径 **/
	private String url;
	
	/** 辅助前端IP **/
	private String thirdPartyIp ;
	
	/** 辅助前端端口 **/
	private Integer thirdPartyPort ;
	
	public TransEntity() {
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public TransEntity(String url) {
		this.url = url;
	}

	public String getThirdPartyIp() {
		return thirdPartyIp;
	}

	public void setThirdPartyIp(String thirdPartyIp) {
		this.thirdPartyIp = thirdPartyIp;
	}

	public Integer getThirdPartyPort() {
		return thirdPartyPort;
	}

	public void setThirdPartyPort(Integer thirdPartyPort) {
		this.thirdPartyPort = thirdPartyPort;
	}
}
