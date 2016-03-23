package com.lanfeng.gupai.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

import com.lanfeng.gupai.gameServer.GameServer;

public class SystemInitListener implements ServletContextListener {

	public SystemInitListener() {
		// TODO Auto-generated constructor stub
	}

	public void contextDestroyed(ServletContextEvent ctxEvent) {
		System.out.println("SIL destoried");
	}

	public void contextInitialized(ServletContextEvent ctxEvent) {
        addEndpoints(ctxEvent);
        System.out.println("SystemInitListener end");
	}
	
	/**
	 * 添加ws端点
	 * @param ctxEvent
	 */
	private void addEndpoints(ServletContextEvent ctxEvent) {
		/*********Endpoint *******************/
		final ServerContainer serverContainer = (ServerContainer)ctxEvent
				.getServletContext().getAttribute(
						"javax.websocket.server.ServerContainer");
		try {
			ServerEndpointConfig c = ServerEndpointConfig.Builder.create(
					GameServer.class, "/gameServer").build();
			c.getUserProperties().put("ServletContext",
					ctxEvent.getServletContext());
			
			serverContainer.addEndpoint(c);
		} catch (DeploymentException e) {
			System.out.println("addEndpoints error");
			System.out.println(e.getMessage());
			System.out.println(e);
		}
	}

}
