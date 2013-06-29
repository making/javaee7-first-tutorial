package todo.app.todo;

import todo.domain.service.todo.TodoEventModel;
import todo.domain.service.todo.TodoEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
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
    private static final Logger logger = Logger.getLogger(TodoWebSocketEndPoint.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        try {
            session.getBasicRemote().sendText("opened");
            logger.log(Level.INFO, "opened {0}", session);
            sessions.add(session);
            logger.log(Level.INFO, "{0} sessions", sessions.size());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @OnClose
    public void onClose(final Session session) {
        logger.log(Level.INFO, "closed {0}", session);
        sessions.remove(session);
        logger.log(Level.INFO, "{0} sessions", sessions.size());
    }

    public void onMessage(@Observes @TodoEvent TodoEventModel todoEventModel) {
        for (Session s : sessions) {
            try {
                s.getBasicRemote().sendText(todoEventModel.toString());
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @OnError
    public void onError(Throwable e) {
        logger.log(Level.SEVERE, "Unexcepted Exception happened!", e);
    }
}
