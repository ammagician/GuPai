/**
 *
 */
package com.lanfeng.gupai.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.lanfeng.gupai.dictionary.CardType;
import com.lanfeng.gupai.model.Card;
import com.lanfeng.gupai.model.CombinationCard;
import com.lanfeng.gupai.model.PairCard;
import com.lanfeng.gupai.model.QuadrupleCard;
import com.lanfeng.gupai.model.TripleCard;

/**
 * @author apang
 *
 */
public class CombinationCardUtil {
	private static final String LIUJIAO = "WUD";
	private static final String SANDING = "WUF";
	private static Map<String, HashSet<String>> quadrupleCards = new HashMap<String, HashSet<String>>();
	private static Map<String, HashSet<String>> tripleCards = new HashMap<String, HashSet<String>>();
	private static Map<String, HashSet<String>> pairCards = new HashMap<String, HashSet<String>>();

	static {
		initQuadrupleCards();
		initTripleCards();
		initPairCards();
	}

	private static void initQuadrupleCards() {
		HashSet<String> stj = new HashSet<String>();
		stj.add("WENA");
		stj.add("WENAA");
		stj.add("WUA");
		stj.add("WUAA");
		quadrupleCards.put("stj", stj);
		HashSet<String> sdb = new HashSet<String>();
		stj.add("WENB");
		stj.add("WENBB");
		stj.add("WUB");
		stj.add("WUBB");
		quadrupleCards.put("sdb", sdb);
		HashSet<String> srq = new HashSet<String>();
		stj.add("WENC");
		stj.add("WENCC");
		stj.add("WUC");
		stj.add("WUCC");
		quadrupleCards.put("srq", srq);
		HashSet<String> sew = new HashSet<String>();
		stj.add("WEND");
		stj.add("WENDD");
		stj.add("WUD");
		stj.add("WUDD");
		quadrupleCards.put("sew", sew);
	}

	private static void initTripleCards() {
		HashSet<String> tj = new HashSet<String>();
		tj.add("WENA");
		tj.add("WENAA");
		tj.add("WUA");
		tripleCards.put("wentj1", tj);
		tj = new HashSet<String>();
		tj.add("WENA");
		tj.add("WENAA");
		tj.add("WUAA");
		tripleCards.put("wentj2", tj);
		tj = new HashSet<String>();
		tj.add("WENA");
		tj.add("WUA");
		tj.add("WUAA");
		tripleCards.put("wutj1", tj);
		tj = new HashSet<String>();
		tj.add("WENAA");
		tj.add("WUA");
		tj.add("WUAA");
		tripleCards.put("wutj2", tj);

		HashSet<String> db = new HashSet<String>();
		db.add("WENB");
		db.add("WENBB");
		db.add("WUB");
		tripleCards.put("wendb1", db);
		db = new HashSet<String>();
		db.add("WENB");
		db.add("WENBB");
		db.add("WUBB");
		tripleCards.put("wendb2", db);
		db = new HashSet<String>();
		db.add("WENB");
		db.add("WUB");
		db.add("WUBB");
		tripleCards.put("wudb1", db);
		db = new HashSet<String>();
		db.add("WENBB");
		db.add("WUB");
		db.add("WUBB");
		tripleCards.put("wudb2", db);

		HashSet<String> rq = new HashSet<String>();
		rq.add("WENC");
		rq.add("WENCC");
		rq.add("WUC");
		tripleCards.put("wenrq1", rq);
		rq = new HashSet<String>();
		rq.add("WENC");
		rq.add("WENCC");
		rq.add("WUCC");
		tripleCards.put("wenrq2", rq);
		rq = new HashSet<String>();
		rq.add("WENC");
		rq.add("WUC");
		rq.add("WUCC");
		tripleCards.put("wurq1", rq);
		rq = new HashSet<String>();
		rq.add("WENCC");
		rq.add("WUC");
		rq.add("WUCC");
		tripleCards.put("wurq2", rq);

		HashSet<String> we = new HashSet<String>();
		we.add("WEND");
		we.add("WENDD");
		we.add("WUD");
		tripleCards.put("wenew1", we);
		we = new HashSet<String>();
		we.add("WEND");
		we.add("WENDD");
		we.add("WUDD");
		tripleCards.put("wenew2", we);
		we = new HashSet<String>();
		we.add("WEND");
		we.add("WUD");
		we.add("WUDD");
		tripleCards.put("wuew1", we);
		we = new HashSet<String>();
		we.add("WENDD");
		we.add("WUD");
		we.add("WUDD");
		tripleCards.put("wuew2", we);
	}

	private static void initPairCards() {
		HashSet<String> st = new HashSet<String>();
		st.add("WENA");
		st.add("WENAA");
		pairCards.put("st", st);
		HashSet<String> sd = new HashSet<String>();
		sd.add("WENB");
		sd.add("WENBB");
		pairCards.put("sd", sd);
		HashSet<String> sr = new HashSet<String>();
		sr.add("WENC");
		sr.add("WENCC");
		pairCards.put("sr", sr);

		HashSet<String> se = new HashSet<String>();
		se.add("WEND");
		se.add("WENDD");
		pairCards.put("se", se);

		HashSet<String> smh = new HashSet<String>();
		smh.add("WENE");
		smh.add("WENEE");
		pairCards.put("smh", smh);

		HashSet<String> shs = new HashSet<String>();
		shs.add("WENF");
		shs.add("WENFF");
		pairCards.put("shs", shs);

		HashSet<String> ssy = new HashSet<String>();
		ssy.add("WENG");
		ssy.add("WENGG");
		pairCards.put("ssy", ssy);

		HashSet<String> sft = new HashSet<String>();
		sft.add("WENH");
		sft.add("WENHH");
		pairCards.put("sft", sft);

		HashSet<String> sht = new HashSet<String>();
		sht.add("WENI");
		sht.add("WENII");
		pairCards.put("sht", sht);

		HashSet<String> syl = new HashSet<String>();
		syl.add("WENJ");
		syl.add("WENJJ");
		pairCards.put("syl", syl);
		HashSet<String> syw = new HashSet<String>();
		syw.add("WENK");
		syw.add("WENKK");
		pairCards.put("syw", syw);

		HashSet<String> dj = new HashSet<String>();
		dj.add("WUA");
		dj.add("WUAA");
		pairCards.put("dj", dj);

		HashSet<String> db = new HashSet<String>();
		db.add("WUB");
		db.add("WUBB");
		pairCards.put("db", db);

		HashSet<String> dq = new HashSet<String>();
		dq.add("WUC");
		dq.add("WUCC");
		pairCards.put("dq", dq);

		HashSet<String> dw = new HashSet<String>();
		dw.add("WUE");
		dw.add("WUEE");
		pairCards.put("dw", dw);

		HashSet<String> zz = new HashSet<String>();
		zz.add("WUD");
		zz.add("WUF");
		pairCards.put("zz", zz);

		HashSet<String> tj1 = new HashSet<String>();
		tj1.add("WENA");
		tj1.add("WUA");
		pairCards.put("tj1", tj1);
		HashSet<String> tj2 = new HashSet<String>();
		tj2.add("WENA");
		tj2.add("WUAA");
		pairCards.put("tj2", tj2);
		HashSet<String> tj3 = new HashSet<String>();
		tj3.add("WENAA");
		tj3.add("WUA");
		pairCards.put("tj3", tj3);

		HashSet<String> tj4 = new HashSet<String>();
		tj4.add("WENAA");
		tj4.add("WUAA");
		pairCards.put("tj4", tj4);

		HashSet<String> db1 = new HashSet<String>();
		db1.add("WENB");
		db1.add("WUB");
		pairCards.put("db1", db1);

		HashSet<String> db2 = new HashSet<String>();
		db2.add("WENB");
		db2.add("WUBB");
		pairCards.put("db2", db2);

		HashSet<String> db3 = new HashSet<String>();
		db3.add("WENBB");
		db3.add("WUB");
		pairCards.put("db3", db3);

		HashSet<String> db4 = new HashSet<String>();
		db4.add("WENB");
		db4.add("WUBB");
		pairCards.put("db4", db4);

		HashSet<String> rq1 = new HashSet<String>();
		rq1.add("WENC");
		rq1.add("WUC");
		pairCards.put("rq1", rq1);

		HashSet<String> rq2 = new HashSet<String>();
		rq2.add("WENC");
		rq2.add("WUCC");
		pairCards.put("rq2", rq2);
		HashSet<String> rq3 = new HashSet<String>();
		rq3.add("WENCC");
		rq3.add("WUC");
		pairCards.put("rq3", rq3);

		HashSet<String> rq4 = new HashSet<String>();
		rq4.add("WENCC");
		rq4.add("WUCC");
		pairCards.put("rq4", rq4);

		HashSet<String> ew1 = new HashSet<String>();
		ew1.add("WEND");
		ew1.add("WUD");
		pairCards.put("ew1", ew1);

		HashSet<String> ew2 = new HashSet<String>();
		ew2.add("WEND");
		ew2.add("WUDD");
		pairCards.put("ew2", ew2);

		HashSet<String> ew3 = new HashSet<String>();
		ew3.add("WENDD");
		ew3.add("WUD");
		pairCards.put("ew3", ew3);

		HashSet<String> ew4 = new HashSet<String>();
		ew4.add("WENDD");
		ew4.add("WUDD");
		pairCards.put("ew4", ew4);
	}

	public static CombinationCard isCombinationCard(List<Card> cards) {
		if (cards == null) {
			return null;
		}
		int size = cards.size();
		if (cards.size() > 4 || cards.size() < 2) {
			return null;
		}

		HashSet<String> keys = new HashSet<String>();
		for (Card c : cards) {
			keys.add(c.getId());
		}

		if (size == 2) {
			return createCombinationCard(cards, size, keys, pairCards.entrySet());
		} else if (size == 3) {
			return createCombinationCard(cards, size, keys, tripleCards.entrySet());
		} else {
			return createCombinationCard(cards, size, keys, quadrupleCards.entrySet());
		}
	}

	private static CombinationCard createCombinationCard(List<Card> cards, int size, HashSet<String> keys,
	        Set<Entry<String, HashSet<String>>> entrySet) {
		for (Entry<String, HashSet<String>> entry : entrySet) {
			HashSet<String> ks = entry.getValue();
			boolean match = ks.containsAll(keys);
			if (match) {
				if (size == 2) {
					return createPairCard(cards);
				} else if (size == 3) {
					return createTripleCard(cards);
				} else {
					return createQuadrupleCard(cards);
				}
			}
		}
		return null;
	}

	private static CombinationCard createQuadrupleCard(List<Card> cards) {
		// TODO Auto-generated method stub
		Card one = cards.get(0);
		Card two = cards.get(1);
		Card three = cards.get(2);
		CardType typeOne = one.getType();
		CardType typeTwo = two.getType();
		int value = typeOne == CardType.WEN ? one.getValue() : (typeTwo == CardType.WEN ? two.getValue() : three
		        .getValue());
		QuadrupleCard qc = new QuadrupleCard(cards, CardType.WEN, value);
		return qc;
	}

	private static CombinationCard createTripleCard(List<Card> cards) {
		Card one = cards.get(0);
		Card two = cards.get(1);
		Card three = cards.get(2);
		CardType typeOne = one.getType();
		CardType typeTwo = two.getType();
		CardType typeThree = three.getType();

		int t = Integer.parseInt("" + typeOne) + Integer.parseInt("" + typeTwo) + Integer.parseInt("" + typeThree);
		CardType type = t == 4 ? CardType.WEN : CardType.WU;

		int value = 0;
		if (type == CardType.WEN) {
			value = typeOne == CardType.WEN ? one.getValue() : (typeTwo == CardType.WEN ? two.getValue() : three
			        .getValue());
		} else {
			value = typeOne == CardType.WU ? one.getValue() : (typeTwo == CardType.WU ? two.getValue() : three
			        .getValue());
		}

		TripleCard tc = new TripleCard(cards, type, value);
		return tc;
	}

	private static PairCard createPairCard(List<Card> cards) {
		Card one = cards.get(0);
		Card two = cards.get(1);
		CardType type = one.getType() == two.getType() ? one.getType() : CardType.COM;

		String idOne = one.getId();
		boolean zhiZun = false;
		if (type == CardType.WU && (LIUJIAO.equals(idOne) || SANDING.equals(idOne))) {
			zhiZun = true;
		}
		int value = 0;
		if (type != CardType.COM) {
			value = one.getValue();
		} else {
			value = one.getType() == CardType.WEN ? one.getValue() : two.getValue();
		}

		PairCard pc = new PairCard(cards, type, value, zhiZun);
		return pc;
	}

	/**
	 *
	 */
	public CombinationCardUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] s) {
		CardType one = CardType.WEN;
		System.out.println("" + CardType.WU + one);
	}

}
