package org.hoh.wechat4j.response;

/**
 * 
* @ClassName: ValidAccessTokenResponse 
* @Description: TODO(响应：检验授权凭证（access_token）是否有效) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午5:42:49 
*
 */
public class ValidAccessTokenResponse {

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

	public boolean ok() {
		return this.errmsg != null && "ok".equals(this.errmsg);
	}

	@Override
	public String toString() {
		return "ValidAccessTokenResponse{" + "errcode='" + errcode + '\'' + ", errmsg='" + errmsg + '\'' + '}';
	}
}
