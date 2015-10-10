/**
 * 
 */
package com.lanfeng.gupai.model.scence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author lanfeng
 *
 */
@Entity
@Table(name="Hall")
public class Hall implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2453750394980846726L;

	@Transient
	private List<Room> rooms = new ArrayList<Room>(100);
	
	@Id
	@GenericGenerator(name="uuid",strategy="uuid")
	@GeneratedValue(generator="uuid")
	@Column(name = "id", nullable = false)
	private String id;
	
	@Column(name = "name")
	private String name;
	@Column(name = "available")
	private boolean available = true;
	
	@Column(name = "areaId")
	private String areaId;
	
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
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	@Override
	public String toString() {
		return "Hall [rooms=" + rooms + ", id=" + id + ", name=" + name
				+ ", available=" + isAvailable() + ", areaId=" + areaId + "]";
	}

	

}
