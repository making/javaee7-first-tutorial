package todo.app.todo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.websocket.Session;
import todo.domain.service.todo.TodoEvent;
import todo.domain.service.todo.TodoEventModel;

@ApplicationScoped
public class TodoEventObserver {
    
    private static final Logger logger = Logger.getLogger(TodoEventObserver.class.getName());

    public void onEventMessage(@Observes @TodoEvent TodoEventModel todoEventModel) {
        for (Session s : TodoWebSocketEndPoint.sessions) {
            try {
                s.getBasicRemote().sendText(todoEventModel.toString());
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
}
