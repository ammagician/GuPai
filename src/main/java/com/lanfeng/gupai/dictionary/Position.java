/**
 * 
 */
package com.lanfeng.gupai.dictionary;

/**
 * @author lanfeng
 *
 */
public enum Position {
	EAST(1), SOUTH(2), WEST(3), NORTH(4);

	private int nCode;

	private Position(int _nCode) {
		this.nCode = _nCode;
	}
	
	public int getCode(){
		return this.nCode;
	}

	@Override
	public String toString() {
		return String.valueOf(this.nCode);
	}
}
