package com.josereyes.payments.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.josereyes.payments.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler{

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(MethodArgumentNotValidException.class)                    //400 Bad Request
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request){
        String message = "Datos invalidos";

        if (!exception.getBindingResult().getFieldErrors().isEmpty()){
            message = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        }

        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), message, request.getRequestURI());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(PaymentNotFoundException.class)                       //404 Not Found
    public ResponseEntity<ErrorResponse> handlerPaymentNotFound(PaymentNotFoundException exception, HttpServletRequest request){
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), exception.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidPaymentStatusTransitionException.class)        //409 Conflict
    public ResponseEntity<ErrorResponse> handleInvalidTransition(InvalidPaymentStatusTransitionException exception, HttpServletRequest request){
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)                //400 Bad Request
    public ResponseEntity<ErrorResponse> handlerInvalidJson(HttpMessageNotReadableException exception, HttpServletRequest request){
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "El cuerpo de la peticion falta o contiene datos invalidos", request.getRequestURI());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)            //400 Bad Request
    public ResponseEntity<ErrorResponse> handlerInvaldParameter(MethodArgumentTypeMismatchException exception, HttpServletRequest request){
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Valor invalido para el parametro: " + exception.getName(), request.getRequestURI());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)                                      //500 Internal Server Error
    public ResponseEntity<ErrorResponse> handlerUnexpectedError(Exception exception, HttpServletRequest request){
        if (exception instanceof org.springframework.web.ErrorResponse){
            org.springframework.web.ErrorResponse springError = (org.springframework.web.ErrorResponse) exception;

            ErrorResponse response = new ErrorResponse(LocalDateTime.now(), springError.getStatusCode().value(), "No se puede procesar la peticion", request.getRequestURI());

            return ResponseEntity.status(springError.getStatusCode()).body(response);
        }

        log.error("Error inesperado al procesar la paticion", exception);

        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrio un error interno", request.getRequestURI());
        

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}