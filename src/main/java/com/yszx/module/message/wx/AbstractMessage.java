package com.yszx.module.message.wx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaodajia.framework.exception.BaseRuntimeWithCodeException;
import com.xiaodajia.framework.exception.ErrorCodelEnum;
import com.xiaodajia.framework.util.string.StringUtils;
import com.yszx.module.core.dto.WxParamDTO;

/**
 * 消息抽象类
 * 
 * @author liub
 *
 */
public abstract class AbstractMessage {

	protected WxParamDTO param;

	public String templateId;

	public AbstractMessage() {
	}

	public AbstractMessage(WxParamDTO param) {
		this.param = param;
	}
	
	

	public WxParamDTO getParam() {
		return param;
	}

	public void setParam(WxParamDTO param) {
		this.param = param;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * 填充消息数据
	 * 
	 * @param dataParams
	 * @param key
	 * @param value
	 * @param color
	 */
	public static void fillMessageData(Map<String, Object> dataParams,
			String key, String value, String color) {
		if (!StringUtils.isEmpty(value)) {
			Map<String, Object> firstData = new HashMap<String, Object>();
			firstData.put("value", value);
			firstData.put("color", !StringUtils.isEmpty(color) ? color
					: "#173177");
			dataParams.put(key, firstData);
		}
	}

	/**
	 * 发送模板消息
	 * 
	 * @param templateId
	 *            模版ID
	 * @param url
	 * @param dataParams
	 */
	public static void sendAndFillMessageData(WxParamDTO wxParam, String openId,
			String templateId, String url, List<MsgParamDTO> msgParams)
			throws BaseRuntimeWithCodeException {
		if (null == msgParams && msgParams.isEmpty()) {
			throw new BaseRuntimeWithCodeException("消息对象为空",
					ErrorCodelEnum.PARAMETER_ERROR.getCode());
		}
		Map<String, Object> dataParams = new HashMap<String, Object>();
		for (MsgParamDTO msgParam : msgParams) {
			if (msgParam.isValidateStatus()) {
				if (StringUtils.isEmpty(msgParam.getValue())) {
					throw new BaseRuntimeWithCodeException("微信发送消息："
							+ msgParam.getValidateHint(),
							ErrorCodelEnum.PARAMETER_ERROR.getCode());
				}
			}
			fillMessageData(dataParams, msgParam.getKey(), msgParam.getValue(),
					msgParam.getColor());
		}
		WxMessageUtil.sendWxMessage(wxParam,openId ,templateId, url, dataParams);
	}
}
