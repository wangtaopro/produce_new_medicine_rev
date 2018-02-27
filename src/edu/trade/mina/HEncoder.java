package edu.trade.mina;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class HEncoder implements ProtocolEncoder {
	
	private final static Log log = LogFactory.getLog(HEncoder.class);
	
	private final Charset charset;

	public HEncoder(Charset charset) {
		this.charset = charset;
	}

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		
		CharsetEncoder ce = charset.newEncoder();
		String mes = (String) message;
		IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);
        buffer.putString(mes,ce);
        buffer.flip();
        out.write(buffer);
		
		/*System.out.println("---------encode-------------");
		String mes = (String) message;
		byte[] data = mes.getBytes("UTF-8");
		IoBuffer buffer = IoBuffer.allocate(data.length + 4);
		buffer.putInt(data.length);
		buffer.put(data);
		buffer.flip();
		out.write(buffer);
		out.flush();*/
	}

	public void dispose(IoSession session) throws Exception {
		log.debug("Dispose called,session is " + session);
	}
}
