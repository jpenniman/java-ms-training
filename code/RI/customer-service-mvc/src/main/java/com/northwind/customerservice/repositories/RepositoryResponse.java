package com.acme.customerservice.repositories;

public class RepositoryResponse<T> {

    private RepositoryResultStatus status;
    private String message;
    private RuntimeException exception;
    private T result;

    public RepositoryResponse(RepositoryResultStatus status, T result) {
        this.status = status;
        this.result = result;
    }

    public RepositoryResponse(RepositoryResultStatus status, RuntimeException exception) {
        this.status = status;
        this.exception = exception;
    }

    public RepositoryResultStatus getStatus() {
        return status;
    }

    public void setStatus(RepositoryResultStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RuntimeException getException() {
        return exception;
    }

    public void setException(RuntimeException exception) {
        this.exception = exception;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
