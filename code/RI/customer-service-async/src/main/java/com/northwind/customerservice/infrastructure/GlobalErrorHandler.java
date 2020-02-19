package com.northwind.customerservice.infrastructure;

import com.northwind.customerservice.api.ApiError;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler /*extends ResponseEntityExceptionHandler*/ {

    private MeterRegistry meterRegistry;
    private Log logger;

    public GlobalErrorHandler(MeterRegistry meterRegistry, LoggerFactory loggerFactory) {
        this.meterRegistry = meterRegistry;
        this.logger = loggerFactory.getLog("com.northwind.customerservice.api");
    }

    private ApiError parseError(Exception ex, WebRequest request) {
        ApiError error = new ApiError();
        error.setDetail(ex.getMessage());
        error.setSource(request.getDescription(false).split("=")[1]);
        return error;
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiError> handleGlobalException(Exception ex, WebRequest request) {
        ApiError error = parseError(ex, request);
        error.setTitle("Server Error");
        logger.error(error.toString(), ex);
        meterRegistry.counter("request.failure").increment();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiError> handleUnprocessableEntity(IllegalArgumentException ex, WebRequest request) {
        ApiError error = parseError(ex, request);
        error.setTitle("Unable to process entity");
        logger.debug(error.toString(), ex);
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    //@Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError error = parseError(ex, request);
        error.setTitle("Bad request");
        error.setDetail("The request is invalid. Please refer to the documentation for details on how to construct a valid request for " + error.getSource());
        logger.debug(error.toString(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

   // @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError error = parseError(ex, request);
        error.setTitle("Bad request");
        error.setDetail("The request is invalid. Please refer to the documentation for details on how to construct a valid request for " + error.getSource());
        logger.debug(error.toString(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //@Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError error = parseError(ex, request);
        error.setTitle("Bad request");
        error.setDetail("The request is invalid. Please refer to the documentation for details on how to construct a valid request for " + error.getSource());
        logger.debug(error.toString(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //@Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError error = parseError(ex, request);
        error.setTitle("Bad request");
        error.setDetail("The request is invalid. Please refer to the documentation for details on how to construct a valid request for " + error.getSource());
        logger.debug(error.toString(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
