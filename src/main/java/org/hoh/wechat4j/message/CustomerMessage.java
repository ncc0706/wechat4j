package org.hoh.wechat4j.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hoh.wechat4j.enums.MsgType;
import org.hoh.wechat4j.response.ArticleResponse;
import org.hoh.wechat4j.response.MusicResponse;
import org.hoh.wechat4j.response.VideoResponse;
import org.hoh.wechat4j.token.AccessTokenProxy;
import org.sword.lang.HttpUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
* @ClassName: CustomerMessage 
* @Description: TODO(发送客服消息) 
* @author derrick_hoh@163.com
* @date 2016年7月29日 上午9:29:16 
*
 */
public class CustomerMessage {
	private static Logger logger = Logger.getLogger(CustomerMessage.class);

	private static final String MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
	private String toUserOpenId; //接受者
	private String msgType;      //消息类i系那个
	private String msgBody;      //发送的消息POST数据

	/**
	 * 
	* <p>Title: </p> 
	* <p>需要主动去刷新access_token,不建议使用建议自己去获取access_token保存，并定时刷新然后使用SendMsg(String toUserOpenId,String accessToken)来替代本方法</p> 
	* @param toUserOpenId
	 */
	public CustomerMessage(String toUserOpenId) {
		this.toUserOpenId = toUserOpenId;
	}

	/**
	 * 
	* @Title: send 
	* @Description: TODO(发送客服消息) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void send() {
		String accessToken = AccessTokenProxy.getAccessToken().getAccess_token();
		if (StringUtils.isBlank(this.toUserOpenId))
			return;
		if (StringUtils.isBlank(accessToken)) {
			logger.error("[SEND FAIL] send fail could not get access token");
			return;
		}

		if (StringUtils.isNotBlank(accessToken)) {
			String url = MSG_URL + accessToken;
			HttpUtils.post(url, msgBody);
		}
	}

	/**
	 * 
	* @Title: sendText 
	* @Description: TODO(发送文本消息) 
	* @param @param content    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendText(String content) {
		this.msgType = MsgType.text.name();
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("content", content);
		JSONObject json = new JSONObject();
		json.put("touser", this.toUserOpenId);
		json.put("msgtype", this.msgType);
		json.put("text", jsonMsg);
		this.msgBody = json.toJSONString();
		send();
	}

	/**
	 * 
	* @Title: sendImage 
	* @Description: TODO(发送图片消息) 
	* @param @param mediaId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendImage(String mediaId) {
		this.msgType = MsgType.image.name();
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("media_id", mediaId);
		JSONObject json = new JSONObject();
		json.put("touser", this.toUserOpenId);
		json.put("msgtype", this.msgType);
		json.put("image", jsonMsg);
		this.msgBody = json.toJSONString();
		send();
	}

	/**
	 * 
	* @Title: sendVoice 
	* @Description: TODO(发送语音消息) 
	* @param @param mediaId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendVoice(String mediaId) {
		this.msgType = MsgType.voice.name();
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("media_id", mediaId);
		JSONObject json = new JSONObject();
		json.put("touser", this.toUserOpenId);
		json.put("msgtype", this.msgType);
		json.put("voice", jsonMsg);
		this.msgBody = json.toJSONString();
		send();
	}

	/**
	 * 
	* @Title: sendVideo 
	* @Description: TODO(发送视频消息) 
	* @param @param title
	* @param @param description
	* @param @param mediaId
	* @param @param thumbMediaId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendVideo(String title, String description, String mediaId, String thumbMediaId) {
		VideoResponse video = new VideoResponse();
		video.setTitle(title);
		video.setDescription(description);
		video.setMediaId(thumbMediaId);
		video.setThumbMediaId(thumbMediaId);
		sendVideo(video);
	}

	/**
	 * 
	* @Title: sendVideo 
	* @Description: TODO(发送视频消息) 
	* @param @param video    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendVideo(VideoResponse video) {
		this.msgType = MsgType.video.name();
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("media_id", video.getMediaId());
		jsonMsg.put("thumb_media_id", video.getThumbMediaId());
		jsonMsg.put("title", video.getTitle());
		jsonMsg.put("description", video.getDescription());
		JSONObject json = new JSONObject();
		json.put("touser", this.toUserOpenId);
		json.put("msgtype", this.msgType);
		json.put("video", jsonMsg);
		this.msgBody = json.toJSONString();
		send();
	}

	/**
	 * 
	* @Title: sendMusic 
	* @Description: TODO(发送音乐消息) 
	* @param @param title
	* @param @param description
	* @param @param musicURL
	* @param @param hQMusicUrl
	* @param @param thumbMediaId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendMusic(String title, String description, String musicURL, String hQMusicUrl, String thumbMediaId) {
		MusicResponse music = new MusicResponse();
		music.setTitle(title);
		music.setDescription(description);
		music.setMusicURL(musicURL);
		music.setHQMusicUrl(hQMusicUrl);
		music.setThumbMediaId(thumbMediaId);
		sendMusic(music);
	}

	/**
	 * 
	* @Title: sendMusic 
	* @Description: TODO(发送音乐消息) 
	* @param @param music    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendMusic(MusicResponse music) {
		this.msgType = MsgType.music.name();
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("title", music.getTitle());
		jsonMsg.put("description", music.getDescription());
		jsonMsg.put("musicurl", music.getMusicURL());
		jsonMsg.put("hqmusicurl", music.getHQMusicUrl());
		jsonMsg.put("thumb_media_id", music.getThumbMediaId());
		JSONObject json = new JSONObject();
		json.put("touser", this.toUserOpenId);
		json.put("msgtype", this.msgType);
		json.put("music", jsonMsg);
		this.msgBody = json.toJSONString();
		send();
	}

	/**
	 * 
	* @Title: sendNew 
	* @Description: TODO(发送图文消息，单条图文消息) 
	* @param @param title 图文消息标题
	* @param @param description  图文消息描述
	* @param @param picUrl   图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	* @param @param url    点击图文消息跳转链接
	* @return void    返回类型 
	* @throws
	 */
	public void sendNew(String title, String description, String picUrl, String url) {
		ArticleResponse item = new ArticleResponse();
		item.setTitle(title);
		item.setDescription(description);
		item.setPicUrl(picUrl);
		item.setUrl(url);
		sendNews(item);
	}

	/**
	 * 
	* @Title: sendNews 
	* @Description: TODO(发送图文消息，单条图文消息) 
	* @param @param item    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendNews(ArticleResponse item) {
		List<ArticleResponse> items = new ArrayList<ArticleResponse>();
		items.add(item);
		sendNews(items);
	}

	/**
	 * 
	* @Title: sendNews 
	* @Description: TODO(发送图文消息) 
	* @param @param items    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendNews(List<ArticleResponse> items) {
		this.msgType = MsgType.news.name();
		JSONArray jsonArray = new JSONArray();
		for (ArticleResponse item : items) {
			JSONObject jsonItem = new JSONObject();
			jsonItem.put("title", item.getTitle());
			jsonItem.put("description", item.getDescription());
			jsonItem.put("url", item.getUrl());
			jsonItem.put("picurl", item.getPicUrl());
			jsonArray.add(jsonItem);
		}
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("articles", jsonArray);
		JSONObject json = new JSONObject();
		json.put("touser", this.toUserOpenId);
		json.put("msgtype", this.msgType);
		json.put("news", jsonMsg);
		this.msgBody = json.toJSONString();
		send();

	}

}
