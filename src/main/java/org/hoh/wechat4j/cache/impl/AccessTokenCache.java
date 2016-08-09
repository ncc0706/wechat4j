package org.hoh.wechat4j.cache.impl;

import org.hoh.wechat4j.cache.EhcacheHandler;
import org.hoh.wechat4j.token.AccessTokenProxy;

public class AccessTokenCache {
	private static class SingletonHolder {
		private static AccessTokenCache instance = new AccessTokenCache();
	}

	public static final AccessTokenCache getInstance() {
		return SingletonHolder.instance;
	}

	public void set(String key, Object value) {
		if (value == null) {
			return;
		}
		EhcacheHandler.getInstance().set("expiresCache", key, value);
	}

	public void remove(String key) {
		EhcacheHandler.getInstance().delete("expiresCache", key);
	}

	public String getAccessToken() throws Exception {
		Object object = EhcacheHandler.getInstance().get("expiresCache", "access_token");
		if (object != null) {
			if ((object instanceof String)) {
				return (String) object;
			}
			return null;
		}
		String token = AccessTokenProxy.getAccessToken().getAccess_token();
		if (token != null) {
			EhcacheHandler.getInstance().set("expiresCache", "access_token", token);
		}
		return token;
	}
}
