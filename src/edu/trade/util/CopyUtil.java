package edu.trade.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 拷贝工具
 */
public class CopyUtil {
	
	/** 深度复制 **/
	public static Serializable deeplyCopy(Serializable serializable) throws RuntimeException{
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(baos);
			os.writeObject(serializable);
			os.close();
			baos.close();
			
			byte[] b = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			ObjectInputStream ois = new ObjectInputStream(bais);
			
			Serializable copy = (Serializable) ois.readObject();
			ois.close();
			bais.close();
			return copy;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("复制出错！");
		}
	}
	
}
