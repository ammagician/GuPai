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
public class Room {
	private List<Table> tables = new ArrayList<Table>(100);
	private String id;
	private String name;
	private boolean available = true;
	/**
	 * 
	 */
	public Room() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean isAvailable(){
		for(Table t : tables){
			if(t == null){
				continue;
			}
			if(!t.isAvailable()){
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
		return "Room [tables=" + tables + ", id=" + id + ", name=" + name
				+ ", available=" + isAvailable() + "]";
	}

	
}
