package com.lanfeng.gupai.action.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lanfeng.gupai.action.BaseAction;
import com.lanfeng.gupai.model.scence.Room;
import com.lanfeng.gupai.service.IRoomService;
import com.lanfeng.gupai.utils.common.JSONArray;
import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;

public class RoomAction extends BaseAction{

	private static final long serialVersionUID = -458831708075468652L;
	
	private IRoomService roomService;

	public String doGetRoomList() throws IOException {
		HttpServletRequest req = getRequest();
		
		/*
		HttpSession session = req.getSession();
		String user = (String) session.getAttribute("user");
		if(!StringUtil.isValid(user)){
			JSONObject rs = new JSONObject();
			rs.put("msg", "No login");
			writer(rs, -1);
			return NONE;
		}
		*/
		
		String hallId = req.getParameter("hallId");
		if(!StringUtil.isValid(hallId)){
			JSONObject rd = JSONObject.fromObject(data);
			hallId = rd.getString("hallId");
		}
		List<Room> rs = new ArrayList<Room>();
		if(StringUtil.isValid(hallId)){
			rs = roomService.getRoomsByHallId(hallId);
		}else{
			rs = roomService.getALLRooms();
		}
		writer(rs);
		return NONE;
	}
	
	public String doAddRooms(){
		HttpServletRequest req = getRequest();
		String hallId = req.getParameter("hallId");
		if(!StringUtil.isValid(hallId)){
			JSONObject rd = JSONObject.fromObject(data);
			hallId = rd.getString("hallId");
		}
		List<Room> rs = new ArrayList<Room>();
		for(int i=0,len=100;i<len;i++){
			Room room = new Room();
			room.setHallId(hallId);
			room.setName("room_" + (i + 1));
			rs.add(room);
		}
		roomService.addRooms(rs);
		return NONE;
	}

	public IRoomService getRoomService() {
		return roomService;
	}

	public void setRoomService(IRoomService roomService) {
		this.roomService = roomService;
	}

}
