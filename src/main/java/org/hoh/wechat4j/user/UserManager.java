package org.hoh.wechat4j.user;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hoh.wechat4j.cache.impl.AccessTokenCache;
import org.hoh.wechat4j.enums.LanguageType;
import org.hoh.wechat4j.exception.WeChatException;
import org.hoh.wechat4j.utils.WeChatUtil;
import org.sword.lang.HttpUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
* @ClassName: UserManager 
* @Description: TODO(用户管理) 
* @author derrick_hoh@163.com
* @date 2016年7月29日 上午9:36:32 
*
 */
public class UserManager {

	Logger logger = Logger.getLogger(UserManager.class);
	//获取用户列表
	private static final String USRE_GET_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=";
	//设置用户备注名
	private static final String USER_UPDATE_REMARK_POST_URL = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=";
	//获取用户基本信息
	private static final String USER_INFO_GET_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=";
	//创建分组
	private static final String GROUP_CREATE_POST_URL = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=";
	//查询所有分组
	private static final String GROUP_GET_POST_URL = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=";
	//查询用户所在分组
	private static final String GROUP_GETID_POST_URL = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=";
	//修改分组名
	private static final String GROUP_UPDATE_POST_URL = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=";
	//移动用户分组
	private static final String GROUP_MEMBERS_UPDATE_POST_URL = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=";
	//批量移动用户分组
	private static final String GROUP_MEMBERS_DATCHUPDATE_POST_URL = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=";
	//删除分组
	private static final String GROUP_DELETE_POST_URL = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=";

	/**
	 * 获取所有的关注者列表
	 * @return
	 * @throws Exception 
	 */
	public List<String> allSubscriber() throws Exception {
		Follwers follwers = subscriberList();
		String nextOpenId = follwers.getNextOpenid();
		while (StringUtils.isNotBlank(nextOpenId)) {
			Follwers f = subscriberList(nextOpenId);
			nextOpenId = f.getNextOpenid();
			if (f.getData() != null) {
				follwers.getData().getOpenid().addAll(f.getData().getOpenid());
			}
		}
		return follwers.getData().getOpenid();
	}

	/**
	 * 获取帐号的关注者列表前10000人
	 * @return
	 * @throws Exception 
	 */
	public Follwers subscriberList() throws Exception {
		return subscriberList(null);
	}

	/**
	 * 获取帐号的关注者列表
	 * @param nextOpenId
	 * @return
	 * @throws Exception 
	 */
	public Follwers subscriberList(String nextOpenId) throws Exception {
		String url = USRE_GET_URL + AccessTokenCache.getInstance().getAccessToken();
		if (StringUtils.isNotBlank(nextOpenId)) {
			url += "&next_openid=" + nextOpenId;
		}
		String resultStr = HttpUtils.get(url);
		logger.info("return data " + resultStr);
		try {
			WeChatUtil.isSuccess(resultStr);
		} catch (WeChatException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return JSONObject.parseObject(resultStr, Follwers.class);
	}

	/**
	 * 设置用户备注名
	 * @param openid 用户openid
	 * @param remark 新的备注名，长度必须小于30字符
	 * @return
	 * @throws Exception 
	 */
	public void updateRemark(String openId, String remark) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("openid", openId);
		jsonObject.put("remark", remark);
		String requestData = jsonObject.toString();
		logger.info("request data " + requestData);
		String resultStr = HttpUtils.post(USER_UPDATE_REMARK_POST_URL + AccessTokenCache.getInstance().getAccessToken(), requestData);
		logger.info("return data " + resultStr);
		WeChatUtil.isSuccess(resultStr);
	}

	/**
	 * 获取用户基本信息
	 * @param openid 普通用户的标识，对当前公众号唯一
	 * @return
	 * @throws Exception 
	 */
	public User getUserInfo(String openId) throws Exception {
		return getUserInfo(openId, null);
	}

	/**
	 * 获取用户基本信息
	 * @param openid 普通用户的标识，对当前公众号唯一
	 * @param lang 返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
	 * @return
	 * @throws Exception 
	 */
	public User getUserInfo(String openId, LanguageType lang) throws Exception {
		String url = USER_INFO_GET_URL + AccessTokenCache.getInstance().getAccessToken() + "&openid=" + openId;
		if (lang != null) {
			url += "&lang=" + lang.name();
		}
		String resultStr = HttpUtils.get(url); 
		try {
			WeChatUtil.isSuccess(resultStr);
		} catch (WeChatException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		User user = JSONObject.parseObject(resultStr, User.class);
		return user;
	}

	/**
	 * 创建分组
	 * @param name  分组名字（30个字符以内）
	 * @return 
	 * @throws Exception 
	 */
	public Group createGroup(String name) throws Exception {
		JSONObject nameJson = new JSONObject();
		JSONObject groupJson = new JSONObject();
		nameJson.put("name", name);
		groupJson.put("group", nameJson);
		String requestData = groupJson.toString();
		logger.info("request data " + requestData);
		String resultStr = HttpUtils.post(GROUP_CREATE_POST_URL + AccessTokenCache.getInstance().getAccessToken(), requestData);
		logger.info("return data " + resultStr);
		WeChatUtil.isSuccess(resultStr);
		return JSONObject.parseObject(resultStr).getObject("group", Group.class);
	}

	/**
	 * @throws Exception 
	 * 
	* @Title: getGroup 
	* @Description: TODO(查询所有分组) 
	* @param @return    设定文件 
	* @return List<Group>    返回类型 
	* @throws
	 */
	public List<Group> getGroup() throws Exception {
		String resultStr = HttpUtils.post(GROUP_GET_POST_URL + AccessTokenCache.getInstance().getAccessToken());
		logger.info("return data " + resultStr);
		try {
			WeChatUtil.isSuccess(resultStr);
		} catch (WeChatException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		JSONObject jsonObject = JSONObject.parseObject(resultStr);
		List<Group> groups = JSON.parseArray(jsonObject.getString("groups"), Group.class);
		return groups;
	}

	/**
	 * @throws Exception 
	 * 
	* @Title: getIdGroup 
	* @Description: TODO(查询用户所在分组) 
	* @param @param openId  用户的OpenID
	* @param @return    设定文件 
	* @return Integer     用户所属的groupid
	* @throws
	 */
	public Integer getIdGroup(String openId) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("openid", openId);

		String requestData = jsonObject.toString();
		logger.info("request data " + requestData);
		String resultStr = HttpUtils.post(GROUP_GETID_POST_URL + AccessTokenCache.getInstance().getAccessToken(), requestData);
		logger.info("return data " + resultStr);
		try {
			WeChatUtil.isSuccess(resultStr);
		} catch (WeChatException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		JSONObject resultJson = JSONObject.parseObject(resultStr);
		int groupId = resultJson.getIntValue("groupid");
		return groupId;
	}

	/**
	 * @throws Exception 
	 * 
	* @Title: updateGroup 
	* @Description: TODO(修改分组名) 
	* @param @param groupId  groupId 分组id
	* @param @param name  分组名称
	* @param @throws WeChatException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void updateGroup(int groupId, String name) throws Exception {
		JSONObject nameJson = new JSONObject();
		JSONObject groupJson = new JSONObject();
		nameJson.put("id", groupId);
		nameJson.put("name", name);
		groupJson.put("group", nameJson);
		String requestData = groupJson.toString();
		logger.info("request data " + requestData);
		String resultStr = HttpUtils.post(GROUP_UPDATE_POST_URL + AccessTokenCache.getInstance().getAccessToken(), requestData);
		logger.info("return data " + resultStr);
		WeChatUtil.isSuccess(resultStr);
	}

	/**
	 * @throws Exception 
	 * 
	* @Title: membersUpdateGroup 
	* @Description: TODO(移动用户分组) 
	* @param @param openId  用户的OpenID
	* @param @param groupId  分组id
	* @param @throws WeChatException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void membersUpdateGroup(String openId, int groupId) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("openid", openId);
		jsonObject.put("to_groupid", groupId);
		String requestData = jsonObject.toString();
		logger.info("request data " + requestData);
		String resultStr = HttpUtils.post(GROUP_MEMBERS_UPDATE_POST_URL + AccessTokenCache.getInstance().getAccessToken(), requestData);
		logger.info("return data " + resultStr);
		WeChatUtil.isSuccess(resultStr);
	}

	/**
	 * @throws Exception 
	 * 
	* @Title: membersDatchUpdateGroup 
	* @Description: TODO(批量移动用户分组) 
	* @param @param openIds  用户唯一标识符openid的列表（size不能超过50）
	* @param @param groupId   分组id
	* @param @throws WeChatException    设定文件 
	* @return void   是否修改成功
	* @throws
	 */
	public void membersDatchUpdateGroup(String[] openIds, int groupId) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("openid_list", openIds);
		jsonObject.put("to_groupid", groupId);
		String requestData = jsonObject.toString();
		logger.info("request data " + requestData);
		String resultStr = HttpUtils.post(GROUP_MEMBERS_DATCHUPDATE_POST_URL + AccessTokenCache.getInstance().getAccessToken(), requestData);
		logger.info("return data " + resultStr);
		WeChatUtil.isSuccess(resultStr);
	}

	/**
	 * @throws Exception 
	 * 
	* @Title: deleteGroup 
	* @Description: TODO(删除分组) 
	* @param @param groupId
	* @param @throws WeChatException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void deleteGroup(int groupId) throws Exception {
		JSONObject idJson = new JSONObject();
		idJson.put("id", groupId);
		JSONObject groupJson = new JSONObject();
		groupJson.put("group", idJson);
		String requestData = groupJson.toJSONString();
		logger.info("request data " + requestData);
		String resultStr = HttpUtils.post(GROUP_DELETE_POST_URL + AccessTokenCache.getInstance().getAccessToken(), requestData);
		logger.info("return data " + resultStr);
		WeChatUtil.isSuccess(resultStr);
	}
}
