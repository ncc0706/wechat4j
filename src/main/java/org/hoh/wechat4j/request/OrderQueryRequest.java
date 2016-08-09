package org.hoh.wechat4j.request;

import javax.xml.bind.annotation.XmlRootElement;

import org.hoh.wechat4j.constants.Config;
/**
 * 
* @ClassName: OrderQueryRequest 
* @Description: TODO(查询订单请求对象) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午5:50:46 
*
 */
@XmlRootElement(name = "xml")
public class OrderQueryRequest {

    private String appid = Config.instance().getAppid();
    private String mch_id = Config.instance().getMchId();
    private String transaction_id;
    private String out_trade_no;
    private String nonce_str;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "OrderqueryRequest{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
