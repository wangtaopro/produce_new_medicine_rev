package edu.trade.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 连接数据库util
 */
public class DBConnect {
	
	private SqlSessionFactory factory = null;
	private static Reader reader = null;
	
	
	/** 获取session **/
	public SqlSession getSession(){
		String config = "mapper/config.xml";
		try {
			reader = Resources.getResourceAsReader(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		factory = new SqlSessionFactoryBuilder().build(reader);
		return factory.openSession();
	}
	
}
