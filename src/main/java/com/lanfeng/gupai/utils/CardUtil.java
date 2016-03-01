/**
 *
 */
package com.lanfeng.gupai.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.lanfeng.gupai.dictionary.CardType;
import com.lanfeng.gupai.model.Card;
import com.lanfeng.gupai.model.ICard;
import com.lanfeng.gupai.model.PairCard;
import com.lanfeng.gupai.model.QuadrupleCard;
import com.lanfeng.gupai.model.TripleCard;
import com.lanfeng.gupai.utils.common.ComparatorUtil;

/**
 * @author apang
 *
 */
public class CardUtil {

	public static int getValue(ICard cardObj) {
		int value = -1;
		if (cardObj instanceof Card) {
			value = ((Card) cardObj).getValue();
		} else if (cardObj instanceof PairCard) {

		} else if (cardObj instanceof TripleCard) {

		} else if (cardObj instanceof QuadrupleCard) {

		}

		return value;
	}
	
	public static Pair<List<Card>, List<Card>> separateCards(List<Card> cards){
		List<Card> wenCards = new ArrayList<Card>();
		List<Card> wuCards = new ArrayList<Card>();
		for(Card c : cards){
			CardType t = c.getType();
			if(t.equals(CardType.WEN)){
				wenCards.add(c);
			}else{
				wuCards.add(c);
			}
		}
		Comparator<Card> comparator = ComparatorUtil.getCardValueComparator();
		Collections.sort(wenCards, comparator);
		Collections.sort(wuCards, comparator);
		adjustZhiZunCards(wuCards);
		
		Pair<List<Card>, List<Card>> pair = Pair.of(wenCards, wuCards);
		return pair;
	}
	
	//配对至尊，要求武牌排序后
	public static void adjustZhiZunCards(List<Card> cards){
		int liuJiaoIndex = -1;
		int sanDingIndex = -1;
		for(int i=0,len=cards.size(); i<len; i++){
			Card c = cards.get(i);
			CardType t = c.getType();
			int v = c.getValue();
			if(t.equals(CardType.WU)){
				if(v == 4){
					liuJiaoIndex = i;
				}else if(v == 6){
					sanDingIndex = i;
				}
			}else{
				//只排武牌
				return; 
			}
		}
		
		if(liuJiaoIndex > -1 && sanDingIndex > -1 && sanDingIndex != liuJiaoIndex + 1){
			Card liuJiao = cards.get(liuJiaoIndex);
			cards.remove(liuJiaoIndex);
			cards.add(sanDingIndex-1, liuJiao);
		}
	}
	
	//可配对武牌
	public static List<Card> getSameValueWuCards(List<Card> cards, int value){
		ArrayList<Card> newCards = new ArrayList<Card>();
		if(0 < value && value < 5 && value != 4){
			for(int i=0,len=cards.size(); i<len; i++){
				Card c = cards.get(i);
				CardType t = c.getType();
				int v = c.getValue();
				if(t.equals(CardType.WU) && value == v){
					newCards.add(c);
					cards.remove(c);
					--i;
					--len;
				}
			}
		}
		return newCards;
	}
	
	public static List<Card> sortCards(List<Card> cards){
		Pair<List<Card>, List<Card>> pair = separateCards(cards);
		List<Card> wenCards = pair.getLeft();
		List<Card> wuCards = pair.getRight();
		
		ArrayList<Card> newCards = new ArrayList<Card>();
		for(int i=0,len=wenCards.size(); i<len; i++){
			 Card c = wenCards.get(i);
			 newCards.add(c);
			 int v = c.getValue();
			 if( i+1 < len){
				 Card c2 = wenCards.get(i+1);
				 if(c2.getValue() == v){
					 newCards.add(c2);
					 i++;
				 }
			 }
			 
			 List<Card> matchWuCards = getSameValueWuCards(wuCards, v);
			 if(!matchWuCards.isEmpty()){
				 newCards.addAll(matchWuCards);
			 }
		}
		
		newCards.addAll(wuCards);
		return newCards;
	}


	public static void main(String[] s) {
		List<Card> cards = CardsCreator.getInstance().getCards();
		System.out.println(cards);
	}

}
