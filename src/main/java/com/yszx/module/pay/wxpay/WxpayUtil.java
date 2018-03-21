package com.yszx.module.pay.wxpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

import org.apache.commons.httpclient.NameValuePair;

import com.yszx.module.pay.config.WxPayConfig;
import com.yszx.module.pay.util.MD5Util;
import com.yszx.module.pay.util.XMLUtil;
import com.yszx.module.util.LogUtils;

public class WxpayUtil {

	public static String key = "wxpay_access_token";

	public static String convertStreamToString(InputStream is) {
		StringBuilder sb1 = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;

		try {
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, "UTF-8");
				sb1.append(str);
			}
		} catch (IOException e) {
			// TODO add log
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO add log
			}
		}
		return sb1.toString();
	}

	public static String PayPost(String urlStr, String dataparam) {
		String requestResult = "";
		BufferedReader br = null;
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter out = new OutputStreamWriter(
					con.getOutputStream());
			String xmlInfo = dataparam;
			LogUtils.log("【请求微信预支付接口】 --- urlStr：" + urlStr + ";xmlInfo"
					+ xmlInfo);
			out.write(new String(xmlInfo.getBytes("UTF-8")));
			out.flush();
			out.close();
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				requestResult = requestResult + line;
			}
		} catch (Exception e) {
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return requestResult;
	}

	/**
	 * 
	 * @param orderid
	 *            商家唯一订单号 对应out_trade_no
	 * @param price
	 *            支付金额 对应total_fee
	 * @param notifyurl
	 *            接收微信支付异步通知回调地址
	 * @param pars
	 * @return
	 * @throws Exception
	 */
	public static List<NameValuePair> getPayParams(String orderid,
			String price, String notifyurl, Map<String, Object> pars)
			throws Exception {
		List<NameValuePair> temp = null;
		String prepayid = "";

		String times = WXUtil.getTimeStamp();
		// 随机字符串，不长于32位
		String noncestr = WXUtil.getNonceStr();

		String localip = "10.9.20.1"; // 默认ip，如果取不到就采用默认的，防止空值；
		try {
			InetAddress addr = InetAddress.getLocalHost();
			localip = addr.getHostAddress().toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 对参数进行加密；
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("appid", WxPayConfig.wx_appid);
		sParaTemp.put("mch_id", WxPayConfig.wx_partner);
		sParaTemp.put("nonce_str", noncestr);
		sParaTemp.put("body", WxPayConfig.wx_paybody);
		sParaTemp.put("out_trade_no", orderid);
		sParaTemp.put("total_fee", price);
		sParaTemp.put("spbill_create_ip", localip);
		sParaTemp.put("notify_url", notifyurl);
		sParaTemp.put("trade_type", WxPayConfig.wx_tradetype);

		String sign = wxSign(sParaTemp);
		LogUtils.log("【微信预支付交易单】 --- sign：" + sign);
		sParaTemp.put("sign", sign);
		// 开始组装参数xml
		String requestxml = getRequestXml(sParaTemp);
		LogUtils.log("【微信预支付交易单】 --- requestxml：" + requestxml);

		String requestUrl = WxPayConfig.wx_gateurl;

		String resp = PayPost(requestUrl, requestxml);
		LogUtils.log("【微信预支付交易单】 --- 微信预支付id回执：" + resp);

		Map<Object, Object> respMap = XMLUtil.doXMLParse(resp);
		LogUtils.log("【微信预支付交易单】 --- 微信预支付id回执Map数据：" + respMap.toString());

		String respresut = String.valueOf(respMap.get("result_code"));
		if ("SUCCESS".equals(respresut)) {
			// TODO 严谨处理，增加成功判断
			prepayid = (String.valueOf(respMap.get("prepay_id")));
			LogUtils.log("【微信预支付交易单】 --- prepayid：" + prepayid);

			temp = new LinkedList<NameValuePair>();
			temp.add(new NameValuePair("appid", WxPayConfig.wx_appid));
			temp.add(new NameValuePair("package", WxPayConfig.wx_package));
			temp.add(new NameValuePair("partnerid", WxPayConfig.wx_partner));
			temp.add(new NameValuePair("prepayid", prepayid));
			temp.add(new NameValuePair("noncestr", noncestr));
			temp.add(new NameValuePair("timestamp", times));
			String resultsign = wxSign(temp);
			temp.add(new NameValuePair("sign", resultsign));
			temp.remove(0);
			temp.remove(0);
			temp.add(new NameValuePair("out_trade_no", orderid));
		}

		return temp;
	}

	/**
	 * 生成微信预支付订单
	 * 
	 * @param orderId
	 *            商家唯一订单号 对应out_trade_no
	 * @param price
	 *            支付金额 对应total_fee
	 * @param notifyUrl
	 *            接收微信支付异步通知回调地址
	 * @param hostIp
	 *            终端IP
	 * @param params
	 *            其他参数
	 * @return Map 预支付结果 -- code 操作状态（成功：SUCCESS 失败：FAIL） -- codeMsg 操作文案 --
	 *         prepayId 预支付交易会话标识（重要，返回APP端发起微信支付） -- noncestr 微信返回的随机字符串 --
	 *         timestamp 操作时间戳 -- codeUrl 二维码链接
	 * @throws Exception
	 */
	public static Map<String, Object> genPayParams(String orderId,
			String price, String notifyUrl, String body, String hostIp,
			Map<String, Object> params) throws Exception {
		return genPayParams(orderId, price, notifyUrl, body, hostIp, null,
				null, null, params);
	}

	/**
	 * 生成微信预支付订单
	 * 
	 * @param orderId
	 *            商家唯一订单号 对应out_trade_no
	 * @param price
	 *            支付金额 对应total_fee
	 * @param notifyUrl
	 *            接收微信支付异步通知回调地址
	 * @param body
	 *            商品描述
	 * @param hostIp
	 *            终端IP
	 * @param deviceInfo
	 *            设备号
	 * @param detail
	 *            商品详情
	 * @param attach
	 *            附加数据
	 * @param params
	 *            其他参数
	 * @return Map 预支付结果 -- code 操作状态（成功：SUCCESS 失败：FAIL） -- codeMsg 操作文案 --
	 *         prepayId 预支付交易会话标识（重要，返回APP端发起微信支付） -- noncestr 微信返回的随机字符串 --
	 *         timestamp 操作时间戳 -- codeUrl 二维码链接
	 * @throws Exception
	 */
	public static Map<String, Object> genPayParams(String orderId,
			String price, String notifyUrl, String body, String hostIp,
			String deviceInfo, String detail, String attach,
			Map<String, Object> params) throws Exception {
		return genPayParams(orderId, price, notifyUrl, body, hostIp, null,
				null, deviceInfo, detail, attach, null, null, null, null,
				params);

	}

	/**
	 * 生成微信预支付订单
	 * 
	 * @param orderId
	 *            商家唯一订单号 对应out_trade_no
	 * @param price
	 *            支付金额 对应total_fee
	 * @param notifyUrl
	 *            接收微信支付异步通知回调地址
	 * @param body
	 *            终端IP
	 * @param hostIp
	 *            终端IP
	 * @param tradeType
	 *            交易类型
	 * @param productId
	 *            商品ID
	 * @param params
	 *            其他参数
	 * @return Map 预支付结果 -- code 操作状态（成功：SUCCESS 失败：FAIL） -- codeMsg 操作文案 --
	 *         prepayId 预支付交易会话标识（重要，返回APP端发起微信支付） -- noncestr 微信返回的随机字符串 --
	 *         timestamp 操作时间戳 -- codeUrl 二维码链接
	 * @throws Exception
	 */
	public static Map<String, Object> genPayParams(String orderId,
			String price, String notifyUrl, String body, String hostIp,
			String tradeType, String productId, Map<String, Object> params)
			throws Exception {
		return genPayParams(orderId, price, notifyUrl, body, hostIp, tradeType,
				productId, null, null, null, null, null, null, null, params);
	}

	/**
	 * 生成微信预支付订单
	 * 
	 * @param orderId
	 *            商家唯一订单号 对应out_trade_no
	 * @param price
	 *            支付金额 对应total_fee
	 * @param notifyUrl
	 *            接收微信支付异步通知回调地址
	 * @param body
	 *            商品描述
	 * @param hostIp
	 *            终端IP
	 * @param tradeType
	 *            交易类型
	 * @param productId
	 *            商品ID
	 * @param deviceInfo
	 *            设备号
	 * @param detail
	 *            商品详情
	 * @param attach
	 *            附加数据
	 * @param feeType
	 *            货币类型
	 * @param goodsTag
	 *            订单优惠标记
	 * @param timeStart
	 *            交易起始时间
	 * @param timeExpire
	 *            交易结束时间
	 * @param params
	 *            其他参数
	 * @return Map 预支付结果 -- code 操作状态（成功：SUCCESS 失败：FAIL） -- codeMsg 操作文案 --
	 *         prepayId 预支付交易会话标识（重要，返回APP端发起微信支付） -- noncestr 微信返回的随机字符串 --
	 *         timestamp 操作时间戳 -- codeUrl 二维码链接
	 * @throws Exception
	 */
	public static Map<String, Object> genPayParams(String orderId,
			String price, String notifyUrl, String body, String hostIp,
			String tradeType, String productId, String deviceInfo,
			String detail, String attach, String feeType, String goodsTag,
			String timeStart, String timeExpire, Map<String, Object> params)
			throws Exception {
		Map<String, Object> wxPayResult = new HashMap<String, Object>();
		wxPayResult.put("code", "FAIL");
		// 随机字符串，不长于32位
		String noncestr = WXUtil.getNonceStr();

		if (hostIp == null || "".equals(hostIp)) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				hostIp = addr.getHostAddress().toString();
			} catch (Exception e) {
			}
		}
		try {
			// 对参数进行加密；
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("appid", WxPayConfig.wx_appid);
			sParaTemp.put("mch_id", WxPayConfig.wx_partner);
			sParaTemp.put("out_trade_no", orderId);
			sParaTemp.put("total_fee", price);
			sParaTemp.put("nonce_str", noncestr);
			if (null == body) {
				body = WxPayConfig.wx_paybody;
			}
			sParaTemp.put("body", body);
			sParaTemp.put("spbill_create_ip", hostIp);
			sParaTemp.put("notify_url", notifyUrl);
			if (null == tradeType) {
				tradeType = WxPayConfig.wx_tradetype;
			} else {
				sParaTemp.put("product_id", productId);
			}
			sParaTemp.put("trade_type", tradeType);

			String sign = wxSign(sParaTemp);
			LogUtils.log("【微信预支付交易单】 --- sign：" + sign);
			sParaTemp.put("sign", sign);
			// 开始组装参数xml
			String requestxml = getRequestXml(sParaTemp);
			LogUtils.log("【微信预支付交易单】 --- requestxml：" + requestxml);

			String requestUrl = WxPayConfig.wx_gateurl;

			String resp = PayPost(requestUrl, requestxml);
			LogUtils.log("【微信预支付交易单】 --- 微信预支付id回执：" + resp);

			Map<Object, Object> respMap = XMLUtil.doXMLParse(resp);
			LogUtils.log("【微信预支付交易单】 --- 微信预支付id回执Map数据：" + respMap.toString());

			String returnCode = String.valueOf(respMap.get("return_code"));

			if ("SUCCESS".equals(returnCode)) {
				wxPayResult.put("noncestr", noncestr);
				wxPayResult.put("timestamp", WXUtil.getTimeStamp());
				String resultCode = String.valueOf(respMap.get("result_code"));
				if ("SUCCESS".equals(resultCode)) {
					// 返回trade_type、prepay_id
					String prepayid = (String.valueOf(respMap.get("prepay_id")));
					LogUtils.log("【微信预支付交易单】 --- prepayid：" + prepayid);
					wxPayResult.put("code", "SUCCESS");
					wxPayResult.put("prepayId", prepayid);
					wxPayResult.put("tradeType", respMap.get("trade_type"));
					wxPayResult.put("codeUrl", respMap.get("code_url"));

				} else {
					String errCode = respMap.get("err_code").toString();
					String errCodeDes = respMap.get("err_code_des").toString();
					LogUtils.log("【微信预支付交易单-失败】 --- 错误代码：" + errCode + ";描述："
							+ errCodeDes);
					wxPayResult.put("codeMsg", errCodeDes);
				}
			} else {
				LogUtils.log("【微信预支付交易单-失败】 --- 预支付交易失败，原因："
						+ respMap.get("return_msg"));
				wxPayResult.put("codeMsg", respMap.get("return_msg"));
			}
		} catch (Exception e) {
			LogUtils.log("【微信预支付交易单-失败】 --- 系统异常！");
			wxPayResult.put("codeMsg", "系统异常！");
		}
		return wxPayResult;
	}

	public static String createSign(String characterEncoding,
			SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + WxPayConfig.wx_partnerkey);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding)
				.toUpperCase();
		return sign;
	}

	public static String wxDatatoString(String wxString) {
		String result = "";
		try {
			result = wxString.substring(8, wxString.length() - 2);
			// TODO add log
			// LogService.log("微信支付结果------"+result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	/**
	 * 将请求参数转换为xml格式的string
	 * 
	 * @param parameters
	 *            请求参数
	 * @return 转换成xml格式的string
	 */
	public static String getRequestXml(Map<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append("<" + k + ">" + v + "</" + k + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public static String wxSign(List<NameValuePair> linkdata) {
		String sResult = "";
		Map<String, String> sParaTemp = new HashMap<String, String>();
		for (int i = 0; i < linkdata.size(); i++) {
			sParaTemp
					.put(linkdata.get(i).getName(), linkdata.get(i).getValue());
			sResult = wxSign(sParaTemp);
		}
		return sResult;
	}

	public static String wxSign(Map<String, String> sParaTemp) {

		String sResult = "";
		try {
			if (sParaTemp == null || sParaTemp.size() == 0) {
				throw new Exception();
			}

			ArrayList<String> mKeyArray = new ArrayList<String>();
			for (String key : sParaTemp.keySet()) {
				mKeyArray.add(key);
			}
			Collections.sort(mKeyArray);
			StringBuilder mDataBuilder = new StringBuilder();
			for (int i = 0; i < mKeyArray.size(); i++) {
				mDataBuilder.append(mKeyArray.get(i) + "=")
						.append(sParaTemp.get(mKeyArray.get(i))).append("&");
			}
			mDataBuilder.append("key=" + WxPayConfig.wx_partnerkey);
			return MD5Util.MD5Encode(mDataBuilder.toString(), "UTF-8")
					.toUpperCase();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sResult;
	}

	public static String ArrayToXml(Map<String, String> arr) {
		String xml = "<xml>";
		Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			if (val.matches("\\d *")) {
				xml += "<" + key + ">" + val + "</" + key + ">";
			} else
				xml += "<" + key + "><![CDATA[" + val + "]]></" + key + ">";
		}
		xml += "</xml>";
		return xml;
	}

	/**
	 * 将传入的map中的value全部urlencode
	 * 
	 * @param param
	 */
	public static Map<String, String> urlencodeValue(Map<String, String> param) {
		Map<String, String> ret = new HashMap<String, String>();
		try {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				ret.put(entry.getKey(),
						URLEncoder.encode(entry.getValue(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 将map转变为url请求参数，即：aaa=1&bbb=2的格式
	 * 
	 * @param params
	 * @return
	 */
	public static String getURLParamByMap(Map<String, String> params) {
		Set<String> keys = params.keySet();
		StringBuilder sb = new StringBuilder();
		for (String k : keys) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append("&");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static void main(String[] args) throws NumberFormatException,
			Exception {
		// List<NameValuePair> list =
		// WxpayUtil.getPayParams(System.currentTimeMillis()+"",
		// String.valueOf((long) (Double.parseDouble("0.01") *
		// 100)),SystemConfig.wxNotifyUrl, null);
		// for (NameValuePair nameValuePair : list) {
		// System.out.println(nameValuePair.getName()+"-->"+nameValuePair.getValue());
		// }
		// // JSONObject object = (JSONObject) JSONObject.toJSON(list);
		// // System.out.println(object);
	}
}
