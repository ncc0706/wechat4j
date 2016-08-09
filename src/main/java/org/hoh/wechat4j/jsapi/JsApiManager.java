package org.hoh.wechat4j.jsapi;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.hoh.wechat4j.cache.impl.TicketCache;
import org.hoh.wechat4j.parameter.JsApiParameter;
import org.hoh.wechat4j.utils.RandomStringGenerator;

/**
 * JS SDK 管理类
 * <p>参考<a href="http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html">开发文档</a></p>
 * Created by xuwen on 2015-12-11.
 */
public class JsApiManager {

	private static Logger logger = Logger.getLogger(JsApiManager.class);

	/**
	 * 给需要调用JS SDK的URL签名
	 *
	 * @param url
	 * @return
	 * @throws Exception 
	 */
	public static JsApiParameter signature(String url) throws Exception {
		StringBuffer signatureSource = new StringBuffer();
		String nonceStr = RandomStringGenerator.generate();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String jsapiTicket = TicketCache.getInstance().getTicket();
		signatureSource.append("jsapi_ticket=").append(jsapiTicket);
		signatureSource.append("&noncestr=").append(nonceStr);
		signatureSource.append("&timestamp=").append(timestamp);
		signatureSource.append("&url=").append(url);
		logger.info("sign source : " + signatureSource);
		String signature = DigestUtils.sha1Hex(signatureSource.toString());
		logger.info("sign : " + signature);
		return new JsApiParameter(url, jsapiTicket, nonceStr, timestamp, signature);
	}

}
