package com.lanfeng.gupai.utils;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lanfeng.gupai.service.impl.DeskService;
import com.lanfeng.gupai.service.impl.HallService;
import com.lanfeng.gupai.service.impl.RoomService;

public class ServiceUtil {
	private static DeskService ds = null;
	private static RoomService rs = null;
	private static HallService hs = null;
	public static DeskService getDeskService(ServletContext sc){
		if(ds != null){
			return ds;
		}
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		//ds = (DeskService)ac.getBean(DeskService.class);
		ds = (DeskService)ac.getBean("DeskService");
		return ds;
	}

	public static RoomService getRoomService(ServletContext sc){
		if(rs != null){
			return rs;
		}
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		rs = (RoomService)ac.getBean("RoomService");
		return rs;
	}
	
	public static HallService getHallService(ServletContext sc){
		if(hs != null){
			return hs;
		}
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		hs = (HallService)ac.getBean("HallService");
		return hs;
	}
}
