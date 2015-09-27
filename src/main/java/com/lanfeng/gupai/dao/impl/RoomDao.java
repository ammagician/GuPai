package com.lanfeng.gupai.dao.impl;

import java.util.List;

import com.lanfeng.gupai.dao.IRoomDao;
import com.lanfeng.gupai.model.scence.Room;

public class RoomDao extends CommonDao implements IRoomDao {
	
	public List<Room> getRoomsByHallId(String hallId) {
		// TODO Auto-generated method stub
		return getALL("room");
	}

	public List<Room> getALLRooms() {
		// TODO Auto-generated method stub
		return getALL("room");
	}

}
