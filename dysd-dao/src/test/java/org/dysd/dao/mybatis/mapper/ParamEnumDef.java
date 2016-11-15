package org.dysd.dao.mybatis.mapper;

import java.io.Serializable;
import java.util.List;

public class ParamEnumDef implements Serializable{

	private String paramCode;//参数代码
	private String paramName;//参数名称
	private String paramGroup;//参数组别
	private String paramAttr;//参数属性
	private boolean editable;//是发可编辑
	private int seqno;//排序
	private String des;//描述
	private List<ParamEnumData> datas;//枚举项数据
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4076216504994692735L;
	
	public String getParamCode() {
		return paramCode;
	}
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamGroup() {
		return paramGroup;
	}
	public void setParamGroup(String paramGroup) {
		this.paramGroup = paramGroup;
	}
	public String getParamAttr() {
		return paramAttr;
	}
	public void setParamAttr(String paramAttr) {
		this.paramAttr = paramAttr;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
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
	public List<ParamEnumData> getDatas() {
		return datas;
	}
	public void setDatas(List<ParamEnumData> datas) {
		this.datas = datas;
	}
}
