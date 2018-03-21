package com.yszx.module.pay.wxpay;

import java.util.Random;

import com.yszx.module.pay.util.MD5ForPay;




public class WXUtil {
	
	public static String getNonceStr() {
		Random random = new Random();
		return MD5ForPay.encode(String.valueOf(random.nextInt(10000)));
	}

	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	public static void main(String[] args) {
		System.out.println(getTimeStamp());
	}
}
