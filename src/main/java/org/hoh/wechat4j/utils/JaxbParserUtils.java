package org.hoh.wechat4j.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
* @ClassName: JaxbParserUtils 
* @Description: TODO(使用Jaxb2.0实现XML,特别支持Root对象是List的情形) 
* @author derrick_hoh@163.com
* @date 2016年8月2日 下午3:42:57 
*
 */
public class JaxbParserUtils {
	private static final Logger logger = Logger.getLogger(JaxbParserUtils.class);
	// 多线程安全的Context.
	private JAXBContext jaxbContext;

	/**
	 * 
	* <p>Title: 所有需要序列化的Root对象的类型</p> 
	* <p>Description: </p> 
	* @param types
	 */
	public JaxbParserUtils(Class<?>... types) {
		try {
			jaxbContext = JAXBContext.newInstance(types);
		} catch (JAXBException e) {
			logger.error("[JAXB INIT FAIL] jaxb init fail.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	* @Title: toXml 
	* @Description: TODO(对象转XML) 
	* @param @param root
	* @param @param encoding
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String toXml(Object root, String encoding) {
		try {
			StringWriter writer = new StringWriter();
			createMarshaller(encoding).marshal(root, writer);

			return writer.toString();
		} catch (JAXBException e) {
			logger.error("[TO XML IS FAIL] to xml is fail. this encoding is：" + encoding);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	* @Title: toXml 
	* @Description: TODO(对象转XML) 
	* @param @param root
	* @param @param rootName
	* @param @param encoding
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	public String toXml(Collection root, String rootName, String encoding) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;

			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName), CollectionWrapper.class,
					wrapper);

			StringWriter writer = new StringWriter();
			createMarshaller(encoding).marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e) {
			logger.error("[TO XML IS FAIL] to xml is fail.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	* @Title: fromXml 
	* @Description: TODO(XML转对象) 
	* @param @param xml
	* @param @return    设定文件 
	* @return T    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller().unmarshal(reader);
		} catch (JAXBException e) {
			logger.error("[XML TO OBJECT IS FAIL] xml to object is fail.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	* @Title: fromXml 
	* @Description: TODO(XML转对象支持大小写敏感或不敏感) 
	* @param @param xml
	* @param @param caseSensitive
	* @param @return    设定文件 
	* @return T    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(String xml, boolean caseSensitive) {
		try {
			String fromXml = xml;
			if (!caseSensitive)
				fromXml = xml.toLowerCase();
			StringReader reader = new StringReader(fromXml);
			return (T) createUnmarshaller().unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	* @Title: createMarshaller 
	* @Description: TODO(创建Marshaller, 设定encoding(可为Null).) 
	* @param @param encoding
	* @param @return    设定文件 
	* @return Marshaller    返回类型 
	* @throws
	 */
	public Marshaller createMarshaller(String encoding) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//是否格式化生成的xml串
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);        //是否省略xml头声明信息
			if (StringUtils.isNotBlank(encoding)) {
				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			}
			return marshaller;
		} catch (JAXBException e) {
			logger.error("[CREATE MARSHALLER IS FAIL] create marshaller is fail. this encoding is：" + encoding);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	* @Title: createUnmarshaller 
	* @Description: TODO(创建UnMarshaller.) 
	* @param @return    设定文件 
	* @return Unmarshaller    返回类型 
	* @throws
	 */
	public Unmarshaller createUnmarshaller() {
		try {
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	* @ClassName: CollectionWrapper 
	* @Description: TODO(封装Root Element 是 Collection的情况) 
	* @author derrick_hoh@163.com
	* @date 2016年8月2日 下午3:41:37 
	*
	 */
	public static class CollectionWrapper {
		@SuppressWarnings("rawtypes")
		@XmlAnyElement
		protected Collection collection;
	}
}
