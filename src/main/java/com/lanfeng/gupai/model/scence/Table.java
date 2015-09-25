/**
 * 
 */
package com.lanfeng.gupai.model.scence;

import java.util.ArrayList;
import java.util.List;

import com.lanfeng.gupai.dictionary.Position;

/**
 * @author lanfeng
 *
 */
public class Table {

	private List<Seat> seats = new ArrayList<Seat>(4);
	private String id;
	private String name;
	private boolean available = true;
	/**
	 * 
	 */
	public Table() {
		this.init();
	}
	
	private void init(){
		Seat west = new Seat(Position.WEST);
		Seat east = new Seat(Position.EAST);
		Seat north = new Seat(Position.NORTH);
		Seat south = new Seat(Position.SOUTH);
		seats.add(east);
		seats.add(west);
		seats.add(south);
		seats.add(north);
	}

	public boolean isAvailable(){
		for(Seat s : seats){
			if(!s.isAvailable()){
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
		return "Table [seats=" + seats + ", id=" + id + ", name=" + name
				+ ", available=" + isAvailable() + "]";
	}

	
	
	
}
