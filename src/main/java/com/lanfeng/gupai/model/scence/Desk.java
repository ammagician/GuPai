/**
 * 
 */
package com.lanfeng.gupai.model.scence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanfeng.gupai.dictionary.Position;

/**
 * @author lanfeng
 *
 */

@Entity
@Table(name="Desk")
public class Desk {
	@Transient
	private List<Seat> seats = new ArrayList<Seat>(4);
	
	@Column(name = "roomId")
	private String roomId;
	
	@Id
	@GenericGenerator(name="uuid",strategy="uuid")
	@GeneratedValue(generator="uuid")
	@Column(name = "id", nullable = false)
	private String id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "available")
	private boolean available = true;
	/**
	 * 
	 */
	public Desk() {
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
	
	

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	@Override
	public String toString() {
		return "Desk [seats=" + seats + ", roomId=" + roomId + ", id=" + id
				+ ", name=" + name + ", available=" + isAvailable() + "]";
	}

}
