package com.nagarro.af.bookingtablesystem.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class TradeWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        InputStreamReader isReader =
                new InputStreamReader(
                        Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("logged-users.txt")));
        BufferedReader br = new BufferedReader(isReader);

        String line;
        while ((line = br.readLine()) != null) {
            TextMessage message = new TextMessage(line);
            session.sendMessage(message);
            Thread.sleep(1000);
        }
        sessions.add(session);
    }

}