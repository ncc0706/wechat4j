/**
 * 
 */
package org.hoh.wechat4j.request;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 
* @ClassName: SendPicsInfo 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author derrick_hoh@163.com
* @date 2016年7月27日 上午10:19:30 
*
 */
public class SendPicsInfo {
	private String Count;
	private List<Item> item;

	@XmlElement(name = "Count")
	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}

	@XmlElementWrapper(name = "PicList")
	@XmlElement(name = "item")
	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> item) {
		this.item = item;
	}

}
