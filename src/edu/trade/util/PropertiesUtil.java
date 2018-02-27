package edu.trade.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author wangtao
 * 资源文件工具
 */
public class PropertiesUtil {
	
	private static final String OUTTER_IP = "OUTTER-IP";
	
	private static Properties properties1 = new Properties();
	
	/** 通过静态加载减少从硬盘获取的时间,修改配置需重启 **/
	static {
		try {
			InputStream resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("filter.properties");
			properties1.load(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 通过关键字获取资源 **/
	public static String[] getResource(String key){
		String str = (String) properties1.get(key);
		
		if("".equals(str) || str == null){
			throw new RuntimeException("权限不足！");
		}
		
		if(str.contains(",")){
			return str.split(",");
		} else {
			String[] strings = new String[1];
			strings[0] = str;
			return strings;
		}
	}

	
	/** 检查当前请求是否具有权限 **/
	public static boolean hasRight(String IP){
		if("".equals(IP))
			return false;
		
		String[] strings = getResource(OUTTER_IP);
		
		for(String str : strings){
			if(str.startsWith(IP)){
				return true;
			}
		}
		
		return false;
	}
	
	/** 通过IP获得端口 **/
	public static Integer getPort(String IP){
		if(!hasRight(IP)){
			return 0;
		}
		
		String[] strings = getResource(OUTTER_IP);
		
		for(String str : strings){
			if(str.startsWith(IP)){
				return Integer.parseInt(str.split("-")[1]);
			}
		}
		return 0;
	}
	
	/** 获取配置资源 **/
	public static List<String> getIO(String io){
		String[] strings = getResource(io);
		return Arrays.asList(strings);
	}
	
	/** 获取properties **/
	public static Properties getProperties(String path){
		Properties properties = null;
		try {
			InputStream resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(path);
			properties = new Properties();
			properties.load(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	/** 通过key获取值 **/
	public static String getValueByKey(String key){
		return (String) properties1.getProperty(key);
	}
}
