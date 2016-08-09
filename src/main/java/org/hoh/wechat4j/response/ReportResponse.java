package org.hoh.wechat4j.response;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
* @ClassName: ReportResponse 
* @Description: TODO(测速上报请求对象) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午5:57:59 
*
 */
@XmlRootElement(name = "xml")
public class ReportResponse {

    private String result_code;

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    @Override
    public String toString() {
        return "ReportResponse{" +
                "result_code='" + result_code + '\'' +
                '}';
    }
}
