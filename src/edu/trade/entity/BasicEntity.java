package edu.trade.entity;

import java.io.Serializable;

/**
 * 实时扣款基础信息实体
 */
public class BasicEntity implements Serializable{
	
	private static final long serialVersionUID = -7422976976140630199L;
	
	/** 卡号 **/
	private String medCardNo;
	/** 客户号 **/
	private String contNo;
	/** 总价 **/
	private Double sumPrice;
	/** 药品报销总金额 */
	private Double sumClmPrice;
	/** 交易总额 **/
	private Double transSumVolume;
	/** 商家代码 **/
	private String medShopCode;
	/** 商家名称 **/
	private String medShopName;
	/** 商铺代码 **/
	private String counterNo;
	/** 商铺名称 **/
	private String counterName;
	/** 省 **/
	private String province;
	/** 市 **/
	private String city;
	/** 县 **/
	private String county;
	/** 购药渠道 **/
	private String buyChnl;
	/** 购买日期 **/
	private String buyDate;
	/** 购买时间 **/
	private String buyTime;
	/** 寄送地址 **/
	private String address;
	/** 收件人 **/
	private String addressee;
	/** 联系方式 **/
	private String mobile;
	/** 客户端IP **/
	private String clientIp;
	/** 浏览器类型 **/
	private String browserType;
	/** 批次数量 **/
	private Integer batchCount;
	/** 实时交易信息 **/
	private RealTimeTradeEntity[] realTimeTradeEntityList;
	/** 第三方批次号 **/
	private String foreignBatchNo;
	/** 泰康批次号 **/
	private String taiKangBatchNo;
	
	/** 药品状态 **/
	private String medState;
	
	/** 药品分类1 **/
	private String medType1;
	
	/** 上传明细日期 **/
	private String createDate;
	/**tradeVolume**/
	private Double tradeVolume;
	
	public String getMedType1() {
		return medType1;
	}

	public void setMedType1(String medType1) {
		this.medType1 = medType1;
	}

	public String getForeignBatchNo() {
		return foreignBatchNo;
	}

	public void setForeignBatchNo(String foreignBatchNo) {
		this.foreignBatchNo = foreignBatchNo;
	}
	
	public RealTimeTradeEntity[] getRealTimeTradeEntityList() {
		return realTimeTradeEntityList;
	}

	public void setRealTimeTradeEntityList(
			RealTimeTradeEntity[] realTimeTradeEntityList) {
		this.realTimeTradeEntityList = realTimeTradeEntityList;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public BasicEntity() {
	}
	
	public String getMedCardNo() {
		return medCardNo;
	}
	public void setMedCardNo(String medCardNo) {
		this.medCardNo = medCardNo;
	}
	public String getContNo() {
		return contNo;
	}
	public void setContNo(String contNo) {
		this.contNo = contNo;
	}
	public Double getSumPrice() {
		return sumPrice;
	}
	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}
	public String getMedShopCode() {
		return medShopCode;
	}
	public void setMedShopCode(String medShopCode) {
		this.medShopCode = medShopCode;
	}
	public String getMedShopName() {
		return medShopName;
	}
	public void setMedShopName(String medShopName) {
		this.medShopName = medShopName;
	}
	public String getCounterNo() {
		return counterNo;
	}
	public void setCounterNo(String counterNo) {
		this.counterNo = counterNo;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getBuyChnl() {
		return buyChnl;
	}
	public void setBuyChnl(String buyChnl) {
		this.buyChnl = buyChnl;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Double getSumClmPrice() {
		return sumClmPrice;
	}

	public void setSumClmPrice(Double sumClmPrice) {
		this.sumClmPrice = sumClmPrice;
	}
	

	public Double getTransSumVolume() {
		return transSumVolume;
	}

	public void setTransSumVolume(Double transSumVolume) {
		this.transSumVolume = transSumVolume;
	}

	public Integer getBatchCount() {
		return batchCount;
	}

	public void setBatchCount(Integer batchCount) {
		this.batchCount = batchCount;
	}

	public String getTaiKangBatchNo() {
		return taiKangBatchNo;
	}

	public void setTaiKangBatchNo(String taiKangBatchNo) {
		this.taiKangBatchNo = taiKangBatchNo;
	}

	public String getMedState() {
		return medState;
	}

	public void setMedState(String medState) {
		this.medState = medState;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Double getTradeVolume() {
		return tradeVolume;
	}

	public void setTradeVolume(Double tradeVolume) {
		this.tradeVolume = tradeVolume;
	}

}
