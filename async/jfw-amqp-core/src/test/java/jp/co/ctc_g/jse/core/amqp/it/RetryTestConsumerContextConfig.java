/*
 * Copyright (c) 2013 ITOCHU Techno-Solutions Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.ctc_g.jse.core.amqp.it;

import jp.co.ctc_g.jse.core.amqp.config.ExceptionQueueContextConfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:/jp/co/ctc_g/jse/core/amqp/it/RabbitMQ.properties")
public class RetryTestConsumerContextConfig extends ExceptionQueueContextConfig {

    @Bean
    @DependsOn("amqpAdmin")
    public Exchange retryTestExchange() {
        return new DirectExchange("retry.test.exchange");
    }

    @Bean
    @DependsOn("amqpAdmin")
    public Binding binding() {
        return BindingBuilder.bind(retryTestQueue()).to(retryTestExchange()).with("retry.test.binding").noargs();
    }

    @Bean
    @DependsOn("amqpAdmin")
    public Queue retryTestQueue() {
        return new Queue("retry.test.queue", true, false, false, deadLetterArguments());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreResourceNotFound(true);
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

}