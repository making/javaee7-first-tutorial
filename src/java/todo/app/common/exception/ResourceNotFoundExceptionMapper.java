package todo.app.common.exception;

import java.util.Arrays;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import todo.domain.common.exception.ResourceNotFoundException;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        ErrorModel errorModel = new ErrorModel(Arrays.asList(exception.getMessage()));
        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorModel).build();
    }
}
