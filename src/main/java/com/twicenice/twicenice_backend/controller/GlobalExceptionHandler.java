package com.twicenice.twicenice_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("ERROR: ").append(e.getClass().getName()).append("\n");
        sb.append("MESSAGE: ").append(e.getMessage()).append("\n");
        Throwable cause = e.getCause();
        int depth = 0;
        while (cause != null && depth < 5) {
            sb.append("CAUSED BY: ").append(cause.getClass().getName())
              .append(" - ").append(cause.getMessage()).append("\n");
            cause = cause.getCause();
            depth++;
        }
        System.err.println("=== GLOBAL EXCEPTION ===\n" + sb);
        return ResponseEntity.status(500).body(sb.toString());
    }
}
