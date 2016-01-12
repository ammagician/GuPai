package com.lanfeng.gupai.utils;

import com.lanfeng.gupai.dictionary.Position;

public class PositionMap {	
	public static Position getPosition(String p) {
		if("e".equals(p) || "east".equals(p) || "EAST".equals(p)){
			return Position.EAST;
		}
		if("s".equals(p) || "south".equals(p) || "SOUTH".equals(p)){
			return Position.SOUTH;
		}
		if("w".equals(p) || "west".equals(p) || "WEST".equals(p)){
			return Position.WEST;
		}
		if("n".equals(p) || "north".equals(p) || "NORTH".equals(p)){
			return Position.NORTH;
		}
		return null;
	}
	
	public static Position getPosition(int p) {
		if(p == 1){
			return Position.EAST;
		}
		if(p == 2){
			return Position.SOUTH;
		}
		if(p == 3){
			return Position.WEST;
		}
		if(p == 4){
			return Position.NORTH;
		}
		return null;
	}

}
