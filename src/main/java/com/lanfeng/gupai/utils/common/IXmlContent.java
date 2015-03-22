package com.lanfeng.gupai.utils.common;

import org.dom4j.Document;

public interface IXmlContent<T> {
	T parse(Document doc);
}
