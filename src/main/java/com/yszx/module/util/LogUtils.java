package com.yszx.module.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类描述：日志常用类
 * 
 */
public class LogUtils {
	static Logger monitorLogger = LoggerFactory.getLogger("monitor");

	public static void log(String msg) {
		monitorLogger.info(msg);
	}
	
	public static void log(Object repairNumber ,String msg) {
		monitorLogger.info("【："+repairNumber +"；" + msg);
	}

	public static void info(String msg, Object arg1, Object arg2) {
		monitorLogger.info(msg, arg1, arg2);
	}

	public static void info(String msg, Object... arguments) {
		monitorLogger.info( msg, arguments);
	}

}
