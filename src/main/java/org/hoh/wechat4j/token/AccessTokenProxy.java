package org.hoh.wechat4j.token;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hoh.wechat4j.constants.Config;
import org.sword.lang.HttpUtils;

import com.alibaba.fastjson.JSONObject;
/**
 * 
* @ClassName: AccessTokenProxy 
* @Description: TODO(获取AccessToken) 
* @author derrick_hoh@163.com
* @date 2016年8月4日 上午11:50:59 
*
 */
public class AccessTokenProxy {

	private static Logger logger = Logger.getLogger(AccessTokenProxy.class);
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

	public static String AccessTokenURL() {
		String appid = Config.instance().getAppid();
		String appsecret = Config.instance().getAppSecret();
		String url = ACCESS_TOKEN_URL + "&appid=" + appid + "&secret=" + appsecret; 
		return url;
	}

	public static AccessToken getAccessToken() {
		AccessToken accessToken = new AccessToken();
		String result = HttpUtils.get(AccessTokenURL());
		JSONObject jsonObject = JSONObject.parseObject(result);
		String access_token = jsonObject.get("access_token").toString();
		String expires_in = jsonObject.get("expires_in").toString();
		accessToken.setAccess_token(access_token);
		accessToken.setExpires_in(expires_in);
		if (StringUtils.isBlank(access_token)) {
			logger.error("[GET TOKEN FAIL]get access_token fail" + access_token);
		}
		if (StringUtils.isBlank(expires_in)) {
			logger.error("[GET EXPIRES FAIL]get expires_in fail" + access_token);
		}
		return accessToken;
	}

	public static String getTicket() { 
		return null;
	}

}
