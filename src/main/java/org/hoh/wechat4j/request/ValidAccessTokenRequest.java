package org.hoh.wechat4j.request;

/**
 * 
* @ClassName: ValidAccessTokenRequest 
* @Description: TODO(请求：检验授权凭证（access_token）是否有效) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午5:42:31 
*
 */
public class ValidAccessTokenRequest {

	public ValidAccessTokenRequest(String access_token, String openid) {
		this.access_token = access_token;
		this.openid = openid;
	}

	private String access_token;
	private String openid;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Override
	public String toString() {
		return "ValidAccessTokenRequest{" + "access_token='" + access_token + '\'' + ", openid='" + openid + '\'' + '}';
	}
}
