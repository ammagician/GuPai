package com.lanfeng.gupai.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.lanfeng.gupai.dao.IHallDao;
import com.lanfeng.gupai.model.scence.Hall;
import com.lanfeng.gupai.utils.common.StringUtil;

public class HallDao extends CommonDao<Hall, Serializable> implements IHallDao {

	public List<Hall> getHallsByAreaId(String areaId) {
		String hql = "select h from Hall h where h.areaId='" + areaId + "'";
		return getALLByHql(hql);
	}

	public List<Hall> getALLHalls() {
		return (List<Hall>)getALL("Hall");
	}

	public Hall addHall(Hall hall) {
		// TODO Auto-generated method stub
		return add(hall);
	}
}
