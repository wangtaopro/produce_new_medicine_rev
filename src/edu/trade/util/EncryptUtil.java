package edu.trade.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;

/**
 * 加密信息类
 */
public class EncryptUtil {
	
	private static final String ALGORITHM = "DESede";
	
    /** 
     * keybyte为加密密钥，长度为24字节，src为被加密的数据缓冲区（源）
     */
    public static byte[] encryptStr(byte[] keybyte, byte[] src) {
       try {
            /** 生成密钥 **/
            SecretKey deskey = new SecretKeySpec(keybyte, ALGORITHM);
            
            /** 加密 **/
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    /**
     * keybyte为加密密钥，长度为24字节，src为加密后的缓冲区
     */
    public static byte[] decryptStr(byte[] keybyte, byte[] src) {
    	try {
            /** 生成密钥 **/
            SecretKey deskey = new SecretKeySpec(keybyte, ALGORITHM);

            /** 解密 **/
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
            
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    /**
     * 对文件进行加密并写出到指定地址文件中
     */
    public static void encryptFile(String srcFile, String destFile, String key) throws Exception{
    	final byte[] keyBytes = key.getBytes();
    	byte[] readFileToByteArray = FileUtils.readFileToByteArray(new File(srcFile));
    	byte[] encoded = encryptStr(keyBytes, readFileToByteArray);
    	FileUtils.writeByteArrayToFile(new File(destFile), encoded);
    }
    
    /**
     * 解密文件并将解密后文件写出到指定文件
     */
    public static void decryptFile(String srcFile, String destFile, String key) throws Exception{
    	final byte[] keyBytes = key.getBytes();
    	byte[] readFileToByteArray = FileUtils.readFileToByteArray(new File(srcFile));
    	byte[] encoded = decryptStr(keyBytes, readFileToByteArray);
    	FileUtils.writeByteArrayToFile(new File(destFile), encoded);
    }

    /** 
     * 生成密钥,固定为24位
     */
    public static byte[] createKey() throws UnsupportedEncodingException{
    	UUID uuid = UUID.randomUUID();
    	String replace = uuid.toString().replace("-", "").substring(0,24);
    	return replace.getBytes();
    }
    
    /**
     * 将2进制转换为16进制
     */
	public static String toString(byte buffer[]) {
		StringBuffer returnBuffer = new StringBuffer();
		try {
			for (int pos = 0, len = buffer.length; pos < len; pos++) {
				returnBuffer.append(hexToAscii((buffer[pos] >>> 4) & 0x0F)).append(
						hexToAscii(buffer[pos] & 0x0F));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("后端加密异常，请检查数据格式！");
		}
		return returnBuffer.toString();
	}

	/**
	 * 将16进制转换为2进制
	 */
	public static byte[] fromString(String inHex) {
		int len = inHex.length();
		int pos = 0;
		byte buffer[] = new byte[((len + 1) / 2)];
		
		try {
			if ((len % 2) == 1) {
				buffer[0] = (byte) asciiToHex(inHex.charAt(0));
				pos = 1;
				len--;
			}
			
			for (int ptr = pos; len > 0; len -= 2) {
				buffer[pos++] = (byte) ((asciiToHex(inHex.charAt(ptr++)) << 4) | (asciiToHex(inHex.charAt(ptr++))));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("后端解密异常，请检查数据格式！");
		}
		
		return buffer;
	}
	
	/**
	 * 将ascii转换成Hex
	 */
	private static int asciiToHex(char c) {
		if ((c >= 'a') && (c <= 'f')) {
			return (c - 'a' + 10);
		}
		if ((c >= 'A') && (c <= 'F')) {
			return (c - 'A' + 10);
		}
		if ((c >= '0') && (c <= '9')) {
			return (c - '0');
		}
		throw new Error("ascii to hex failed");
	}
	
	/**
	 * 将hex转换成Ascii
	 */
	private static char hexToAscii(int h) {
		if ((h >= 10) && (h <= 15)) {
			return (char) ('A' + (h - 10));
		}
		if ((h >= 0) && (h <= 9)) {
			return (char) ('0' + h);
		}
		throw new Error("hex to ascii failed");
	}
	
}
    
