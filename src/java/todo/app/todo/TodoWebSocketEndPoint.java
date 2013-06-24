/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.app.todo;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author maki
 */
@ServerEndpoint("/todos/monitor")
@ApplicationScoped
public class TodoWebSocketEndPoint {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        try {
            session.getBasicRemote().sendText("opened");
            sessions.add(session);
        } catch (IOException ex) {
            Logger.getLogger(TodoWebSocketEndPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnClose
    public void onClose(final Session session) {
        try {
            session.getBasicRemote().sendText("closed");
            sessions.remove(session);
        } catch (IOException ex) {
            Logger.getLogger(TodoWebSocketEndPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onMessage(@Observes @TodoEvent TodoEventModel todoEventModel) {
        System.out.println("observe " + todoEventModel);
        for (Session s : sessions) {
            try {
                s.getBasicRemote().sendText(todoEventModel.toString());
            } catch (IOException ex) {
                Logger.getLogger(TodoWebSocketEndPoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
