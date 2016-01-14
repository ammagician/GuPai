/**
 * 
 */
package com.lanfeng.gupai.model.scence;

import com.lanfeng.gupai.cacheCenter.Cachable;
import com.lanfeng.gupai.dictionary.Position;
import com.lanfeng.gupai.utils.common.JSONArray;
import com.lanfeng.gupai.utils.common.JSONObject;

/**
 * @author lanfeng
 *
 */
public class Seat extends Cachable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2059291397390603644L;
	private Position position;
	private boolean available = true;
	private String userId = "";
	
	public Seat() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Seat(Position position) {
		super();
		this.position = position;
	}
	
	public Seat(Position position, boolean available, String userId) {
		super();
		this.position = position;
		this.available = available;
		this.userId = userId;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "Seat [position=" + position + ", available=" + available
				+ ", userId=" + userId + "]";
	}
	
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		json.put("userId", userId);
		json.put("position", position);
		json.put("available", available);
		
		return json;
	}
	
}
