package todo.domain.service.todo;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import todo.domain.common.exception.BusinessException;
import todo.domain.common.exception.ResourceNotFoundException;
import todo.domain.model.Todo;

@Stateless
public class TodoService {

    private static final long MAX_UNFINISHED_COUNT = 5;
    @PersistenceContext
    protected EntityManager entityManager;
    @Inject
    @TodoEvent
    protected Event<TodoEventModel> todoEvent;

    public List<Todo> findAll() {
        TypedQuery q = entityManager.createNamedQuery("Todo.findAll", Todo.class);
        return q.getResultList();
    }

    public Todo findOne(Integer todoId) {
        Todo todo = entityManager.find(Todo.class, todoId);
        if (todo == null) {
            throw new ResourceNotFoundException("[E404] The requested Todo is not found. (id=" + todoId + ")");
        }
        return todo;
    }

    public Todo create(Todo todo) {
        TypedQuery<Long> q = entityManager.createQuery("SELECT COUNT(x) FROM Todo x WHERE x.finished = :finished", Long.class)
                .setParameter("finished", false);
        long unfinishedCount = q.getSingleResult();
        if (unfinishedCount >= MAX_UNFINISHED_COUNT) {
            throw new BusinessException("[E001] The count of un-finished Todo must not be over "
                    + MAX_UNFINISHED_COUNT + ".");
        }

        todo.setFinished(false);
        todo.setCreatedAt(new Date());
        entityManager.persist(todo);
        entityManager.flush();
        todoEvent.fire(new TodoEventModel(todo, TodoEventModel.EventType.CREATE));
        return todo;
    }

    public Todo finish(Integer todoId) {
        Todo todo = findOne(todoId);
        if (todo.isFinished()) {
            throw new BusinessException("[E002] The requested Todo is already finished. (id="
                    + todoId + ")");
        }
        todo.setFinished(true);
        entityManager.merge(todo);
        todoEvent.fire(new TodoEventModel(todo, TodoEventModel.EventType.UPDATE));
        return todo;
    }

    public void delete(Integer todoId) {
        Todo todo = findOne(todoId);
        entityManager.remove(todo);
        todoEvent.fire(new TodoEventModel(todo, TodoEventModel.EventType.DELETE));
    }
}
