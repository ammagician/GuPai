/**
 *
 */
package com.lanfeng.gupai.model;

import java.util.ArrayList;
import java.util.List;

import com.lanfeng.gupai.dictionary.CardType;

/**
 * @author apang
 *
 */
public class CombinationCard {
	protected List<Card> cards = new ArrayList<Card>();
	protected CardType type; // wen or wu
	protected int value; // da xiao

	public CombinationCard(List<Card> cards, CardType type, int value) {
		super();
		this.cards = cards;
		this.type = type;
		this.value = value;
	}

	public CombinationCard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public CardType getType() {
		return type;
	}

	public void setType(CardType type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CombinationCard [cards=" + cards + ", type=" + type + ", value=" + value + "]";
	}

}
