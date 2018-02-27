package edu.trade.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 类util 
 */
public class ClassUtil {

	/** 扫描固定包结构 **/
	public static Map<Object,String> scanPackage() throws Exception{
		
		String dir1 = ClassUtil.class.getClassLoader().getResource("").getFile().concat("edu/trade/entity/");
		String dir2 = ClassUtil.class.getClassLoader().getResource("").getFile().concat("edu/trade/dto/");
		
		File file = new File(dir1);
		File[] filesList = file.listFiles();
		
		File file1 = new File(dir2);
		File[] filesList1 = file1.listFiles();

		Map<Object,String> retMap = new HashMap<Object,String>();
		
		for (int i = 0; i < filesList.length; i++) {
			File file2 = filesList[i];
			String path = dir1+file2.getName();
			
			if(path.endsWith(".class")){
				Class<?> class1 = Class.forName(path.substring(path.indexOf("com")).replace("/", ".").replace(".class", ""));
				retMap.put(class1.newInstance(),class1.getSimpleName());			
			}
			
		}
		
		for (int i = 0; i < filesList1.length; i++) {
			File file2 = filesList1[i];
			String path = dir2+file2.getName();
			if(path.endsWith(".class")){
				Class<?> class1 = Class.forName(path.substring(path.indexOf("com")).replace("/", ".").replace(".class", ""));
				retMap.put(class1.newInstance(),class1.getSimpleName());
			}
		}
		
		return retMap;
		
	}
	
	@Test
	public void test1() throws Exception{
		Map<Object,String> objs = ClassUtil.scanPackage();
		System.out.println(objs);
	}
	
}
