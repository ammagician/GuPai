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
public class QuadrupleCard extends CombinationCard {

	/**
	 * @param cards
	 * @param type
	 * @param value
	 */
	public QuadrupleCard(List<Card> cards, CardType type, int value) {
		super(cards, type, value);
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 */
	public QuadrupleCard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "QuadrupleCard [cards=" + cards + ", type=" + type + ", value=" + value + "]";
	}

}
