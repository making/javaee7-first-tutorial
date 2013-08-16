package todo.app.common.exception;

import java.util.Arrays;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import todo.domain.common.exception.BusinessException;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {
        ErrorModel errorModel = new ErrorModel(Arrays.asList(exception.getMessage()));
        return Response.status(Response.Status.CONFLICT)
                .entity(errorModel).build();
    }
}
