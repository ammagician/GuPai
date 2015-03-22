/**
 *
 */
package com.lanfeng.gupai.model;

import com.lanfeng.gupai.dictionary.CardType;

/**
 * @author apang
 *
 */
public class Card {
	private String id;
	private String name;
	private CardType type; // wen or wu
	private int value; // da xiao

	public Card() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param name
	 * @param type
	 * @param value
	 */
	public Card(String id, String name, CardType type, int value) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", name=" + name + ", type=" + type.name() + ", value=" + value + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public Card clone() throws CloneNotSupportedException {
		Card c = new Card(id, name, type, value);
		c.setType(CardType.valueOf(type.name()));
		return c;
	}

}
