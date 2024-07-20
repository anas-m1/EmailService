package com.example.emailservice.dtos;

import lombok.Data;

@Data
public class SendEmailDTO {
    private String to;
    private String from;
    private String subject;
    private String body;
}
