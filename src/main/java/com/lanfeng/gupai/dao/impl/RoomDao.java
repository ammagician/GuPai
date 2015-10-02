package com.lanfeng.gupai.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.lanfeng.gupai.dao.IRoomDao;
import com.lanfeng.gupai.model.scence.Room;

public class RoomDao extends CommonDao<Room, Serializable> implements IRoomDao {
	
	public List<Room> getRoomsByHallId(String hallId) {
		String hql = "select r from Room r where r.hallId='" + hallId + "'";
		return getALLByHql(hql);
	}

	public List<Room> getALLRooms() {
		// TODO Auto-generated method stub
		return getALL("Room");
	}

	public Room addRoom(Room room) {
		return add(room);
	}

	public void addRooms(List<Room> rooms) {
		batchAdd(rooms);
	}

}
