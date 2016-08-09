package org.hoh.wechat4j.response;

import java.util.Arrays;
/**
 * 
* @ClassName: UserinfoResponse 
* @Description: TODO(响应：拉取用户信息(需scope为 snsapi_userinfo)) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午5:41:54 
*
 */
public class UserInfoResponse {

	private String openid;
	private String nickname;
	private String sex;
	private String province;
	private String city;
	private String country;
	private String headimgurl;
	private String[] privilege;
	private String unionid;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String[] getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	@Override
	public String toString() {
		return "GetUserinfoResponse{" + "openid='" + openid + '\'' + ", nickname='" + nickname + '\'' + ", sex='" + sex + '\''
				+ ", province='" + province + '\'' + ", city='" + city + '\'' + ", country='" + country + '\'' + ", headimgurl='"
				+ headimgurl + '\'' + ", privilege=" + Arrays.toString(privilege) + ", unionid='" + unionid + '\'' + '}';
	}
}
