package edu.trade.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 通过spring工具使用IOC中的bean
 */
public class SpringUtil implements ApplicationContextAware{

	private static ApplicationContext ac;
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringUtil.ac = applicationContext;
	}
	
	/** 获取ac容器 **/
	public static ApplicationContext getApplicationContext(){
		return ac;
	}

	/** 获取IOC中的bean **/
	public static Object getBean(String src){
		return ac.getBean(src);
	}
	
}
