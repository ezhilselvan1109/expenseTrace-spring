package com.expensetrace.app.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SuccessResponse {
    private boolean success;
    private int statusCode;
    private Object data;
    private String message;
    private Object meta;
}

/*
{
  "success": true,
  "statusCode":200,
  "data": {
    "id": 123,
    "name": "Example",
    "email": "example@email.com"
  },
  "message": "Resource fetched successfully"
}
*/