package edu.trade.util;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 *  xml工具 
 */
@SuppressWarnings("unchecked")
public class XmlUtil<T> {
	
	private final static XStream xStream = new XStream(new DomDriver("UTF-8"));
	
	/** 自动检测指定包，将包中的实体转换成简单名称 **/
	static{
		
		xStream.autodetectAnnotations(true);
		xStream.setMode(XStream.NO_REFERENCES);
		try {
			Map<Object, String> scanPackage = ClassUtil.scanPackage();
			for(Entry<Object, String> entry:scanPackage.entrySet()){
				xStream.alias(entry.getValue(), entry.getKey().getClass());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 将复杂对象转换成XML **/
	public String object2XML(T t) throws Exception{
		return xStream.toXML(t);
	}
	
	
	/** 将xml字符串转换成object **/
	public T xml2Object(String xml) throws Exception{
		return (T) xStream.fromXML(xml);
	}
	
	
	/** 将文件转换成object **/
	public T file2Object(File file) throws Exception{
		return (T) xStream.fromXML(file);
	}
}
