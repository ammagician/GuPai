package com.lanfeng.gupai.gameServer;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lanfeng.gupai.cacheCenter.CacheCenter;
import com.lanfeng.gupai.model.scence.Desk;
import com.lanfeng.gupai.service.impl.DeskService;
import com.lanfeng.gupai.utils.PositionMap;
import com.lanfeng.gupai.utils.ServiceUtil;
import com.lanfeng.gupai.utils.common.JSONObject;

@ServerEndpoint(value = "/gameServer", configurator = GetHttpSessionConfigurator.class)
public class GameServer extends HttpServlet implements ServletContextListener {
	/**
	 * 获取ServletContext
	 */
	private static ServletContext sc;

	public void contextInitialized(ServletContextEvent ctxEvent) {
		sc = ctxEvent.getServletContext();
		System.out.println("System init end");
	}

	public void contextDestroyed(ServletContextEvent ctxEvent) {
		System.out.println("SIL destoried");
	}

	private static final Logger logger = LogManager.getLogger(GameServer.class);
	private static final long serialVersionUID = -4921051955121849404L;
	private static final LinkedList<Session> clients = new LinkedList<Session>();
	private static ConcurrentMap<String, String> onlineUser = new ConcurrentHashMap<String, String>();
	private static ConcurrentMap<String, Set<Session>> deskUser = new ConcurrentHashMap<String, Set<Session>>();
	private HttpSession httpSession;

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		System.out.println("connect" + session.getId());
		logger.info("webSocket " + session.getId() + " connected.");

		httpSession = (HttpSession) config.getUserProperties().get(
				HttpSession.class.getName());
		clients.add(session);
		
		String userId = (String) httpSession.getAttribute("user");
		onlineUser.put(session.getId(), userId);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("message" + message);
		logger.info("webSocket send message \n" + message);
		
		JSONObject obj = JSONObject.fromObject(message);
		String eventType = obj.getString("eventType");
		JSONObject result = new JSONObject();
		result.put("eventType", eventType);
		JSONObject data = obj.getJSONObject("data");
		String roomId = data.getString("roomId");
		String deskId = data.getString("deskId");
		String position = data.getString("position");
		
		String sId = session.getId();
		String uId = onlineUser.get(sId);
		String jsonStr = CacheCenter.getString(sId);	
		try{
			httpSession.isNew();
		}catch(Exception e){
			System.out.println("httpSession timeout");			
			Set<Session> users = getDeskUser(jsonStr);
			if (users != null) {
				login(users, session, jsonStr);
				leaveSeat(session, sId, uId, jsonStr);
			}
			return;
		}

		if ("initPlayGround".equals(eventType)) {
			initPlayGround(session, roomId, deskId);
		} else if ("sitSeat".equals(eventType)) {
			sitSeat(session, sId, uId, roomId, deskId, position);
		} else if ("leaveSeat".equals(eventType)) {
			leaveSeat(session, sId, uId, jsonStr);
		}
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("close" + session.getId());
		logger.info("webSocket " + session.getId() + " close.");
		
		String sId = session.getId();
		String uId = onlineUser.get(sId);
		onlineUser.remove(sId);
		clients.remove(session);
		String jsonStr = CacheCenter.getString(sId);	
		leaveSeat(session, sId, uId, jsonStr);
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("webSocket error");
		System.out.println(t.toString());
	}
	
	private void initPlayGround(Session session, String roomId, String deskId){
		DeskService ds = ServiceUtil.getDeskService(sc);
		Desk d = ds.getDesk(roomId, deskId);
		JSONObject desk = new JSONObject();
		desk.put("desk", d.toJSON());
		JSONObject result = new JSONObject();
		result.put("eventType", "initPlayGround");
		result.put("data", desk);
		sendMessage(session, result);
		// CardsCreator.getInstance().getCards().toString());
	}
	
	private Set<Session> getDeskUser(String jsonStr){
		if(jsonStr == null){
			return null;
		}
		JSONObject msg = JSONObject.fromObject(jsonStr);
		String deskId = msg.getString("deskId");
		
		Set<Session> users = deskUser.get(deskId);
		
		return users;
	}
	
	private void login(Set<Session> users, Session session, String jsonStr){
		if(jsonStr == null){
			return;
		}
		JSONObject msg = JSONObject.fromObject(jsonStr);
		String deskId = msg.getString("deskId");
		users.remove(session);
		if(users.isEmpty()){
			deskUser.remove(deskId);
		}
		
		JSONObject result = new JSONObject();
		result.put("eventType", "login");
		sendMessage(session, result);
		clients.remove(session);
		onlineUser.remove(session.getId());
	}
	
	private void sitSeat(Session session, String sId, String uId, String roomId, String deskId, String position){
		Set<Session> users = deskUser.get(deskId);
		if (users == null) {
			users = new HashSet<Session>();
			deskUser.put(deskId, users);
		}
		users.add(session);
		
		DeskService ds = ServiceUtil.getDeskService(sc);
		JSONObject data = new JSONObject();
		data.put("userId", uId);
		data.put("roomId", roomId);
		data.put("deskId", deskId);
		data.put("position", position);
		CacheCenter.setString(sId, data.toString());

		ds.sitDesk(uId, roomId, deskId, PositionMap.getPosition(position));

		JSONObject result = new JSONObject();
		result.put("eventType", "sitSeat");
		result.put("data", data);
		sendAllMessage(result);
	} 
	
	private void leaveSeat(Session session, String sId, String uId, String jsonStr){
		if(jsonStr == null){
			return;
		}
		JSONObject msg = JSONObject.fromObject(jsonStr);

		DeskService ds = ServiceUtil.getDeskService(sc);
		String deskId = msg.getString("deskId");
		ds.leaveDesk(uId, msg.getString("roomId"), deskId, PositionMap.getPosition(msg.getString("position")));

		JSONObject result = new JSONObject();
		result.put("eventType", "leaveSeat");
		result.put("data", msg);
		sendAllMessage(result);
		CacheCenter.removeString(sId);
		Set<Session> users = deskUser.get(deskId);
		users.remove(session);
		if(users.isEmpty()){
			deskUser.remove(deskId);
		}
	} 
	
	private void sendMessage(Set<Session> ss, JSONObject msg) {
		for (Session s : ss) {
			sendMessage(s, msg);
		}
	}
	
	private void sendAllMessage(JSONObject msg) {
		for (Session s : clients) {
			sendMessage(s, msg);
		}
	}
	
	private void sendMessage(Session s, JSONObject msg) {
		try {
			s.getBasicRemote().sendText(msg.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
