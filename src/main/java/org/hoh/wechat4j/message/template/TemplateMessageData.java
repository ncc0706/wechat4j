/**
 * 
 */
package org.hoh.wechat4j.message.template;

/**
 * 
* @ClassName: TemplateMessageData 
* @Description: TODO(模板消息数据实体类) 
* @author derrick_hoh@163.com
* @date 2016年7月29日 上午9:28:53 
*
 */
public class TemplateMessageData {
	private String name; //json中的数据名称
	private String value; //keynote value
	private String color; //data keynote color

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
