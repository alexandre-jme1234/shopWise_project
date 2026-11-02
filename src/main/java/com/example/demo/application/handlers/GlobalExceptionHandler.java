package com.example.demo.application.handlers;
import com.example.demo.domain.exceptions.ClientExceptions;
import com.example.demo.domain.exceptions.RendezVousExceptions;
import com.example.demo.domain.exceptions.RendezVousStatutExceptions;
import com.example.demo.domain.exceptions.ServiceExceptions;
import com.example.demo.domain.exceptions.ShopExceptions;
import com.example.demo.domain.exceptions.UserExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientExceptions.class)
    public ProblemDetail handleClientNotFound(ClientExceptions ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler(ShopExceptions.class)
    public ProblemDetail handleShopNotFound(ShopExceptions ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ServiceExceptions.class)
    public ProblemDetail handleServiceNotFound(ServiceExceptions ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RendezVousExceptions.class)
    public ProblemDetail handleRendezVousNotFound(RendezVousExceptions ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RendezVousStatutExceptions.class)
    public ProblemDetail handleRendezVousStatut(RendezVousStatutExceptions ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UserExceptions.class)
    public ProblemDetail handleShopNotFound(UserExceptions ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }


}