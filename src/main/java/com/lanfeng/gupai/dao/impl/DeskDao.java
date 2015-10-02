package com.lanfeng.gupai.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.lanfeng.gupai.dao.IDeskDao;
import com.lanfeng.gupai.model.scence.Desk;

public class DeskDao extends CommonDao<Desk, Serializable> implements IDeskDao {

	public List<Desk> getDesksByRoomId(String roomId) {
		// TODO Auto-generated method stub
		String hql = "select d from Desk d where d.roomId='" + roomId + "'";
		return getALLByHql(hql);
	}

	public List<Desk> getALLDesks() {
		// TODO Auto-generated method stub
		return getALL("Desk");
	}

	public Desk addDesk(Desk desk) {
		return add(desk);
	}

	public void addDesks(List<Desk> desks) {
		batchAdd(desks);		
	}

}
