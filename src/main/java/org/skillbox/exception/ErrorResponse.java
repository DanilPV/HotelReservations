package org.skillbox.exception;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String message;
    private Map<String, String> errors;

}