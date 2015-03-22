package com.lanfeng.gupai.gameServer;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.http.HttpServlet;
import javax.websocket.OnClose;
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
		clients.add(session);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		JSONObject jsonObj = JSONObject.fromObject(message);
		for (Session client : clients) {
			try {
				client.getBasicRemote().sendText(jsonObj.getString("txt"));
				client.getBasicRemote().sendText(CardsCreator.getInstance().getCards().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@OnClose
	public void onClose(Session peer) {
		clients.remove(peer);
	}
}
