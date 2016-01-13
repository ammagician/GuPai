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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lanfeng.gupai.cacheCenter.CacheCenter;
import com.lanfeng.gupai.service.impl.DeskService;
import com.lanfeng.gupai.utils.PositionMap;
import com.lanfeng.gupai.utils.ServiceUtil;
import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;

@ServerEndpoint("/gameServer")
public class GameServer extends HttpServlet implements ServletContextListener{
	/**
	 *
	 */
	private static ServletContext sc;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		
		HttpSession httpSession = req.getSession();
		String userId = (String)httpSession.getAttribute("user");
		
		System.out.println(userId);
	}

	public void contextInitialized(ServletContextEvent ctxEvent) {
		System.out.println("System init");
		sc = ctxEvent.getServletContext();
        System.out.println("System init end");
	}
	
	public void contextDestroyed(ServletContextEvent ctxEvent) {
		System.out.println("SIL destoried");
	}	
	
	
	private static final Logger logger = LogManager.getLogger(GameServer.class);
	private static final long serialVersionUID = -4921051955121849404L;
	private static final LinkedList<Session> clients = new LinkedList<Session>();
	private static ConcurrentMap<String, Set<Session>> deskUser = new ConcurrentHashMap<String, Set<Session>>();
	

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("connect" + session.getId());
		logger.info("webSocket " + session.getId() +" connected.");
		
		clients.add(session);
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
		if("initPlayGround".equals(eventType)){
			DeskService ds = ServiceUtil.getDeskService(sc);
			try {
				//result.put("data", CardsCreator.getInstance().getCards().toString());
				String deskId = data.getString("deskId");
				Set<Session> users = deskUser.get("deskId");
				if(users == null){
					users = new HashSet<Session>();
					deskUser.put(deskId, users);
				}
				users.add(session);
				
				ds.getDesk(data.getString("roomId"), data.getString("deskId"));
				session.getBasicRemote().sendText(result.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if("sitSeat".equals(eventType)){
			DeskService ds = ServiceUtil.getDeskService(sc);
			String deskId = data.getString("deskId");
			boolean exit = data.getBoolean("exit");
			if(exit){
				CacheCenter.removeString(session.getId());
				deskUser.get(deskId).remove(session);
			}else{
				CacheCenter.setString(session.getId(), data.toString());
			}		
			
			ds.sitDesk(data.getString("roomId"), deskId, 
					PositionMap.getPosition(data.getString("position")), exit);
			
			result.put("data", obj.getJSONObject("data"));
			sendMessage(result);
		}
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("close" + session.getId());
		logger.info("webSocket " + session.getId() +" close.");
		
		clients.remove(session);
		String d = CacheCenter.getString(session.getId());
		if(StringUtil.isValid(d)){
			JSONObject result = new JSONObject();
			result.put("eventType", "sitSeat");
			JSONObject msg = JSONObject.fromObject(d);
			
			DeskService ds = ServiceUtil.getDeskService(sc);
			ds.sitDesk(msg.getString("roomId"), msg.getString("deskId"), 
					PositionMap.getPosition(msg.getString("position")), true);
			
			msg.put("exit", true);
			result.put("data", msg);
			
			sendMessage(msg);
		}
	}
	
	@OnError
	public void onError(Throwable t) {
		System.out.println("webSocket error");
		//System.out.println(t.toString());
	}
	
	private void sendMessage(JSONObject msg){
		for (Session client : clients) {
			try {
				client.getBasicRemote().sendText(msg.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
