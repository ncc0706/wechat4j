package org.hoh.wechat4j.parameter;

import org.hoh.wechat4j.constants.Config;
/**
 * 
* @ClassName: JsApiParameter 
* @Description: TODO(JS API 配置对象) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午6:04:09 
*
 */
public class JsApiParameter {

    private String appid = Config.instance().getAppid();
    private String url;
    private String jsapiTicket;
    private String nonceStr;
    private String timeStamp;
    private String signature;

    public JsApiParameter(String url, String jsapiTicket, String nonceStr, String timeStamp, String signature) {
        this.url = url;
        this.jsapiTicket = jsapiTicket;
        this.nonceStr = nonceStr;
        this.timeStamp = timeStamp;
        this.signature = signature;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "JsApiConfig{" +
                "appid='" + appid + '\'' +
                ", url='" + url + '\'' +
                ", jsapiTicket='" + jsapiTicket + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
