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

    public static final String QUEUE_POST_PROCESSING = "post-processing.v1.q";
    public static final String DEAD_LETTER_QUEUE_POST_PROCESSING = "post-processing.v1.dlq";
    public static final String QUEUE_POST_RESULT = "post-processing-result.v1.q"; // Pós PRocessamento
    public static final String DEAD_LETTER_QUEUE_POST_RESULT = "post-processing-result.v1.dlq";



    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper){
        return new Jackson2JsonMessageConverter(objectMapper);

    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
       return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queuePostProcessing(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_POST_PROCESSING);
        return QueueBuilder.durable(QUEUE_POST_PROCESSING).withArguments(args)
                .build();
    }

    @Bean
    public Queue deadLetterQueuePostProcessing(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_POST_PROCESSING)
                .build();
    }

    @Bean
    public Queue queuePostProcessingResult(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_POST_RESULT);
        return QueueBuilder.durable(QUEUE_POST_RESULT).withArguments(args)
                .build();
    }


    @Bean
    public Queue deadLetterQueuePostProcessingResult(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_POST_RESULT)
                .build();
    }




}
