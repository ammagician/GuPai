package com.lanfeng.gupai.action;

import java.io.IOException;

import com.lanfeng.gupai.utils.common.JSONArray;
import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;

public class RoomAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -458831708075468652L;

	public String getRoomList() throws IOException {
		JSONObject rd = JSONObject.fromObject(data);
		String hallId = rd.getString("hallId");
		JSONArray rooms = new JSONArray();
		if(StringUtil.isValid(hallId)){
			rooms = null;
		}
		writer(rooms);
		return "";
	}

}
