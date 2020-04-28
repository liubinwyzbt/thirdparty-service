package com.yszx.module.message.wx;

import com.xiaodajia.framework.entity.BaseEntity;

/**
 * 消息实体
 * 
 * @author liub
 *
 */
public class MsgParamDTO extends BaseEntity {
	private String key;
	private String value;
	private String color;

	private boolean validateStatus = false;;

	private String validateHint;

	public MsgParamDTO() {

	}

	public MsgParamDTO(String key, String value, String color) {
		super();
		this.key = key;
		this.value = value;
		this.color = color;
	}

	public MsgParamDTO(String key, String value, String color,
			boolean validateStatus, String validateHint) {
		super();
		this.key = key;
		this.value = value;
		this.color = color;
		this.validateStatus = validateStatus;
		this.validateHint = validateHint;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isValidateStatus() {
		return validateStatus;
	}

	public void setValidateStatus(boolean validateStatus) {
		this.validateStatus = validateStatus;
	}

	public String getValidateHint() {
		return validateHint;
	}

	public void setValidateHint(String validateHint) {
		this.validateHint = validateHint;
	}
	
	

}
