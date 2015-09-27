package com.lanfeng.gupai.service;

import java.util.List;

import com.lanfeng.gupai.model.scence.Room;

public interface IRoomService {
	public List<Room> getRoomsByHallId(String hallId);
	public List<Room> getALLRooms();
}
