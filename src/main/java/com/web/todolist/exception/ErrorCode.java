package com.web.todolist.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(9998, "invalid key", HttpStatus.BAD_REQUEST),

    TODO_NOT_FOUND(1001, "Todo not found", HttpStatus.NOT_FOUND),
    TITLE_EXISTS(1002, "Title already exists", HttpStatus.CONFLICT),

    TITLE_REQUIRED(1003, "Title is required", HttpStatus.CONFLICT),
    TITLE_INVALID(1004, "Title must be less than 100 characters", HttpStatus.CONFLICT),
    DESCRIPTION_INVALID(1005, "Description must be less than 500 characters", HttpStatus.CONFLICT),
    ;

    


    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
