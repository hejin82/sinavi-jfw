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

package jp.co.ctc_g.jse.core.amqp.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import jp.co.ctc_g.jfw.core.util.Maps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/jp/co/ctc_g/jse/core/amqp/exception/ErrorResources-Context.xml")
public class AmqpApplicationRecoverableExceptionTest {

    @Test
    public void メッセージコードが設定されたインスタンスが生成される() {
        AmqpApplicationRecoverableException exception = new AmqpApplicationRecoverableException("nfcode");
        assertThat(exception, instanceOf(AmqpApplicationRecoverableException.class));
        assertThat(exception.getCode(), is("nfcode"));
        assertThat(exception.getMessage(), is("nfcode"));
        assertThat(exception.getRoutingKey(), is("application.recoverable.exception.key"));
        assertThat(exception.getDate(), notNullValue());
        assertThat(exception.getId(), notNullValue());
    }

    @Test
    public void リソースより取得したメッセージが設定されたインスタンスが生成される() {
        AmqpApplicationRecoverableException exception = new AmqpApplicationRecoverableException("code", Maps.hash("id", "12345"));
        assertThat(exception, instanceOf(AmqpApplicationRecoverableException.class));
        assertThat(exception.getCode(), is("code"));
        assertThat(exception.getMessage(), is("例外テスト ID:12345"));
        assertThat(exception.getRoutingKey(), is("application.recoverable.exception.key"));
        assertThat(exception.getDate(), notNullValue());
        assertThat(exception.getId(), notNullValue());
    }

    @Test
    public void メッセージコードとルーティングキーが設定されたインスタンスが生成される() {
        AmqpApplicationRecoverableException exception = new AmqpApplicationRecoverableException("nfcode", "routing.key");
        assertThat(exception, instanceOf(AmqpApplicationRecoverableException.class));
        assertThat(exception.getCode(), is("nfcode"));
        assertThat(exception.getMessage(), is("nfcode"));
        assertThat(exception.getRoutingKey(), is("routing.key"));
        assertThat(exception.getDate(), notNullValue());
        assertThat(exception.getId(), notNullValue());
    }

    @Test
    public void リソースより取得したメッセージとルーティングキーが設定されたインスタンスが生成される() {
        AmqpApplicationRecoverableException exception = new AmqpApplicationRecoverableException("code", Maps.hash("id", "12345"), "routing.key");
        assertThat(exception, instanceOf(AmqpApplicationRecoverableException.class));
        assertThat(exception.getCode(), is("code"));
        assertThat(exception.getMessage(), is("例外テスト ID:12345"));
        assertThat(exception.getRoutingKey(), is("routing.key"));
        assertThat(exception.getDate(), notNullValue());
        assertThat(exception.getId(), notNullValue());
    }

    @Test
    public void メッセージコードと原因となった例外が設定されたインスタンスが生成される() {
        AmqpApplicationRecoverableException exception = new AmqpApplicationRecoverableException("nfcode", new RuntimeException());
        assertThat(exception, instanceOf(AmqpApplicationRecoverableException.class));
        assertThat(exception.getMessage(), is("nfcode"));
        assertThat(exception.getRoutingKey(), is("application.recoverable.exception.key"));
        assertThat(exception.getCause(), instanceOf(RuntimeException.class));
        assertThat(exception.getDate(), notNullValue());
        assertThat(exception.getId(), notNullValue());
    }

    @Test
    public void リソースより取得したメッセージと原因となった例外が設定されたインスタンスが生成される() {
        AmqpApplicationRecoverableException exception = new AmqpApplicationRecoverableException("code", Maps.hash("id", "12345"), new RuntimeException());
        assertThat(exception, instanceOf(AmqpApplicationRecoverableException.class));
        assertThat(exception.getCode(), is("code"));
        assertThat(exception.getMessage(), is("例外テスト ID:12345"));
        assertThat(exception.getRoutingKey(), is("application.recoverable.exception.key"));
        assertThat(exception.getCause(), instanceOf(RuntimeException.class));
        assertThat(exception.getDate(), notNullValue());
        assertThat(exception.getId(), notNullValue());
    }

    @Test
    public void メッセージコードとルーティングキーと原因となった例外が設定されたインスタンスが生成される() {
        AmqpApplicationRecoverableException exception = new AmqpApplicationRecoverableException("nfcode", "routing.key", new RuntimeException());
        assertThat(exception instanceof AmqpApplicationRecoverableException, is(true));
        assertThat(exception.getMessage(), is("nfcode"));
        assertThat(exception.getRoutingKey(), is("routing.key"));
        assertThat(exception.getCause(), instanceOf(RuntimeException.class));
        assertThat(exception.getDate(), notNullValue());
        assertThat(exception.getId(), notNullValue());
    }

    @Test
    public void リソースより取得したメッセージとルーティングキーと原因となった例外が設定されたインスタンスが生成される() {
        AmqpApplicationRecoverableException exception = new AmqpApplicationRecoverableException("code", Maps.hash("id", "12345"), "routing.key",
            new RuntimeException());
        assertThat(exception, instanceOf(AmqpApplicationRecoverableException.class));
        assertThat(exception.getCode(), is("code"));
        assertThat(exception.getMessage(), is("例外テスト ID:12345"));
        assertThat(exception.getRoutingKey(), is("routing.key"));
        assertThat(exception.getCause(), instanceOf(RuntimeException.class));
        assertThat(exception.getDate(), notNullValue());
        assertThat(exception.getId(), notNullValue());
    }
}
