package com.yszx.module.core.dto;

import com.xiaodajia.framework.entity.BaseEntity;

public class WxParamDTO extends BaseEntity {
	private String appId;
	private String appkey;
	private String appSecret;
	private String merchant;
	private String merchantkey;
	private String gateurl;
	private String tradeType;
	private String tradeNative;
	private String signType;
	private String notifyUrl;
	private String wxPackage;
	private String extStr1;

	private String openId;

	private String subAppid;
	private String subMerchantId;
	private String spBillCreateIp;// 终端IP

	public WxParamDTO() {

	}

	public WxParamDTO(String appId, String appkey, String appSecret,
			String merchant, String merchantkey, String gateurl,
			String tradeType, String tradeNative, String signType,
			String notifyUrl, String wxPackage, String extStr1) {
		super();
		this.appId = appId;
		this.appkey = appkey;
		this.appSecret = appSecret;
		this.merchant = merchant;
		this.merchantkey = merchantkey;
		this.gateurl = gateurl;
		this.tradeType = tradeType;
		this.tradeNative = tradeNative;
		this.signType = signType;
		this.notifyUrl = notifyUrl;
		this.wxPackage = wxPackage;
		this.extStr1 = extStr1;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getMerchantkey() {
		return merchantkey;
	}

	public void setMerchantkey(String merchantkey) {
		this.merchantkey = merchantkey;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSubMerchantId() {
		return subMerchantId;
	}

	public void setSubMerchantId(String subMerchantId) {
		this.subMerchantId = subMerchantId;
	}

	public String getSpBillCreateIp() {
		return spBillCreateIp;
	}

	public void setSpBillCreateIp(String spBillCreateIp) {
		this.spBillCreateIp = spBillCreateIp;
	}

	public String getSubAppid() {
		return subAppid;
	}

	public void setSubAppid(String subAppid) {
		this.subAppid = subAppid;
	}

}
