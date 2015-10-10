package com.lanfeng.gupai.service.impl;

import java.util.List;

import com.lanfeng.gupai.cacheCenter.CacheCenter;
import com.lanfeng.gupai.dao.IRoomDao;
import com.lanfeng.gupai.model.scence.Room;
import com.lanfeng.gupai.service.IRoomService;

public class RoomService implements IRoomService{
	
	private IRoomDao roomDao;

	public List<Room> getRoomsByHallId(String hallId) {
		// TODO Auto-generated method stub
		List<Room> rooms = CacheCenter.getRooms(hallId);
		if(rooms.isEmpty()){
			rooms = roomDao.getRoomsByHallId(hallId);
			CacheCenter.setRooms(hallId, rooms);
		}
		
		return rooms;
	}

	public List<Room> getALLRooms() {
		// TODO Auto-generated method stub
		return roomDao.getALLRooms();
	}

	public Room addRoom(Room room) {
		// TODO Auto-generated method stub
		return roomDao.addRoom(room);
	}
	
	public IRoomDao getRoomDao() {
		return roomDao;
	}

	public void setRoomDao(IRoomDao roomDao) {
		this.roomDao = roomDao;
	}

	public void addRooms(List<Room> rooms) {
		roomDao.addRooms(rooms);
	}

}
