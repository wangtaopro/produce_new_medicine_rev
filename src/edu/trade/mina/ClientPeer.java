package edu.trade.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.alibaba.fastjson.JSONObject;
import edu.trade.dto.Body;

/**
 * 客户端
 */
public class ClientPeer implements Callable<String>{
	
	private static final int AWAIT_TIME = 3000;
	private static final int READ_AWAIT_TIME = 3000;
	private static final int WRITE_AWAIT_TIME = 3000;
	private static final int MULTI_FACTOR = 1000;
	
	private Body body ;
	private String IP;
	private int PORT;
	
	public ClientPeer(){
	}
	
	public ClientPeer(Body body, String ip, int port) {
		this.body = body;
		IP = ip;
		PORT = port;
	}
	
	public String call() throws Exception {
		
		String socketResult = "";
		
		try {
			/** 建立客户端 **/
			IoConnector ioConnector = new NioSocketConnector();
			ioConnector.setConnectTimeoutMillis(AWAIT_TIME * MULTI_FACTOR);
			
			/*IoSessionConfig config = null;
			config.setIdleTime(IdleStatus.WRITER_IDLE, 1000);*/
			
			/** 添加辅助 **/
			ioConnector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
			ioConnector.setHandler(new ClientHandler());
			
			/** 开始连接 **/
			ConnectFuture future = ioConnector.connect(new InetSocketAddress(IP,PORT));
			future.awaitUninterruptibly();
			IoSession session = future.getSession();
			
			/** 设置读写空闲时长 **/
			ioConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, AWAIT_TIME );
			ioConnector.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, READ_AWAIT_TIME );
			ioConnector.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE, WRITE_AWAIT_TIME );			
			
			/** 写出数据 **/
			body.setBasicEntity(null);
			body.setCustomerEntity(null);
			WriteFuture future2 = session.write(JSONObject.toJSONString(body));
			future2.awaitUninterruptibly(AWAIT_TIME,TimeUnit.SECONDS);
			
			/** 取回数据 **/
			if( future2.isWritten()){
				session.getConfig().setUseReadOperation(true);
				ReadFuture readFuture = session.read();
				readFuture.awaitUninterruptibly(AWAIT_TIME ,TimeUnit.SECONDS);
				
				if(readFuture.isRead()){
					socketResult =  (String) readFuture.getMessage();
				}
			}
			
			/** 关闭连接 **/
			ioConnector.dispose();
			return socketResult;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("访问服务器 "+IP+":"+PORT+" 异常！");
		}
	}

	
}
