package com.ceres.open.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.TradeSub;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.JsonUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static constants.Const.*;

@Service
@Slf4j
public class SimpleServer extends WebSocketServer {

	@Autowired
	public SimpleServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
		ServerHandshakeBuilder builder = super.onWebsocketHandshakeReceivedAsServer( conn, draft, request );
		// 此处做拒绝握手
		return builder;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		//This method sends a message to the new client
		conn.send("Welcome to the server!");
		log.info("new connection to " + conn.getRemoteSocketAddress());
		// flag1
/*		String hostName = conn.getRemoteSocketAddress().getHostName();
		conn.setAttachment(hostName);
		log.info("主机:{},已经连接",hostName);*/
		// flag2
		String hostName = conn.getRemoteSocketAddress().getHostName();
		int port = conn.getRemoteSocketAddress().getPort();
		conn.setAttachment(hostName + port);
		log.info("主机:{},已经连接",hostName + port);
		// count ping
		COUNT_PING.put(conn.getAttachment(),1);
		// 断开连接过于频繁的，限制其访问
		Integer num2 = COUNT_CONN.get(hostName);
		if (num2 != null && num2 > CONN_WEIGHT) {
			conn.close();//
			return;
		}
		// count conn
		countConn(hostName);
	}

	private void countConn(String mark) {
		Integer num = COUNT_CONN.get(mark);
		if (num == null) {
			COUNT_CONN.put(mark,1);
			return;
		}
		COUNT_CONN.put(mark,num + 1);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		log.info("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
		COUNT_PING.remove(conn.getAttachment());
		log.info("Count_Ping已经移除{}",conn.getAttachment().toString());
		removeTopicFlagConn(conn);
		log.info("TOPIC_FLAG_CONN已经移除{}",conn.getAttachment().toString());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		// 1. 断开空闲连接
		String hostName = conn.getAttachment();
		Integer num = COUNT_PING.get(hostName);
		if (num == null) {
			// 说明已经移除（未及时维持心跳 或者 ping过于频繁，侵占资源）
			conn.close();
		} else {
			// update
			COUNT_PING.put(hostName,num + PING_VALUE);
		}
		// 2.处理
		log.info("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
		JsonNode jsonNode = JsonUtils.readTree(message);
		JsonNode type = jsonNode.get("type");
		String typeValue = type.asText();
		// 处理消息
		if (PING.equals(typeValue)) {
			try {
				conn.send(PONG);
			} catch (Exception e) {
				log.info("此连接已经断开,msg:{}",e.getMessage());
			}
		} else if (TRADE_TYPE.equals(typeValue)) {
			log.info("订阅trade");
			TradeSub tradeSub = JsonUtils.parse(message, TradeSub.class);
			String symbol = tradeSub.getSymbol();
			//
			Map<String, WebSocket> innerMap = TOPIC_FLAG_CONN.get(symbol);
			if (innerMap == null) {
				innerMap = new HashMap<>();
			}
			innerMap.put(conn.getAttachment(),conn);
			TOPIC_FLAG_CONN.put(symbol,innerMap);
		}
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		log.info("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		log.info("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
		COUNT_PING.remove(conn.getAttachment());
		log.info("Count_Ping已经移除{}",conn.getAttachment().toString());
		removeTopicFlagConn(conn);
		log.info("TOPIC_FLAG_CONN已经移除",conn.getAttachment().toString());
	}
	
	@Override
	public void onStart() {
		log.info("server started successfully");
	}

	private void removeTopicFlagConn(WebSocket conn) {
		Collection<Map<String, WebSocket>> values = TOPIC_FLAG_CONN.values();
		Iterator<Map<String, WebSocket>> it = values.iterator();
		while (it.hasNext()) {
			Map<String, WebSocket> connMap = it.next();
			connMap.remove(conn.getAttachment());
		}
	}
}