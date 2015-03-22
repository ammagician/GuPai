package com.lanfeng.gupai.dictionary;

public enum CardType {
	WEN(1), WU(2), COM(3);

	private int nCode;

	private CardType(int _nCode) {
		this.nCode = _nCode;
	}

	@Override
	public String toString() {
		return String.valueOf(this.nCode);
	}
}
