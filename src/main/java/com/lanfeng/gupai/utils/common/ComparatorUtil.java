package com.lanfeng.gupai.utils.common;

import java.util.Comparator;

import com.lanfeng.gupai.model.Card;

public class ComparatorUtil {

	public static Comparator<Card> getCardValueComparator() {
		Comparator<Card> c = new Comparator<Card>(){

			public int compare(Card c1, Card c2) {
				int v1 = c1.getValue();
				int v2 = c2.getValue();
				return v1 == v2? 0 : v1 > v2? 1:-1;
			}
		};
		return c;
	}

}
