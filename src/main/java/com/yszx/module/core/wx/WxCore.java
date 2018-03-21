package com.yszx.module.core.wx;

import org.json.JSONObject;

import com.xiaodajia.framework.entity.ResultMsg;
import com.xiaodajia.framework.util.date.DateTime;
import com.xiaodajia.framework.util.web.http.HttpURLConnectionUtils;
import com.yszx.module.util.SimpleCacheUtil;

public class WxCore {
	/**
	 * 获取微信的access_token
	 * 
	 * @param appId
	 * @param secret
	 * @return
	 */
	public static String getWxAccessToken(String appId, String secret) {
		// Object accessTokenKeyValue =
		// EhcacheUtil.getInstance().get("WxAccessToken",
		// access_token_key);
		Object accessTokenKeyValue = SimpleCacheUtil.getInstance().get(
				"WxAccessToken");
		if (null != accessTokenKeyValue) {
			System.out.println("access_token_key缓存获取成功："
					+ accessTokenKeyValue.toString() + ";时间："
					+ DateTime.getNowTime());
			return accessTokenKeyValue.toString();
		} else {
			String access_token = "";
			ResultMsg result = HttpURLConnectionUtils.sendGet(
					"https://api.weixin.qq.com/cgi-bin/token",
					"grant_type=client_credential&appid=" + appId + "&secret="
							+ secret);
			if (null != result && "0".equals(result.getCode())) {
				JSONObject jsStr = new JSONObject(result.getAction());
				if (null != jsStr) {
					access_token = jsStr.getString("access_token");
					SimpleCacheUtil.getInstance().put("WxAccessToken",
							access_token, 3600);
				}
			}
			return access_token;
		}
	}
}
