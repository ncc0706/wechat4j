package org.hoh.wechat4j.pay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.hoh.wechat4j.constants.Config;
import org.hoh.wechat4j.enums.PayCode;
import org.hoh.wechat4j.exception.PayApiException;
import org.hoh.wechat4j.exception.PayBusinessException;
import org.hoh.wechat4j.request.CloseOrderRequest;
import org.hoh.wechat4j.request.DownloadBillRequest;
import org.hoh.wechat4j.request.OrderQueryRequest;
import org.hoh.wechat4j.request.RefundQueryRequest;
import org.hoh.wechat4j.request.RefundRequest;
import org.hoh.wechat4j.request.ReportRequest;
import org.hoh.wechat4j.request.UnifiedorderRequest;
import org.hoh.wechat4j.response.CloseOrderResponse;
import org.hoh.wechat4j.response.OrderQueryResponse;
import org.hoh.wechat4j.response.PayResultNotifyResponse;
import org.hoh.wechat4j.response.RefundQueryResponse;
import org.hoh.wechat4j.response.RefundResponse;
import org.hoh.wechat4j.response.ReportResponse;
import org.hoh.wechat4j.response.UnifiedorderResponse;
import org.sword.lang.JaxbParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
* @ClassName: PayManager 
* @Description: TODO(支付管理) 
* @author derrick_hoh@163.com
* @date 2016年7月28日 下午6:02:11 
*
 */
public class PayManager {

	private static Logger logger = Logger.getLogger(PayManager.class);

	/**
	 * 统一下单
	 */
	private static final String HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 查询订单
	 */
	private static final String HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
	/**
	 * 关闭订单
	 */
	private static final String HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_CLOSEORDER = "https://api.mch.weixin.qq.com/pay/closeorder";
	/**
	 * 申请退款
	 */
	private static final String HTTPS_API_MCH_WEIXIN_QQ_COM_SECAPI_PAY_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	/**
	 * 查询退款
	 */
	private static final String HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_REFUNDQUERY = "https://api.mch.weixin.qq.com/pay/refundquery";
	/**
	 * 下载对账单
	 */
	private static final String HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_DOWNLOADBILL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	/**
	 * 测速上报
	 */
	private static final String HTTPS_API_MCH_WEIXIN_QQ_COM_PAYITIL_REPORT = "https://api.mch.weixin.qq.com/payitil/report";

	/**
	 * 统一下单
	 * <p>参考<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1">开发文档</p>
	 *
	 * @param request
	 * @return
	 * @throws SignatureException
	 * @throws PayApiException
	 * @throws PayBusinessException
	 */
	public static UnifiedorderResponse unifiedorder(UnifiedorderRequest request)
			throws SignatureException, PayApiException, PayBusinessException {
		JaxbParser requestParser = buildJAXBParser(UnifiedorderRequest.class);
		JaxbParser responseParser = buildJAXBParser(UnifiedorderResponse.class);
		request.setSign(signature(request));
		String postData = requestParser.toXML(request);
		logger.info("post data \n" + postData);
		String postResult = post(HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_UNIFIEDORDER, postData);
		logger.info("post result \n" + postResult);
		checkAccess(postResult);
		checkBusiness(postResult);
		validResponseSign(postResult);
		UnifiedorderResponse response = (UnifiedorderResponse) responseParser.toObj(postResult);
		return response;
	}

	/**
	 * 查询订单
	 * <p>参考<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2">开发文档</p>
	 *
	 * @param request
	 * @return
	 * @throws SignatureException
	 * @throws PayApiException
	 * @throws PayBusinessException
	 */
	public static OrderQueryResponse orderquery(OrderQueryRequest request)
			throws SignatureException, PayApiException, PayBusinessException {
		JaxbParser requestParser = buildJAXBParser(OrderQueryRequest.class);
		JaxbParser responseParser = buildJAXBParser(OrderQueryResponse.class);
		request.setSign(signature(request));
		String postData = requestParser.toXML(request);
		logger.info("post data \n" + postData);
		String postResult = post(HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_ORDERQUERY, postData);
		logger.info("post result \n" + postResult);
		checkAccess(postResult);
		checkBusiness(postResult);
		validResponseSign(postResult);
		OrderQueryResponse response = (OrderQueryResponse) responseParser.toObj(postResult);
		try {
			parseCouponsForOrderquery(postResult, response);
		} catch (Exception e) {
			logger.error("解析代金券或立减优惠失败", e);
			PayApiException exception = new PayApiException(PayCode.FAIL, "解析代金券或立减优惠失败");
			throw exception;
		}
		return response;
	}

	/**
	 * 关闭订单
	 * <p>参考<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2">开发文档</p>
	 *
	 * @param request
	 * @return
	 * @throws SignatureException
	 * @throws PayApiException
	 * @throws PayBusinessException
	 */
	public static CloseOrderResponse closeorder(CloseOrderRequest request)
			throws SignatureException, PayApiException, PayBusinessException {
		JaxbParser requestParser = buildJAXBParser(CloseOrderRequest.class);
		JaxbParser responseParser = buildJAXBParser(CloseOrderResponse.class);
		request.setSign(signature(request));
		String postData = requestParser.toXML(request);
		logger.info("post data \n" + postData);
		String postResult = post(HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_CLOSEORDER, postData);
		logger.info("post result \n" + postResult);
		checkAccess(postResult);
		checkBusiness(postResult);
		validResponseSign(postResult);
		CloseOrderResponse response = (CloseOrderResponse) responseParser.toObj(postResult);
		return response;
	}

	/**
	 * 申请退款
	 * <p>参考<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4">开发文档</p>
	 *
	 * @param request
	 * @return
	 * @throws SignatureException
	 * @throws PayApiException
	 * @throws PayBusinessException
	 */
	public static RefundResponse refund(RefundRequest request) throws SignatureException, PayApiException, PayBusinessException {
		JaxbParser requestParser = buildJAXBParser(RefundRequest.class);
		JaxbParser responseParser = buildJAXBParser(RefundResponse.class);
		request.setSign(signature(request));
		String postData = requestParser.toXML(request);
		logger.info("post data \n" + postData);
		String postResult = post(HTTPS_API_MCH_WEIXIN_QQ_COM_SECAPI_PAY_REFUND, postData);
		logger.info("post result \n" + postResult);
		checkAccess(postResult);
		checkBusiness(postResult);
		validResponseSign(postResult);
		RefundResponse response = (RefundResponse) responseParser.toObj(postResult);
		return response;
	}

	/**
	 * 查询退款
	 * <p>参考<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4">开发文档</p>
	 *
	 * @param request
	 * @return
	 * @throws SignatureException
	 * @throws PayApiException
	 * @throws PayBusinessException
	 */
	public static RefundQueryResponse refundquery(RefundQueryRequest request)
			throws SignatureException, PayApiException, PayBusinessException {
		JaxbParser requestParser = buildJAXBParser(RefundQueryRequest.class);
		JaxbParser responseParser = buildJAXBParser(RefundQueryResponse.class);
		request.setSign(signature(request));
		String postData = requestParser.toXML(request);
		logger.info("post data \n" + postData);
		String postResult = post(HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_REFUNDQUERY, postData);
		logger.info("post result \n" + postResult);
		checkAccess(postResult);
		checkBusiness(postResult);
		validResponseSign(postResult);
		RefundQueryResponse response = (RefundQueryResponse) responseParser.toObj(postResult);
		try {
			parseCouponsForRefundquery(postResult, response);
		} catch (Exception e) {
			logger.error("解析代金券或立减优惠失败", e);
			PayApiException exception = new PayApiException(PayCode.FAIL, "解析代金券或立减优惠失败");
			throw exception;
		}
		return response;
	}

	/**
	 * 下载对账单
	 * <p>参考<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_6">开发文档</p>
	 *
	 * @param request
	 * @return
	 * @throws PayApiException
	 */
	@SuppressWarnings("unused")
	public static String downloadbill(DownloadBillRequest request) throws PayApiException {
		JaxbParser requestParser = buildJAXBParser(DownloadBillRequest.class);
		JaxbParser responseParser = buildJAXBParser(PayApiException.class);
		request.setSign(signature(request));
		String postData = requestParser.toXML(request);
		logger.info("post data \n" + postData);
		String postResult = post(HTTPS_API_MCH_WEIXIN_QQ_COM_PAY_DOWNLOADBILL, postData);
		logger.info("post result \n" + postResult);
		PayApiException exception = null;
		try {
			Map<String, Object> mapFromXMLString = getMapFromXMLString(postResult);
			exception = new PayApiException(mapFromXMLString.get("return_code").toString(), mapFromXMLString.get("return_msg").toString());
		} catch (Exception e) {
			// 如果不是XML则说明对账单下载成功
		}
		if (exception != null) {
			throw exception;
		} else {
			return postResult;
		}
	}

	/**
	 * 测速上报
	 * <p>参考<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_8">开发文档</p>
	 *
	 * @param request
	 * @return
	 * @throws SignatureException
	 * @throws PayApiException
	 * @throws PayBusinessException
	 */
	public static ReportResponse report(ReportRequest request) throws SignatureException, PayApiException, PayBusinessException {
		JaxbParser requestParser = buildJAXBParser(ReportRequest.class);
		JaxbParser responseParser = buildJAXBParser(ReportResponse.class);
		request.setSign(signature(request));
		String postData = requestParser.toXML(request);
		logger.info("post data \n" + postData);
		String postResult = post(HTTPS_API_MCH_WEIXIN_QQ_COM_PAYITIL_REPORT, postData);
		logger.info("post result \n" + postResult);
		checkAccess(postResult);
		checkBusiness(postResult);
		// 测速上报不会返回签名
		// validResponseSign(postResult);
		ReportResponse response = (ReportResponse) responseParser.toObj(postResult);
		return response;
	}

	/**
	 * 封装支付结果通知
	 * <p/>
	 * <b>注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。 </b>
	 * <p><a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7">开发文档</p>
	 *
	 * @param servletRequest
	 * @return
	 * @throws SignatureException
	 * @throws PayApiException
	 * @throws PayBusinessException
	 */
	public static PayResultNotifyResponse parsePayResultNotify(ServletRequest servletRequest, ServletResponse servletResponse)
			throws SignatureException, PayApiException, PayBusinessException {
		JaxbParser responseParser = buildJAXBParser(PayResultNotifyResponse.class);
		JaxbParser exceptionParser = buildJAXBParser(PayApiException.class);
		PayApiException exception = new PayApiException(PayCode.SUCCESS, "OK");
		String postResult;
		try {
			int len;
			byte[] b = new byte[1024];
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			InputStream servletInputStream = servletRequest.getInputStream();
			while ((len = servletInputStream.read(b)) != -1) {
				stream.write(b, 0, len);
			}
			postResult = stream.toString(Consts.UTF_8.name());
		} catch (IOException e) {
			logger.error("支付结果通知数据解析失败", e);
			exception = new PayApiException(PayCode.FAIL, "支付结果通知数据解析失败");
			responseToWechat(servletResponse, exceptionParser.toXML(exception));
			throw exception;
		}
		logger.info("result data \n" + postResult);
		checkAccess(postResult);
		try {
			validResponseSign(postResult);
		} catch (SignatureException e) {
			exception = new PayApiException(PayCode.FAIL, "签名校验失败");
			responseToWechat(servletResponse, exceptionParser.toXML(exception));
			throw e;
		}
		checkBusiness(postResult);
		PayResultNotifyResponse response = (PayResultNotifyResponse) responseParser.toObj(postResult);
		try {
			parseCouponsForPayResultNotify(postResult, response);
		} catch (Exception e) {
			logger.error("解析代金券或立减优惠失败", e);
			exception = new PayApiException(PayCode.FAIL, "解析代金券或立减优惠失败");
			responseToWechat(servletResponse, exceptionParser.toXML(exception));
			throw exception;
		}
		responseToWechat(servletResponse, exceptionParser.toXML(exception));
		return response;
	}

	/**
	 * 
	* @Title: responseToWechat 
	* @Description: TODO(商户处理支付结果通知后同步返回给微信参数) 
	* @param @param servletResponse
	* @param @param postData
	* @param @throws PayApiException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private static void responseToWechat(ServletResponse servletResponse, String postData) throws PayApiException {
		try {
			servletResponse.getOutputStream().write(postData.getBytes(Consts.UTF_8));
			servletResponse.getOutputStream().flush();
			servletResponse.getOutputStream().close();
		} catch (IOException e) {
			throw new PayApiException(PayCode.FAIL, "支付结果通知同步返回失败");
		}
	}

	/**
	 * 
	* @Title: parseCouponsForPayResultNotify 
	* @Description: TODO(解析支付结果通知的代金券或立减优惠) 
	* @param @param postResult
	* @param @param payResultNotifyResponse
	* @param @throws ParserConfigurationException
	* @param @throws IOException
	* @param @throws SAXException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private static void parseCouponsForPayResultNotify(String postResult, PayResultNotifyResponse payResultNotifyResponse)
			throws ParserConfigurationException, IOException, SAXException {
		List<String> coupon_id_$n = new ArrayList<String>();
		List<Integer> coupon_fee_$n = new ArrayList<Integer>();
		Map<String, Object> mapFromPayResultNotifyXML = getMapFromXMLString(postResult);
		Iterator<String> iterator = mapFromPayResultNotifyXML.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			// 解析代金券或立减优惠，$n为下标，从0开始编号
			if (key.matches("^coupon_id_[0-9]+$")) { // coupon_id_$n
				coupon_id_$n.add(mapFromPayResultNotifyXML.get(key).toString());
			} else if (key.matches("^coupon_fee_[0-9]+$")) { // coupon_fee_$n
				coupon_fee_$n.add(Integer.valueOf(mapFromPayResultNotifyXML.get(key).toString()));
			}
		}
		payResultNotifyResponse.setCoupon_id_$n(coupon_id_$n.toArray(new String[coupon_id_$n.size()]));
		payResultNotifyResponse.setCoupon_fee_$n(coupon_fee_$n.toArray(new Integer[coupon_fee_$n.size()]));
	}

	/**
	 * 
	* @Title: parseCouponsForOrderquery 
	* @Description: TODO(解析查询订单的代金券或立减优惠) 
	* @param @param postResult
	* @param @param orderqueryResponse
	* @param @throws ParserConfigurationException
	* @param @throws IOException
	* @param @throws SAXException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private static void parseCouponsForOrderquery(String postResult, OrderQueryResponse orderqueryResponse)
			throws ParserConfigurationException, IOException, SAXException {
		List<String> coupon_batch_id_$n = new ArrayList<String>();
		List<String> coupon_id_$n = new ArrayList<String>();
		List<Integer> coupon_fee_$n = new ArrayList<Integer>();
		Map<String, Object> mapFromPayResultNotifyXML = getMapFromXMLString(postResult);
		Iterator<String> iterator = mapFromPayResultNotifyXML.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			// 解析代金券或立减优惠，$n为下标，从0开始编号
			if (key.matches("^coupon_batch_id_[0-9]+$")) { // coupon_batch_id_$n
				coupon_batch_id_$n.add(mapFromPayResultNotifyXML.get(key).toString());
			} else if (key.matches("^coupon_id_[0-9]+$")) { // coupon_id_$n
				coupon_id_$n.add(mapFromPayResultNotifyXML.get(key).toString());
			} else if (key.matches("^coupon_fee_[0-9]+$")) { // coupon_fee_$n
				coupon_fee_$n.add(Integer.valueOf(mapFromPayResultNotifyXML.get(key).toString()));
			}
		}
		orderqueryResponse.setCoupon_batch_id_$n(coupon_batch_id_$n.toArray(new String[coupon_id_$n.size()]));
		orderqueryResponse.setCoupon_id_$n(coupon_id_$n.toArray(new String[coupon_id_$n.size()]));
		orderqueryResponse.setCoupon_fee_$n(coupon_fee_$n.toArray(new Integer[coupon_fee_$n.size()]));
	}

	/**
	 * 
	* @Title: parseCouponsForRefundquery 
	* @Description: TODO(解析查询退款的代金券或立减优惠) 
	* @param @param postResult
	* @param @param refundqueryResponse
	* @param @throws ParserConfigurationException
	* @param @throws IOException
	* @param @throws SAXException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private static void parseCouponsForRefundquery(String postResult, RefundQueryResponse refundqueryResponse)
			throws ParserConfigurationException, IOException, SAXException {
		List<String> out_refund_no_$n = new ArrayList<String>();
		List<String> refund_id_$n = new ArrayList<String>();
		List<String> refund_channel_$n = new ArrayList<String>();
		List<Integer> refund_fee_$n = new ArrayList<Integer>();
		List<Integer> coupon_refund_fee_$n = new ArrayList<Integer>();
		List<Integer> coupon_refund_count_$n = new ArrayList<Integer>();
		List<List<String>> coupon_refund_batch_id_$n_$m = new ArrayList<List<String>>();
		List<List<String>> coupon_refund_id_$n_$m = new ArrayList<List<String>>();
		List<List<Integer>> coupon_refund_fee_$n_$m = new ArrayList<List<Integer>>();
		List<String> refund_status_$n = new ArrayList<String>();
		Map<String, Object> mapFromPayResultNotifyXML = getMapFromXMLString(postResult);
		Iterator<String> iterator = mapFromPayResultNotifyXML.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			// 解析代金券或立减优惠，$n为下标，$n为下标，从0开始编号
			if (key.matches("^out_refund_no_[0-9]+$")) { // out_refund_no_$n
				out_refund_no_$n.add(mapFromPayResultNotifyXML.get(key).toString());
			} else if (key.matches("^refund_id_[0-9]+$")) { // refund_id_$n
				refund_id_$n.add(mapFromPayResultNotifyXML.get(key).toString());
			} else if (key.matches("^refund_channel_[0-9]+$")) { // refund_channel_$n
				refund_channel_$n.add(mapFromPayResultNotifyXML.get(key).toString());
			} else if (key.matches("^refund_fee_[0-9]+$")) { // refund_fee_$n
				refund_fee_$n.add(Integer.valueOf(mapFromPayResultNotifyXML.get(key).toString()));
			} else if (key.matches("^coupon_refund_fee_[0-9]+$")) { // coupon_refund_fee_$n
				coupon_refund_fee_$n.add(Integer.valueOf(mapFromPayResultNotifyXML.get(key).toString()));
			} else if (key.matches("^coupon_refund_fee_[0-9]+$")) { // coupon_refund_fee_$n
				coupon_refund_fee_$n.add(Integer.valueOf(mapFromPayResultNotifyXML.get(key).toString()));
			} else if (key.matches("^coupon_refund_count_[0-9]+$")) { // coupon_refund_count_$n
				coupon_refund_count_$n.add(Integer.valueOf(mapFromPayResultNotifyXML.get(key).toString()));
			} else if (key.matches("^coupon_refund_batch_id_[0-9]+_[0-9]+$")) { // coupon_refund_batch_id_$n_$m
				String[] indexs = key.replace("coupon_refund_batch_id_", "").split("_");
				int index0 = Integer.valueOf(indexs[0]);
				if (coupon_refund_batch_id_$n_$m.size() <= index0) {
					coupon_refund_batch_id_$n_$m.add(new ArrayList<String>());
				}
				coupon_refund_batch_id_$n_$m.get(index0).add(mapFromPayResultNotifyXML.get(key).toString());
			} else if (key.matches("^coupon_refund_id_[0-9]+_[0-9]+$")) { // coupon_refund_id_$n_$m
				String[] indexs = key.replace("coupon_refund_id_", "").split("_");
				int index0 = Integer.valueOf(indexs[0]);
				if (coupon_refund_id_$n_$m.size() <= index0) {
					coupon_refund_id_$n_$m.add(new ArrayList<String>());
				}
				coupon_refund_id_$n_$m.get(index0).add(mapFromPayResultNotifyXML.get(key).toString());
			} else if (key.matches("^coupon_refund_fee_[0-9]+_[0-9]+$")) { // coupon_refund_fee_$n_$m
				String[] indexs = key.replace("coupon_refund_fee_", "").split("_");
				int index0 = Integer.valueOf(indexs[0]);
				if (coupon_refund_fee_$n_$m.size() <= index0) {
					coupon_refund_fee_$n_$m.add(new ArrayList<Integer>());
				}
				coupon_refund_fee_$n_$m.get(index0).add(Integer.valueOf(mapFromPayResultNotifyXML.get(key).toString()));
			} else if (key.matches("^refund_status_[0-9]+$")) { // refund_status_$n
				refund_status_$n.add(mapFromPayResultNotifyXML.get(key).toString());
			}
		}
		refundqueryResponse.setOut_refund_no_$n(out_refund_no_$n.toArray(new String[out_refund_no_$n.size()]));
		refundqueryResponse.setRefund_id_$n(refund_id_$n.toArray(new String[refund_id_$n.size()]));
		refundqueryResponse.setRefund_channel_$n(refund_channel_$n.toArray(new String[refund_channel_$n.size()]));
		refundqueryResponse.setRefund_fee_$n(refund_fee_$n.toArray(new Integer[refund_fee_$n.size()]));
		refundqueryResponse.setCoupon_refund_fee_$n(coupon_refund_fee_$n.toArray(new Integer[coupon_refund_fee_$n.size()]));
		refundqueryResponse.setCoupon_refund_count_$n(coupon_refund_count_$n.toArray(new Integer[coupon_refund_count_$n.size()]));
		String[][] coupon_refund_batch_id_$n_$m_array = new String[coupon_refund_batch_id_$n_$m.size()][];
		for (int i = 0; i < coupon_refund_batch_id_$n_$m.size(); i++) {
			List<String> item = coupon_refund_batch_id_$n_$m.get(i);
			coupon_refund_batch_id_$n_$m_array[i] = item.toArray(new String[item.size()]);
		}
		refundqueryResponse.setCoupon_refund_batch_id_$n_$m(coupon_refund_batch_id_$n_$m_array);
		String[][] coupon_refund_id_$n_$m_array = new String[coupon_refund_id_$n_$m.size()][];
		for (int i = 0; i < coupon_refund_id_$n_$m.size(); i++) {
			List<String> item = coupon_refund_id_$n_$m.get(i);
			coupon_refund_id_$n_$m_array[i] = item.toArray(new String[item.size()]);
		}
		refundqueryResponse.setCoupon_refund_id_$n_$m(coupon_refund_id_$n_$m_array);
		Integer[][] coupon_refund_fee_$n_$m_array = new Integer[coupon_refund_fee_$n_$m.size()][];
		for (int i = 0; i < coupon_refund_fee_$n_$m.size(); i++) {
			List<Integer> item = coupon_refund_fee_$n_$m.get(i);
			coupon_refund_fee_$n_$m_array[i] = item.toArray(new Integer[item.size()]);
		}
		refundqueryResponse.setCoupon_refund_fee_$n_$m(coupon_refund_fee_$n_$m_array);
		refundqueryResponse.setRefund_status_$n(refund_status_$n.toArray(new String[refund_status_$n.size()]));
	}

	/**
	 * 
	* @Title: buildH5PayConfig 
	* @Description: TODO(构造H5调用支付的参数对象) 
	* @param @param timeStamp
	* @param @param nonceStr
	* @param @param prepayId
	* @param @return    设定文件 
	* @return H5PayParam    返回类型 
	* @throws
	 */
	public static H5PayParam buildH5PayConfig(String timeStamp, String nonceStr, String prepayId) {
		H5PayParam config = new H5PayParam();
		config.setTimeStamp(timeStamp);
		config.setNonceStr(nonceStr);
		config.setPackageWithPrepayId("prepay_id=" + prepayId);
		config.setPaySign(signature(config));
		return config;
	}

	/**
	 * 
	* @Title: checkAccess 
	* @Description: TODO(检查响应结果是否正确) 
	* @param @param postResult
	* @param @throws PayApiException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private static void checkAccess(String postResult) throws PayApiException {
		PayApiException exception = null;
		try {
			Map<String, Object> map = getMapFromXMLString(postResult);
			if (PayCode.FAIL.equals(map.get("return_code").toString())) {
				exception = new PayApiException(PayCode.FAIL, map.get("return_msg").toString());
			}
		} catch (Exception e) {
			logger.error("回包数据解析失败", e);
			exception = new PayApiException(PayCode.FAIL, "回包数据解析失败");
		}
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * 
	* @Title: checkBusiness 
	* @Description: TODO(检查业务结果是否正确) 
	* @param @param postResult
	* @param @throws PayBusinessException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private static void checkBusiness(String postResult) throws PayBusinessException {
		PayBusinessException exception = null;
		try {
			Map<String, Object> map = getMapFromXMLString(postResult);
			if (PayCode.FAIL.equals(map.get("result_code").toString())) {
				exception = new PayBusinessException(PayCode.FAIL, map.get("err_code").toString(), map.get("err_code_des").toString());
			}
		} catch (Exception e) {
			logger.error("回包数据解析失败", e);
			exception = new PayBusinessException(PayCode.FAIL, "RESPONSE_PARSE_ERROR", "回包数据解析失败");
		}
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * 
	* @Title: validResponseSign 
	* @Description: TODO(校验响应数据的签名) 
	* @param @param xmlStr
	* @param @throws SignatureException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private static void validResponseSign(String xmlStr) throws SignatureException {
		try {
			Map<String, Object> map = getMapFromXMLString(xmlStr);
			String orignalSign = map.get("sign").toString();
			if (!orignalSign.equals(signature(map))) {
				throw new SignatureException();
			}
			logger.debug("返回数据的签名校验成功");
		} catch (Exception e) {
			throw new SignatureException();
		}
	}

	/**
	 * 
	* @Title: signature 
	* @Description: TODO(支付签名算法) 
	* @param @param object
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private static String signature(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = object.getClass().getDeclaredFields();
		// 字典序
		Arrays.sort(fields, new Comparator<Field>() {
			@Override
			public int compare(Field o1, Field o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		for (Field field : fields) {
			// sign不参与签名
			if ("sign".equals(field.getName())) {
				continue;
			}
			XmlElement xmlElement = field.getAnnotation(XmlElement.class);
			if (xmlElement == null) {
				try {
					Method getMethod = object.getClass()
							.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
					xmlElement = getMethod.getAnnotation(XmlElement.class);
				} catch (NoSuchMethodException e) {
					logger.warn("get method not found : " + field.getName());
					// skip this
				}
			}
			field.setAccessible(true);
			try {
				map.put(xmlElement != null ? xmlElement.name() : field.getName(), field.get(object));
			} catch (IllegalAccessException e) {
				// never throws...
			}
		}
		return signature(map);
	}

	/**
	 * 
	* @Title: signature 
	* @Description: TODO(支付签名算法) 
	* @param @param map
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private static String signature(Map<String, Object> map) {
		map.put("sign", ""); // sign不参与签名
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != null && !"".equals(entry.getValue())) {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder signatureTemp = new StringBuilder();
		for (int i = 0; i < size; i++) {
			signatureTemp.append(arrayToSort[i]);
		}
		signatureTemp.append("key=").append(Config.instance().getMchKey());
		String signatureSource = signatureTemp.toString();
		logger.debug("sign source : " + signatureSource);
		String signature = DigestUtils.md5Hex(signatureSource).toUpperCase();
		logger.debug("sign result : " + signature);
		return signature;
	}

	/**
	 * 
	* @Title: getMapFromXMLString 
	* @Description: TODO(XML串转化成Map) 
	* @param @param xmlString
	* @param @return
	* @param @throws ParserConfigurationException
	* @param @throws IOException
	* @param @throws SAXException    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	private static Map<String, Object> getMapFromXMLString(String xmlString)
			throws ParserConfigurationException, IOException, SAXException {
		//xmlString = xmlString.replaceAll("\n","");
		//xmlString = xmlString.replaceAll("<!\\[CDATA\\[(.*?)\\]\\]","$1");
		//这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		ByteArrayInputStream is = null;
		if (xmlString != null && !"".equals(xmlString.trim())) {
			is = new ByteArrayInputStream(xmlString.getBytes());
		}
		Document document = builder.parse(is);
		//获取到document里面的全部结点
		NodeList allNodes = document.getFirstChild().getChildNodes();
		Node node;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		int i = 0;
		while (i < allNodes.getLength()) {
			node = allNodes.item(i);
			if (node instanceof Element) {
				map.put(node.getNodeName(), node.getTextContent());
			}
			i++;
		}
		return map;

	}

	/**
	 * 
	* @Title: buildJAXBParser 
	* @Description: TODO(构建XML解析器JAXBPaser) 
	* @param @param clazz
	* @param @return    设定文件 
	* @return JaxbParser    返回类型 
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	private static JaxbParser buildJAXBParser(Class clazz) {
		JaxbParser parser = new JaxbParser(clazz);
		Field[] fields = clazz.getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
		}
		parser.setCdataNode(fieldNames);
		return parser;
	}

	/**
	 * 
	* @Title: post 
	* @Description: TODO(POST请求) 
	* @param @param url
	* @param @param xml
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private static String post(String url, String xml) {
		try {
			HttpEntity entity = Request.Post(url).bodyString(xml, ContentType.create("text/xml", Consts.UTF_8.name())).execute()
					.returnResponse().getEntity();
			if (entity != null) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				entity.writeTo(byteArrayOutputStream);
				return byteArrayOutputStream.toString(Consts.UTF_8.name());
			}
			return null;
		} catch (Exception e) {
			logger.error("post请求异常，" + e.getMessage() + "\npost url:" + url);
			e.printStackTrace();
		}
		return null;
	}

}
