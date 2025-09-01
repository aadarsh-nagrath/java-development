package com.enterpriseshop.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ Configuration for EnterpriseShop messaging
 * 
 * Provides:
 * - Queue definitions
 * - Exchange configurations
 * - Binding setup
 * - Message converter configuration
 */
@Configuration
public class RabbitMQConfig {
    
    @Value("${spring.rabbitmq.host:localhost}")
    private String host;
    
    @Value("${spring.rabbitmq.port:5672}")
    private int port;
    
    @Value("${spring.rabbitmq.username:admin}")
    private String username;
    
    @Value("${spring.rabbitmq.password:admin}")
    private String password;
    
    // Exchange names
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
    
    // Queue names
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String SMS_QUEUE = "sms.queue";
    public static final String PUSH_QUEUE = "push.queue";
    public static final String ORDER_PROCESSING_QUEUE = "order.processing.queue";
    public static final String PAYMENT_PROCESSING_QUEUE = "payment.processing.queue";
    public static final String DEAD_LETTER_QUEUE = "dead.letter.queue";
    
    // Routing keys
    public static final String EMAIL_ROUTING_KEY = "notification.email";
    public static final String SMS_ROUTING_KEY = "notification.sms";
    public static final String PUSH_ROUTING_KEY = "notification.push";
    public static final String ORDER_ROUTING_KEY = "order.created";
    public static final String PAYMENT_ROUTING_KEY = "payment.processed";
    
    /**
     * Configure message converter for JSON
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    /**
     * Configure RabbitTemplate with JSON converter
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
    
    /**
     * Dead Letter Exchange for failed messages
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }
    
    /**
     * Dead Letter Queue for failed messages
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE)
                .withArgument("x-message-ttl", 86400000) // 24 hours
                .build();
    }
    
    /**
     * Bind dead letter queue to exchange
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("dead.letter");
    }
    
    /**
     * Notification Exchange for all notification types
     */
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }
    
    /**
     * Email Queue for email notifications
     */
    @Bean
    public Queue emailQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", "dead.letter");
        args.put("x-message-ttl", 300000); // 5 minutes
        
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArguments(args)
                .build();
    }
    
    /**
     * SMS Queue for SMS notifications
     */
    @Bean
    public Queue smsQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", "dead.letter");
        args.put("x-message-ttl", 300000); // 5 minutes
        
        return QueueBuilder.durable(SMS_QUEUE)
                .withArguments(args)
                .build();
    }
    
    /**
     * Push Notification Queue
     */
    @Bean
    public Queue pushQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", "dead.letter");
        args.put("x-message-ttl", 300000); // 5 minutes
        
        return QueueBuilder.durable(PUSH_QUEUE)
                .withArguments(args)
                .build();
    }
    
    /**
     * Order Processing Queue
     */
    @Bean
    public Queue orderProcessingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", "dead.letter");
        args.put("x-message-ttl", 600000); // 10 minutes
        
        return QueueBuilder.durable(ORDER_PROCESSING_QUEUE)
                .withArguments(args)
                .build();
    }
    
    /**
     * Payment Processing Queue
     */
    @Bean
    public Queue paymentProcessingQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", "dead.letter");
        args.put("x-message-ttl", 600000); // 10 minutes
        
        return QueueBuilder.durable(PAYMENT_PROCESSING_QUEUE)
                .withArguments(args)
                .build();
    }
    
    /**
     * Bind email queue to notification exchange
     */
    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(notificationExchange())
                .with(EMAIL_ROUTING_KEY);
    }
    
    /**
     * Bind SMS queue to notification exchange
     */
    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind(smsQueue())
                .to(notificationExchange())
                .with(SMS_ROUTING_KEY);
    }
    
    /**
     * Bind push queue to notification exchange
     */
    @Bean
    public Binding pushBinding() {
        return BindingBuilder.bind(pushQueue())
                .to(notificationExchange())
                .with(PUSH_ROUTING_KEY);
    }
    
    /**
     * Order Exchange for order-related events
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }
    
    /**
     * Bind order processing queue to order exchange
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderProcessingQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }
    
    /**
     * Payment Exchange for payment-related events
     */
    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(PAYMENT_EXCHANGE);
    }
    
    /**
     * Bind payment processing queue to payment exchange
     */
    @Bean
    public Binding paymentBinding() {
        return BindingBuilder.bind(paymentProcessingQueue())
                .to(paymentExchange())
                .with(PAYMENT_ROUTING_KEY);
    }
}
