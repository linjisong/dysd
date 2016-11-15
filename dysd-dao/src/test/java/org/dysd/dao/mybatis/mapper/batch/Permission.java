package org.dysd.dao.mybatis.mapper.batch;

public class Permission {

	private int permId;
	
	private String permType;
	
	private String permTypeKey;
	
	private String permAttr;

	public int getPermId() {
		return permId;
	}

	public void setPermId(int permId) {
		this.permId = permId;
	}

	public String getPermType() {
		return permType;
	}

	public void setPermType(String permType) {
		this.permType = permType;
	}

	public String getPermTypeKey() {
		return permTypeKey;
	}

	public void setPermTypeKey(String permTypeKey) {
		this.permTypeKey = permTypeKey;
	}

	public String getPermAttr() {
		return permAttr;
	}

	public void setPermAttr(String permAttr) {
		this.permAttr = permAttr;
	}
}
