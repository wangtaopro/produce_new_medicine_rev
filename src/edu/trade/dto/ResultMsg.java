package edu.trade.dto;

import java.io.Serializable;

import edu.trade.entity.ReturnInfoEntity;

/**
 * @author wangtao
 */
public class ResultMsg implements Serializable{
	
	private static final long serialVersionUID = 3973989294269580532L;

	private ReturnInfoEntity[] returnInfoEntitys ;
	
	public ResultMsg() {
	}
	
	public ReturnInfoEntity[] getReturnInfoEntitys() {
		return returnInfoEntitys;
	}

	public void setReturnInfoEntitys(ReturnInfoEntity[] returnInfoEntitys) {
		this.returnInfoEntitys = returnInfoEntitys;
	}
	
}
