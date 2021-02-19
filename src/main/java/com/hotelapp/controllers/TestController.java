package com.hotelapp.controllers;

import com.hotelapp.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/tests")
@Validated
public class TestController {

    @Autowired
    @NotNull MailService mailService;

    @GetMapping
    public String exampleResponseParamDependent(@RequestParam(required = false) String text){

        if(isNull(text))
            return "No text given";

        return text;
    }

    @PostMapping
    public String sendEmail(@RequestParam String from, @RequestParam String to){


        String sampleText= "Samples text";

        String samplThema= "Tests";

        return mailService.sendMail(to,sampleText,from,samplThema);
    }
}
