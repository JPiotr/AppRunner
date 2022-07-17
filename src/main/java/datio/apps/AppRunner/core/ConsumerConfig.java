package datio.apps.AppRunner.core;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class ConsumerConfig {

    @Bean
    public Queue typeQueue() {
        return new Queue("type-collector");
    }

    @Bean
    public Queue appRunner() {
        return new Queue("app-runner");
    }
}
