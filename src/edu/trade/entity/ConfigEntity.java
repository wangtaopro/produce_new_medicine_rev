package edu.trade.entity;

import java.io.Serializable;

/**
 * 配置文件 
 */
public class ConfigEntity implements Serializable{

	private static final long serialVersionUID = -3862952457720674724L;
	
	/** TPA编码 **/
	private String tpaCode ;
	/** 业务类型 **/
	private String businessType;
	/** ip **/
	private String ip ;
	/** 用户名称  **/
	private String username;
	/** 用户密码 **/
	private String password;
	/** 端口 **/
	private String port;
	/** TPA路径 **/
	private String tpaRelPath;
	/** TPA绝对路径 **/
	private String tpaAbsPath;
	/** 药店路径 **/
	private String midRelPath;
	/** 药店绝对路径 **/
	private String midAbsPath;
	/** 传输路径 **/
	private String transRelPath;
	/** 传输绝对路径 **/
	private String transAbsPath;
	/** EBA路径 **/
	private String ebaRelPath;
	/** EBA绝对路径 **/
	private String ebaAbsPath;
	/** 备份路径 **/
	private String backupRelPath;
	/** 备份绝对路径 **/
	private String backupAbsPath;
	/** 备用字段1 -- 该字段被用作存储密钥 **/
	private String standByFlag1;
	/** 备用字段2 **/
	private String standByFlag2;
	/** 备用字段3 **/
	private String standByFlag3;
	/** 操作员 **/
	private String operator;
	/** 生成日期 **/
	private String makeDate;
	/** 生成时间 **/
	private String makeTime;
	/** 修改日期 **/
	private String modifyDate;
	/** 修改时间 **/
	private String modifyTime;
	
	/** 与该表形成一对一关系 **/
	private ConfigInfoEntity configInfoEntity;
	
	public ConfigInfoEntity getConfigInfoEntity() {
		return configInfoEntity;
	}
	public void setConfigInfoEntity(ConfigInfoEntity configInfoEntity) {
		this.configInfoEntity = configInfoEntity;
	}
	public String getTpaCode() {
		return tpaCode;
	}
	public void setTpaCode(String tpaCode) {
		this.tpaCode = tpaCode;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getMakeDate() {
		return makeDate;
	}
	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}
	public String getMakeTime() {
		return makeTime;
	}
	public void setMakeTime(String makeTime) {
		this.makeTime = makeTime;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getTpaRelPath() {
		return tpaRelPath;
	}
	public void setTpaRelPath(String tpaRelPath) {
		this.tpaRelPath = tpaRelPath;
	}
	public String getTpaAbsPath() {
		return tpaAbsPath;
	}
	public void setTpaAbsPath(String tpaAbsPath) {
		this.tpaAbsPath = tpaAbsPath;
	}
	public String getMidRelPath() {
		return midRelPath;
	}
	public void setMidRelPath(String midRelPath) {
		this.midRelPath = midRelPath;
	}
	public String getMidAbsPath() {
		return midAbsPath;
	}
	public void setMidAbsPath(String midAbsPath) {
		this.midAbsPath = midAbsPath;
	}
	public String getTransRelPath() {
		return transRelPath;
	}
	public void setTransRelPath(String transRelPath) {
		this.transRelPath = transRelPath;
	}
	public String getTransAbsPath() {
		return transAbsPath;
	}
	public void setTransAbsPath(String transAbsPath) {
		this.transAbsPath = transAbsPath;
	}
	public String getEbaRelPath() {
		return ebaRelPath;
	}
	public void setEbaRelPath(String ebaRelPath) {
		this.ebaRelPath = ebaRelPath;
	}
	public String getEbaAbsPath() {
		return ebaAbsPath;
	}
	public void setEbaAbsPath(String ebaAbsPath) {
		this.ebaAbsPath = ebaAbsPath;
	}
	public String getBackupRelPath() {
		return backupRelPath;
	}
	public void setBackupRelPath(String backupRelPath) {
		this.backupRelPath = backupRelPath;
	}
	public String getBackupAbsPath() {
		return backupAbsPath;
	}
	public void setBackupAbsPath(String backupAbsPath) {
		this.backupAbsPath = backupAbsPath;
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
}
