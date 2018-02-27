package edu.trade.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class HCoderFactory implements ProtocolCodecFactory {
	private final HEncoder encoder;
	private final HDecoder decoder;

	public HCoderFactory() {
		//this(Charset.defaultCharset());
		this(Charset.forName("UTF-8"));

	}

	public HCoderFactory(Charset charSet) {
		this.encoder = new HEncoder(charSet);
		this.decoder = new HDecoder(charSet);
	}

	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return encoder;
	}
}