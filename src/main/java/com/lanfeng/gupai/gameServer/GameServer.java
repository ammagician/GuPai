package com.lanfeng.gupai.gameServer;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
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
import com.lanfeng.gupai.utils.CardsCreator;
import com.lanfeng.gupai.utils.PositionMap;
import com.lanfeng.gupai.utils.ServiceUtil;
import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;

@ServerEndpoint("/gameServer")
public class GameServer extends HttpServlet implements ServletContextListener{
	/**
	 *
	 */
	public void contextInitialized(ServletContextEvent ctxEvent) {
		System.out.println("System init");
		getSC(ctxEvent);
        System.out.println("System init end");
	}
	
	public void contextDestroyed(ServletContextEvent ctxEvent) {
		System.out.println("SIL destoried");
	}
	
	/**
	 * @param ctxEvent
	 */
	private void getSC(ServletContextEvent ctxEvent) {
		sc = ctxEvent.getServletContext();
	}	
	
	private static ServletContext sc;
	private static final Logger logger = LogManager.getLogger(GameServer.class);
	private static final long serialVersionUID = -4921051955121849404L;
	private static final LinkedList<Session> clients = new LinkedList<Session>();

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("connect" + session.getId());
		clients.add(session);
		logger.info("webSocket " + session.getId() +" connected.");
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		logger.info("webSocket send message \n" + message);
		System.out.println("message" + message);
		JSONObject obj = JSONObject.fromObject(message);
		String eventType = obj.getString("eventType");
		JSONObject result = new JSONObject();
		result.put("eventType", eventType);
		if("initPlayGround".equals(eventType)){
			try {
				result.put("data", CardsCreator.getInstance().getCards().toString());
				session.getBasicRemote().sendText(result.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if("sitSeat".equals(eventType)){
			JSONObject data = obj.getJSONObject("data");
			DeskService ds = ServiceUtil.getDeskService(sc);
			boolean exit = data.getBoolean("exit");
			if(exit){
				CacheCenter.removeString(session.getId());
			}else{
				CacheCenter.setString(session.getId(), data.toString());
			}		
			
			ds.sitDesk(data.getString("roomId"), data.getString("deskId"), 
					PositionMap.getPosition(data.getString("position")), exit);
			
			for (Session client : clients) {
				try {
					result.put("data", obj.getJSONObject("data"));
					client.getBasicRemote().sendText(result.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			for (Session client : clients) {
				try {
					result.put("data", CardsCreator.getInstance().getCards().toString());
					client.getBasicRemote().sendText(result.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("close" + session.getId());
		clients.remove(session);
		logger.info("webSocket " + session.getId() +" close.");
		
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
			for (Session client : clients) {
				try {
					client.getBasicRemote().sendText(result.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@OnError
	public void onError(Throwable t) {
		System.out.println("webSocket error");
		//System.out.println(t.toString());
	}
}
