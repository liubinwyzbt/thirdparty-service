package com.yszx.module.core.dto;

import com.xiaodajia.framework.entity.BaseEntity;

public class WxParamDTO extends BaseEntity {
	private String appId;
	private String secret;
	private String partner;
	private String partnerkey;
	private String gateurl;
	private String tradeType;
	private String tradeNative;
	private String signType;
	private String notifyUrl;
	private String wxPackage;
	private String extStr1;

	public WxParamDTO() {

	}

	public WxParamDTO(String appId, String secret, String partner,
			String partnerkey, String gateurl, String tradeType,
			String tradeNative, String signType, String notifyUrl,
			String wxPackage) {
		super();
		this.appId = appId;
		this.secret = secret;
		this.partner = partner;
		this.partnerkey = partnerkey;
		this.gateurl = gateurl;
		this.tradeType = tradeType;
		this.tradeNative = tradeNative;
		this.signType = signType;
		this.notifyUrl = notifyUrl;
		this.wxPackage = wxPackage;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getPartnerkey() {
		return partnerkey;
	}

	public void setPartnerkey(String partnerkey) {
		this.partnerkey = partnerkey;
	}

	public String getGateurl() {
		return gateurl;
	}

	public void setGateurl(String gateurl) {
		this.gateurl = gateurl;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeNative() {
		return tradeNative;
	}

	public void setTradeNative(String tradeNative) {
		this.tradeNative = tradeNative;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getWxPackage() {
		return wxPackage;
	}

	public void setWxPackage(String wxPackage) {
		this.wxPackage = wxPackage;
	}

	public String getExtStr1() {
		return extStr1;
	}

	public void setExtStr1(String extStr1) {
		this.extStr1 = extStr1;
	}

}
