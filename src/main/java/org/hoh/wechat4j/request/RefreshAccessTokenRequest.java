package org.hoh.wechat4j.request;

import org.hoh.wechat4j.constants.Config;

/**
 * 
* @ClassName: RefreshAccessTokenRequest 
* @Description: TODO(请求：刷新access_token（如果需要）) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午5:39:53 
*
 */
public class RefreshAccessTokenRequest {

	public RefreshAccessTokenRequest(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	private String appid = Config.instance().getAppid();
	private String grant_type = "refresh_token";
	private String refresh_token;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@Override
	public String toString() {
		return "RefreshAccessTokenRequest{" + "appid='" + appid + '\'' + ", grant_type='" + grant_type + '\'' + ", refresh_token='"
				+ refresh_token + '\'' + '}';
	}
}
