package org.hoh.wechat4j.request;

import org.hoh.wechat4j.constants.Config;
/**
 * 
* @ClassName: GetAccessTokenRequest 
* @Description: TODO(请求：通过code换取网页授权access_token) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午5:37:23 
*
 */
public class AccessTokenRequest {

    public AccessTokenRequest(String code) {
        this.code = code;
    }

    private String appid = Config.instance().getAppid();
    private String secret = Config.instance().getAppSecret();
    private String code;
    private String grant_type = "authorization_code";

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
}
