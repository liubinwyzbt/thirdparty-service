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
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.xiaodajia.framework.exception.BaseRuntimeWithCodeException;
import com.xiaodajia.framework.exception.ErrorCodelEnum;
import com.xiaodajia.framework.util.string.StringUtils;
import com.yszx.module.core.dto.WxParamDTO;
import com.yszx.module.pay.config.WxPayConfig;
import com.yszx.module.pay.util.MD5Util;
import com.yszx.module.pay.util.RequestHandler;
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

	//
	// /**
	// *
	// * @param orderid
	// * 商家唯一订单号 对应out_trade_no
	// * @param price
	// * 支付金额 对应total_fee
	// * @param notifyurl
	// * 接收微信支付异步通知回调地址
	// * @param pars
	// * @return
	// * @throws Exception
	// */
	// public static List<NameValuePair> getPayParams(String orderid,
	// String price, String notifyurl, Map<String, Object> pars)
	// throws Exception {
	// List<NameValuePair> temp = null;
	// String prepayid = "";
	//
	// String times = WXUtil.getTimeStamp();
	// // 随机字符串，不长于32位
	// String noncestr = WXUtil.getNonceStr();
	//
	// String localip = "10.9.20.1"; // 默认ip，如果取不到就采用默认的，防止空值；
	// try {
	// InetAddress addr = InetAddress.getLocalHost();
	// localip = addr.getHostAddress().toString();
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// // 对参数进行加密；
	// Map<String, String> sParaTemp = new HashMap<String, String>();
	// sParaTemp.put("appid", WxPayConfig.wx_appid);
	// sParaTemp.put("mch_id", WxPayConfig.wx_partner);
	// sParaTemp.put("nonce_str", noncestr);
	// sParaTemp.put("body", WxPayConfig.wx_paybody);
	// sParaTemp.put("out_trade_no", orderid);
	// sParaTemp.put("total_fee", price);
	// sParaTemp.put("spbill_create_ip", localip);
	// sParaTemp.put("notify_url", notifyurl);
	// sParaTemp.put("trade_type", WxPayConfig.wx_tradetype);
	//
	// String sign = wxSign(sParaTemp);
	// LogUtils.log("【微信预支付交易单】 --- sign：" + sign);
	// sParaTemp.put("sign", sign);
	// // 开始组装参数xml
	// String requestxml = getRequestXml(sParaTemp);
	// LogUtils.log("【微信预支付交易单】 --- requestxml：" + requestxml);
	//
	// String requestUrl = WxPayConfig.wx_gateurl;
	//
	// String resp = PayPost(requestUrl, requestxml);
	// LogUtils.log("【微信预支付交易单】 --- 微信预支付id回执：" + resp);
	//
	// Map<Object, Object> respMap = XMLUtil.doXMLParse(resp);
	// LogUtils.log("【微信预支付交易单】 --- 微信预支付id回执Map数据：" + respMap.toString());
	//
	// String respresut = String.valueOf(respMap.get("result_code"));
	// if ("SUCCESS".equals(respresut)) {
	// // TODO 严谨处理，增加成功判断
	// prepayid = (String.valueOf(respMap.get("prepay_id")));
	// LogUtils.log("【微信预支付交易单】 --- prepayid：" + prepayid);
	//
	// temp = new LinkedList<NameValuePair>();
	// temp.add(new NameValuePair("appid", WxPayConfig.wx_appid));
	// temp.add(new NameValuePair("package", WxPayConfig.wx_package));
	// temp.add(new NameValuePair("partnerid", WxPayConfig.wx_partner));
	// temp.add(new NameValuePair("prepayid", prepayid));
	// temp.add(new NameValuePair("noncestr", noncestr));
	// temp.add(new NameValuePair("timestamp", times));
	// String resultsign = wxSign(temp);
	// temp.add(new NameValuePair("sign", resultsign));
	// temp.remove(0);
	// temp.remove(0);
	// temp.add(new NameValuePair("out_trade_no", orderid));
	// }
	//
	// return temp;
	// }

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
	 *            其他参数(profitSharing: 是否分账;sceneInfo:场景信息)
	 * @return Map 预支付结果 -- code 操作状态（成功：SUCCESS 失败：FAIL） -- codeMsg 操作文案 --
	 *         prepayId 预支付交易会话标识（重要，返回APP端发起微信支付） -- noncestr 微信返回的随机字符串 --
	 *         timestamp 操作时间戳 -- codeUrl 二维码链接
	 * @throws Exception
	 */
	public static Map<String, Object> genPayParams(WxParamDTO wxParam,
			String orderId, String price, String notifyUrl, String body,
			String hostIp, Map<String, Object> params) throws Exception {
		return genPayParams(wxParam, orderId, price, notifyUrl, body, hostIp,
				null, null, null, params);
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
	 *            其他参数(profitSharing: 是否分账;sceneInfo:场景信息)
	 * @return Map 预支付结果 -- code 操作状态（成功：SUCCESS 失败：FAIL） -- codeMsg 操作文案 --
	 *         prepayId 预支付交易会话标识（重要，返回APP端发起微信支付） -- noncestr 微信返回的随机字符串 --
	 *         timestamp 操作时间戳 -- codeUrl 二维码链接
	 * @throws Exception
	 */
	public static Map<String, Object> genPayParams(WxParamDTO wxParam,
			String orderId, String price, String notifyUrl, String body,
			String hostIp, String deviceInfo, String detail, String attach,
			Map<String, Object> params) throws Exception {
		return genPayParams(wxParam, orderId, price, notifyUrl, body, hostIp,
				null, null, deviceInfo, detail, attach, null, null, null, null,
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
	 *            商品描述
	 * @param hostIp
	 *            终端IP
	 * @param tradeType
	 *            交易类型
	 * @param attach
	 *            附加数据
	 * @param params
	 *            其他参数(profitSharing: 是否分账;sceneInfo:场景信息)
	 * @return Map 预支付结果 -- code 操作状态（成功：SUCCESS 失败：FAIL） -- codeMsg 操作文案 --
	 *         prepayId 预支付交易会话标识（重要，返回APP端发起微信支付） -- noncestr 微信返回的随机字符串 --
	 *         timestamp 操作时间戳 -- codeUrl 二维码链接
	 * @throws Exception
	 */
	public static Map<String, Object> genPayParams(WxParamDTO wxParam,
			String orderId, String price, String notifyUrl, String body,
			String hostIp, String tradeType, String attach,
			Map<String, Object> params) throws Exception {
		return genPayParams(wxParam, orderId, price, notifyUrl, body, hostIp,
				tradeType, null, null, null, attach, null, null, null, null,
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
	public static Map<String, Object> genPayParams(WxParamDTO wxParam,
			String orderId, String price, String notifyUrl, String body,
			String hostIp, String tradeType, String productId,
			String deviceInfo, String detail, String attach, String feeType,
			String goodsTag, String timeStart, String timeExpire,
			Map<String, Object> payParams) throws Exception {
		if (null == wxParam) {
			throw new Exception("wxParam参数为空");
		}
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
			sParaTemp.put("appid", wxParam.getAppId());
			sParaTemp.put("mch_id", wxParam.getMerchant());
			if(StringUtils.isNotEmpty(wxParam.getSubAppid())){
				sParaTemp.put("sub_appid", wxParam.getSubAppid());
			}
			if(StringUtils.isNotEmpty(wxParam.getSubMerchantId())){
				sParaTemp.put("sub_mch_id", wxParam.getSubMerchantId());
			}
			sParaTemp.put("out_trade_no", orderId);
			sParaTemp.put("total_fee", price);
			sParaTemp.put("nonce_str", noncestr);
			if (null == body) {
				body = WxPayConfig.wx_paybody;
			}
			sParaTemp.put("body", body);
			if (!StringUtils.isEmpty(attach)) {
				sParaTemp.put("attach", attach);
			}
			sParaTemp.put("spbill_create_ip", hostIp);
			sParaTemp.put("notify_url", notifyUrl);
			if (null == tradeType) {
				tradeType = WxPayConfig.wx_tradetype;
			} else {
				// sParaTemp.put("product_id", productId);
			}
			sParaTemp.put("trade_type", tradeType);
			if(!StringUtils.isEmpty(wxParam.getOpenId())){
				sParaTemp.put("openid", wxParam.getOpenId()); 
			}
			if(!StringUtils.isEmpty(wxParam.getSubOpenId())){
				sParaTemp.put("sub_openid", wxParam.getSubOpenId()); 
			}
			if(null != payParams){
				if(null != payParams.get("profitSharing") && !"".equals(payParams.get("profitSharing").toString())){
					//是否分账
					sParaTemp.put("profit_sharing", payParams.get("profitSharing").toString()); 
				}
				if(null != payParams.get("sceneInfo") && !"".equals(payParams.get("sceneInfo").toString())){
					//场景信息
					sParaTemp.put("scene_info", payParams.get("sceneInfo").toString()); 
				}
			}

			String sign = wxSign(sParaTemp, wxParam.getMerchantkey());
			LogUtils.log("【微信预支付交易单】 --- sign：" + sign);
			sParaTemp.put("sign", sign);
			// 开始组装参数xml
			String requestxml = getRequestXml(sParaTemp);
			LogUtils.log("【微信预支付交易单】 --- requestxml：" + requestxml);

			String requestUrl = "";
			if (StringUtils.isEmpty(wxParam.getGateurl())) {
				requestUrl = WxPayConfig.wx_gateurl;
			} else {
				requestUrl = wxParam.getGateurl();
			}

			String resp = PayPost (requestUrl, requestxml);
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
					wxPayResult.put("mwebUrl", respMap.get("mweb_url"));
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
	
	/**
	 * @Description：微信支付，企业向个人付款
	 * @param openid 收款人的openID(微信的openID)
	 * @param amount 付款金额
	 * @param desc 付款描述
	 * @param partner_trade_no 订单号(系统业务逻辑用到的订单号)
	 * @return map{state:SUCCESS/FAIL}{payment_no:
	 *         '支付成功后，微信返回的订单号'}{payment_time:'支付成功的时间'}{err_code:'支付失败后，返回的错误代码'}{err_code_des:'支付失败后，返回的错误描
	 *         述 ' }
	 * @throws ParseException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws DocumentException
	 */
	public static Map<String, String> transferToUser(WxParamDTO wxParam, int amount, String desc, 
			String partner_trade_no,HttpServletRequest requesty,HttpServletResponse responsey) throws Exception {
		Map<String, String> map = new HashMap<String, String>(); // 定义一个返回MAP
		if(StringUtils.isEmpty(wxParam.getOpenId())){
			throw new Exception("用户OpenId为空");
		}
		try {
			// 读取配置文件信息，包括微信支付的APPID，商户ID和证书路径
//			InputStream configFile = WeixinDraw.class.getResourceAsStream("weixin.config.xml");
//			SAXReader reader = new SAXReader();
//			Document doc = reader.read(configFile);
//			Element config = doc.getRootElement();

			String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
//			InetAddress ia = InetAddress.getLocalHost();
			String ip = requesty.getRemoteAddr(); // 获取本机IP地址
			String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");// 随机获取UUID
//			String appid = config.elementTextTrim("appKey");// 微信分配的公众账号ID（企业号corpid即为此appId）
//			String mchid = config.elementTextTrim("mchid");// 微信支付分配的商户号
			// 设置支付参数
			SortedMap<String, String> signParams = new TreeMap<String, String>();

			signParams.put("mch_appid", wxParam.getAppId()); // 微信分配的公众账号ID（企业号corpid即为此appId）
			signParams.put("mchid", wxParam.getMerchant());// 微信支付分配的商户号
			signParams.put("nonce_str", uuid); // 随机字符串，不长于32位
			signParams.put("partner_trade_no", partner_trade_no); // 商户订单号，需保持唯一性
			signParams.put("openid", wxParam.getOpenId()); // 商户appid下，某用户的openid
			signParams.put("check_name", "NO_CHECK"); // NO_CHECK：不校验真实姓名
														// FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）
														// OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
			signParams.put("amount", amount+""); // 企业付款金额，单位为分
			signParams.put("desc", desc); // 企业付款操作说明信息。必填。
			signParams.put("spbill_create_ip", ip); // 调用接口的机器Ip地址

			// 生成支付签名，要采用URLENCODER的原始值进行MD5算法！
			
			RequestHandler reqHandler = new RequestHandler(requesty, responsey);
			reqHandler.init( wxParam.getAppId(),  wxParam.getAppSecret(),  wxParam.getMerchantkey());
			String sign = reqHandler.createSign(signParams);
			
//			String sign = "";
//			sign = createSign("UTF-8", signParams);
			// System.out.println(sign);
			String data = "<xml><mch_appid>";
			data += wxParam.getAppId() + "</mch_appid><mchid>"; // APPID
			data += wxParam.getMerchant() + "</mchid><nonce_str>"; // 商户ID
			data += uuid + "</nonce_str><partner_trade_no>"; // 随机字符串
			data += partner_trade_no + "</partner_trade_no><openid>"; // 订单号
			data += wxParam.getOpenId() + "</openid><check_name>NO_CHECK</check_name><amount>"; // 是否强制实名验证
			data += amount + "</amount><desc>"; // 企业付款金额，单位为分
			data += desc + "</desc><spbill_create_ip>"; // 企业付款操作说明信息。必填。
			data += ip + "</spbill_create_ip><sign>";// 调用接口的机器Ip地址
			data += sign + "</sign></xml>";// 签名
			System.out.println(data);
			// 获取证书，发送POST请求；
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			InputStream instream = null;
			try{
				instream = WxpayUtil.class.getResourceAsStream("/apiclient_cert.p12");
				//FileInputStream instream = new FileInputStream(new File("/opt/web/wjhl_h5/cert/apiclient_cert.p12")); // 从配置文件里读取证书的路径信息
//				FileInputStream instream = new FileInputStream(new File(config.elementTextTrim("cert_path"))); // 从配置文件里读取证书的路径信息
				keyStore.load(instream, wxParam.getMerchant().toCharArray());// 证书密码是商户ID
			}catch(Exception e){
				System.out.println("微信零钱发放=" + e.getMessage());
			}finally{
				if(null != instream){
					instream.close();
				}
			}
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, wxParam.getMerchant().toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpPost httpost = new HttpPost(url); //
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept", "*/*");
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpost.addHeader("Host", "api.mch.weixin.qq.com");
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpost.addHeader("Cache-Control", "max-age=0");
			httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();

			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			EntityUtils.consume(entity);
			// 把返回的字符串解释成DOM节点
			Document dom = DocumentHelper.parseText(jsonStr);
			Element root = dom.getRootElement();
			String returnCode = root.element("result_code").getText(); // 获取返回代码
			if (StringUtils.equals(returnCode, "SUCCESS")) { // 判断返回码为成功还是失败
				String payment_no = root.element("payment_no").getText(); // 获取支付流水号
				String payment_time = root.element("payment_time").getText(); // 获取支付时间
				map.put("state", returnCode);
				map.put("payment_no", payment_no);
				map.put("payment_time", payment_time);
				return map;
			} else {
				String err_code = root.element("err_code").getText(); // 获取错误代码
				String err_code_des = root.element("err_code_des").getText();// 获取错误描述
				map.put("state", returnCode);// state
				map.put("err_code", err_code);// err_code
				map.put("err_code_des", err_code_des);// err_code_des
				return map;
			}

		} catch (DocumentException ex) {
			ex.printStackTrace();
			return map;
		} catch (UnrecoverableKeyException ex) {
			ex.printStackTrace();
			return map;
		} catch (KeyManagementException ex) {
			ex.printStackTrace();
			return map;
		} catch (KeyStoreException ex) {
			ex.printStackTrace();
			return map;
		} catch (IOException ex) {
			ex.printStackTrace();
			return map;
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
			return map;
		}
	}

	public static String createSign(String characterEncoding,
			SortedMap<Object, Object> parameters, String merchantkey) {
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
		sb.append("key=" + merchantkey);
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

	public static String wxSign(List<NameValuePair> linkdata, String merchantkey) {
		String sResult = "";
		Map<String, String> sParaTemp = new HashMap<String, String>();
		for (int i = 0; i < linkdata.size(); i++) {
			sParaTemp
					.put(linkdata.get(i).getName(), linkdata.get(i).getValue());
			sResult = wxSign(sParaTemp, merchantkey);
		}
		return sResult;
	}

	public static String wxSign(Map<String, String> sParaTemp,
			String merchantkey) {
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
			mDataBuilder.append("key=" + merchantkey);
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

	public static String getRemoteHost(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
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
