package todo.app.todo;

import java.net.URI;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import todo.domain.model.Todo;
import todo.domain.model.group.Create;
import todo.domain.service.todo.TodoService;

@Path("todos")
@RequestScoped
public class TodoResource {

    @EJB
    protected TodoService todoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Todo> getTodos() {
        return todoService.findAll();
    }

    @GET
    @Path("{todoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Todo getTodo(@PathParam("todoId") Integer todoId) {
        return todoService.findOne(todoId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTodos(@Valid @ConvertGroup(from = Default.class, to = Create.class) Todo todo, @Context UriInfo uriInfo) {
        Todo createdTodo = todoService.create(todo);
        Integer todoId = createdTodo.getTodoId();
        URI newUri = uriInfo.getRequestUriBuilder()
                .path(todoId.toString()).build();
        return Response.created(newUri).entity(createdTodo).build();
    }

    @PUT
    @Path("{todoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Todo putTodo(@PathParam("todoId") Integer todoId) {
        Todo todo = todoService.finish(todoId);
        return todo;
    }

    @DELETE
    @Path("{todoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteTodo(@PathParam("todoId") Integer todoId) {
        todoService.delete(todoId);
    }
}
