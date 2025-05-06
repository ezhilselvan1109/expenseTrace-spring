package com.expensetrace.app.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private boolean success;
    private int statusCode;
    private Object error;
}

/*
{
        "success":false,
        "statusCode":404,
        "error":{
            "code":"INVALID_INPUT",
            "message":"The email address is invalid",
            "details":{
            "field":"email",
            "issue":"Invalid format"
            }
        }
}
*/