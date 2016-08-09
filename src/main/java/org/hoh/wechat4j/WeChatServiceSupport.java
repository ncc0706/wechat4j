package org.hoh.wechat4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hoh.wechat4j.enums.EventType;
import org.hoh.wechat4j.enums.MsgType;
import org.hoh.wechat4j.request.WechatRequest;
import org.hoh.wechat4j.response.ArticleResponse;
import org.hoh.wechat4j.response.ImageResponse;
import org.hoh.wechat4j.response.MusicResponse;
import org.hoh.wechat4j.response.TransInfoResponse;
import org.hoh.wechat4j.response.VideoResponse;
import org.hoh.wechat4j.response.VoiceResponse;
import org.hoh.wechat4j.response.WechatResponse;
import org.hoh.wechat4j.utils.JaxbParserUtils;
import org.hoh.wechat4j.utils.JaxbParserUtils.CollectionWrapper;
import org.hoh.wechat4j.utils.StreamUtils;

/**
 * 
* @ClassName: WeChatServiceSupport 
* @Description: TODO(微信开发接口) 
* @author derrick_hoh@163.com
* @date 2016年7月27日 上午10:17:00 
*
 */
public abstract class WeChatServiceSupport{
	private static final Logger logger = Logger.getLogger(WeChatServiceSupport.class);
	private HttpServletRequest request;
	protected WechatRequest wechatRequest;
	protected WechatResponse wechatResponse;

	public WeChatServiceSupport(HttpServletRequest request) {
		super();
		this.request = request;
		this.wechatRequest = new WechatRequest();
		this.wechatResponse = new WechatResponse();
	}

	public String accept()  { 
		String result = dispatcher();
		return result;
	}

	/**
	 * @throws UnsupportedEncodingException 
	 * 
	* @Title: dispatcher 
	* @Description: TODO(分发处理) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private String dispatcher(){
		String requestData = null;
		try {
			requestData = StreamUtils.streamToString(request.getInputStream()); 
		} catch (IOException e) {
			logger.error("[REQUEST DATA FAIL] this request data is failed" + e.getMessage());
			e.printStackTrace();
		}
		resolveXMLData(requestData);
		messageDispatcher();
		String result = response();
		return result;
	}

	/**
	 * @throws UnsupportedEncodingException 
	 * 
	* @Title: response 
	* @Description: TODO(返回响应数据) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private String response() {
		String result = null;
		try {
			JaxbParserUtils requestBinder = new JaxbParserUtils(WechatResponse.class, CollectionWrapper.class);
			result = requestBinder.toXml(wechatResponse, "UTF-8"); 
		} catch (Exception e) {
			logger.error("[JAXBPARSER XML FAIL] JaxbParser xml is fail." + e.getMessage());
			e.printStackTrace();
		}   
		return result;
	}

	/**
	 * 
	* @Title: responseBase 
	* @Description: TODO(响应数据基础构造) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void responseBase() {
		wechatResponse.setToUserName(this.wechatRequest.getFromUserName());
		wechatResponse.setFromUserName(wechatRequest.getToUserName());
		wechatResponse.setCreateTime(wechatRequest.getCreateTime());
	}

	/**
	 * 回复文本消息
	 * @param content 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
	 */
	public void responseText(String content) {
		responseBase();
		wechatResponse.setMsgType(MsgType.text.name());
		wechatResponse.setContent(content);
	}

	/**
	 * 
	* @Title: responseImage 
	* @Description: TODO(回复图片消息) 
	* @param @param mediaId 通过上传多媒体文件，得到的id
	* @return void    返回类型 
	* @throws
	 */
	public void responseImage(String mediaId) {
		responseBase();
		wechatResponse.setMsgType(MsgType.image.name());
		ImageResponse image = new ImageResponse();
		image.setMediaId(mediaId);
		wechatResponse.setImage(image);
	}

	/**
	 * 
	* @Title: responseVoice 
	* @Description: TODO(回复语音消息) 
	* @param @param mediaId  通过上传多媒体文件，得到的id
	* @return void    返回类型 
	* @throws
	 */
	public void responseVoice(String mediaId) {
		responseBase();
		wechatResponse.setMsgType(MsgType.voice.name());
		VoiceResponse voice = new VoiceResponse();
		voice.setMediaId(mediaId);
		wechatResponse.setVoice(voice);
	}

	/**
	 * 
	* @Title: responseVideo 
	* @Description: TODO(回复视频消息) 
	* @param @param mediaId      通过上传多媒体文件，得到的id
	* @param @param title        视频消息的标题
	* @param @param description    视频消息的描述
	* @return void    返回类型 
	* @throws
	 */
	public void responseVideo(String mediaId, String title, String description) {
		VideoResponse video = new VideoResponse();
		video.setMediaId(mediaId);
		video.setTitle(title);
		video.setDescription(description);
		responseVideo(video);
	}

	/**
	 * 
	* @Title: responseVideo 
	* @Description: TODO(回复视频消息) 
	* @param @param video  视频消息
	* @return void    返回类型 
	* @throws
	 */
	public void responseVideo(VideoResponse video) {
		responseBase();
		wechatResponse.setMsgType(MsgType.video.name());
		wechatResponse.setVideo(video);
	}

	/**
	 * 
	* @Title: responseMusic 
	* @Description: TODO(回复音乐消息) 
	* @param @param title   音乐标题
	* @param @param description 音乐描述
	* @param @param musicURL  音乐链接
	* @param @param hQMusicUrl  高质量音乐链接，WIFI环境优先使用该链接播放音乐
	* @param @param thumbMediaId    缩略图的媒体id，通过上传多媒体文件，得到的id
	* @return void    返回类型 
	* @throws
	 */
	public void responseMusic(String title, String description, String musicURL, String hQMusicUrl, String thumbMediaId) {
		MusicResponse music = new MusicResponse();
		music.setTitle(title);
		music.setDescription(description);
		music.setMusicURL(musicURL);
		music.setHQMusicUrl(hQMusicUrl);
		music.setThumbMediaId(thumbMediaId);
		responseMusic(music);
	}

	/**
	 * 
	* @Title: responseMusic 
	* @Description: TODO(回复音乐消息) 
	* @param @param music    音乐消息
	* @return void    返回类型 
	* @throws
	 */
	public void responseMusic(MusicResponse music) {
		responseBase();
		wechatResponse.setMsgType(MsgType.music.name());
		wechatResponse.setMusic(music);
	}

	/**
	 * 
	* @Title: responseNew 
	* @Description: TODO(回复图文消息，单条图文消息) 
	* @param @param title  图文消息标题
	* @param @param description  图文消息描述
	* @param @param picUrl       图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	* @param @param url    点击图文消息跳转链接
	* @return void    返回类型 
	* @throws
	 */
	public void responseNew(String title, String description, String picUrl, String url) {
		ArticleResponse item = new ArticleResponse();
		item.setTitle(title);
		item.setDescription(description);
		item.setPicUrl(picUrl);
		item.setUrl(url);
		responseNews(item);
	}

	/**
	 * 
	* @Title: responseNews 
	* @Description: TODO(回复图文消息单条) 
	* @param @param item    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void responseNews(ArticleResponse item) {
		List<ArticleResponse> items = new ArrayList<ArticleResponse>();
		items.add(item);
		responseNews(items);
	}

	/**
	 * 
	* @Title: responseNews 
	* @Description: TODO(回复图文消息) 
	* @param @param items    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void responseNews(List<ArticleResponse> items) {
		responseBase();
		wechatResponse.setMsgType(MsgType.news.name());
		wechatResponse.setArticleCount(String.valueOf(items.size()));
		wechatResponse.setArticle(items);

	}

	/**
	 * 
	* @Title: responseCustomerService 
	* @Description: TODO(消息转发到多客服) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void responseCustomerService() {
		responseBase();
		wechatResponse.setMsgType(MsgType.transfer_customer_service.name());
	}

	/**
	 * 
	* @Title: responseCustomerService 
	* @Description: TODO(消息转发到指定客服) 
	* @param @param kfAccount    客服账号
	* @return void    返回类型 
	* @throws
	 */
	public void responseCustomerService(String kfAccount) {
		responseBase();
		wechatResponse.setMsgType(MsgType.transfer_customer_service.name());
		wechatResponse.setTransInfo(new TransInfoResponse(kfAccount));

	}

	/**
	 * 
	* @Title: responseCustomerService 
	* @Description: TODO(消息转发到指定客服) 
	* @param @param transInfo    客服
	* @return void    返回类型 
	* @throws
	 */
	public void responseCustomerService(TransInfoResponse transInfo) {
		responseBase();
		wechatResponse.setMsgType(MsgType.transfer_customer_service.name());
		wechatResponse.setTransInfo(transInfo);

	}

	/***
	 * 
	* @Title: resolveData 
	* @Description: TODO(解析接受XML数据) 
	* @param @param requestData    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void resolveXMLData(String xmlString) {
		try {
			JaxbParserUtils resultBinder = new JaxbParserUtils(WechatRequest.class, CollectionWrapper.class);
			this.wechatRequest = resultBinder.fromXml(xmlString);
		} catch (Exception e) {
			logger.error("[PARSE FAIL] this post xml data parse error");
			e.printStackTrace();
		}
	}

	/**
	 * 
	* @Title: messageDispatcher 
	* @Description: TODO(消息分发处理) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void messageDispatcher() {
		if (StringUtils.isBlank(wechatRequest.getMsgType())) {
			logger.info("[MSG TYPE IS NULL] this argument is required; it must not be null");
		}
		MsgType msgType = MsgType.valueOf(wechatRequest.getMsgType()); 
		switch (msgType) {
		case event:
			onEvent();
			break;
		case text:
			onText();
			break;
		case image:
			onImage();
			break;
		case voice:
			onVoice();
			break;
		case video:
			onVideo();
			break;
		case shortvideo:
			onShortVideo();
			break;
		case location:
			onLocation();
			break;
		case link:
			onLink();
			break;
		default:
			onUnknown();
			break;

		}
	}

	/**
	 * 
	* @Title: onEvent 
	* @Description: TODO(事件分发处理) 
	* @param @param msgType    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void onEvent() {
		EventType event = EventType.valueOf(wechatRequest.getEvent());
		switch (event) {
		case CLICK:
			clickHandler();
			break;
		case subscribe:
			subscribeHandler();
			break;
		case unsubscribe:
			unsubscribeHandler();
			break;
		case SCAN:
			scanHandler();
			break;
		case LOCATION:
			locationHandler();
			break;
		case VIEW:
			viewHandler();
			break;
		case TEMPLATESENDJOBFINISH:
			templateMsgCallback();
			break;
		case scancode_push:
			scanCodePush();
			break;
		case scancode_waitmsg:
			scanCodeWaitMsg();
			break;
		case pic_sysphoto:
			picSysPhoto();
			break;
		case pic_photo_or_album:
			picPhotoOrAlbum();
			break;
		case pic_weixin:
			picWeixin();
			break;
		case location_select:
			locationSelect();
			break;
		case kf_create_session:
			kfCreateSession();
			break;
		case kf_close_session:
			kfCloseSession();
			break;
		case kf_switch_session:
			kfSwitchSession();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	* @Title: clickHandler 
	* @Description: TODO(点击事件处理) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void clickHandler();

	/**
	 * 
	* @Title: subscribeHandler 
	* @Description: TODO(关注事件处理) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void subscribeHandler();

	/**
	 * 
	* @Title: unsubscribeHandler 
	* @Description: TODO(取消关注事件处理) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void unsubscribeHandler();

	/**
	 * 
	* @Title: scanHandler 
	* @Description: TODO(扫描事件处理) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void scanHandler();

	/**
	 * 
	* @Title: locationHandler 
	* @Description: TODO(地理位置事件处理) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void locationHandler();

	/**
	 * 
	* @Title: viewHandler 
	* @Description: TODO(跳转链接) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void viewHandler();

	/**
	 * 
	* @Title: onText 
	* @Description: TODO(文本消息处理) 
	* @param     MsgType=text
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void onText();

	/**
	 * 
	* @Title: onImage 
	* @Description: TODO(图像消息) 
	* @param     MsgType=image
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void onImage();

	/**
	 * 
	* @Title: onVoice 
	* @Description: TODO(语音消息) 
	* @param     MsgType=voice
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void onVoice();

	/**
	 * 
	* @Title: onVideo 
	* @Description: TODO(视频消息) 
	* @param     MsgType=video
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void onVideo();

	/**
	 * 
	* @Title: onShortVideo 
	* @Description: TODO(小视频消息) 
	* @param     MsgType=shortvideo
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void onShortVideo();

	/**
	 * 
	* @Title: onLocation 
	* @Description: TODO(地理位置消息) 
	* @param     MsgType=location
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void onLocation();

	/**
	 * 
	* @Title: onLink 
	* @Description: TODO(链接消息) 
	* @param     MsgType=link
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void onLink();

	/**
	 * 
	* @Title: onUnknown 
	* @Description: TODO(未知消息类型的错误处理逻辑，不需要处理则空方法即可) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void onUnknown();
 

	/**
	 * 
	* @Title: templateMsgCallback 
	* @Description: TODO(模板消息发送回调) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void templateMsgCallback();

	/**
	 * 
	* @Title: scanCodePush 
	* @Description: TODO(扫码推事件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void scanCodePush();

	/**
	 * 
	* @Title: scanCodeWaitMsg 
	* @Description: TODO(扫码推事件且弹出“消息接收中”提示框的事件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void scanCodeWaitMsg();

	/**
	 * 
	* @Title: picSysPhoto 
	* @Description: TODO(弹出系统拍照发图的事件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void picSysPhoto();

	/**
	 * 
	* @Title: picPhotoOrAlbum 
	* @Description: TODO(弹出拍照或者相册发图的事件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void picPhotoOrAlbum();

	/**
	 * 
	* @Title: picWeixin 
	* @Description: TODO(扫码推事件且弹出“消息接收中”提示框的事件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void picWeixin();

	/**
	 * 
	* @Title: locationSelect 
	* @Description: TODO(弹出地理位置选择器的事件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void locationSelect();

	/**
	 * 
	* @Title: kfCreateSession 
	* @Description: TODO(客服人员有接入会话) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void kfCreateSession();

	/**
	 * 
	* @Title: kfCloseSession 
	* @Description: TODO(客服人员有关闭会话) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void kfCloseSession();

	/**
	 * 
	* @Title: kfSwitchSession 
	* @Description: TODO(客服人员有转接会话) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	protected abstract void kfSwitchSession();

}
