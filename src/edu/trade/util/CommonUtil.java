package edu.trade.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import edu.trade.dto.ReturnEntity;
import edu.trade.entity.Header;
import edu.trade.entity.OtherEntity;
import edu.trade.entity.ReturnInfoEntity;

/**
 * 公共util 
 */
public class CommonUtil {
	
	/** 获取结果 msg 返回信息，resultCode 结果编码，batchNo 批次号，tradeType 交易类型 **/
	public static  ReturnEntity getReturnEntity(String msg,String resultCode,Header header1){
		
		String hostName = "";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		Header header = header1;
		if(header != null){
			header.setIp(hostName);
		}
		
		ReturnEntity returnEntity = new ReturnEntity();
		returnEntity.setResultCode(resultCode);
		
		ReturnInfoEntity[] returnInfoEntitys = new ReturnInfoEntity[1];
		ReturnInfoEntity returnInfoEntity = new ReturnInfoEntity();
		returnInfoEntity.setMsg(msg);
		
		returnInfoEntitys[0] = returnInfoEntity;
		returnEntity.setReturnInfoEntitys(returnInfoEntitys);
		returnEntity.setHeader(header);
		
		return returnEntity;
	}
	
	/**
	 * 修改状态变量 
	 */
	public static OtherEntity toggleState(){
		OtherEntity otherEntity = new OtherEntity();
		otherEntity.setState("1");
		return otherEntity;
	}

}
