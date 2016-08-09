package org.hoh.wechat4j.user;
/**
 * 
* @ClassName: Group 
* @Description: TODO(分组) 
* @author derrick_hoh@163.com
* @date 2016年7月29日 上午10:02:28 
*
 */
public class Group {
	/**分组id*/
	private Integer id;
	/**分组名字*/
	private String name;
	/**分组内用户数量*/
	private int count;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}
