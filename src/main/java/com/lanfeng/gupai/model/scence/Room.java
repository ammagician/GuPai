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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author lanfeng
 *
 */
@Entity
@Table(name="Room")
public class Room {
	@OneToMany
	@JoinColumn(name="roomId")
	private List<Desk> desks = new ArrayList<Desk>(100);
	
	@Column(name = "hallId")
	private String hallId;
	
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
	public Room() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean isAvailable(){
		for(Desk d : desks){
			if(d == null){
				continue;
			}
			if(!d.isAvailable()){
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

	public String getHallId() {
		return hallId;
	}

	public void setHallId(String hallId) {
		this.hallId = hallId;
	}

	public List<Desk> getDesks() {
		return desks;
	}

	public void setDesks(List<Desk> desks) {
		this.desks = desks;
	}

	@Override
	public String toString() {
		return "Room [desks=" + desks + ", hallId=" + hallId + ", id=" + id
				+ ", name=" + name + ", available=" + isAvailable() + "]";
	}

	

	
}
