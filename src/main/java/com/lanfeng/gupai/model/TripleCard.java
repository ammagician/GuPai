/**
 *
 */
package com.lanfeng.gupai.model;

import java.util.List;

import com.lanfeng.gupai.dictionary.CardType;

/**
 * @author apang
 *
 */
public class TripleCard extends CombinationCard {

	/**
	 *
	 */
	public TripleCard() {
		// TODO Auto-generated constructor stub
		super();
	}

	public TripleCard(List<Card> cards, CardType type, int value) {
		super(cards, type, value);
	}

	@Override
	public String toString() {
		return "TripleCard [cards=" + cards + ", type=" + type + ", value=" + value + "]";
	}

}
