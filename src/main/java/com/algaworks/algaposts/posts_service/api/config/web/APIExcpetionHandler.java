package com.algaworks.algaposts.posts_service.api.config.web;

//import com.algaworks.algasensors.device.management.api.client.SensorMonitoringClientBAdGatewayException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.channels.ClosedChannelException;

@RestControllerAdvice
public class APIExcpetionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( {
            SocketTimeoutException.class,
            ConnectException.class,
            ClosedChannelException.class

    } )
    public ProblemDetail handle(IOException e ){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);

        problemDetail.setTitle("Gateway Timeout");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("/errors/gateway-timeout"));
        return problemDetail;
    }
/*
    @ExceptionHandler(SensorMonitoringClientBAdGatewayException.class)
    public ProblemDetail handle(SensorMonitoringClientBAdGatewayException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);

        problemDetail.setTitle("Bad Gateway");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("/errors/bad-gateway"));
        return problemDetail;
    }

 */
}
