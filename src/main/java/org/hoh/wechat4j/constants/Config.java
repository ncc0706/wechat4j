package org.hoh.wechat4j.constants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
* @ClassName: Config 
* @Description: TODO(微信开发配置文件) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午4:51:01 
*
 */
public class Config {
	private Logger logger = Logger.getLogger(Config.class);
	private Properties properties = null;
	private String url = null;;
	private String token = null;;
	private String encodingAESKey = null;;
	private String appid = null;;
	private String appSecret = null;;
	private String mchId = null;;
	private String mchKey = null;;
	private String accessTokenServer = null;;
	private String jsApiTicketServer = null;;
	private static Config config = new Config();

	public static Config instance() {
		return config;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEncodingAESKey() {
		return encodingAESKey;
	}

	public void setEncodingAESKey(String encodingAESKey) {
		this.encodingAESKey = encodingAESKey;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getMchKey() {
		return mchKey;
	}

	public void setMchKey(String mchKey) {
		this.mchKey = mchKey;
	}

	public String getAccessTokenServer() {
		return accessTokenServer;
	}

	public void setAccessTokenServer(String accessTokenServer) {
		this.accessTokenServer = accessTokenServer;
	}

	public String getJsApiTicketServer() {
		return jsApiTicketServer;
	}

	public void setJsApiTicketServer(String jsApiTicketServer) {
		this.jsApiTicketServer = jsApiTicketServer;
	}

	public Config getConfig() {
		return config;
	}

	private Config() {
		InputStream inputStream = null;
		Properties properties = null;
		try {
			inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties");
			properties = new Properties();
			properties.load(inputStream);

			this.url = properties.getProperty("wechat.url");
			if (StringUtils.isNotBlank(url))
				this.url = this.url.trim();
			this.encodingAESKey = properties.getProperty("wechat.encodingaeskey");
			if (StringUtils.isNotBlank(encodingAESKey))
				this.encodingAESKey = this.encodingAESKey.trim();
			this.token = properties.getProperty("wechat.token");
			if (StringUtils.isNotBlank(token))
				this.token = this.token.trim();
			this.appid = properties.getProperty("wechat.appid");
			if (StringUtils.isNotBlank(appid))
				this.appid = this.appid.trim();
			this.appSecret = properties.getProperty("wechat.appsecret");
			if (StringUtils.isNotBlank(appSecret))
				this.appSecret = this.appSecret.trim();
			this.mchId = properties.getProperty("wechat.mch.id");
			if (StringUtils.isNotBlank(url))
				this.mchId = this.mchId.trim();
			this.mchKey = properties.getProperty("wechat.mch.key");
			if (StringUtils.isNotBlank(mchKey))
				this.mchKey = this.mchKey.trim();
			this.accessTokenServer = properties.getProperty("wechat.accessToken.server.class");
			if (StringUtils.isNotBlank(accessTokenServer))
				this.accessTokenServer = this.accessTokenServer.trim();
			this.jsApiTicketServer = properties.getProperty("wechat.ticket.jsapi.server.class");
			if (StringUtils.isNotBlank(jsApiTicketServer))
				this.jsApiTicketServer = this.jsApiTicketServer.trim();
			inputStream.close();
		} catch (FileNotFoundException e) {
			logger.debug("Properties file config.properties not found.");
			throw new IllegalArgumentException("Properties file config.properties not found.");
		} catch (IOException e) {
			logger.debug("Properties file config.properties can not be loading.");
			throw new IllegalArgumentException("Properties file config.properties can not be loading.");
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

	}

}
