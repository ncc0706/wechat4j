package org.hoh.wechat4j.user;

import java.util.List;

/**
 * 
* @ClassName: Data 
* @Description: TODO(关注者列表实体类) 
* @author derrick_hoh@163.com
* @date 2016年7月29日 上午10:02:03 
*
 */
public class Data {
	/**
	 * 用户列表
	 */
	private List<String> openid;

	public List<String> getOpenid() {
		return openid;
	}

	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}
}