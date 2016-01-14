package com.lanfeng.gupai.gameServer;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator{

	@Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request,  
    		HandshakeResponse response){
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        Map<String, Object> up = config.getUserProperties();
        if(up == null){
        	System.out.println("modifyHandshake error");
        }else{
        	up.put(HttpSession.class.getName(),httpSession);
        }
    }
}
