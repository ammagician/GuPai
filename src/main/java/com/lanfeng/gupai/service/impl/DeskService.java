package com.lanfeng.gupai.service.impl;

import java.util.List;

import com.lanfeng.gupai.cacheCenter.CacheCenter;
import com.lanfeng.gupai.dao.IDeskDao;
import com.lanfeng.gupai.model.scence.Desk;
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
