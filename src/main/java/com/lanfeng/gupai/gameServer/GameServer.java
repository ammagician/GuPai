package com.lanfeng.gupai.gameServer;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.http.HttpServlet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.lanfeng.gupai.utils.CardsCreator;
import com.lanfeng.gupai.utils.common.JSONObject;

@ServerEndpoint("/gameServer")
public class GameServer extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = -4921051955121849404L;
	private static final LinkedList<Session> clients = new LinkedList<Session>();

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("connect" + session.getId());
		clients.add(session);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
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
	}
	
	@OnError
	public void onError(Throwable t) {
		System.out.println("error" + t.toString());
	}
}
