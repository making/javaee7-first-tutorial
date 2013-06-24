package todo.app.todo;

import todo.domain.model.Todo;

public class TodoEventModel {

    public static enum EventType {

        CREATE, UPDATE, DELETE
    }
    private final Todo todo;
    private final EventType type;
    private final String source;

    public TodoEventModel(Todo todo, EventType type, String source) {
        this.todo = todo;
        this.type = type;
        this.source = source;
    }

    public Todo getTodo() {
        return todo;
    }

    public EventType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "TodoEventModel{" + "todo=" + todo + ", type=" + type + ", source=" + source + '}';
    }
    
    
}
