/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.socketi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.podaci.Jms2Poruka;

/**
 *
 * @author Marko Domladovac
 */
@ServerEndpoint(
        value = "/jms2",
        decoders = {Jms2Coder.class},
        encoders = {Jms2Coder.class}
)
public class Jms2WebSocket {

    private static List<Session> sessions = new ArrayList<>();

    @OnMessage
    public Jms2Poruka onMessage(Jms2Poruka poruka) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(JsonHelper.createJsonObjectString(poruka));
            } catch (IOException ex) {
                Logger.getLogger(Jms2WebSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return poruka;
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }
}
