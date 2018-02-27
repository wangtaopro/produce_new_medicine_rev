package edu.trade.assisstant;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import edu.trade.service.MedicinePublicService;

@Controller
public class CommonAssisstant {
	
	@Resource
	private MedicinePublicService medicinePublicService;
	
	/**
	 * 校验位查询
	 */
	public String queryCheckBit(String checkBit) throws Exception{
		return medicinePublicService.queryCheckBit(checkBit);
	}
	
	/**
	 * 通过Ip获取key
	 */
	public String selectKeyByIp(String ip) {
		return medicinePublicService.selectKeyByIp(ip);
	}
	
}
