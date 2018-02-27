package edu.trade.util;

import edu.trade.dto.Body;
import edu.trade.mina.ClientPeer;

/**
 * 客户端请求util 
 */
public class ClientUtil {
	
	/** 前置机IP **/
	private static final String IP = "10.135.35.49";
	
	/** 前置机端口 **/
	private static final Integer PORT = 9080;
	
	/** 发送请求 **/
	public static String request(Body body) throws Exception{
		
		String result = "";
		
		try {
			
			/** 请求前置机 **/
			ClientPeer clientPeer = new ClientPeer(body,IP,PORT);
			result = clientPeer.call();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("转换请求异常！");
		}
		
		return result ;
	}
}
