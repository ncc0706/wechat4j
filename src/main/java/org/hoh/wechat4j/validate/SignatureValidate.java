package org.hoh.wechat4j.validate;

import java.util.Arrays;

import org.hoh.wechat4j.utils.DecriptUtils;

/**
 * 
* @ClassName: SignatureValidate 
* @Description: TODO(微信开发服务器配置验证) 
* @author derrick_hoh@163.com
* @date 2016年7月27日 上午10:15:20 
*
 */
public class SignatureValidate { 
	private String signature;
	private String timestamp;
	private String nonce;
	private String token;

	/**
	 * 验证构造
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param token
	 */
	public SignatureValidate(String signature, String timestamp, String nonce, String token) {
		this.signature = signature;
		this.timestamp = timestamp;
		this.nonce = nonce;
		this.token = token;
	}

	/**
	 * 验证
	 * @param token
	 * @return true 验证通过，false 验证失败
	 */
	public boolean check() {
		String sha1 = encode(); 
		return sha1.equals(this.signature);
	}

	/**
	 * 得到加密后的数据
	 * @return
	 */
	private String encode() {
		String[] sa = { this.token, this.timestamp, this.nonce };
		Arrays.sort(sa);
		String sortStr = sa[0] + sa[1] + sa[2];
		return DecriptUtils.encode(sortStr);
	}
}
