package com.yszx.module.pay.config;

/**
 * 
 * 类描述：系统常量
 *
 */
public class WxPayConfig {

	/** 微信充值相关参数 */
	public static String wx_tokenurl = "https://api.weixin.qq.com/cgi-bin/token";// 获取access_token对应的url
	public static String wx_appid = "wx20115e3e6d20551c";
	public static String wx_appsecret = "";
	public static String wx_appkey = "";

	public static String wx_partner = "1274914701";
	public static String wx_partnerkey = "wanjiahulianxiaodajiawanjiahulia";

	public static String wx_gateurl = "https://api.mch.weixin.qq.com/pay/unifiedorder";// 获取预支付id的接口url
	public static String wx_package = "Sign=WXPay";
	public static String wx_paybody = "支付";
	public static String wx_tradetype = "APP";
	public static String wx_tradenative = "NATIVE";

}
