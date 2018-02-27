package edu.trade.util;

import java.util.HashSet;

/**
 * 交易类型工具
 */
public class TradeTypeUtil {
	
	/**
	 * ACC 补卡换卡
	 * RTD 实时交易（带明细）
	 * RLD 实时交易（不带明细） 
	 * UBD 明细批量上传
	 * QAB 账户余额查询
	 * QTR 交易结果查询
	 * CMI 信息收集
	 * DCI 发卡
	 * CIC 客户信息变更
	 * CDA 日对账（明细对账）
	 * CDC 日对账（卡对账）
	 */
	private static final HashSet<String> serverSet = new HashSet<String>();
	private static final HashSet<String> clientSet = new HashSet<String>();
	private static final HashSet<String> innerSet = new HashSet<String>();
	
	/** 交易类型检测 **/
	static {
		
		serverSet.add("ACC");
		serverSet.add("RTD");
		serverSet.add("RLD");
		serverSet.add("UBD");
		serverSet.add("QAB");
		serverSet.add("QTR");
		serverSet.add("CMI");
		serverSet.add("DBI");
		
		clientSet.add("CIC");
		clientSet.add("CDA");
		clientSet.add("CDC");
		
		innerSet.add("DCI");
	}
	
	/** 第三方请求交易类型校验 **/
	public static boolean isRequestType(String src){
		
		if("".equals(src) || src == null)
			return false;
		
		if(serverSet.contains(src))
			return true;
		
		return false;
	}
	
	
	/** 第三方请求交易类型校验 **/
	public static boolean isEbaRequestType(String src){
		
		if("".equals(src) || src == null)
			return false;
		
		if(innerSet.contains(src))
			return true;
		
		return false;
	}
	
	
	
	/** 泰康请求交易类型校验 **/
	public static boolean isResponseType(String src){
		
		if("".equals(src) || src == null)
			return false;
		
		if(clientSet.contains(src))
			return true;
		
		return false;
	}
	
}
