package b_yousefi.bookshop.error_handling;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    // 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final List<ErrorCause> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorCause(error.getField(), error.getDefaultMessage()));
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(new ErrorCause(error.getObjectName(), error.getDefaultMessage()));
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final List<ErrorCause> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorCause(error.getField(), error.getDefaultMessage()));
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(new ErrorCause(error.getObjectName(), error.getDefaultMessage()));
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();
        ErrorCause errorCause = new ErrorCause(ex.getPropertyName(), error);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = ex.getRequestPartName() + " part is missing";
        ErrorCause errorCause = new ErrorCause(ex.getRequestPartName(), error);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = ex.getParameterName() + " parameter is missing";
        ErrorCause errorCause = new ErrorCause(ex.getParameterName(), error);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }



    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        ErrorCause errorCause = new ErrorCause(ex.getName(), error);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({org.hibernate.exception.ConstraintViolationException.class})
    public ResponseEntity<Object> handleHibernateConstraintViolationException(final org.hibernate.exception.ConstraintViolationException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());

        String message = ex.getLocalizedMessage();
        if (message.contains("UK_CATEGORY__NAME_PARENT")) {
            message = "Duplicate name, please choose another name";
        }
        ErrorCause errorCause = new ErrorCause(ex.getConstraintName(), message);
        final ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Data validation failed", errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDaoDataIntegrityViolationException(final DataIntegrityViolationException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());

        ErrorCause errorCause;
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            org.hibernate.exception.ConstraintViolationException exDetail =
                    (org.hibernate.exception.ConstraintViolationException) ex.getCause();
            if(exDetail.getConstraintName()!=null) {
                String cause = exDetail.getConstraintName();
                String message = exDetail.getLocalizedMessage();
                if (cause.contains("UK_CATEGORY__NAME_PARENT")) {
                    message = "Duplicate category name, in each category subcategory names must be unique";
                } else if (cause.contains("UK_USER_username")) {
                    message = "This username already exists";
                } else if (cause.contains("UK_USER_email")) {
                    message = "This email already exists";
                } else if (cause.contains("UK_USER_phoneNumber")) {
                    message = "This phone number already exists";
                } else if (cause.contains("UK_AUTHOR__fullName")) {
                    message = "This author name already exists";
                } else if (cause.contains("UK_ORDER_ITEM__order_book")) {
                    message = "This book has been already added to the order";
                }
                errorCause = new ErrorCause(cause, message);
            } else if(exDetail.getCause() instanceof SQLIntegrityConstraintViolationException){
                String message = exDetail.getCause().getLocalizedMessage();
                if(message.contains("Cannot delete or update a parent row: a foreign key constraint fails")){
                    message = "Error occurred while deleting, data has some relation to other data!";
                }
                errorCause = new ErrorCause("Database Error", message);
            } else {
                errorCause = new ErrorCause("Database Error", exDetail.getLocalizedMessage());
            }
        } else {
            errorCause = new ErrorCause(DataIntegrityViolationException.class.getName(), ex.getLocalizedMessage());
        }

        final ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Data validation failed", errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final List<ErrorCause> errors = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            ErrorCause errorCause = new ErrorCause(violation.getPropertyPath().toString(), violation.getMessage());
            errors.add(errorCause);
        }

        final ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Validation Failed", errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        ErrorCause errorCause = new ErrorCause(ex.getHttpMethod(), error);
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = "method" + ex.getMethod() + " is not supported for this request";
        ErrorCause errorCause = new ErrorCause(ex.getMethod(), error);
        final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 415

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = "media type" + ex.getContentType() + " is not supported";
        ErrorCause errorCause = new ErrorCause(Objects.requireNonNull(ex.getContentType()).toString(), error);
        final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 401

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(final AuthenticationException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());

        ErrorCause errorCause = new ErrorCause(AuthenticationException.class.getName(), ex.getMessage());
        final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(),
                errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 403

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());

        ErrorCause errorCause = new ErrorCause(AccessDeniedException.class.getName(), ex.getMessage());
        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 500

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        logger.info(ex.getClass().getName());

        if (ex instanceof TransactionSystemException) {
            if (((TransactionSystemException) ex).getMostSpecificCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) ((TransactionSystemException) ex).getMostSpecificCause();
                return handleConstraintViolation(constraintViolationException, request);
            }
        }

        ErrorCause errorCause = new ErrorCause(null, ex.getMessage());
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errorCause);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
