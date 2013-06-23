package todo.domain.service.todo;

import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.transaction.UserTransaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import todo.domain.common.exception.ResourceNotFoundException;
import todo.domain.model.Todo;

public class TodoServiceTest {

    private EJBContainer container;
    private Context context;

    public TodoServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        context = container.getContext();
    }

    @After
    public void tearDown() {
        container.close();
    }

    /**
     * Test of findAll method, of class TodoService.
     */
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        TodoService instance = (TodoService) context.lookup("java:global/classes/TodoService");
        List<Todo> result = instance.findAll();
        System.out.println(result);
        assertNotNull(result);
    }

    /**
     * Test of create method, of class TodoService.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Todo todo = new Todo();
        UserTransaction ut = (UserTransaction) context.lookup("java:comp/UserTransaction");
        TodoService instance = (TodoService) context.lookup("java:global/classes/TodoService");
        ut.begin();
        todo.setTodoTitle("hoge");

        Todo result = instance.create(todo);
        ut.commit();
        System.out.println("created " + result);
        assertNotNull(result);
        assertNotNull(result.getTodoId());

        ut.begin();
        instance.delete(result.getTodoId());
        ut.commit();
    }

    /**
     * Test of finish method, of class TodoService.
     */
    @Test
    public void testFinish() throws Exception {
        System.out.println("finish");
        Todo todo = new Todo();
        UserTransaction ut = (UserTransaction) context.lookup("java:comp/UserTransaction");
        TodoService instance = (TodoService) context.lookup("java:global/classes/TodoService");
        ut.begin();
        todo.setTodoTitle("hoge");

        Todo result = instance.create(todo);
        ut.commit();
        assertNotNull(result);

        ut.begin();
        Integer todoId = result.getTodoId();
        System.out.println("finish [" + todoId + "]");
        Todo finishedTodo = instance.finish(todoId);
        ut.commit();
        assertNotNull(finishedTodo);
        assertTrue(finishedTodo.isFinished());
        ut.begin();
        instance.delete(todoId);
        ut.commit();
    }

    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        Todo todo = new Todo();
        UserTransaction ut = (UserTransaction) context.lookup("java:comp/UserTransaction");
        TodoService instance = (TodoService) context.lookup("java:global/classes/TodoService");
        ut.begin();
        todo.setTodoTitle("hoge");

        Todo result = instance.create(todo);
        ut.commit();
        assertNotNull(result);

        ut.begin();
        Integer todoId = result.getTodoId();
        System.out.println("delete [" + todoId + "]");
        instance.delete(todoId);
        ut.commit();

        try {
            instance.findOne(todoId);
            fail(todoId + " is not deleted!");
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}