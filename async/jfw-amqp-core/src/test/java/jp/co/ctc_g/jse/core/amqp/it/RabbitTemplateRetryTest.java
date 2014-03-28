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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.net.ConnectException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rabbitmq.client.ConnectionFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RetryTestConsumerContextConfig.class)
@ActiveProfiles("development")
public class RabbitTemplateRetryTest {

    @Autowired
    private RabbitTemplate template;

    private CountDownLatch retry;

    private ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

    @Before
    public void setup() throws Exception {
        retry = new CountDownLatch(10);
        template.setConnectionFactory(new CachingConnectionFactory(connectionFactory));
    }

    @Test
    public void メッセージ送信時に例外が発生した場合はリトライが実行される() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                retry.countDown();
                throw new AmqpConnectException(new ConnectException("Connection refused: connect"));
            }
        }).when(connectionFactory).newConnection((ExecutorService) null);
        try {
            template.convertAndSend("retry.test.exchange", "retry.test.binding", new RetryTestBean("test"));
        } catch (AmqpConnectException e) {
            assertThat(e.getCause().getMessage(), is("Connection refused: connect"));
        }
        assertThat(retry.getCount(), is(0L));
    }

    @Test
    public void メッセージ受信時に例外が発生した場合はリトライが実行される() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                retry.countDown();
                throw new AmqpConnectException(new ConnectException("Connection refused: connect"));
            }
        }).when(connectionFactory).newConnection((ExecutorService) null);
        try {
            template.receiveAndConvert("retry.test.exchange");
        } catch (AmqpConnectException e) {
            assertThat(e.getCause().getMessage(), is("Connection refused: connect"));
        }
        assertThat(retry.getCount(), is(0L));
    }
}
