package edu.trade.mina;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import edu.trade.assisstant.CommonAssisstant;
import edu.trade.dto.Body;
import edu.trade.dto.ReturnEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import edu.trade.business.impl.CheckBalanceAction;
import edu.trade.business.impl.CheckTradeResultAction;
import edu.trade.business.impl.CollectMobileAction;
import edu.trade.business.impl.DeductWithDetailAction;
import edu.trade.business.impl.DeductWithoutDetailAction;
import edu.trade.business.impl.DeleteBatchAction;
import edu.trade.business.impl.DispatchCardAction;
import edu.trade.business.impl.ModifyCardAction;
import edu.trade.business.impl.UpdateDetailAction;
import edu.trade.util.CommonUtil;
import edu.trade.util.EncryptUtil;
import edu.trade.util.PropertiesUtil;
import edu.trade.util.ThreadUtil;
import edu.trade.util.TradeTypeUtil;

/**
 * mina助手
 */
@Controller
public class MinaAssisstant {

	@Resource
	private ModifyCardAction modifyCardAction;
	@Resource
	private CheckBalanceAction checkBalanceAction;
	@Resource
	private CheckTradeResultAction checkTradeResultAction;
	@Resource
	private CollectMobileAction collectMobileAction;
	@Resource
	private DeductWithDetailAction deductWithDetailAction;
	@Resource
	private DeductWithoutDetailAction deductWithoutDetailAction;
	@Resource
	private UpdateDetailAction updateDetailAction;
	@Resource
	private DispatchCardAction dispatchCardAction;
	@Resource
	private DeleteBatchAction deleteBatchAction ;
	@Resource
	private CommonAssisstant commonAssisstant;
	
	private final Lock lock = new ReentrantLock();
	private final String OUTTER_IP = "OUTTER-IP";
	private final String INNER_IP = "INNER-IP";
	
	private final static Log log = LogFactory.getLog(ServerHandler.class);
	
	/** 分发任务-这里以后可以修改为注册策略，更具可插拔性 **/
	public ReturnEntity dispatchTask(String tradeType, Body body){
		try {
			/** 补卡换卡 **/
			if("ACC".equals(tradeType)){
				return modifyCardAction.process(body);
			}
			/** 实时交易-带明细 **/
			if("RTD".equals(tradeType)){
				return deductWithDetailAction.process(body);
			}
			/** 实时交易-不带明细 **/
			if("RLD".equals(tradeType)){
				return deductWithoutDetailAction.process(body);
			}
			/** 明细批量上传 **/
			if("UBD".equals(tradeType)){
				return updateDetailAction.process(body);
			}
			/** 查询账户余额 **/
			if("QAB".equals(tradeType)){
				return checkBalanceAction.process(body);
			}
			/** 查询交易结果 **/
			if("QTR".equals(tradeType)){
				return checkTradeResultAction.process(body);
			}
			/** 收集手机信息 **/
			if("CMI".equals(tradeType)){
				return collectMobileAction.process(body);
			}
			/** 发卡接口 **/
			if("DCI".equals(tradeType)){
				return dispatchCardAction.process(body);
			}
			/** 删除批次 **/
			if("DBI".equals(tradeType)){
				return deleteBatchAction.process(body);
			}
			
			else{
				return CommonUtil.getReturnEntity("请求类型不存在！", "0", body.getHeader());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("处理任务异常！");
		}
	}
	
	/** 与前置机交互请求 **/
	public ReturnEntity invoke(Body src,String IP,int PORT) throws Exception{
		
		lock.lock();
		
		try {
			ClientPeer clientPeer = new ClientPeer(src, IP, PORT);
			FutureTask<String> f = (FutureTask<String>) ThreadUtil.exec.submit(clientPeer);
			ReturnEntity returnEntity = JSONObject.parseObject(f.get(),ReturnEntity.class);
			return returnEntity;
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("交互报文异常！");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("与前置机交互异常！");
		} finally{
			lock.unlock();
		}
	}

	/**
	 * 核心校验方法，需要做成filter
	 */
	public ReturnEntity process(IoSession session, Object message) {
		
		Body body = new Body();
		
		/** 获取IP **/
		String ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
		log.info(ip);
		
		/** 处理EBA信息-无需加密 **/
		List<String> ips = new ArrayList<String>();
		String valueByKey = PropertiesUtil.getValueByKey("EBA_IP");
		if(valueByKey.contains(",")){
			String[] split = valueByKey.split(",");
			List<String> asList = Arrays.asList(split);
			ips.addAll(asList);
		}
		
		/** 对EBA-IP进行处理 **/
		String ebaIP = "";
		if(ips.isEmpty()){
			ebaIP = valueByKey;
		}
		else {
			for (int i = 0; i < ips.size(); i++) {
				String rIP = ips.get(i);
				if(rIP.equals(ip)){
					ebaIP = rIP;
					break;
				}
			}
		}
		if(ebaIP.equals(ip)){
			
			/** 系统的灵活之处，也是不合规范之处，后期需要统一零散化的代码 **/
			session.setAttributeIfAbsent("key", "EBA");
			
			String bodySrc = (String)message;
			Body body2 = JSONObject.parseObject(bodySrc,Body.class);
			
			if(!ebaIP.equals(body2.getHeader().getIp())){
				return CommonUtil.getReturnEntity("权限不足-key！","0",body.getHeader());
			}
			
			String tradeType = body2.getHeader().getTradeType(); 
			
			if(!"DCI".equals(tradeType)){
				return CommonUtil.getReturnEntity("业务类型与客户端不符！","0",body.getHeader());
			}
			
			/** 获取请求信息 **/
			String IP = "";
			Integer PORT = 0;
			List<String> list = PropertiesUtil.getIO(INNER_IP);
			
			for(String str:list){
				String[] ip_port = str.split("-");
				IP = ip_port[0];
				PORT = Integer.parseInt(ip_port[1]);
			}
			
			if("".equals(IP) || PORT == 0){
				return CommonUtil.getReturnEntity("请求服务端异常","0",body2.getHeader());
			}
			
			/** 任务分发 **/
			ReturnEntity result = dispatchTask(tradeType,body2);
			log.info("处理结果为："+JSONObject.toJSONString(result,true));
			return result;
			
		}
		
		/** 处理第三方 **/
		try {
			String newMessage = (String)message;
			String messKeys[];
			String key  ="";
			if(newMessage.contains(":")){
				messKeys=newMessage.split(":");
				message = messKeys[0];
				key = messKeys[1];
			}
			//String key = getKey(ip);
			//获取第三方密钥
			log.info("thirdPartkey:"+key);
			if("".equals(key)||key== null){
				key = "bfafa2d07be9464c89dba76a";
			}
			
			if("".equals(key) || key == null){
				return CommonUtil.getReturnEntity("权限不足-key！","0",body.getHeader());
			}
			
			session.setAttribute("key", key);
			
			if("".equals(message) || message == null){
				return CommonUtil.getReturnEntity("传输信息为空！", null, null);
			}
			
			String bodySrc = new String(EncryptUtil.decryptStr(key.getBytes(), EncryptUtil.fromString((String)message)));
			log.info("第三方接收信息为："+bodySrc);
			
			/** 去除尾空格 **/
			char c = bodySrc.charAt(bodySrc.length()-1);
			if(c == 0)
				bodySrc = bodySrc.substring(0,bodySrc.length()-1);

			
			/** 解析对象 **/
			body = JSONObject.parseObject(bodySrc,Body.class);
			
			/** 权限校验-IP校验 **/
			if(body.getHeader().getBatchNo() == null
					|| body.getHeader().getIp()==null
					|| body.getHeader().getTradeType()==null
					|| body.getHeader().getCheckBit() == null
					|| body.getHeader().getTimestamp() == null){
				return CommonUtil.getReturnEntity("请求头信息异常！","0",body.getHeader());
			}
			
			/** 权限校验-标记位校验 **/
			String checkBit = body.getHeader().getCheckBit();
			if("".equals(checkBit) || checkBit == null){
				return CommonUtil.getReturnEntity("权限不足-标记位！","0",body.getHeader());
			}
			
			checkBit = new String(EncryptUtil.decryptStr(key.getBytes(), EncryptUtil.fromString(checkBit)));
			
			if("".equals(checkBit) || checkBit == null){
				return CommonUtil.getReturnEntity("权限不足！","0",body.getHeader());
			}
			
			/** 从数据库中获取标记位信息 **/
			String checkBitDB = "";
			try {
				checkBitDB = commonAssisstant.queryCheckBit(checkBit);
			} catch (Exception e) {
				e.printStackTrace();
				return CommonUtil.getReturnEntity("权限不足-标记DB！","0",body.getHeader());
			}
			
			if("0".equals(checkBitDB) || "".equals(checkBitDB) || checkBitDB == null){
				return CommonUtil.getReturnEntity("权限不足！","0",body.getHeader());
			}
			
			/** 权限校验-标记位与第三方一致 IP-CheckBit **/
			boolean rightFlag = checkBitAndIP(body, checkBit);
			
			if(!rightFlag){
				return CommonUtil.getReturnEntity("权限不足-校验位-IP！","0",body.getHeader());
			}
			
			/** 第三方请求类型校验 **/
			String tradeType = body.getHeader().getTradeType();
			if(!TradeTypeUtil.isRequestType(tradeType)
					&& !TradeTypeUtil.isEbaRequestType(tradeType) 
					&& !TradeTypeUtil.isResponseType(tradeType)){
				return CommonUtil.getReturnEntity("第三方请求类型异常！","0",body.getHeader());
			}
			
			/** 连接成功日志 **/
			log.info(ip + " 请求成功！");
			
			/** 进入系统后将校验位转为明文 **/
			body.getHeader().setCheckBit(checkBit);
			
			/** 定义IP及端口参数 **/
			int PORT = 0;
			String IP = "";
			
			/** 交易流向泰康 **/
			if(TradeTypeUtil.isRequestType(tradeType) || TradeTypeUtil.isEbaRequestType(tradeType)){
				
				/** 商户号终端号校验 **/
				if(body.getBasicEntity() == null 
						|| "".equals(body.getBasicEntity().getMedShopCode())
						|| "".equals(body.getBasicEntity().getCounterNo())
						|| body.getBasicEntity().getCounterNo() == null
						|| body.getBasicEntity().getMedShopCode() == null){
					return CommonUtil.getReturnEntity("商户号或终端号不能为空！","0",body.getHeader());
				}
				
				List<String> list = PropertiesUtil.getIO(INNER_IP);
				for(String str:list){
					String[] ip_port = str.split("-");
					IP = ip_port[0];
					PORT = Integer.parseInt(ip_port[1]);
				}
			}
			
			/** 交易流向第三方 **/
			else if(TradeTypeUtil.isResponseType(tradeType)){
				
				/** 解决json问题 **/
				body.setOtherEntity(null);
				
				/** 设置目标值 **/
				IP = ip;
				PORT = PropertiesUtil.getPort(ip);
			}
			
			if("".equals(IP) || IP == null || PORT == 0){
				return CommonUtil.getReturnEntity("客户身份与请求类型不匹配！","0",body.getHeader());
			}
			
			log.info("请求IP为："+IP+" 端口为："+PORT);
			
			/** 任务分发,发卡时可能出问题，有可能处理进度太慢 **/
			ReturnEntity result = dispatchTask(tradeType,body);
			log.info("处理结果为："+JSONObject.toJSONString(result,true));
			return result;
			
		}catch (JSONException e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity("信息格式异常！", "0", body.getHeader());
		}catch (NullPointerException e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity("头文件信息异常！", "0", body.getHeader());
		}catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getReturnEntity(e.getMessage(), "0", body.getHeader());
		}
	}
	
	/**
	 * 通过Ip获取key,这个需要再处理
	 */
//	private String getKey(String ip) {
//		return commonAssisstant.selectKeyByIp(ip);
//	}

	/**
	 * IP与校验位相互校验
	 */
	private boolean checkBitAndIP(Body body, String checkBit) {
		List<String> resouce = PropertiesUtil.getIO(OUTTER_IP);
		boolean rightFlag = false;
		
		for(String str : resouce){
			if(str.contains(checkBit) && str.contains(body.getHeader().getIp())){
				rightFlag = true;
				break;
			}
		}
		return rightFlag;
	}
	
}
