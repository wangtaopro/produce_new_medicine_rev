package edu.trade.util;

import java.util.UUID;

/**
 * 批次号Util
 */
public class BatchNoUtil {
	
	/** 生成批次号 **/
	public String getBatchNo(){
		UUID uuid = UUID.randomUUID();
		return String.valueOf(new DateUtil().getTime().replace(':', '-') +"-"+ uuid.toString().substring(0,8));
	}
	
	/** 
	 * 生成批次号 
	 */
	public String getSerialNo(){
		String uuid = UUID.randomUUID().toString(); 
		String serialNo = new DateUtil().getDate().replace("-", "") +
							new DateUtil().getTime().replace(":", "") + 
							uuid.substring(0,4); 
		//去掉"-"符号 
		return serialNo;
	}
	
}