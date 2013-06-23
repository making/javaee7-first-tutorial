package todo.app.common.exception;

import java.util.Arrays;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        System.out.println(exception.getConstraintViolations());
        exception.printStackTrace();
        ErrorModel errorModel = new ErrorModel(Arrays.asList(exception.getMessage()));
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorModel).build();
    }
}
