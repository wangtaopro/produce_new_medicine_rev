package edu.trade.business.impl;

import edu.trade.util.CommonUtil;
import edu.trade.util.PackFTPUtil;
import edu.trade.util.XmlUtil;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import edu.trade.business.MedicineAction;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import edu.trade.service.MedicineBatchService;

/**
 * 明细上传
 */
@Controller("updateDetailAction")
public class UpdateDetailAction extends MedicineAction{

	private static final long serialVersionUID = -663902797454701767L;
	@Resource
	private MedicineBatchService medicineBatchService;

	@Override
	public ReturnEntity deal(Body body) {
		
		
		/** 从服务器获得明细信息 **/
		if(body.getTransEntity() == null || 
				body.getTransEntity().getUrl() == null ||
				"".equals(body.getTransEntity().getUrl())){
			return CommonUtil.getReturnEntity("指定路径为空！", "0", body.getHeader());
		}
		
		/** 从服务器获得明细信息 **/
		String url = body.getTransEntity().getUrl();
		File file = null;
		try {
			file = PackFTPUtil.downloadFromFTP(url,body.getHeader());
		} catch (IOException e1) {
			e1.printStackTrace();
			CommonUtil.getReturnEntity("从ftp获取文件失败", "0", body.getHeader());
		}
		
		/** 准备数据 **/
		if(!file.exists()) {
			return CommonUtil.getReturnEntity("从ftp获取文件失败", "0", body.getHeader());
		}
		Body newBody = createNewbodyFromXmlfile(file);
		ReturnEntity returnEntity = new ReturnEntity();
		
		/** 把获得的明细存储 **/
		if(newBody == null) {
			CommonUtil.getReturnEntity("生成Body失败", "0", body.getHeader());
		} else {
			newBody.setHeader(body.getHeader());
			newBody.setOtherEntity(body.getOtherEntity());
			try {
				returnEntity = medicineBatchService.uploadTradeDetail(newBody);
			} catch (Exception e) {
				e.printStackTrace();
				return CommonUtil.getReturnEntity("明细存储失败", "0", newBody.getHeader());
			}
		}
		
		return returnEntity;
	}
	
	/**
	 * 把xml文件转换成Body对象
	 * @param file
	 * @return
	 */
	private Body createNewbodyFromXmlfile(File file){
		XmlUtil<Body> xmlfileToObjectUtil = new XmlUtil<Body>();
		Body newBody = null;
		try {
			newBody = xmlfileToObjectUtil.file2Object(file);
		} catch (Exception e) {
			e.printStackTrace();
			return newBody;
		}
		return newBody;
	}

}



































