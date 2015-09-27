package com.lanfeng.gupai.action;

import java.io.IOException;

import com.lanfeng.gupai.utils.common.JSONArray;
import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;

public class TableAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4040398619577278280L;

	public String getTableListByRoomId() throws IOException {
		JSONObject rd = JSONObject.fromObject(data);
		String roomId = rd.getString("roomId");
		JSONArray tables = new JSONArray();
		if(StringUtil.isValid(roomId)){
			tables = null;
		}
		writer(tables);
		return "";
	}

}
