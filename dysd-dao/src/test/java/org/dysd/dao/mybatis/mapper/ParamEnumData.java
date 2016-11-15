package org.dysd.dao.mybatis.mapper;

import java.io.Serializable;

public class ParamEnumData implements Serializable{

	private String paramCode;//参数代码
	private String dataCode;//枚举项代码
	private String dataText;//枚举型文本
	private String dataParam;//枚举型参数
	private int seqno;//排序
	private String des;//描述
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4225707430506680193L;
	
	public String getParamCode() {
		return paramCode;
	}
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	public String getDataCode() {
		return dataCode;
	}
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}
	public String getDataText() {
		return dataText;
	}
	public void setDataText(String dataText) {
		this.dataText = dataText;
	}
	public String getDataParam() {
		return dataParam;
	}
	public void setDataParam(String dataParam) {
		this.dataParam = dataParam;
	}
	public int getSeqno() {
		return seqno;
	}
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
}
