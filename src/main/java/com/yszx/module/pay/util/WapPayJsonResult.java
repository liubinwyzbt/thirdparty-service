package com.yszx.module.pay.util;

import java.util.HashMap;
import java.util.Map;



/**
 * WAP支付通知回调之后执行业务逻辑的返回值
 * 
 */
public class WapPayJsonResult {
	public static final int SUCCESS = PayResult.paysucc.getCode();
	public static final int ERROR = PayResult.payerror.getCode();
	private int result;
	private String message;
	private Map<String, Object> parameters = new HashMap<String, Object>();

	public WapPayJsonResult(int result, String message) {
		this.result = result;
		this.message = message;
	}

	public boolean isSuccess() {
		return SUCCESS == result;
	}

	public static WapPayJsonResult paysuccess() {
		return new WapPayJsonResult(PayResult.paysucc.getCode(), PayResult.paysucc.getDesc());
	}

	public static WapPayJsonResult payerror() {
		return new WapPayJsonResult(PayResult.payerror.getCode(),PayResult.payerror.getDesc());
	}
	
	public static WapPayJsonResult dorefund() {
		return new WapPayJsonResult(PayResult.dorefund.getCode(),PayResult.payerror.getDesc());
	}
	
	public void addParameter(String key, Object val) {
		parameters.put(key, val);
	}

	public void addParameters(Map<String, Object> map) {
		parameters.putAll(map);
	}

	public Object getParameter(String key) {
		if (parameters != null)
			return parameters.get(key);
		return null;
	}

	public String getStringValue(String key) {
		return (String) getParameter(key);
	}

	public int getIntValue(String key) {
		return Integer.parseInt(getStringValue(key));
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	public enum PayResult{
		paysucc(0, "支付成功"), 
		payerror(-1, "支付失败"), 
		dorefund(-2, "执行退款");

		private int code;
		private String desc;

		private PayResult(int code, String desc) {
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

}
