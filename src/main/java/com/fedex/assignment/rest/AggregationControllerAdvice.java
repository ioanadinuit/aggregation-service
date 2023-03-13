package com.fedex.assignment.rest;

import com.fedex.assignment.model.ErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AggregationControllerAdvice {

    @ExceptionHandler({ExecutionException.class, InterruptedException.class})
    public ResponseEntity<ErrorResponseDTO> webfluxInterrupted(Throwable err) {
        return ResponseEntity.internalServerError().body(
                ErrorResponseDTO.builder()
                        .message(err.getMessage())
                        .status(500)
                        .build()
        );
    }
}
