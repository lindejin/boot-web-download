package com.rch.download.config;

import com.rch.download.config.response.RetCode;
import com.rch.download.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.rch.download.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        response.put("message", "服务器内部错误");
//
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(response);
//    }

    /**
     * 日志记录
     *
     * @param ex
     */
    @ExceptionHandler(Exception.class)
    public RetCode<String> handleException(Exception ex) {
        ex.printStackTrace();
        log.error(ExceptionUtils.getFullExceptionMessage(ex));
        return RetCode.err("Exception:" + ex);
    }
}