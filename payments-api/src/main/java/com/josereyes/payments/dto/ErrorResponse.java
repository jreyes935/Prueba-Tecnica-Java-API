package com.josereyes.payments.dto;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timeStamp, int status, String message, String path){}