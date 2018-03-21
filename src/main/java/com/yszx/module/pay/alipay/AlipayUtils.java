package com.yszx.module.pay.alipay;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

public class AlipayUtils {

//	private static String appId = "2017071607779345";//
//
//	private static String url = "https://openapi.alipaydev.com/gateway.do";
//
//	//商户密钥
//	private static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDeg2JjQJO/tf+b/vhmrUrbdYyjfBrlhSmln1UkquAgIE57MhgDskkDrMTO4Z/rNiQhy5sF8zgvj+wxVVY5FN4+oivQXDUOKz3oqPo0TjZnTKDt9Mu78DZYeqbDvPzltsLXIORFpcf3x4SS1UKQIXD0EDLjMAOyZbsU5qJZJitoasvLnFfty5j2npZhm/x3CyX+FaPWvHMrK3tMNxnLmcrbYbqKB9BcPuweSoEdIm4Qcpk+HzDPdMmCjnKQDxlAkbLS2xo1U4QjxkbAPfxyBnZOmdAj/FpsszFJ0zXJCYyTGP3kozWc2GunpoA9rXkizuXyIaw2IeBXYtzproyROjeVAgMBAAECggEAfhhwYhSPZf3qS2mzNOSRXkqAF4lV6KmMz1m6s1vORQZp0zCxIS8gZ1+57A86Q93bnfc286lANtvVmqNkiFaGF3JpZ9tJCa2BmN5hY9DKzcFEFXU7G1bmRT/ET3pCyp7GTYlLMxjeeFDtlRQbc3gv/W9Mlyh3f9pcqVyFtgWGZdXJA6U5ejtYL5WhXPwGAm98yfEDTApo23aXDBhSyxNR2/12DEnX1VKmMDY5RdcDS1zIXNR3Bs+y70t6YyceRWyI6g0qqbeFux4ZyTgPj+cYzZmEyILnH1hNsL6nzELpz7jlHbGBsHOsj0JTE5bxWIK2Jfcn16oNx/S4uVz9j8/2wQKBgQDvWtcQl+iCdXdn7qO8k1uoiM+HUuKxS3r0HaRgoAsdt0RCMDnEkwVz9YTB00l6zLZPwgO4H9xQsgEa4O23ub4F/nweYM53yQJNXL+sb7lRWSNvaCnVbqhAn16TQgAtO5EYS2Im+DttTHeZeemWqiL7bzLzdfh4khCCByYMVFBCxQKBgQDt/LfQ09Zt76SA/SNBU64PyYILab+uhOC2sp0D758ocr+fEFGwK3JeJV1NAG9OjZ8MejE2iBt7hEqwbWwwImke1EmqYgvGtb2lpKIvclRAy4JefIFUQAUS6rzQm6DIuZfQYLvjY7vOKR6229lpeZoJtGGGvHCyY0BN7Oi/rQEukQKBgAUIYfFFYGwt0B7tU6AJcSnwBn0P7xhPP/yWCvjFJLaxPcwue2h4tuqjRPFWYfslBshSigV7FgTUWVWZWSY9z5MMsMtquaynG1dJFfaoOrtNJoOXKxoDw6HoN/Lctmg9EmXyX9P18DnyG6mseI7kcOvcgLPBzUGrwJEbWV90/mMJAoGBAIDpn/hyh3jNkuJ8PJiqZ2MN2G+uPLKgL8lHP15ecmxKnBnhkR5Iqo7s1nlbfu+iL0Fm/zl2yMxDgOHIpA3+1cppW+37Zj2hPLV5VJE28QSYOGpOLxAgDZ+Nn/GwG4wfXGj3vU8TOywVghmzRTWpNROErj952+IFeVO8vOb3I+jBAoGBAO16apqRbFIhubH3U/Zn4a0K7q60whte6AcbPVcCMPJ22OcERrQRx6A/87bFRYEBY6gKDfm0O02tvAtdjvEbxX/22SiKuUWcGU4YsS6kwfGCN0jF/X3UzY94e/+BrZgIdzcYHttZcXxABcgug6NtjWYICl5XZssxw5WT0KuYdzs/";
//
//	//支付宝公钥
//	private static String alipayPulicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtxLEVwIh/Oarc+xzNBGb3kIp/JOP+4bhQ8gKyoWfgWtCtDDqQzAq8v73MxKlEUydW9TjW1r8zJpZrMd3BtTAabNPoEvaEK8ar2Rg05ELci6BQ6LgsNyaq29Fa2LUA6TlVFTQ0/tKoFlT3GyomU8OUcfFFJ1NTddtQiq0RbYk8KTw862qJo4LvsAUMmkHKf2Uh8phesxkz8sPEnx4KFoZr/nl1Z7ylEg/9fF8OQqgZpr1UD773I3p01YBvZpdIf40xoSPifVAkw1bijtQ7YAGPzZaN5zJpVWPhCTNd2IBddyCFz/Bb5/5NEgZ/Wm1xloSyUNB3ueT54i2sF9uHE5YOQIDAQAB";

//	private static AlipayClient alipayClient = new DefaultAlipayClient(url,appId, privateKey, "json", "UTF-8", alipayPulicKey, "RSA2");

	
	//---------------封装开始-------------
	//正式环境
	private static String ALIPAY_GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
	//测试环境
//	private static String ALIPAY_GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
	
	//支付宝公钥
	public static String ALIPAY_PULICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgTwdH+b5cYJNU4Jdl7nVCDyBp2xlg8B/vL2mX66rlF9Chf9oksLBt6xOLQBmcYAvaDBEskvha/vwFcJRL86PnLYULQxc6PpzL7/HM1E0b43PA+ZE77slK7k7Vh89eLmZ/MRWngkd0Bcb2Cx1Vu5q+lpFVTm7erTOQ49HTpPrYk9w9cL36IokFLcMMSrTwKiqrv18W/IpDcqSLQmYOx7iJO2AWp+PRjITaD7Kfg3oey8UHY4i20ceD2tbRlHCQKy2MnK/jpa6wlVvPhbWi7uhYi9KEtrnEJ5qceV6l61CsAyHYV23g/L/Gcv1Rj/hhfRQkhmYm+O1BUNjtkHk0qcf8wIDAQAB";
    //字符集
	private static String CHAR_SET = "UTF-8";
	//加密方式
	private static String SIGN_TYPE_RSA = "RSA2";
	
	/*
	 * 封装给终端支付宝sdk的字符串
	 * appId：收款商户的appId
	 * privateKey：收款商户的私钥
	 * BodyDesc：商品描述
	 * subject：订单关键字
	 * orderNo：本系统订单流水号
	 * amount：交易金额
	 * myselfServiceParamsMap:自己平台的业务参数,支付宝回调时将会返回
	 * notifyUrl：支付宝回调路径
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static String getAlipayAppPayParams(
			String appId,String privateKey,String BodyDesc,
			String subject,String orderNo,BigDecimal amount,
			Map<String,Object> myselfServiceParamsMap ,String notifyUrl)throws Exception {
		if( StringUtils.isEmpty(appId) ){
			throw new Exception("支付宝的appId不能为空");
		}
		if( StringUtils.isEmpty(privateKey) ){
			throw new Exception("支付宝的privateKey不能为空");
		}
		if( StringUtils.isEmpty(BodyDesc) ){
			throw new Exception("支付宝的BodyDesc不能为空");
		}
		if( StringUtils.isEmpty(subject) ){
			throw new Exception("支付宝的subject不能为空");
		}
		if( amount==null ){
			throw new Exception("支付宝的amount不能为空");
		}
		if( StringUtils.isEmpty(notifyUrl) ){
			throw new Exception("支付宝的notifyUrl不能为空");
		}
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		//支付宝回调时原路返回的参数，必须进行UrlEncode之后才可以发送给支付宝
        model.setPassbackParams(URLEncoder.encode(myselfServiceParamsMap.toString()));
//			// 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
//			model.setBody("我是测试数据");
//			// 商品的标题/交易标题/订单标题/订单关键字等。
//			model.setSubject("App支付测试Java");
//			// 商户网站唯一订单号
//			model.setOutTradeNo("TEST_WJHL_201708011513111");
//			// 设置未付款支付宝交易的超时时间，一旦超时，该笔交易就会自动被关闭。
//			model.setTimeoutExpress("30m");
//			// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
//			model.setTotalAmount("0.01");
        // 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
		model.setBody(BodyDesc);
		// 商品的标题/交易标题/订单标题/订单关键字等。
		model.setSubject(subject);
		// 商户网站唯一订单号
		model.setOutTradeNo(orderNo);
		// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
		model.setTotalAmount(String.valueOf(amount.doubleValue()));
		// 设置未付款支付宝交易的超时时间，一旦超时，该笔交易就会自动被关闭。
		model.setTimeoutExpress("30m");
		// 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		AlipayClient ALIPAY_CLIENT = new DefaultAlipayClient(ALIPAY_GATEWAY_URL,appId, privateKey, "json", CHAR_SET, ALIPAY_PULICKEY, SIGN_TYPE_RSA);
		// 这里和普通的接口调用不同，使用的是sdkExecute
		AlipayTradeAppPayResponse response = ALIPAY_CLIENT.sdkExecute(request);
		// 就是orderString
		// 可以直接给客户端请求，无需再做处理。
		return response.getBody();
	} 
	
	
//	public static String aliWapPay() {
//		try {
//			AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
//			alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
//			alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");// 在公共参数中设置回跳和通知地址
//			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
//			model.setSubject("Iphone6 16G");
//			model.setOutTradeNo("20150320010101002");
//			model.setTotalAmount("88.88");
//			model.setProductCode("QUICK_WAP_WAY");
//			model.setSellerId("2088123456789012");
//			alipayRequest.setBizModel(model);
//
//			alipayRequest.setBizContent("{"
//					+ "    \"out_trade_no\":\"20150320010101002\","
//					+ "    \"total_amount\":88.88,"
//					+ "    \"subject\":\"Iphone6 16G\","
//					+ "    \"seller_id\":\"2088123456789012\","
//					+ "    \"product_code\":\"QUICK_WAP_WAY\"" + "  }");// 填充业务参数
//			return alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//	}

//	public static String getAlipayAppPayParams(String notifyUrl)
//			throws Exception {
//		try {
//			// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
//			AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//			// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//
//			// 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
//			model.setBody("我是测试数据");
//			// 商品的标题/交易标题/订单标题/订单关键字等。
//			model.setSubject("App支付测试Java");
//			// 商户网站唯一订单号
//			model.setOutTradeNo("TEST_WJHL_201708011513111");
//			// 设置未付款支付宝交易的超时时间，一旦超时，该笔交易就会自动被关闭。
//			model.setTimeoutExpress("30m");
//			// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
//			model.setTotalAmount("0.01");
//			// 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
//			model.setProductCode("QUICK_MSECURITY_PAY");
//
//			request.setBizModel(model);
//			request.setNotifyUrl(notifyUrl);
//			// 这里和普通的接口调用不同，使用的是sdkExecute
//			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//			// 就是orderString
//			// 可以直接给客户端请求，无需再做处理。
//			String str = response.getBody();
//			return str;
//
//		} catch (AlipayApiException e) {
//			throw new Exception(e.getErrMsg());
//		}
//	}

//	public static String receiveAlipayAppPayNotify(HttpServletRequest request)
//			throws Exception {
//		try {
//			// 获取支付宝POST过来反馈信息
//			Map<String, String> params = new HashMap<String, String>();
//			Map requestParams = request.getParameterMap();
//			for (Iterator iter = requestParams.keySet().iterator(); iter
//					.hasNext();) {
//				String name = (String) iter.next();
//				String[] values = (String[]) requestParams.get(name);
//				String valueStr = "";
//				for (int i = 0; i < values.length; i++) {
//					valueStr = (i == values.length - 1) ? valueStr + values[i]
//							: valueStr + values[i] + ",";
//				}
//				// 乱码解决，这段代码在出现乱码时使用。
//				// valueStr = new
//				// String(valueStr.getBytes("ISO-8859-1"),"utf-8");
//				params.put(name, valueStr);
//			}
//			// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
//			if (AlipaySignature.rsaCheckV1(params, alipayPulicKey, "GBK","RSA2")) {
//				return "success";
//			} else {
//				return "failure";
//			}
//		} catch (AlipayApiException e) {
//			throw new Exception(e.getErrMsg());
//		}
//	}
	
//	回调参数:key===gmt_create;value===2017-08-09 20:40:29
//	回调参数:key===charset;value===UTF-8
//	回调参数:key===seller_email;value===18911330912@189.cn
//	回调参数:key===subject;value===小钥匙小区一号楼一单元701
//	回调参数:key===sign;value===Sr9RPcnj5QAgd75Od105K2Hk+u9SaBXo8w6VUGB1TZGFA00WXoPC2PLyquG9pfSpLYubcS5lavbEz5u9gdUDRuwXbzt6n/0XsUmWm+w4i6wGktWDgOsQPTIAjYh7YVYWzTUEPHPhi7twsdWYH778fV9IN/WVdjuaQEmTcX/+GGi00+CzmJSMvJlRQ7x887kesVGY2uZNtNOIg6GDMAz2HgrIGxIzbqwNVPqYvcWo0A4EAwXRqDKutPNWu2BsXjPTZfv72NPkxRJQgmSAuw45LhyrZ4qg91ldJGzFtSTxXXoZhXPRuOZ9+4fm/05VEfBSGVk87isD3K+2LfJz8D+cYw==
//	回调参数:key===body;value===小钥匙小区一号楼一单元701
//	回调参数:key===buyer_id;value===2088312068879163
//	回调参数:key===invoice_amount;value===0.01
//	回调参数:key===notify_id;value===6ae67073a5034b864a2f5720ed9d227h8i
//	回调参数:key===fund_bill_list;value===[{"amount":"0.01","fundChannel":"ALIPAYACCOUNT"}]
//	回调参数:key===notify_type;value===trade_status_sync
//	回调参数:key===trade_status;value===TRADE_SUCCESS
//	回调参数:key===receipt_amount;value===0.01
//	回调参数:key===app_id;value===2016103102442683
//	回调参数:key===buyer_pay_amount;value===0.01
//	回调参数:key===sign_type;value===RSA2
//	回调参数:key===seller_id;value===2088521041394445
//	回调参数:key===gmt_payment;value===2017-08-09 20:40:30
//	回调参数:key===notify_time;value===2017-08-09 20:40:30
//	回调参数:key===passback_params;value===%7Bamount%3D0.01%2C+orderNo%3D2017080920402267175961%2C+payListId%3D1502154537174002%2C+couponId%3D1%2C+phoneno%3D15910634998%2C+communityId%3D1%7D
//	回调参数:key===version;value===1.0
//	回调参数:key===out_trade_no;value===2017080920402267175961
//	回调参数:key===total_amount;value===0.01
//	回调参数:key===trade_no;value===2017080921001004160211031466
//	回调参数:key===auth_app_id;value===2016103102442683
//	回调参数:key===buyer_logon_id;value===272***@qq.com
//	回调参数:key===point_amount;value===0.00
    public static void checkSign(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("gmt_create", "2017-08-09 20:40:29");
		params.put("charset", "UTF-8");
		params.put("seller_email", "18911330912@189.cn");
		params.put("subject", "小钥匙小区一号楼一单元701");
		params.put("sign", "Sr9RPcnj5QAgd75Od105K2Hk+u9SaBXo8w6VUGB1TZGFA00WXoPC2PLyquG9pfSpLYubcS5lavbEz5u9gdUDRuwXbzt6n/0XsUmWm+w4i6wGktWDgOsQPTIAjYh7YVYWzTUEPHPhi7twsdWYH778fV9IN/WVdjuaQEmTcX/+GGi00+CzmJSMvJlRQ7x887kesVGY2uZNtNOIg6GDMAz2HgrIGxIzbqwNVPqYvcWo0A4EAwXRqDKutPNWu2BsXjPTZfv72NPkxRJQgmSAuw45LhyrZ4qg91ldJGzFtSTxXXoZhXPRuOZ9+4fm/05VEfBSGVk87isD3K+2LfJz8D+cYw==");
		params.put("body", "小钥匙小区一号楼一单元701");
		params.put("buyer_id", "2088312068879163");
		params.put("invoice_amount", "0.01");
		params.put("notify_id", "6ae67073a5034b864a2f5720ed9d227h8i");
		params.put("fund_bill_list", "[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}]");
		params.put("notify_type", "trade_status_sync");
		params.put("trade_status", "TRADE_SUCCESS");
		params.put("receipt_amount", "0.01");
		params.put("app_id", "2016103102442683");
		params.put("buyer_pay_amount", "0.01");
		params.put("sign_type", "RSA2");
		params.put("seller_id", "2088521041394445");
		params.put("gmt_payment", "2017-08-09 20:40:30");
		params.put("notify_time", "2017-08-09 20:40:30");
		params.put("passback_params", "%7Bamount%3D0.01%2C+orderNo%3D2017080920402267175961%2C+payListId%3D1502154537174002%2C+couponId%3D1%2C+phoneno%3D15910634998%2C+communityId%3D1%7D");
		params.put("version", "1.0");
		params.put("out_trade_no", "2017080920402267175961");
		params.put("total_amount", "0.01");
		params.put("trade_no", "2017080921001004160211031466");
		params.put("auth_app_id", "2016103102442683");
		params.put("buyer_logon_id", "272***@qq.com");
		params.put("point_amount", "0.00");
		try {
			System.out.println(AlipaySignature.rsaCheckV1(params, AlipayUtils.ALIPAY_PULICKEY, "UTF-8","RSA2"));
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) {
		try {
//			 String result = getAlipayAppPayParams("http://wuyetongxin.com:8102/property-manager-web1");
//			 System.out.println(result);
			//智通账号
//			String appId = "2017071607779345";
//			String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDeg2JjQJO/tf+b/vhmrUrbdYyjfBrlhSmln1UkquAgIE57MhgDskkDrMTO4Z/rNiQhy5sF8zgvj+wxVVY5FN4+oivQXDUOKz3oqPo0TjZnTKDt9Mu78DZYeqbDvPzltsLXIORFpcf3x4SS1UKQIXD0EDLjMAOyZbsU5qJZJitoasvLnFfty5j2npZhm/x3CyX+FaPWvHMrK3tMNxnLmcrbYbqKB9BcPuweSoEdIm4Qcpk+HzDPdMmCjnKQDxlAkbLS2xo1U4QjxkbAPfxyBnZOmdAj/FpsszFJ0zXJCYyTGP3kozWc2GunpoA9rXkizuXyIaw2IeBXYtzproyROjeVAgMBAAECggEAfhhwYhSPZf3qS2mzNOSRXkqAF4lV6KmMz1m6s1vORQZp0zCxIS8gZ1+57A86Q93bnfc286lANtvVmqNkiFaGF3JpZ9tJCa2BmN5hY9DKzcFEFXU7G1bmRT/ET3pCyp7GTYlLMxjeeFDtlRQbc3gv/W9Mlyh3f9pcqVyFtgWGZdXJA6U5ejtYL5WhXPwGAm98yfEDTApo23aXDBhSyxNR2/12DEnX1VKmMDY5RdcDS1zIXNR3Bs+y70t6YyceRWyI6g0qqbeFux4ZyTgPj+cYzZmEyILnH1hNsL6nzELpz7jlHbGBsHOsj0JTE5bxWIK2Jfcn16oNx/S4uVz9j8/2wQKBgQDvWtcQl+iCdXdn7qO8k1uoiM+HUuKxS3r0HaRgoAsdt0RCMDnEkwVz9YTB00l6zLZPwgO4H9xQsgEa4O23ub4F/nweYM53yQJNXL+sb7lRWSNvaCnVbqhAn16TQgAtO5EYS2Im+DttTHeZeemWqiL7bzLzdfh4khCCByYMVFBCxQKBgQDt/LfQ09Zt76SA/SNBU64PyYILab+uhOC2sp0D758ocr+fEFGwK3JeJV1NAG9OjZ8MejE2iBt7hEqwbWwwImke1EmqYgvGtb2lpKIvclRAy4JefIFUQAUS6rzQm6DIuZfQYLvjY7vOKR6229lpeZoJtGGGvHCyY0BN7Oi/rQEukQKBgAUIYfFFYGwt0B7tU6AJcSnwBn0P7xhPP/yWCvjFJLaxPcwue2h4tuqjRPFWYfslBshSigV7FgTUWVWZWSY9z5MMsMtquaynG1dJFfaoOrtNJoOXKxoDw6HoN/Lctmg9EmXyX9P18DnyG6mseI7kcOvcgLPBzUGrwJEbWV90/mMJAoGBAIDpn/hyh3jNkuJ8PJiqZ2MN2G+uPLKgL8lHP15ecmxKnBnhkR5Iqo7s1nlbfu+iL0Fm/zl2yMxDgOHIpA3+1cppW+37Zj2hPLV5VJE28QSYOGpOLxAgDZ+Nn/GwG4wfXGj3vU8TOywVghmzRTWpNROErj952+IFeVO8vOb3I+jBAoGBAO16apqRbFIhubH3U/Zn4a0K7q60whte6AcbPVcCMPJ22OcERrQRx6A/87bFRYEBY6gKDfm0O02tvAtdjvEbxX/22SiKuUWcGU4YsS6kwfGCN0jF/X3UzY94e/+BrZgIdzcYHttZcXxABcgug6NtjWYICl5XZssxw5WT0KuYdzs/";
//			//万家互联账号
//			String appId = "2016103102442683";
//			String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC40EYNayT8XkyNWyd7G1ZdiYePwVCvz/MNZeVN4IyfKmSZqGOje9SlNatyR3Y6VhItEGtdcoEFS1RP0tPieruU+AHhWcg44LdNwKh9PCPW9Bpq22H7xmAp2TrdEIwccQ546pyjdyRDRWMtKD47XB0yDzFhcXhNzVqTyUwsxTzITQ2d1F7vsw9az6qVpzFiH2S/a8ZWfSy/QK7YHz2syUhYHHB6I7M76SyappLjSWsAR6m8TXNZT5PLAsBU9IDO7y4nyJnitOAvPBbW7YRnQtJ+VfuFWujdf7RzSCMeoKHsOD7v7+/xS1+w8eKPJzIKIIPD1RqZmAHhzFTkBAr8hJp1AgMBAAECggEBAI5Wwo1VJyBD1HvOZ+L11Beoc8LxJZ6y70QzZV8MgeGYefCqc0sXdVhktI4IofMeFVOl4LsRFHOqZ94ux0gYNN7wACq6xm2gDS2SI3KI3iWQzcPdAWMzjx7MEvzt4KuLG5VfRJLVkRVKnK7L3tpDAK1sbCDbjseFUKygDc4xfrXY6rUiLd4WzbJPoULSxTbqkwaHGKQRi4AhHIsX0AjY0WfN0eqF0o3qjN2WuS3sFQsbL4tXhoduPSVwfCbaECBNMEmIVLZz9w5rBdm8iptJ0IuVBUSixC0K/QwAM6skYhwawcXa/0Gj+4R1GfOIgvQz1WaYpgqo09wc5pyiV/nrzMUCgYEA6TR5zAh1/nRxFWvoo5+PY9j50h/4wwtQoWfqNQ+Lh0sxxjHI0vDrRtGF3QcZgopkz0vTWa3bqwICSRR6iFJq74TUcxtsTDTaUc9rDrE+ad70Kjc2MEKHFjgldQHodBkrTuuaG5sed9rebmXB3FlyvRwLDJZfhBeM4prAyB2E4icCgYEAyuDkXW9P35dRhmBc0x+v7k+6+iVqBCqvpvddPgwsOs0lxgdgTXcTU789ZIe9c9feD+tD6y/O3+AoEt+x5xvGbxljPkrg5wWlYXSfab9Y5AsS/0sMqU9J5gmwZgZScS+SQYh820Q9gT8QhnZxzcmEPrFqU3WccbGtFIqDC9727AMCgYBckFyr1Mvo9RURuKQ9lg7XRf9bHR+jY3Fcr59x8jqiuAEItygMOl5Y3L9yjOfePcpn0aQRW4Xuqbx4f21ngLc7XRDqo37n/K6PfHH1AftREJL/0qxqmW1/L0gG2nwG4Rqkl7WysW51imVxkmhRaxu03lWwaFt/Vc2xAFX22GivTwKBgFGaniC7GlysZ/1uG0hMsZ5g8auWiws8BEVbsGS5zrykMfJymxjtsZd5D/+03c6P4OF0V3BtXYANkPDui4eTU4DyrEmtIsOqMLh/iXOmpOGtVqnbjS85FldyREL1fgnLAcBcXyRvUXIAZOxBDkuhKBFzmXoSj6fnuN/PSyDmQVnzAoGAHBMXHVBFtkoafTMCcF1x6J1HA6HptciI5Ul5Zeo833Obp7lRsQfbkn4gmJ5pHDGvpHJNEpwJvJrTmDmOVu7etfCuT8/27U0IVdGKB1T/6wd0ntkW80d3Z2ougujLcZ+yRRwLKai4qxrsAge301ANwMa1W4ha0EYbum0S/xX1jMI=";
//			//------------
//			String notifyUrl = "http://wuyetongxin.com:8102/property-manager-web1";
//			//测试+商户编号+交易种类+时间戳
//			String orderNo = "TEST_"+"WJHL_"+"WYF_"+DateUtils.getNowNo();
//			String BodyDesc = "交易的具体描述BODY";
//			//订单关键字
//			String subject = "湘林小区_"+"物业费";
//			BigDecimal amount = new  BigDecimal("0.01");
//			Map<String,Object> myselfServiceParamsMap = new HashMap<String,Object>();
//			myselfServiceParamsMap.put("orderNo",orderNo);
//			myselfServiceParamsMap.put("amount",amount.doubleValue()+"");
//			myselfServiceParamsMap.put("merchantId","merchant_123");
//			String str = getAlipayAppPayParams(appId, privateKey, BodyDesc, subject, orderNo, amount, myselfServiceParamsMap, notifyUrl);
//			System.out.println(str);
			
			
			
			checkSign();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
