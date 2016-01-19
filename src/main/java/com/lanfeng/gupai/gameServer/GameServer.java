package com.lanfeng.gupai.gameServer;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.lanfeng.gupai.model.Card;
import com.lanfeng.gupai.model.scence.Desk;
import com.lanfeng.gupai.service.impl.DeskService;
import com.lanfeng.gupai.utils.CardsCreator;
import com.lanfeng.gupai.utils.PositionMap;
import com.lanfeng.gupai.utils.ServiceUtil;
import com.lanfeng.gupai.utils.common.JSONArray;
import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;

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
		JSONObject data = obj.getJSONObject("data");
		String roomId = data.getString("roomId");
		String deskId = data.getString("deskId");
		String position = data.getString("position");
		
		String sId = session.getId();
		String uId = onlineUser.get(sId);
		String jsonStr = CacheCenter.getString(sId + "-SitSeat");	
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
		} else if ("readyPlay".equals(eventType)) {
			readyPlay(session, deskId, true);
		} else if ("cancelReady".equals(eventType)) {
			readyPlay(session, deskId, false);
		} else if ("playCard".equals(eventType)) {
			JSONObject cardsInfo = data.getJSONObject("cardsInfo");
			boolean pass = data.getBoolean("pass");
			String startPosition = data.getString("startPosition");
			playCard(session, deskId, position, startPosition, pass, cardsInfo);
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
		String jsonStr = CacheCenter.getString(sId + "-SitSeat");	
		leaveSeat(session, sId, uId, jsonStr);
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("webSocket error");
		System.out.println(t.toString());
	}
	
	private void playCard(Session session, String deskId, String position, String startPosition, boolean pass, JSONObject cardsInfo){
		if(!pass){
			 String k = deskId+ "-Tour";
			 String v = CacheCenter.getString(k);
			 JSONObject tour = null;
			 if(StringUtil.isValid(v)){
				 tour = JSONObject.fromObject(v);
			 }else{
				 tour = new JSONObject();
			 }
			 tour.put("cardsInfo", cardsInfo);
			 tour.put("position", position);
			 
			 CacheCenter.setString(k, tour.toString());
			 
			 String kk = deskId+ "-Circle";
			 String vv = CacheCenter.getString(kk);
			 JSONObject circle = null;
			 JSONArray winCards = null;
			 if(StringUtil.isValid(v)){
				 circle = JSONObject.fromObject(vv);
				 winCards = circle.getJSONArray(position);
			 }else{
				 circle = new JSONObject();
				 winCards = new JSONArray();
				 circle.put(position, winCards);
				 circle.put("size", 0);
			 }
			 winCards.add(cardsInfo);
			 int size = circle.getInt("size") + cardsInfo.getInt("size");
			 circle.put("size", size);
			 CacheCenter.setString(kk, circle.toString());
		}
		
		JSONObject msg = new JSONObject();
		msg.put("cardsInfo", cardsInfo);
		msg.put("position", position);
		msg.put("pass", pass);

		JSONObject result = new JSONObject();
		result.put("eventType", "playCard");
		result.put("data", msg);
		
		Set<Session> users = deskUser.get(deskId);
		sendMessage(users, result);
		
		String nextPosition = PositionMap.getNextPosition(position).toString();
		if(startPosition.equals(nextPosition)){
			tourEnd(users, deskId);
		}
	}
	
	private void tourEnd(Set<Session> users, String deskId){
		try {
			Thread.sleep(1500);
			//先不考虑倒钳
			String k = deskId+ "-Tour";
			String v = CacheCenter.getString(k);
			JSONObject tour = JSONObject.fromObject(v);
			JSONObject result = new JSONObject();
			result.put("eventType", "endTour");
			result.put("data", tour);
			
			sendMessage(users, result);
			//circleEnd(users, deskId);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void circleEnd(Set<Session> users, String deskId){
		try {
			
			//先不考虑倒钳
			String k = deskId+ "-Circle";
			String v = CacheCenter.getString(k);
			JSONObject circle = JSONObject.fromObject(v);
			int size = circle.getInt("size");
			if(size == 8){
				Thread.sleep(1500);
				JSONObject result = new JSONObject();
				result.put("eventType", "circleEnd");
				result.put("data", circle);
				
				//to do
				//计算分数，重新发牌
				sendMessage(users, result);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readyPlay(Session session, String deskId, boolean ready){
		session.getUserProperties().put("readyPlay", ready);
		JSONObject msg = new JSONObject();
		msg.put("readyPlay", false);
		JSONObject result = new JSONObject();
		result.put("eventType", "cancelReady");
		result.put("data", msg);
		if(!ready){
			sendMessage(session, result);
		}else{
			Set<Session> users = deskUser.get(deskId);
			if(users.size() == 4){
				for(Session s : users){
					Object userReady = s.getUserProperties().get("readyPlay");
					System.out.println("user ready: " + s.getId() + " --- " + userReady);
					if(userReady == null || !(Boolean)userReady){
						msg.put("readyPlay", true);
						result.put("eventType", "readyPlay");
						sendMessage(session, result);
						return;
					}
				}
				distributeCards(users);
			}else{
				msg.put("readyPlay", true);
				result.put("eventType", "readyPlay");
				sendMessage(session, result);
			}
		}
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
		CacheCenter.setString(sId + "-SitSeat", data.toString());

		ds.sitDesk(uId, roomId, deskId, PositionMap.getPosition(position));

		JSONObject result = new JSONObject();
		result.put("eventType", "sitSeat");
		result.put("data", data);
		sendAllMessage(result);
	} 
	
	private void distributeCards(Set<Session> users){
		Map<String, List<Card>> ms = CardsCreator.getInstance().distributeCards();
		JSONObject data = new JSONObject();
		data.put("NORTH", ms.get("NORTH"));
		data.put("WEST", ms.get("WEST"));
		data.put("SOUTH", ms.get("SOUTH"));
		data.put("EAST", ms.get("EAST"));
		
		JSONObject result = new JSONObject();
		result.put("eventType", "distributeCards");
		result.put("data", data);
		
		sendMessage(users, result);
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
		CacheCenter.removeString(sId + "-SitSeat");
		Set<Session> users = deskUser.get(deskId);
		session.getUserProperties().put("readyPlay", false);
		users.remove(session);
		if(users.isEmpty()){
			deskUser.remove(deskId);
		}
		
		session.getUserProperties().put("readyPlay", false);
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
