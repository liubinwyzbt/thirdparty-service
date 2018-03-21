package com.yszx.module.pay.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayInputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLUtil {
	public static void main(String[] args) {
		try {
			Map<String, String> sreult = doXMLParse("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx9edefd157f9a66a1]]></appid><mch_id><![CDATA[1221627301]]></mch_id><nonce_str><![CDATA[VOnS66vyqmCXr7nm]]></nonce_str><sign><![CDATA[DD790A51281C3788E426F908C24E7D6B]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id><![CDATA[wx201507041513178637ca32cf0389338060]]></prepay_id><trade_type><![CDATA[APP]]></trade_type></xml>");
			// TODO add log
			// LogService.log(sreult.toString());
			if (sreult.get("result_code").equals("SUCCESS")) {
				// TODO add log
				// LogService.log(sreult.get("result_code").toString());
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			// TODO add log
			// LogService.log(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// TODO add log
			// LogService.log(e.getMessage());
		}
	}

	/**
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map doXMLParse(String strxml) throws JDOMException,
			IOException {
		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		Map m = new HashMap();

		InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = XMLUtil.getChildrenText(children);
			}

			m.put(k, v);
		}
		in.close();
		return m;
	}

	/**
	 * 
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(XMLUtil.getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param strxml
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static String getXMLEncoding(String strxml) throws JDOMException,
			IOException {
		InputStream in = HttpClientUtil.String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		in.close();
		return (String) doc.getProperty("encoding");
	}

}
