/**
 * 
 */
package com.lanfeng.gupai.model.scence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lanfeng
 *
 */
public class Hall {
	private List<Room> rooms = new ArrayList<Room>(100);
	private String id;
	private String name;
	private boolean available = true;
	/**
	 * 
	 */
	public Hall() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean isAvailable(){
		for(Room r : rooms){
			if(r == null){
				continue;
			}
			if(!r.isAvailable()){
				return false;
			}
		}
		return true;
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

	@Override
	public String toString() {
		return "Hall [rooms=" + rooms + ", id=" + id + ", name=" + name
				+ ", available=" + isAvailable() + "]";
	}

}
