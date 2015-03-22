/**
 *
 */
package com.lanfeng.gupai.utils.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

/**
 * @author apang
 *
 */
public class XmlParser {
	public static <T> ResultPair<T> parseFile(IXmlContent<T> parser, String xmlFile, Log log) {

		String fileDest = PathUtil.getResourcePath() + xmlFile;
		beforeParse(fileDest, log);
		Boolean ok = false;
		T result = null;
		try {
			SAXReader reader = new SAXReader();
			Document doc;
			doc = reader.read(fileDest);
			result = parser.parse(doc);
			ok = true;
		} catch (DocumentException e) {
			log.error(e);
			e.printStackTrace();
		}

		afterParse(result, log);
		return ResultPair.of(ok, result);
	}

	public static <T> ResultPair<T> parse(IXmlContent<T> parser, String xml) {
		return parse(parser, xml, null);
	}

	public static <T> ResultPair<T> parse(IXmlContent<T> parser, String xml, Log log) {

		beforeParse(xml, log);

		if (StringUtils.isEmpty(xml)) {
			return ResultPair.fail();
		}

		Boolean ok = false;
		T result = null;
		try {
			Document doc = DocumentHelper.parseText(xml);
			result = parser.parse(doc);
			ok = true;
		} catch (DocumentException e) {
			log.error(e);
			e.printStackTrace();
		}
		afterParse(result, log);

		return ResultPair.of(ok, result);
	}

	public static <T> ResultPair<T> parse(IXmlContent<T> parser, Document doc, Log log) {
		T result = null;
		result = parser.parse(doc);
		return ResultPair.of(true, result);
	}

	public static void beforeParse(String xml, Log log) {
		if (log != null) {
			log.debug("XmlPaser.beforeParse");
			log.debug(xml);
		}
	}

	public static <T> void afterParse(T result, Log log) {
		if (log != null) {
			log.debug("XmlPaser.afterParse");
			log.debug(result);
		}
	}

}
