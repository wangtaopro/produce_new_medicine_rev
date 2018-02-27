package edu.trade.util;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.junit.Test;

/**
 * 解压缩文件Util 
 */
public class ZipUtil {
	
	/**
	 * 获取压缩文件
	 * @param zipFileName 待压缩文件路径
	 * @param targetFileName 压缩目标路径
	 */
	public static boolean zip(String zipFileName,String targetFileName){
		
		try {
			ZipFile zipFile = new ZipFile(targetFileName);
			ZipParameters parameters = new ZipParameters();
			
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			
			File file = new File(zipFileName);		
			zipFile.createZipFile(file, parameters);
			
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 解压文件
	 * @param unzipFileName 待解压文件路径
	 * @param targetFileName 目标解压文件路径
	 */
	public static boolean unzip(String unzipFileName,String targetFileName) throws ZipException{
		ZipFile zipFile = new ZipFile(targetFileName);
		zipFile.extractAll(unzipFileName);
		return true;		
	}
	
	@Test
	public void test(){
		try {
			ZipUtil.zip("D:/txt/file.log", "D:/txt/file.zip");
			ZipUtil.unzip("D:/txt", "D:/txt/file.zip");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
