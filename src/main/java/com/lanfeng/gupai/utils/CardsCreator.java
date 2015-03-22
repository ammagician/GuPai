/**
 *
 */
package com.lanfeng.gupai.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import com.lanfeng.gupai.dictionary.CardType;
import com.lanfeng.gupai.model.Card;
import com.lanfeng.gupai.utils.common.IXmlContent;
import com.lanfeng.gupai.utils.common.XmlParser;

/**
 * @author apang
 *
 */
public class CardsCreator implements IXmlContent<List<Card>> {
	protected Log log = LogFactory.getLog(this.getClass());

	/**
	 *
	 */
	private CardsCreator() {
		// TODO Auto-generated constructor stub
	}

	private static CardsCreator cc;
	private List<Card> cards = null;

	public static CardsCreator getInstance() {
		if (cc != null) {
			return cc;
		}
		cc = new CardsCreator();
		return cc;
	}

	public List<Card> getCards() {
		return coloneCards();
	}

	private List<Card> coloneCards() {
		if (cards == null) {
			cards = XmlParser.parseFile(this, "model/Cards.xml", log).getRight();
		}
		List<Card> cs = new ArrayList<Card>();
		for (Card c : cards) {
			Card n;
			try {
				n = c.clone();
				cs.add(n);
			} catch (CloneNotSupportedException e) {
				log.info("Cards.xml wrong!");
			}
		}

		Collections.shuffle(cs);
		return cs;
	}

	public List<Card> parse(Document doc) {
		List<Card> cs = new ArrayList<Card>();
		Element root = doc.getRootElement();
		List csEl = root.selectNodes("card");
		for (int i = 0; i < csEl.size(); i++) {
			Element cel = (Element) csEl.get(i);
			String id = cel.attributeValue("id");
			String name = cel.attributeValue("name");
			int value = Integer.parseInt(cel.attributeValue("value"));
			CardType type = CardType.valueOf(cel.attributeValue("type"));
			Card c = new Card(id, name, type, value);
			cs.add(c);
		}
		return cs;
	}
}
