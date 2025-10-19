package com.algaworks.algaposts.posts_service.rabbitmq;

import com.algaworks.algaposts.posts_service.api.model.PostOutput;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.algaworks.algaposts.posts_service.rabbitmq.RabbitMQConfig.QUEUE_PROCESS_POST;


@Slf4j
@RequiredArgsConstructor
@Component
public class RabbitMQListener {

   // private final TemperatureMonitoringService temperatureMonitoringService;

    private final RabbitTemplate rabbitTemplate;


    @SneakyThrows
    @RabbitListener(queues = QUEUE_PROCESS_POST, concurrency = "2-3")
    public void handleProcessingPost(@Payload PostOutput postOutput,
                       @Headers Map<String,Object> headers

    ){
        log.info("Post updated. Postid {} Author {}", postOutput.getId(), postOutput.getAuthor());

        String fila =  "text-processor-service.post-processing.v1.q";
        String exchange = "post-processing.post-received.v1.e";
        String routingKey = "";
        Object payload = postOutput;

        rabbitTemplate.convertAndSend(exchange, routingKey, payload);

       // temperatureMonitoringService.processTemperatureReading(temperatureLogData);
       Thread.sleep(Duration.ofSeconds(5));
    }

}
