package org.hoh.wechat4j.oauth;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hoh.wechat4j.constants.Config;
import org.hoh.wechat4j.exception.OAuthException;
import org.hoh.wechat4j.request.AccessTokenRequest;
import org.hoh.wechat4j.request.RefreshAccessTokenRequest;
import org.hoh.wechat4j.request.UserInfoRequest;
import org.hoh.wechat4j.request.ValidAccessTokenRequest;
import org.hoh.wechat4j.response.AccessTokenResponse;
import org.hoh.wechat4j.response.RefreshAccessTokenResponse;
import org.hoh.wechat4j.response.UserInfoResponse;
import org.hoh.wechat4j.response.ValidAccessTokenResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 网页授权获取用户基本信息
 * <p>参考<a href="http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html">开发文档</a></p>
 * Created by xuwen on 2015/12/11.
 */
public class OAuthManager {

	private static Logger logger = Logger.getLogger(OAuthManager.class);

	/* 生成OAuth重定向URI（用户同意授权，获取code） */
	private static final String HTTPS_OPEN_WEIXIN_QQ_COM_CONNECT_OAUTH2_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize";
	/* 通过code换取网页授权access_token */
	private static final String HTTPS_API_WEIXIN_QQ_COM_SNS_OAUTH2_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
	/* 刷新access_token（如果需要） */
	private static final String HTTPS_API_WEIXIN_QQ_COM_SNS_OAUTH2_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	/* 拉取用户信息(需scope为 snsapi_userinfo) */
	private static final String HTTPS_API_WEIXIN_QQ_COM_SNS_USERINFO = "https://api.weixin.qq.com/sns/userinfo";
	/* 检验授权凭证（access_token）是否有效 */
	private static final String HTTPS_API_WEIXIN_QQ_COM_SNS_AUTH = "https://api.weixin.qq.com/sns/auth";

	/**
	 * 
	* @Title: generateRedirectURI 
	* @Description: TODO(生成OAuth重定向URI（用户同意授权，获取code）) 
	* @param @param redirectURI
	* @param @param scope
	* @param @param state
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String generateRedirectURI(String redirectURI, String scope, String state) {
		StringBuffer url = new StringBuffer();
		url.append(HTTPS_OPEN_WEIXIN_QQ_COM_CONNECT_OAUTH2_AUTHORIZE);
		url.append("?appid=").append(urlEncode(Config.instance().getAppid()));
		url.append("&redirect_uri=").append(urlEncode(redirectURI));
		url.append("&response_type=code");
		url.append("&scope=").append(urlEncode(scope));
		url.append("&state=").append(urlEncode(state));
		url.append("#wechat_redirect");
		return url.toString();
	}

	/**
	 * 
	* @Title: getAccessToken 
	* @Description: TODO(通过code换取网页授权access_token) 
	* @param @param request
	* @param @return
	* @param @throws OAuthException    设定文件 
	* @return AccessTokenResponse    返回类型 
	* @throws
	 */
	public static AccessTokenResponse getAccessToken(AccessTokenRequest request) throws OAuthException {
		String response = post(HTTPS_API_WEIXIN_QQ_COM_SNS_OAUTH2_ACCESS_TOKEN, request);
		check(response);
		return JSONObject.parseObject(response, AccessTokenResponse.class);
	}

	/**
	 * 
	* @Title: refreshAccessToken 
	* @Description: TODO(刷新access_token（如果需要）) 
	* @param @param request
	* @param @return
	* @param @throws OAuthException    设定文件 
	* @return RefreshAccessTokenResponse    返回类型 
	* @throws
	 */
	public static RefreshAccessTokenResponse refreshAccessToken(RefreshAccessTokenRequest request) throws OAuthException {
		String response = post(HTTPS_API_WEIXIN_QQ_COM_SNS_OAUTH2_REFRESH_TOKEN, request);
		check(response);
		return JSONObject.parseObject(response, RefreshAccessTokenResponse.class);
	}

	/**
	 * 
	* @Title: getUserinfo 
	* @Description: TODO(拉取用户信息(需scope为 snsapi_userinfo)) 
	* @param @param request
	* @param @return
	* @param @throws OAuthException    设定文件 
	* @return UserinfoResponse    返回类型 
	* @throws
	 */
	public static UserInfoResponse getUserinfo(UserInfoRequest request) throws OAuthException {
		String response = post(HTTPS_API_WEIXIN_QQ_COM_SNS_USERINFO, request);
		check(response);
		return JSONObject.parseObject(response, UserInfoResponse.class);
	}

	/**
	 * 检验授权凭证（access_token）是否有效
	 * <p>参考<a href="http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html#.E9.99.84.EF.BC.9A.E6.A3.80.E9.AA.8C.E6.8E.88.E6.9D.83.E5.87.AD.E8.AF.81.EF.BC.88access_token.EF.BC.89.E6.98.AF.E5.90.A6.E6.9C.89.E6.95.88">开发文档</a></p>
	 *
	 * @param request
	 * @return
	 */
	public static ValidAccessTokenResponse validAccessToken(ValidAccessTokenRequest request) throws OAuthException {
		String response = post(HTTPS_API_WEIXIN_QQ_COM_SNS_AUTH, request);
		check(response);
		return JSONObject.parseObject(response, ValidAccessTokenResponse.class);
	}

	/**
	 * 使用UTF-8进行URL编码
	 *
	 * @param str
	 * @return
	 */
	private static String urlEncode(String str) {
		String result = null;
		try {
			result = URLEncoder.encode(str, Consts.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			logger.error("[URLEncode IS FAIL] encode is fail." + e.getMessage());
		}
		return result;
	}

	/**
	 * 
	* @Title: check 
	* @Description: TODO(检查响应结果是否正确) 
	* @param @param response
	* @param @throws OAuthException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private static void check(String response) throws OAuthException {
		JSONObject exception = JSON.parseObject(response);
		String errcode = exception.getString("errcode");
		String errmsg = exception.getString("errmsg");
		if (errmsg != null && !"ok".equals(errmsg)) {
			throw new OAuthException(errcode, errmsg);
		}
	}

	/**
	 * 
	* @Title: post 
	* @Description: TODO(请求) 
	* @param @param url
	* @param @param data
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private static String post(String url, Object data) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (data != null) {
			Field[] fields = data.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = null;
				try {
					value = field.get(data);
				} catch (IllegalAccessException e) {
					logger.error("[POST Field is fail.]" + e.getMessage());
				}
				if (value != null) {
					params.add(new BasicNameValuePair(field.getName(), value.toString()));
				}
			}
		}
		try {
			HttpEntity entity = Request.Post(url).bodyForm(params.toArray(new NameValuePair[params.size()])).execute().returnResponse().getEntity();
			return entity != null ? EntityUtils.toString(entity, Consts.UTF_8) : null;
		} catch (Exception e) {
			logger.error("post请求异常，" + e.getMessage() + "\n post url:" + url);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	* @Title: post 
	* @Description: TODO(请求) 
	* @param @param url
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@SuppressWarnings("unused")
	private static String post(String url) {
		return post(url, null);
	}

}
