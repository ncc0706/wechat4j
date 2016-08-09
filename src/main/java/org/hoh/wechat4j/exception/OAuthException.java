package org.hoh.wechat4j.exception;

/**
 * 
* @ClassName: OAuthException 
* @Description: TODO(OAuth API 调用异常) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午5:35:11 
*
 */
public class OAuthException extends Exception {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = 1469992334325557669L;

	public OAuthException() {
	}

	public OAuthException(String errcode, String errmsg) {
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	private String errcode;
	private String errmsg;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	@Override
	public String toString() {
		return "OAuthException{" + "errcode='" + errcode + '\'' + ", errmsg='" + errmsg + '\'' + '}';
	}
}
