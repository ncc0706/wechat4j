/**
 * 
 */
package org.hoh.wechat4j.request;

import javax.xml.bind.annotation.XmlElement;
/**
 * 
* @ClassName: ScanCodeInfo 
* @Description: TODO(扫描信息实体类) 
* @author derrick_hoh@163.com
* @date 2016年7月27日 上午10:18:51 
*
 */
public class ScanCodeInfo {
	private String ScanType;   //扫描类型，一般是qrcode
	private String ScanResult; //扫描结果，即二维码对应的字符串信息
	
	@XmlElement(name="ScanType")
	public String getScanType() {
		return ScanType;
	}
	public void setScanType(String scanType) {
		ScanType = scanType;
	}
	@XmlElement(name="ScanResult")
	public String getScanResult() {
		return ScanResult;
	}
	public void setScanResult(String scanResult) {
		ScanResult = scanResult;
	}
	
	
}
