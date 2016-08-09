package org.hoh.wechat4j.pay;

import javax.xml.bind.annotation.XmlElement;

import org.hoh.wechat4j.constants.Config;

/**
 * 
* @ClassName: H5PayParam 
* @Description: TODO(H5调起支付API的参数对象) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午5:48:06 
*
 */
public class H5PayParam {
	private String appid = Config.instance().getAppid();
	private String timeStamp;
	private String nonceStr;
	private String packageWithPrepayId; // 参数名package
	private String signType = "MD5";
	private String paySign;

	@XmlElement(name = "appId")
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	@XmlElement(name = "package")
	public String getPackageWithPrepayId() {
		return packageWithPrepayId;
	}

	public void setPackageWithPrepayId(String packageWithPrepayId) {
		this.packageWithPrepayId = packageWithPrepayId;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	@Override
	public String toString() {
		return "H5PayConfig{" + "appid='" + appid + '\'' + ", timeStamp='" + timeStamp + '\'' + ", nonceStr='" + nonceStr + '\''
				+ ", packageWithPrepayId='" + packageWithPrepayId + '\'' + ", signType='" + signType + '\'' + ", paySign='" + paySign + '\''
				+ '}';
	}
}
