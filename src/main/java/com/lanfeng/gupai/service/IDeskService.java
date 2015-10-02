package com.lanfeng.gupai.service;

import java.util.List;

import com.lanfeng.gupai.model.scence.Desk;

public interface IDeskService {
	public List<Desk> getDesksByRoomId(String roomId);
	public List<Desk> getALLDesks();
	public Desk addDesk(Desk desk);
	public void addDesks(List<Desk> desks);
}
