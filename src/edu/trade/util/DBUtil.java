package edu.trade.util;

import java.io.IOException;

import org.apache.ibatis.session.SqlSession;

import edu.trade.entity.ConfigEntity;
import edu.trade.entity.ConfigInfoEntity;
import edu.trade.mapper.MedicinePublicMapper;

/**
 * 数据库util
 */
public class DBUtil {
	
	
	/** 获取配置文件中的信息 **/
	public ConfigEntity getConfigByShortName(ConfigInfoEntity configInfoEntity) throws IOException{
		SqlSession session = new DBConnect().getSession();
		MedicinePublicMapper mapper = session.getMapper(MedicinePublicMapper.class);
		ConfigEntity configEntity = mapper.selectConfigByName(configInfoEntity);
		session.close();
		return  configEntity;
	}
	
}
