/**
 *
 */
package com.lanfeng.gupai.utils;

import java.util.List;

import com.lanfeng.gupai.model.Card;
import com.lanfeng.gupai.model.PairCard;
import com.lanfeng.gupai.model.QuadrupleCard;
import com.lanfeng.gupai.model.TripleCard;

/**
 * @author apang
 *
 */
public class CardUtils {

	public static int getValue(Object cardObj) {
		int value = -1;
		if (cardObj instanceof Card) {
			value = ((Card) cardObj).getValue();
		} else if (cardObj instanceof PairCard) {

		} else if (cardObj instanceof TripleCard) {

		} else if (cardObj instanceof QuadrupleCard) {

		}

		return value;
	}

	public static void main(String[] s) {
		List<Card> cards = CardsCreator.getInstance().getCards();
		System.out.println(cards);
	}

}
