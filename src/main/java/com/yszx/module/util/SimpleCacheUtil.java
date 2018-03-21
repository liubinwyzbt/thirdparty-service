package com.yszx.module.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCacheUtil {
	private SimpleCacheUtil() {
	}

	private static SimpleCacheUtil ehCache;

	public static SimpleCacheUtil getInstance() {
		if (ehCache == null) {
			ehCache = new SimpleCacheUtil();
		}
		return ehCache;
	}

	// 缓存map
	private static Map<String, CacheEntity> cacheMap = new ConcurrentHashMap<String, CacheEntity>();

	// 启动一个线程,定时扫描map,清除过期数据
	private static Thread cleanMapThreadd = new Thread(new CleanMapTask());

	// 缓存类
	private static class CacheEntity {
		private long expireTime;
		private Object o;

		public CacheEntity(Object o, long expireTime) {
			this.o = o;
			this.expireTime = expireTime;
		}

		public long getExpireTime() {
			return expireTime;
		}

		public Object getO() {
			return o;
		}
	}

	// 清理缓存任务
	private static class CleanMapTask implements Runnable {
		public void run() {
			while (true) {
				long currentTime = System.currentTimeMillis();
				for (Map.Entry<String, CacheEntity> entry : cacheMap.entrySet()) {
					CacheEntity entity = entry.getValue();
					long expireTime = entity.getExpireTime();
					if (currentTime > expireTime) {
						cacheMap.remove(entry.getKey());
					}
				}
				try {
					Thread.sleep(10 * 60 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 放入缓存
	 * 
	 * @param key
	 *            缓存键
	 * @param value
	 *            缓存对象
	 * @param expireTime
	 *            缓存时间 单位秒
	 */
	public void put(String key, Object value, long expireTime) {
		cacheMap.put(key, new CacheEntity(value, System.currentTimeMillis()
				+ expireTime * 1000));
		if (!cleanMapThreadd.isAlive()) {
			cleanMapThreadd.start();
		}
	}

	/**
	 * 获取缓存对象
	 * 
	 * @param key
	 *            缓存键
	 * @return 缓存对象
	 */
	public Object get(String key) {
		CacheEntity entity = cacheMap.get(key);
		if (entity != null) {
			long currentTime = System.currentTimeMillis();
			long expireTime = entity.getExpireTime();
			if (currentTime <= expireTime) {
				return entity.getO();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
