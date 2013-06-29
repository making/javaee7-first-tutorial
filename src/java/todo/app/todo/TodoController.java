package todo.app.todo;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import todo.domain.common.exception.BusinessException;
import todo.domain.model.Todo;
import todo.domain.service.todo.TodoService;

@Named(value = "todoController")
@RequestScoped
public class TodoController {

    @EJB
    protected TodoService todoService;
    protected Todo todo = new Todo();
    protected List<Todo> todoList;

    /**
     * Creates a new instance of TodoController
     */
    public TodoController() {
    }

    public Todo getTodo() {
        return todo;
    }

    public List<Todo> getTodoList() {
        return todoList;
    }

    @PostConstruct
    public void init() {
        todoList = todoService.findAll();
    }

    public String create() {
        try {
            todoService.create(todo);
        } catch (BusinessException e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            return "list.xhtml";
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Created successfully!", null));

        return "list.xhtml?faces-redirect=true";
    }

    public String finish(Integer todoId) {
        System.out.println("finish " + todoId);
        try {
            todoService.finish(todoId);
        } catch (BusinessException e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            return "list.xhtml";
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Finished successfully!", null));

        return "list.xhtml?faces-redirect=true";
    }

    public String delete(Integer todoId) {
        todoService.delete(todoId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted successfully!", null));
        return "list.xhtml?faces-redirect=true";
    }
}
