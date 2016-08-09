package org.hoh.wechat4j.exception;

/**
 * 签名异常
 * Created by xuwen on 2015-12-12.
 */
public class SignatureException extends Exception {

    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -584492738503296497L;

	public SignatureException() {
        super("返回结果的签名校验失败，数据可能已经被篡改");
    }

}
