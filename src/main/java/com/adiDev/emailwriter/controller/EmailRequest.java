package com.adiDev.emailwriter.controller;

import lombok.Data;

@Data
public class EmailRequest {
    private String emailContent;
    private String tone;
}
