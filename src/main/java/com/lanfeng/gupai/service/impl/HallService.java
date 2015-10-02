package com.lanfeng.gupai.service.impl;

import java.util.List;

import com.lanfeng.gupai.dao.IHallDao;
import com.lanfeng.gupai.model.scence.Hall;
import com.lanfeng.gupai.service.IHallService;

public class HallService implements IHallService {

	private IHallDao hallDao;
	
	public HallService() {
		// TODO Auto-generated constructor stub
	}

	public List<Hall> getHallsByAreaId(String areaId) {
		// TODO Auto-generated method stub
		return hallDao.getHallsByAreaId(areaId);
	}

	public List<Hall> getALLHalls() {
		// TODO Auto-generated method stub
		return hallDao.getALLHalls();
	}

	public IHallDao getHallDao() {
		return hallDao;
	}

	public void setHallDao(IHallDao hallDao) {
		this.hallDao = hallDao;
	}

	public Hall addHall(Hall hall) {
		return hallDao.addHall(hall);
	}

}
