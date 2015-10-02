/**
 * 
 */
package com.lanfeng.gupai.action;

import java.io.IOException;
import java.util.List;

import com.lanfeng.gupai.model.scence.Hall;
import com.lanfeng.gupai.service.IHallService;
import com.lanfeng.gupai.utils.common.JSONObject;

/**
 * @author lanfeng
 *
 */
public class HallAction extends BaseAction{

	/**
	 * 
	 */
	private IHallService hallService;
	
	public String doGetHallListByAreaId() throws IOException {
		JSONObject requestData = JSONObject.fromObject(data);
		String areaId = "001";
		List<Hall> halls = hallService.getHallsByAreaId(areaId);
		
		writer(halls);
		return NONE;
	}
	
	public String doGetALLHalls() throws IOException {
		JSONObject requestData = JSONObject.fromObject(data);
		List<Hall> halls = hallService.getALLHalls();
		
		writer(halls);
		return NONE;
	}
	
	public String doAddHall() throws IOException {
		Hall hall = new Hall();
		hall.setName("hall_1");
		hallService.addHall(hall);
		
		writer(hall);
		return NONE;
	}

	public IHallService getHallService() {
		return hallService;
	}

	public void setHallService(IHallService hallService) {
		this.hallService = hallService;
	}

}
