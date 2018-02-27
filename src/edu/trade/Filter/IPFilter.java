package edu.trade.Filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

public class IPFilter extends IoFilterAdapter{

	@Override
	public void exceptionCaught(NextFilter nextFilter, IoSession session,
			Throwable cause) throws Exception {
		
		
		
	}
	
}
