package com.lanfeng.gupai.dao;

import java.util.List;

import com.lanfeng.gupai.model.scence.Desk;

public interface IDeskDao {
	public List<Desk> getDesksByRoomId(String roomId);
	public List<Desk> getALLDesks();
	public Desk addDesk(Desk desk);
	public void addDesks(List<Desk> desks);
}
