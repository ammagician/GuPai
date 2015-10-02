package com.lanfeng.gupai.dao;

import java.util.List;

import com.lanfeng.gupai.model.scence.Hall;

public interface IHallDao {
	public List<Hall> getHallsByAreaId(String areaId);
	public List<Hall> getALLHalls();
	public Hall addHall(Hall hall);
}
