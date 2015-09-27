package com.lanfeng.gupai.dao;

import java.util.List;

import com.lanfeng.gupai.model.scence.Room;

public interface IRoomDao {
	public List<Room> getRoomsByHallId(String hallId);
	public List<Room> getALLRooms();
}
