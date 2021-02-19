package com.hotelapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("private")
public class PrivateController {

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String restrictedData(){


        return "Hello it works at : "+ LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
