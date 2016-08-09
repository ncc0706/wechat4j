package org.hoh.wechat4j.user;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * 
* @ClassName: Follwers 
* @Description: TODO(关注者集合) 
* @author derrick_hoh@163.com
* @date 2016年7月29日 上午10:02:19 
*
 */
public class Follwers {
	private int total;//	关注该公众账号的总用户数
	private int count;//	拉取的OPENID个数，最大值为10000
	private Data data;//	列表数据，OPENID的列表
	private String nextOpenid;//拉取列表的后一个用户的OPENID
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	@JSONField(name="next_openid")
	public String getNextOpenid() {
		return nextOpenid;
	}
	@JSONField(name="next_openid")
	public void setNextOpenid(String nextOpenid) {
		this.nextOpenid = nextOpenid;
	}
	
}

