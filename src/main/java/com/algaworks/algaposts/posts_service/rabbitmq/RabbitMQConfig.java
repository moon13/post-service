package com.algaworks.algaposts.posts_service.rabbitmq;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    private static final String PROCESS_POST = "text-processor-service.post-processing.v1";
    private static final String QUEUE_POS_PROCESSAMENTO = "post-service.post-processing-result.v1.q"; // Pós PRocessamento
    public static final String QUEUE_PROCESS_POST =  PROCESS_POST + ".q";


    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper){
        return new Jackson2JsonMessageConverter(objectMapper);

    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
       return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queue(){
        return QueueBuilder.durable(QUEUE_PROCESS_POST)
                .build();
    }

    @Bean
    public Queue queuePosProcesamento(){
        return QueueBuilder.durable(QUEUE_POS_PROCESSAMENTO)
                .build();
    }

    @Bean
    public FanoutExchange exchange(){
        return ExchangeBuilder.fanoutExchange(
                "post-processing.post-received.v1.e"
        ).build();
    }

    @Bean
    public Binding bindingProcessPost(){
        return BindingBuilder.bind(queue()).to(exchange());
    }

    @Bean
    public Binding bindingProcessPosProcessamneto(){
        return BindingBuilder.bind(queuePosProcesamento()).to(exchange());
    }

}
