package edu.trade.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import edu.trade.entity.ReturnInfoEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.entity.ConfigEntity;
import edu.trade.entity.ConfigInfoEntity;
import edu.trade.entity.CustomerEntity;
import edu.trade.entity.Header;
import edu.trade.entity.TransEntity;

public class PackFTPUtil {
	
	private static final String FIXED = "_TK_";
	private static final String SUFFIXXML = ".xml";
	private static final String SUFFIXZIP = ".zip";
	private static final String OUTTER_IP = "OUTTER-IP";
	private static final Log log = LogFactory.getLog(PackFTPUtil.class);
	
	/**
	 * 连接SFTP服务器
	 * @param username 用户名
	 * @param password 密码
	 * @param host 主机
	 * @param port 端口
	 */
	public static ChannelSftp connectSFTP(String username, String password,String host, int port) {
		
		JSch jsch = new JSch();
		Channel channel = null;
		
		try {
			Session session = jsch.getSession(username, host, port);
			session.setPassword(password);
			Properties sshConfig = new Properties();
			
			sshConfig.put("StrictHostKeyChecking", "no");
			session.setConfig(sshConfig);
			session.setServerAliveInterval(92000);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			
			if (channel.isConnected()) {
				log.info("Connect Success!!");
			} else {
				throw new RuntimeException("连接服务器异常！");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("连接服务器异常！");
		}
		return (ChannelSftp) channel;
	}

	/**
	 * 下载处理
	 * @param downloadPath 文件地址
	 * @param FileName  文件名称
	 * @param localPath 下载至本地路径
	 * @param header 
 	 */
	public static boolean dealData(String downloadPath, String FileName,String localPath, Header header) {
		
		/** 获取配置信息 **/
		ConfigEntity configByShortName = getConfigEntityFromDB(header,4);
		String username = configByShortName.getUsername();
		String pwd = configByShortName.getPassword();
		String IP = configByShortName.getIp();
		int PORT = Integer.parseInt(configByShortName.getPort());
		String key = configByShortName.getStandByFlag1();
		
		ChannelSftp Sftp = connectSFTP(username, pwd, IP, PORT);
		
		log.info("连接【"+IP+":"+PORT+"】成功！");
		
		if (Sftp == null)
			throw new RuntimeException("获取服务失败！");
		
		try {
			downLoadZipFile(Sftp, downloadPath, FileName, localPath, key);
		} catch (Exception e) {
			disConnect(Sftp);
			e.printStackTrace();
		}
		
		disConnect(Sftp);
		return true;
	}

	/**
	 * 下载压缩文件
	 * @param Sftp
	 * @param downloadPath
	 * @param fileName
	 * @param localPath
	 * @param key
	 */
	public static boolean downLoadZipFile(ChannelSftp Sftp, String downloadPath,String fileName, String localPath, String key) {
		try {
			log.info("当前路径为："+downloadPath);
			
			Sftp.cd(downloadPath);
			
			File out_Directory = new File(localPath);
			
			if (!out_Directory.exists())
				out_Directory.mkdirs();
			
			if(fileName.startsWith("/")){
				fileName = fileName.substring(1);
			}
			
			File dest = new File(out_Directory.getPath() +"/"+ getDateName());
			if(!dest.exists()){
				dest.mkdirs();
			}
			
			Sftp.get(fileName, dest.getPath());
			
			log.info("文件下载成功！！！");
			
			/** 先解压再解密 **/
			ZipUtil.unzip(localPath+"/"+ getDateName(), localPath+ "/"+ getDateName() + "/" +fileName);
			fileName = fileName.substring(0,fileName.indexOf(".")).concat(".xml");
			EncryptUtil.decryptFile(localPath +"/"+ getDateName() + "/" + fileName,localPath + fileName, key);
			log.info("localPath:" + localPath+" 文件解密成功");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * 生成数据名
	 */
	private static String getDateName() {
		return new DateUtil().getDate();
	}

	/**
	 * 断开SFTP连接
	 */
	public static void disConnect(ChannelSftp Sftp) {
		if (Sftp != null){
			try {
				Sftp.getSession().disconnect();
				log.info("Session disconnect Success!!");
			} catch (JSchException e) {
				log.info("Session disconnect Fail!!");
				e.printStackTrace();
			} finally {
				Sftp.disconnect();
				log.info("Sftp disconnect Success!!");
			}
		}
	}
	
	/**
	 * 从服务器下载指定文件
	 */
	public static File downloadFromFTP(String url,Header header) throws IOException {
		
		try {
			if("".equals(url) || url == null){
				throw new RuntimeException("下载路径不存在！");
			}
			
			/** 从传入的url中取出文件名称 **/
			url = url.replace("\\", "/");
			url = url.replace("//", "/");
			url = url.replace("\\\\", "/");
			
			if(url.startsWith("/")){
				url = url.substring(1);
			}
			
			String[] split = url.split("/");
			String fileName = split[split.length-1];
			String path = url.substring(0,url.lastIndexOf("/"));
			
			/** 确定为哪一家第三方 **/
			ConfigEntity configEntity = getConfigEntityFromDB(header,4);
			
			if(configEntity == null){
				throw new IllegalArgumentException("接收信息格式不符！");
			}
			
			boolean flag = dealData(configEntity.getTpaAbsPath() + path , fileName, configEntity.getTpaRelPath(),header);
			
			if(!flag){
				throw new RuntimeException("指定文件不存在！");
			}
			
			log.info("返回文件路径为：" + configEntity.getTpaRelPath() + fileName.substring(0,fileName.lastIndexOf(".")).concat(".xml"));
			
			return  FileUtils.getFile(configEntity.getTpaRelPath() + fileName.substring(0,fileName.lastIndexOf(".")).concat(".xml"));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取指定文件失败！");
		}
	}
	
	/** 
	 * 将EBA数据上传给第三方 
	 */
	public static String uploadToFTP(Body body, ReturnEntity returnEntity) throws Exception {
		
		if(body == null){
			throw new RuntimeException("上传数据不能为空！");
		}
		
		/** 数据筛选 **/
		if(returnEntity != null && returnEntity.getReturnInfoEntitys() != null){
			
			CustomerEntity[] customerEntityList = body.getCustomerEntityList();
			List<CustomerEntity> asList = Arrays.asList(customerEntityList);
			ReturnInfoEntity[] returnInfoEntitys = returnEntity.getReturnInfoEntitys();
			List<ReturnInfoEntity> asList2 = Arrays.asList(returnInfoEntitys);
			
			for (int i = 0; i < asList.size(); i++) {
				String contNo = asList.get(i).getContNo();
				
				for (int j = 0; j < asList2.size(); j++) {
					if(contNo.equals(asList2.get(j).getContNo())){
						asList.remove(i);
						continue;
					}
				}
			}
			
			if(asList.size()>0){
				body.setCustomerEntityList(asList.toArray(customerEntityList));
			} else {
				throw new RuntimeException("无合格发卡信息！");
			}
		}
		
		/** 转换header数据 **/
		Header header = body.getHeader();
		TransEntity transEntity = body.getTransEntity();
		if(transEntity == null){
			throw new RuntimeException("IP配置异常，请联系泰康运维人员！");
		}
		
		Header header2 = new Header();
		BeanUtils.copyProperties(header, header2);
		header2.setIp(transEntity.getThirdPartyIp());
		
		body.setHeader(header2);
		
		/** 获取配置信息 **/
		ConfigEntity configEntity = getConfigEntityFromDB(body.getHeader(),4);
		
		/** 取回header **/
		body.setHeader(header);
		
		/** 获取配置信息 **/
		if(configEntity == null){
			throw new RuntimeException("配置异常，请联系泰康运维人员！");
		}
		
		/** 配置路径 **/
		String batchNo = resembleOutputAndCreateBatchNo(body, configEntity);
		String fileName = body.getHeader().getTradeType() + FIXED + batchNo + SUFFIXXML ;
		String path = configEntity.getTpaRelPath()+fileName ;
		fileName = body.getHeader().getTradeType() + FIXED + batchNo + SUFFIXZIP ;
		
		/** 对写出XML进行控制 **/
		Body body2 = new Body();
		BeanUtils.copyProperties(body, body2);
		
		if(!"CDA".equals(body.getHeader().getTradeType())){
			body2.setBasicEntity(null);
		}
		body2.setTransEntity(null);
		body2.setHeader(null);
		
		/** 写出XML **/
		String xml = new XmlUtil<Body>().object2XML(body2);
		FileUtils.write(new File(path), xml, "UTF-8");
		
		if(!UploadFtpUtil.uploadFile(path, fileName,configEntity)){
			throw new RuntimeException("上传服务器出错！");
		}
		
		return configEntity.getTpaAbsPath()+getDateName()+"/"+fileName;
	}

	/** 在输出前组装body,这里有问题 **/
	private static String resembleOutputAndCreateBatchNo(Body body, ConfigEntity configEntity)
			throws UnknownHostException {
		String checkBit = configEntity.getConfigInfoEntity().getStandByFlag2();
		body.getHeader().setIp(InetAddress.getLocalHost().getHostAddress());
		body.getHeader().setCheckBit(checkBit);
		return body.getHeader().getBatchNo();
	}

	/** 从数据库获取配置信息 **/
	private static ConfigEntity getConfigEntityFromDB(Header header,int index){
		
		try {
			String[] localPath = PropertiesUtil.getResource(OUTTER_IP);
			String shortName = "";
			
			for(String str : localPath){
				if(str.contains(header.getIp())){
					String[] split = str.split("-");
					shortName = split[index];
				}
			}
			
			ConfigInfoEntity configInfoEntity = new ConfigInfoEntity();
			configInfoEntity.setShortName(shortName);
			
			configInfoEntity.setStandByFlag1(header.getCheckBit());
			
			return new DBUtil().getConfigByShortName(configInfoEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取文件错误");
		}
	}
	
	@Test
	public void  Test1(){
		String url= "/2016-01-12/CDA_TK_20-22-0-5cfa2a40.zip";
		/** 从传入的url中取出文件名称 **/
		url = url.replace("\\", "/");
		url = url.replace("//", "/");
		url = url.replace("\\\\", "/");
		
		if(url.startsWith("/")){
			url = url.substring(1);
		}
		
	}

}
