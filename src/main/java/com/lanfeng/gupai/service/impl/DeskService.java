package com.lanfeng.gupai.service.impl;

import java.util.List;

import com.lanfeng.gupai.cacheCenter.CacheCenter;
import com.lanfeng.gupai.dao.IDeskDao;
import com.lanfeng.gupai.dictionary.Position;
import com.lanfeng.gupai.model.scence.Desk;
import com.lanfeng.gupai.model.scence.Seat;
import com.lanfeng.gupai.service.IDeskService;

public class DeskService implements IDeskService {
	private IDeskDao deskDao;
	
	public List<Desk> getDesksByRoomId(String roomId) {
		// TODO Auto-generated method stub
		List<Desk> desks = CacheCenter.getDesks(roomId);
		if(desks.isEmpty()){
			desks = deskDao.getDesksByRoomId(roomId);
			CacheCenter.setDesks(roomId, desks);
		}
		
		return desks;
	}
	
	public Desk getDesk(String roomId, String deskId) {
		List<Desk> desks = this.getDesksByRoomId(roomId);
		return getDesk(desks, deskId);
	}
	
	private Desk getDesk(List<Desk> desks, String deskId){
		for(Desk d : desks){
			if(d.getId().equals(deskId)){
				return d;
			}
		}
		return null;
	}

	public boolean sitDesk(String userId, String roomId, String deskId, Position position) {
		Desk d = getDesk(roomId, deskId);
		if(d == null){
			return false;
		}
		Seat s = d.getSeat(position);
		if(s.isAvailable()){
			CacheCenter.delDesk(roomId, d);
			s.setUserId(userId);
			s.setAvailable(false);
			CacheCenter.setDesk(roomId, d);
			return true;
		}
		return false;
	}
	public void leaveDesk(String userId, String roomId, String deskId, Position position) {
		Desk d = getDesk(roomId, deskId);
		if(d == null){
			return;
		}
		Seat s = d.getSeat(position);
		CacheCenter.delDesk(roomId, d);
		s.setUserId("");
		s.setAvailable(true);
		CacheCenter.setDesk(roomId, d);
	}
	
	public boolean deskAvailable(String roomId, String deskId, Position position) {
		List<Desk> desks = this.getDesksByRoomId(roomId);
		Desk d = getDesk(desks, deskId);
		if(d == null){
			return false;
		}
		Seat s = d.getSeat(position);
		return s.isAvailable();
	}
	
	public List<Desk> getALLDesks() {
		return deskDao.getALLDesks();
	}

	public Desk addDesk(Desk desk) {
		return deskDao.addDesk(desk);
	}

	public void addDesks(List<Desk> desks) {
		deskDao.addDesks(desks);
	}

	public IDeskDao getDeskDao() {
		return deskDao;
	}

	public void setDeskDao(IDeskDao deskDao) {
		this.deskDao = deskDao;
	}
}
