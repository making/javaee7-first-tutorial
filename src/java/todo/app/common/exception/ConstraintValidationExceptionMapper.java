package todo.app.common.exception;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            messages.add(cv.getMessage());
        }
        ErrorModel errorModel = new ErrorModel(messages);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorModel).build();
    }
}
