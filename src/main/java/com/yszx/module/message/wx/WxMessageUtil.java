package com.yszx.module.message.wx;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.xiaodajia.framework.entity.ResultMsg;
import com.xiaodajia.framework.exception.BaseRuntimeWithCodeException;
import com.xiaodajia.framework.exception.ErrorCodelEnum;
import com.xiaodajia.framework.util.string.StringUtils;
import com.xiaodajia.framework.util.web.http.HttpURLConnectionUtils;
import com.yszx.module.core.dto.WxParamDTO;
import com.yszx.module.core.wx.WxCore;
import com.yszx.module.util.LogUtils;

public class WxMessageUtil {

	public static String access_token_key = "access_token_key";

	/**
	 * 同步预警消息签名
	 * 
	 * @param sParaTemp
	 * @return
	 */
	public static void sendWxMessage(WxParamDTO wxParam, String openId,
			String templateId, String url, Map<String, Object> dataParams)
			throws BaseRuntimeWithCodeException {
		try {
			if (StringUtils.isEmpty(openId)) {
				throw new BaseRuntimeWithCodeException("微信用户OPENID为空",
						ErrorCodelEnum.PARAMETER_ERROR.getCode());
			}
			if (StringUtils.isEmpty(templateId)) {
				throw new BaseRuntimeWithCodeException("消息模板为空",
						ErrorCodelEnum.PARAMETER_ERROR.getCode());
			}
			if (dataParams == null || dataParams.size() == 0) {
				throw new BaseRuntimeWithCodeException("消息data为空",
						ErrorCodelEnum.PARAMETER_ERROR.getCode());
			}
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("touser", openId);
			jsonMap.put("template_id", templateId);
			if (!StringUtils.isEmpty(url)) {
				jsonMap.put("url", url);
			}
			jsonMap.put("data", dataParams);
			String josnStr = JSONObject.valueToString(jsonMap);
			String access_token = "";
			access_token = WxCore.getWxAccessToken(wxParam.getAppId(),
					wxParam.getSecret());
			if (StringUtils.isEmpty(access_token)) {
				throw new BaseRuntimeWithCodeException("获取微信TOKEN失败",
						ErrorCodelEnum.PARAMETER_ERROR.getCode());
			}
			ResultMsg sendMsgResult = HttpURLConnectionUtils.sendPost(
					"https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
							+ access_token, josnStr);
			if (null != sendMsgResult && "0".equals(sendMsgResult.getCode())) {
				JSONObject jsStr = new JSONObject(sendMsgResult.getAction());
				if (null != jsStr && 0 == jsStr.getInt("errcode")) {
					System.out.println("发送成功");
					LogUtils.info("发送消息成功： 消息ID：" + jsStr.get("msgid")
							+ ";业务参数：" + josnStr);
				}
			} else {
				LogUtils.info("发送消息失败： 错误编码：" + sendMsgResult.getCode()
						+ ";业务参数：" + josnStr);
			}
		} catch (BaseRuntimeWithCodeException br) {
			throw new BaseRuntimeWithCodeException(br.getMessage(),
					ErrorCodelEnum.ERROR.getCode());
		} catch (Exception e) {
			throw new BaseRuntimeWithCodeException("微信发送消息失败",
					ErrorCodelEnum.ERROR.getCode());
		}
	}
}
