package com.lanfeng.gupai.service;

import java.util.List;

import com.lanfeng.gupai.dictionary.Position;
import com.lanfeng.gupai.model.scence.Desk;

public interface IDeskService {
	public List<Desk> getDesksByRoomId(String roomId);
	public Desk getDesk(String roomId, String deskId);
	public boolean sitDesk(String roomId, String deskId, Position position);
	public List<Desk> getALLDesks();
	public Desk addDesk(Desk desk);
	public void addDesks(List<Desk> desks);
}
