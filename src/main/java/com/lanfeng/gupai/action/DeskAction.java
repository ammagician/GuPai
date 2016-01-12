package com.lanfeng.gupai.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lanfeng.gupai.dictionary.Position;
import com.lanfeng.gupai.model.scence.Desk;
import com.lanfeng.gupai.model.scence.Room;
import com.lanfeng.gupai.service.IDeskService;
import com.lanfeng.gupai.service.IRoomService;
import com.lanfeng.gupai.utils.PositionMap;
import com.lanfeng.gupai.utils.common.JSONArray;
import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;

public class DeskAction extends BaseAction{

	private static final long serialVersionUID = -458831708075468652L;
	
	private IDeskService deskService;
	private IRoomService roomService;

	public String doGetDeskList() throws IOException {
		HttpServletRequest req = getRequest();
		String roomId = req.getParameter("roomId");
		if(!StringUtil.isValid(roomId)){
			JSONObject rd = JSONObject.fromObject(data);
			roomId = rd.getString("roomId");
		}
		List<Desk> desks = new ArrayList<Desk>();
		if(StringUtil.isValid(roomId)){
			desks = deskService.getDesksByRoomId(roomId);
		}else{
			desks = deskService.getALLDesks();
		}
		writer(desks);
		return NONE;
	}
	
	public String doAddDesks(){
		HttpServletRequest req = getRequest();
		String roomId = req.getParameter("roomId");
		if(!StringUtil.isValid(roomId)){
			JSONObject rd = JSONObject.fromObject(data);
			roomId = rd.getString("roomId");
		}
		addDesks(roomId);
		
		return NONE;
	}
	
	public String doAddAllDesks(){
		HttpServletRequest req = getRequest();
		String hallId = req.getParameter("hallId");
		if(!StringUtil.isValid(hallId)){
			JSONObject rd = JSONObject.fromObject(data);
			hallId = rd.getString("hallId");
		}
		List<Room> rooms = roomService.getRoomsByHallId(hallId);
		for(Room r : rooms){
			String rId = r.getId();
			addDesks(rId);
		}
		return NONE;
	}
	
	public String doSitSeat() throws IOException {
		JSONObject rd = JSONObject.fromObject(data);
		String roomId = rd.getString("roomId");
		String deskId = rd.getString("deskId");
		String position = rd.getString("position");
		Position p = PositionMap.getPosition(position);
		boolean success = deskService.sitDesk(roomId, deskId, p);
		writer(success);
		return NONE;
	}
	
	private void addDesks(String roomId){
		List<Desk> ds = new ArrayList<Desk>();
		for(int i=0,len=100;i<len;i++){
			Desk desk = new Desk();
			desk.setRoomId(roomId);
			desk.setName("desk_" + (i + 1));
			ds.add(desk);
		}
		deskService.addDesks(ds);
	}

	public IDeskService getDeskService() {
		return deskService;
	}

	public void setDeskService(IDeskService deskService) {
		this.deskService = deskService;
	}

	public IRoomService getRoomService() {
		return roomService;
	}

	public void setRoomService(IRoomService roomService) {
		this.roomService = roomService;
	}

}
