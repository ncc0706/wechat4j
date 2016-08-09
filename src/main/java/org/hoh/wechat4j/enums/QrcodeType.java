package org.hoh.wechat4j.enums;
/**
 * 
* @ClassName: QrcodeType 
* @Description: TODO(二维码类型) 
* @author derrick_hoh@163.com
* @date 2016年7月29日 上午10:03:17 
*
 */
public enum QrcodeType {
	/** 临时二维码 */
	QR_SCENE,
	/** 永久二维码 */
	QR_LIMIT_SCENE,
	/** 永久的字符串参数值 */
	QR_LIMIT_STR_SCENE;
}
