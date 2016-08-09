/**
 * 对应接口文档地址
 * http://mp.weixin.qq.com/wiki/17/304c1885ea66dbedf7dc170d84999a9d.html
 */
package org.hoh.wechat4j.message;

import org.apache.log4j.Logger;
import org.hoh.wechat4j.message.template.TemplateMessageBody;
import org.hoh.wechat4j.message.template.TemplateMessageData;
import org.hoh.wechat4j.token.AccessTokenProxy;
import org.sword.lang.HttpUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 模板消息接口
 * @author ChengNing
 * @date   2014年12月24日
 */
public class TemplateMessage {

	private static Logger logger = Logger.getLogger(TemplateMessage.class);

	//设置所属行业接口地址
	public static final String SET_INDUSTRY_URL = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=";
	//添加模板id接口地址
	public static final String GET_TEMPLATE_ID_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=";
	//发送模板消息接口地址
	public static final String SEND_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

	private String accessToken;

	public TemplateMessage() {
		this.accessToken = AccessTokenProxy.getAccessToken().getAccess_token();
	}

	/**
	 * 设置所属行业
	 * 接口说明中没有给出
	 */
	public void setIndustry(String... industrys) {
		String url = SET_INDUSTRY_URL + this.accessToken;
		JSONObject json = new JSONObject();
		for (int i = 0; i < industrys.length; i++) {
			json.put("industry_id" + i, industrys[i]);
		}
		String data = json.toJSONString();
		HttpUtils.post(url, data);
	}

	/**
	 * 
	* @Title: getTemplateId 
	* @Description: TODO(获得模板ID) 
	* @param @param templateIdShort
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getTemplateId(String templateIdShort) {
		logger.info("get template id,short template id is:" + templateIdShort);
		//构造请求数据
		String url = GET_TEMPLATE_ID_URL + this.accessToken;
		JSONObject json = new JSONObject();
		json.put("template_id_short", templateIdShort);
		String data = json.toJSONString();
		String result = HttpUtils.post(url, data);
		logger.info("post result:" + result);
		//解析请求数据
		JSONObject resultJson = JSONObject.parseObject(result);
		if (resultJson.getString("errcode").equals("0"))
			return resultJson.getString("template_id");
		logger.error("get template id error:" + resultJson.getString("errmsg"));
		return null;
	}

	/**
	 * 
	* @Title: send 
	* @Description: TODO(发送模板消息) 
	* @param @param postData
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String send(TemplateMessageBody postData) {
		logger.info("send template message");
		//构造请求数据
		String url = SEND_MSG_URL + this.accessToken;
		JSONObject json = new JSONObject();
		json.put("touser", postData.getTouser());
		json.put("template_id", postData.getTemplateId());
		json.put("url", postData.getUrl());
		json.put("topcolor", postData.getTopcolor());
		JSONObject jsonData = new JSONObject();
		for (TemplateMessageData data : postData.getData()) {
			JSONObject keynote = new JSONObject();
			keynote.put("value", data.getValue());
			keynote.put("color", data.getColor());
			jsonData.put(data.getName(), keynote);
		}
		json.put("data", jsonData);

		String data = json.toJSONString();
		String result = HttpUtils.post(url, data);
		logger.info("post result:" + result);
		//解析请求数据
		JSONObject resultJson = JSONObject.parseObject(result);
		if (resultJson.getString("errcode").equals("0"))
			return resultJson.getString("msgid");

		logger.error("send template message error:" + resultJson.getString("errmsg"));
		return null;
	}

}
