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
public class PairCard extends CombinationCard {
	private boolean zhiZun;

	/**
	 *
	 */
	public PairCard() {
		// TODO Auto-generated constructor stub
		super();
	}

	public PairCard(List<Card> cards, CardType type, int value, boolean zhiZun) {
		super(cards, type, value);
		this.zhiZun = zhiZun;
	}

	@Override
	public String toString() {
		return "PairCard [zhiZun=" + zhiZun + ", cards=" + cards + ", type=" + type + ", value=" + value + "]";
	}

}
