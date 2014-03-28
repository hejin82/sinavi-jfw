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

package jp.co.ctc_g.jse.core.amqp;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

public final class TestQueueCleaner {

    public static void cleaner() {
        RabbitAdmin admin = new RabbitAdmin(factory());
        admin.setAutoStartup(true);
        admin.deleteExchange("error.exchange");
        admin.deleteExchange("exception.exchange");
        admin.deleteQueue("recoverable.exception.messages.queue");
        admin.deleteQueue("unrecoverable.exception.messages.queue");
    }
    
    public static void retryCleaner() {
        RabbitAdmin admin = new RabbitAdmin(factory());
        admin.setAutoStartup(true);
        admin.deleteExchange("retry.test.exchange");
        admin.deleteQueue("retry.test.queue");
    }
    
    private static ConnectionFactory factory() {
        CachingConnectionFactory factory = new CachingConnectionFactory("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setChannelCacheSize(10);
        return factory;
    }

}
