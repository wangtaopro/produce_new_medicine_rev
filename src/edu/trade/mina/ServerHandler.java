package edu.trade.mina;

import javax.annotation.Resource;

import edu.trade.dto.ReturnEntity;
import edu.trade.util.EncryptUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;

public class ServerHandler extends IoHandlerAdapter {
	
	private final static Log logger = LogFactory.getLog(ServerHandler.class);
	
	@Resource
	private MinaAssisstant minaAssisstant ;
	
	public ServerHandler() {
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.info(cause.getMessage());
	}
	
	@Override
	public void messageReceived(IoSession session, Object message){
		
		logger.info("Received message is :" + message);
		
		try {
			
			/** 为每个发出的json串加密 **/
			ReturnEntity process = minaAssisstant.process(session,message);
			String ret = JSONObject.toJSONString(process);
			String key = (String) session.getAttribute("key");
			
			/** EBA特殊返回 **/
			if("EBA".equals(key)){
				session.write(ret);
				return ;
			}
			
			/** key从哪里来？ **/ 
			String encryptStr = EncryptUtil.toString(EncryptUtil.encryptStr(key.getBytes(),ret.getBytes()));
			
			logger.info("发出数据为：" + ret);
			
			/** 传出json串 **/
			session.write(encryptStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("服务端收到信息-------------");
		session.close(true);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info(session.getRemoteAddress()+" 消息发送成功！");
		session.close(true);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info(session.getRemoteAddress()+" 客户端关闭！");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info(session.getRemoteAddress()+" 客户端连接！");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		logger.info(session.getServiceAddress() + "服务空闲！");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info(session.getLocalAddress()+" 连接打开！");
	}

}