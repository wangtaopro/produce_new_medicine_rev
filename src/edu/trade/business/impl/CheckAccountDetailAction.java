package edu.trade.business.impl;


import javax.annotation.Resource;

import edu.trade.entity.Header;
import edu.trade.entity.TransEntity;
import edu.trade.util.ClientUtil;
import edu.trade.util.CommonUtil;
import edu.trade.util.PackFTPUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import edu.trade.business.MedicineAction;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicineBatchService;

/**
 * 日对账(明细)
 */
@Controller("checkAccountDetailAction")
public class CheckAccountDetailAction extends MedicineAction{

	private static final long serialVersionUID = -2391174473179183354L;

	private Log log = LogFactory.getLog(CheckAccountDetailAction.class);
	
	@Resource
	private MedicineBatchService medicineBatchService;
	
	@Override
	public ReturnEntity deal(Body body) {
		
		/** 返回对象 **/
		ReturnEntity returnEntity = new ReturnEntity();
		
		/** 进行明细对账 **/
		try {
			/**准备body数据**/


			Body resultBody = new Body();
			resultBody = medicineBatchService.checkAccoutInfo(body);
			
			resultBody.setHeader(body.getHeader());
			resultBody.setTransEntity(body.getTransEntity());
			
			/** 将结果转换成xml并打包上载 **/
			String path = PackFTPUtil.uploadToFTP(resultBody, null);
			
			/** 返回第三方消息，将path存入 **/
			Body returnBody = new Body();
			returnBody.setHeader(body.getHeader());
			TransEntity transEntity = body.getTransEntity();
			transEntity.setUrl(path);
			resultBody.setTransEntity(transEntity);
			
			/** 清洗数据 **/
			Header header = resultBody.getHeader();
//			BasicEntity basicEntity = resultBody.getBasicEntity();
			resultBody = new Body();
			resultBody.setHeader(header);
//			resultBody.setBasicEntity(basicEntity);
			resultBody.setTransEntity(transEntity);
			
			log.info(JSONObject.toJSONString(resultBody));
			
			/** 发消息通知第三方 **/
			String request = ClientUtil.request(resultBody);
			returnEntity = JSONObject.parseObject(request,ReturnEntity.class);
			
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity("明细对账失败", "0", body.getHeader());
		}
		
		return returnEntity;
	}
	

	
}






































