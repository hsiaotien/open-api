package com.ceres.open.client.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import dto.Shake;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import util.JsonUtils;

@Service
@Slf4j
public class EmptyClient extends WebSocketClient {

	public EmptyClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	@Autowired
	public EmptyClient(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		log.info("new connection opened");
		// 已经连接，发送验证信息鉴权
		Shake shake = new Shake();
		shake.setType("shake");
		shake.setValue("hello");
		send(JsonUtils.serialize(shake));
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		log.info("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		log.info("received message: " + message);
	}

	@Override
	public void onMessage(ByteBuffer message) {
		log.info("received ByteBuffer");
	}

	@Override
	public void onError(Exception ex) {
		log.info("an error occurred:" + ex);
	}

//	public static void main(String[] args) throws URISyntaxException {
//		WebSocketClient client = new EmptyClient(new URI("ws://localhost:8887"));
//		client.connect();
//	}
}