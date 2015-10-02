package com.lanfeng.gupai.service;

import java.util.List;

import com.lanfeng.gupai.model.scence.Hall;
import com.lanfeng.gupai.model.scence.Room;

public interface IHallService {
	public List<Hall> getHallsByAreaId(String areaId);
	public List<Hall> getALLHalls();
	public Hall addHall(Hall hall);
}
