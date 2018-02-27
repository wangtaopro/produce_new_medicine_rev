package edu.trade.dto;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import edu.trade.entity.BasicEntity;
import edu.trade.entity.CardEntity;
import edu.trade.entity.CustomerEntity;
import edu.trade.entity.DelEntity;
import edu.trade.entity.Header;
import edu.trade.entity.MedBuyDetailEntity;
import edu.trade.entity.MedBuyMainEntity;
import edu.trade.entity.OtherEntity;
import edu.trade.entity.QueryEntity;
import edu.trade.entity.RealTimeTradeEntity;
import edu.trade.entity.TransEntity;

/**
 * @author wangtao
 * 接收辅助类

 */
public class Body implements Serializable,Comparable<Body>{
	
	private static final long serialVersionUID = -8831781659761500547L;
	
	/** 报文头 **/
	private Header header;
	/** 补卡换卡 **/
	private CardEntity cardEntity ;
	/** 客户信息 **/
	private CustomerEntity customerEntity;
	/** 实时交易 **/
	private RealTimeTradeEntity realTimeTradeEntity;
	/** URL **/
	private TransEntity transEntity;
	/** 交易公共信息 **/
	private BasicEntity basicEntity;
	/** 查询 **/
	private QueryEntity queryEntity;
	/** 其他 **/
	@JSONField(serialize=false)
	@XStreamOmitField()
	private OtherEntity otherEntity;
	/** 删除批次 **/
	private DelEntity delEntity;
	/** 购药分类汇总表 **/
	private MedBuyMainEntity medBuyMainEntity;
	/** 购药清单明细表 **/
	private MedBuyDetailEntity medBuyDetailEntity;
	
	
	private BasicEntity[] basicEntityList;
	private CardEntity[] cardEntityList ;
	private CustomerEntity[] customerEntityList;
	private RealTimeTradeEntity[] realTimeTradeEntityList;
	private TransEntity[] transEntityList;
	private QueryEntity[] queryEntityList;
	private DelEntity[] delEntityList;
 	private MedBuyDetailEntity[] medBuyDetailEntityList;
	
	public QueryEntity[] getQueryEntityList() {
		return queryEntityList;
	}
	
	public void setQueryEntityList(QueryEntity[] queryEntityList) {
		this.queryEntityList = queryEntityList;
	}
	
	public Body() {
		this.setOtherEntity(new OtherEntity());
	}
	
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public CardEntity getCardEntity() {
		return cardEntity;
	}
	public void setCardEntity(CardEntity cardEntity) {
		this.cardEntity = cardEntity;
	}
	public CustomerEntity getCustomerEntity() {
		return customerEntity;
	}
	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}
	public RealTimeTradeEntity getRealTimeTradeEntity() {
		return realTimeTradeEntity;
	}
	public void setRealTimeTradeEntity(RealTimeTradeEntity realTimeTradeEntity) {
		this.realTimeTradeEntity = realTimeTradeEntity;
	}
	public TransEntity getTransEntity() {
		return transEntity;
	}
	public void setTransEntity(TransEntity transEntity) {
		this.transEntity = transEntity;
	}
	
	public CardEntity[] getCardEntityList() {
		return cardEntityList;
	}

	public void setCardEntityList(CardEntity[] cardEntityList) {
		this.cardEntityList = cardEntityList;
	}

	public CustomerEntity[] getCustomerEntityList() {
		return customerEntityList;
	}

	public void setCustomerEntityList(CustomerEntity[] customerEntityList) {
		this.customerEntityList = customerEntityList;
	}

	public RealTimeTradeEntity[] getRealTimeTradeEntityList() {
		return realTimeTradeEntityList;
	}

	public void setRealTimeTradeEntityList(
			RealTimeTradeEntity[] realTimeTradeEntityList) {
		this.realTimeTradeEntityList = realTimeTradeEntityList;
	}

	public TransEntity[] getTransEntityList() {
		return transEntityList;
	}

	public void setTransEntityList(TransEntity[] transEntityList) {
		this.transEntityList = transEntityList;
	}

	public BasicEntity getBasicEntity() {
		return basicEntity;
	}

	public void setBasicEntity(BasicEntity basicEntity) {
		this.basicEntity = basicEntity;
	}

	public QueryEntity getQueryEntity() {
		return queryEntity;
	}

	public void setQueryEntity(QueryEntity queryEntity) {
		this.queryEntity = queryEntity;
	}

	public OtherEntity getOtherEntity() {
		return otherEntity;
	}

	public void setOtherEntity(OtherEntity otherEntity) {
		this.otherEntity = otherEntity;
	}

	public BasicEntity[] getBasicEntityList() {
		return basicEntityList;
	}

	public void setBasicEntityList(BasicEntity[] basicEntityList) {
		this.basicEntityList = basicEntityList;
	}

	public DelEntity getDelEntity() {
		return delEntity;
	}

	public void setDelEntity(DelEntity delEntity) {
		this.delEntity = delEntity;
	}

	public MedBuyMainEntity getMedBuyMainEntity() {
		return medBuyMainEntity;
	}

	public void setMedBuyMainEntity(MedBuyMainEntity medBuyMainEntity) {
		this.medBuyMainEntity = medBuyMainEntity;
	}

	public MedBuyDetailEntity getMedBuyDetailEntity() {
		return medBuyDetailEntity;
	}

	public void setMedBuyDetailEntity(MedBuyDetailEntity medBuyDetailEntity) {
		this.medBuyDetailEntity = medBuyDetailEntity;
	}

	public DelEntity[] getDelEntityList() {
		return delEntityList;
	}

	public void setDelEntityList(DelEntity[] delEntityList) {
		this.delEntityList = delEntityList;
	}

	public MedBuyDetailEntity[] getMedBuyDetailEntityList() {
		return medBuyDetailEntityList;
	}

	public void setMedBuyDetailEntityList(
			MedBuyDetailEntity[] medBuyDetailEntityList) {
		this.medBuyDetailEntityList = medBuyDetailEntityList;
	}

	public int compareTo(Body o) {
		if(header.getBatchNo().equals(o.getHeader().getBatchNo())){
			return 1;
		}
		return 0;
	}
	
}
