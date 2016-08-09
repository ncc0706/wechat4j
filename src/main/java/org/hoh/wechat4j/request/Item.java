package org.hoh.wechat4j.request;

import javax.xml.bind.annotation.XmlElement;
/**
 * 
* @ClassName: Item 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author derrick_hoh@163.com
* @date 2016年7月27日 上午10:18:43 
*
 */
public class Item {
	private String PicMd5Sum;

	@XmlElement(name="PicMd5Sum")
	public String getPicMd5Sum() {
		return PicMd5Sum;
	}
	public void setPicMd5Sum(String picMd5Sum) {
		PicMd5Sum = picMd5Sum;
	}
	
}
