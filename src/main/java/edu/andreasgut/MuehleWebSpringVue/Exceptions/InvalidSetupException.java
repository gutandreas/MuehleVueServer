package edu.andreasgut.MuehleWebSpringVue.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidSetupException extends RuntimeException{

    public InvalidSetupException(String message) {
        super(message);
    }
}
