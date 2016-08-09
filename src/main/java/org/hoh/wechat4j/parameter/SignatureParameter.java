package org.hoh.wechat4j.parameter;

import javax.servlet.http.HttpServletRequest;

/**
 * 
* @ClassName: SignatureParameter 
* @Description: TODO(微信身份认证参数) 
* @author derrick_hoh@163.com
* @date 2016年7月27日 上午11:49:20 
*
 */
public class SignatureParameter { 
	private String signature;
	private String timestamp;
	private String nonce;
	private String echostr;

	public SignatureParameter(HttpServletRequest request) { 
		signature = request.getParameter(WechatParameterName.SIGNATURE);
		timestamp = request.getParameter(WechatParameterName.TIMESTAMP);
		nonce = request.getParameter(WechatParameterName.NONCE);
		echostr = request.getParameter(WechatParameterName.ECHOSTR);
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}
}
