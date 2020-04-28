package com.yszx.module.pay.util;

public enum ADTypeEnum {
	offline(0, "下架"), online(1, "正常");

	private int code;
	private String desc;

	private ADTypeEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static String getDescByCode(Short code) {
		for (ADTypeEnum refer : ADTypeEnum.values())
			if (code == refer.getCode())
				return refer.getDesc();
		return null;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
