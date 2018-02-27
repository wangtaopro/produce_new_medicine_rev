package edu.trade.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import edu.trade.entity.ConfigEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 上传服务器Util
 */
public class UploadFtpUtil {

	private static Log log = LogFactory.getLog(UploadFtpUtil.class);
	
	/**
	 * 将文件上传至服务器
	 * @param filePath 文件路径
	 * @param filename 文件名称
	 */
	public static boolean uploadFile(String filePath, String filename,ConfigEntity configEntity) {

		if(configEntity == null){
			throw new RuntimeException("配置异常，请联系泰康运维人员！");
		}
		
		try {
			
			/** 获取配置参数 **/
			String username = configEntity.getUsername();
			String pwd = configEntity.getPassword();
			String IP = configEntity.getIp();
			int PORT = Integer.parseInt(configEntity.getPort());
			String key = configEntity.getStandByFlag1();
			
			/** 对文件进行加密 **/
			EncryptUtil.encryptFile(filePath,filePath, key);
			
			/** 压缩文件 **/
			ZipUtil.zip(filePath, filePath.substring(0, filePath.indexOf(".")).concat(".zip"));
			filePath = filePath.substring(0, filePath.indexOf(".")).concat(".zip");
			
			/** 上传服务器 **/
			sshSftp(IP, username, pwd, PORT, filePath, filename,configEntity.getTpaAbsPath()+getDateName());
			
		} catch (Exception e) {
			System.out.println("文件上传SFTP失败");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 将文件上传至服务器
	 * 
	 * @param ip IP地址
	 * @param user 用户
	 * @param psw 密码
	 * @param port 端口
	 * @param filePath 文件路径
	 * @param filename 文件名称
	 */
	@SuppressWarnings("unchecked")
	public static void sshSftp(String ip, String user, String psw, int port,
			String filePath, String filename,String destPath) throws Exception {

		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();

		if (port <= 0) {
			session = jsch.getSession(user, ip);
		} else {
			session = jsch.getSession(user, ip, port);
		}

		if (session == null) {
			throw new Exception("session is null");
		}

		session.setPassword(psw);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect(30000);

		try {

			channel = (Channel) session.openChannel("sftp");
			channel.connect(1000);
			ChannelSftp sftp = (ChannelSftp) channel;
			System.out.println("ChannelSftp connect success!!!");
			
			log.info("上传路径[upload path]:"+destPath);
			
			/** 系统脆弱点，需要再调整 **/
	        try {
				Vector<String> contentVector = sftp.ls(destPath);
				if(contentVector ==null){
					sftp.mkdir(destPath);
				}
			} catch (Exception e) {
				sftp.mkdir(destPath);
			}
			sftp.cd(destPath);
			
			OutputStream outstream = null;
			InputStream instram = null;
			outstream = sftp.put(destPath+"/"+filename);
			instram = new FileInputStream(new File(filePath));

			byte b[] = new byte[1024];
			int n = 0;
			while ((n = instram.read(b)) != -1) {
				outstream.write(b, 0, n);
			}

			outstream.flush();
			outstream.close();
			instram.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());

		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}
	
	/**
	 * 生成数据名
	 */
	private static String getDateName() {
		return new DateUtil().getDate();
	}

}
