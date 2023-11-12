package com.example.thymeleaf.controller;

import com.example.thymeleaf.entity.Address;
import com.example.thymeleaf.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView noSuchElementExceptionHandler(NoSuchElementException exception) {
        log.error(exception.getMessage());
        return new ModelAndView("exception")
                .addObject("status", HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({Address.InvalidAddressDataException.class, Student.InvalidStudentDataException.class})
    public ModelAndView defaultExceptionHandler(RuntimeException exception) {
        log.error(exception.getMessage());
        return new ModelAndView("exception")
                .addObject("status", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return new ModelAndView("exception")
                .addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
