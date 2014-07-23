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

package jp.co.ctc_g.jse.core.amqp.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import jp.co.ctc_g.jse.core.amqp.config.amqp.DefaultProperties;
import jp.co.ctc_g.jse.core.amqp.config.amqp.InvalidProperties;
import jp.co.ctc_g.jse.core.amqp.config.amqp.OverrideProperties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(Enclosed.class)
public class AmqpContextConfigTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = DefaultProperties.class)
    public static class AmqpContextConfigLoadTest {

        @Autowired
        private ApplicationContext context;

        @Autowired
        private AmqpContextConfig config;

        @Test
        public void デフォルト値が設定されて指定のインスタンスがDIコンテナに登録される() {
            assertThat(config.host, is("127.0.0.1"));
            assertThat(config.port, is("5672"));
            assertThat(config.username, is("guest"));
            assertThat(config.password, is("guest"));
            assertThat(config.channelCacheSize, is(10));
            assertThat(context.getBean(ConnectionFactory.class), is(notNullValue()));
            assertThat(context.getBean(RabbitTemplate.class), is(notNullValue()));
            assertThat(context.getBean(MessageConverter.class), is(notNullValue()));
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = OverrideProperties.class)
    public static class AmqpContextConfigOverridePropertiesLoadTest {
        @Autowired
        private ApplicationContext context;

        @Autowired
        private AmqpContextConfig config;

        @Test
        public void 値がオーバライドされて指定のインスタンスがDIコンテナに登録される() {
            assertThat(config.host, is("192.168.10.10"));
            assertThat(config.port, is("5673"));
            assertThat(config.username, is("jfw"));
            assertThat(config.password, is("jfw"));
            assertThat(config.channelCacheSize, is(100));
            assertThat(context.getBean(ConnectionFactory.class), is(notNullValue()));
            assertThat(context.getBean(RabbitTemplate.class), is(notNullValue()));
            assertThat(context.getBean(MessageConverter.class), is(notNullValue()));
        }
    }

    @RunWith(BlockJUnit4ClassRunner.class)
    public static class AmqpContextConfigInvalidPropertiesLoadTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void ポート番号が不正な場合は例外がスローされる() throws Throwable {
            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("ポート番号は数字で入力してください。");
            try {
                new AnnotationConfigApplicationContext(InvalidProperties.class);
                fail();
            } catch (BeanCreationException e) {
                throw e.getCause();
            }
        }
    }
}
