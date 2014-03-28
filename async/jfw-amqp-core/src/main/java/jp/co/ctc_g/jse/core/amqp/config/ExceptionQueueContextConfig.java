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

import java.util.HashMap;
import java.util.Map;

import jp.co.ctc_g.jse.core.amqp.exception.AmqpApplicationRecoverableException;
import jp.co.ctc_g.jse.core.amqp.retry.ExceptionMessageExchanger;
import jp.co.ctc_g.jse.core.amqp.retry.LoggingErrorHandler;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.StatefulRetryOperationsInterceptorFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ErrorHandler;

/**
 * <p>
 * このクラスは、例外用のExchange/Queueの生成、ExchangeとQueueのBindを行う設定を提供します。
 * </p>
 * <p>
 * このクラスを利用した場合はRabbitMQに以下の表に示すExchange/Queueを自動生成します。
 * ただし、ExchangeとQueueの自動生成はSpringのBeanProfileのスコープに<strong>development</strong>が指定されている場合のみです。
 * それ以外のスコープではExchangeとQueueを自動生成しません。
 * また、ExchangeとQueueを生成するタイミングは
 * RabbitMQへアクセスするタイミングとなりますが、
 * {@link jp.co.ctc_g.jse.core.amqp.initializer.RabbitMQInitializer}を利用すると
 * アプリケーション起動時に生成します。
 * <table>
 *  <thead>
 *   <tr>
 *    <td>キー名/キュー名</td>
 *    <td>種別(Exchange/Queue)</td>
 *    <td>概要</td>
 *    <td>備考</td>
 *   </tr>
 *  </thead>
 *  <tbody>
 *   <tr>
 *    <td>error.exchange</td>
 *    <td>Exchange</td>
 *    <td>例外を受け付けるExchangeです。このExchangeを経由し、対象のキューに配送します。</td>
 *    <td>
 *     タイプ：TOPIC
 *     <br/>
 *     Bind：recoverable.exception.messages.queue/unrecoverable.exception.messages.queue
 *     BindigKey：#.excpetion.#
 *    </td>
 *   </tr>
 *   <tr>
 *    <td>exception.exchange</td>
 *    <td>Exchange</td>
 *    <td>回復可能・回復不可能な例外を判断するExchangeです。</td>
 *    <td>
 *     タイプ：TOPIC
 *     <br/>
 *     Bind：exception.exchange
 *     <br/>
 *     BindigKey：#.recoverable.exception.#/#.unrecoverable.exception.#
 *    </td>
 *   </tr>
 *   <tr>
 *    <td>unrecoverable.exception.messages.queue</td>
 *    <td>Queue</td>
 *    <td>
 *    回復不能例外のメッセージを管理するキューです。<br/>
 *    {@link jp.co.ctc_g.jse.core.amqp.exception.AmqpApplicationUnrecoverableException}と
 *    {@link jp.co.ctc_g.jse.core.amqp.exception.AmqpSystemException}より派生した回復不能例外が発生したメッセージが配信されます。
 *    </td>
 *    <td>-</td>
 *   </tr>
 *   <tr>
 *    <td>recoverable.exception.messages.queue</td>
 *    <td>Queue</td>
 *    <td>
 *    回復可能例外のメッセージを管理するキューです。<br/>
 *    {@link jp.co.ctc_g.jse.core.amqp.exception.AmqpApplicationRecoverableException}より派生した回復可能例外が発生したメッセージが配信されます。<br/>
 *    このキューに格納されたメッセージは指定された回数リトライを指定しても回復できなかった場合であり、
 *    例えば、10回リトライしてもAmqpApplicationRecoverableExceptionが発生する場合はこのキューにメッセージが配信されます。
 *    </td>
 *    <td>-</td>
 *   </tr>
 *  </tbody>
 * </table>
 * </p>
 * <p>
 * この設定を有効にする方法は{@link AmqpContextConfig}を参照してください。
 * </p>
 * <p>
 * また、例外用のExchangeやQueue名は次のキーを設定することにより、変更することが可能です。
 * </p>
 * <table>
 *  <thead>
 *   <tr>
 *    <th>キー</th>
 *    <th>概要</th>
 *    <th>デフォルト値</th>
 *   </tr>
 *  </thead>
 *  <tbody>
 *   <tr>
 *    <td>rabbitmq.error.exchange</td>
 *    <td>例外用のExchangeキーを指定します。</td>
 *    <td>error.exchange</td>
 *   </tr>
 *   <tr>
 *    <td>rabbitmq.exception.binding.key</td>
 *    <td>error.exchangeからexception.exchangeへと配信するBindキーを指定します。</td>
 *    <td>#.exception.#</td>
 *   </tr>
 *   <tr>
 *    <td>rabbitmq.exception.exchange</td>
 *    <td>例外のルーティングキーに応じて配信するキューを決定するExchangeキーを指定します。</td>
 *    <td>exception.exchange</td>
 *   </tr>
 *   <tr>
 *    <td>rabbitmq.unrecoverable.exception.binding.key</td>
 *    <td>exception.exchangeから回復不可能例外キューへと配信するBindキーを指定します。</td>
 *    <td>#.unrecoverable.exception.#</td>
 *   </tr>
 *   <tr>
 *    <td>rabbitmq.recoverable.exception.binding.key</td>
 *    <td>exception.exchangeから回復可能例外キューへと配信するBindキーを指定します。</td>
 *    <td>#.recoverable.exception.#</td>
 *   </tr>
 *   <tr>
 *    <td>rabbitmq.recoverable.exception.messages.queue</td>
 *    <td>回復可能例外のキュー名を指定します。</td>
 *    <td>recoverable.exception.messages.queue</td>
 *   </tr>
 *   <tr>
 *    <td>rabbitmq.unrecoverable.exception.messages.queue</td>
 *    <td>回復不可能例外のキュー名を指定します。</td>
 *    <td>unrecoverable.exception.messages.queue</td>
 *   </tr>
 *   <tr>
 *    <td>rabbitmq.unknown.unrecoverable.exception.routing.key</td>
 *    <td>予期しない例外発生時に利用するデフォルトのルーティングキーを指定します。</td>
 *    <td>unknown.unrecoverable.exception.key</td>
 *   </tr>
 *  </tbody>
 * </table>
 * @author ITOCHU Techno-Solutions Corporation.
 */
@Configuration
public class ExceptionQueueContextConfig extends AmqpContextConfig {

    /**
     * メッセージ受信後の処理で回復不能例外が発生したメッセージのルーティングキーを保存するキー
     */
    protected static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     * メッセージ受信後の処理で回復不可能例外が発生したメッセージのExchange名を保存するキー
     */
    protected static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";

    private static final String DEFAULT_ERROR_EXCHANGE_KEY = "error.exchange";
    private static final String DEFAULT_EXCEPTION_EXCHANGE_KEY = "exception.exchange";
    private static final String DEFAULT_RECOVERABLE_BINDING_KEY = "#.recoverable.exception.#";
    private static final String DEFAULT_UNRECOVERABLE_BINDING_KEY = "#.unrecoverable.exception.#";
    private static final String DEFALUT_RECOVERABLE_QUEUE_NAME = "recoverable.exception.messages.queue";
    private static final String DEFAULT_UNRECOVERABLE_EXCEPTION_QUEUE_NAME = "unrecoverable.exception.messages.queue";
    private static final String DEFAULT_EXCEPTION_BINDING_KEY = "#.exception.#";
    private static final String DEFAULT_EXCEPTION_ROUTING_KEY = "unknown.unrecoverable.exception.key";

    /**
     * 例外用のExchangeキー
     * メッセージ受信側で例外が発生したときの種類に関係なく、例外を受け付けるExchangeキーです。
     * 
     * デフォルト：error.exchange
     */
    @Value("${rabbitmq.error.exchange:" + DEFAULT_ERROR_EXCHANGE_KEY + "}")
    protected String errorExchangeKey;

    /**
     * error.exchangeからexception.exchangeへと配信するBindキー
     * 
     * デフォルト：#.excpetion.#
     */
    @Value("${rabbitmq.exception.binding.key:" + DEFAULT_EXCEPTION_BINDING_KEY + "}")
    protected String exceptionBindingKey;

    /**
     * 例外のルーティングキーに応じて配信するキューを決定するExchangeキー
     * 
     * デフォルト：exception.exchange
     */
    @Value("${rabbitmq.exception.exchange:" + DEFAULT_EXCEPTION_EXCHANGE_KEY + "}")
    protected String exceptionExchangeKey;

    /**
     * 回復可能例外を指定回数リトライしても失敗する場合にメッセージが配信されるキュー
     * 
     * デフォルト：recoverable.exception.messages.queue
     */
    @Value("${rabbitmq.recoverable.exception.messages.queue:" + DEFALUT_RECOVERABLE_QUEUE_NAME + "}")
    protected String recoverableExceptionQueue;

    /**
     * 回復不能例外が発生した場合にメッセージが配信されるキュー
     * 
     * デフォルト：unrecoverable.exception.messages.queue
     */
    @Value("${rabbitmq.unrecoverable.exception.messages.queue:" + DEFAULT_UNRECOVERABLE_EXCEPTION_QUEUE_NAME + "}")
    protected String unrecoverableExceptionQueue;

    /**
     * exception.exchangeから回復不可能例外キューへと配信するBindキー
     * 
     * デフォルト：#.unrecoverable.exception.#
     */
    @Value("${rabbitmq.unrecoverable.exception.binding.key:" + DEFAULT_UNRECOVERABLE_BINDING_KEY + "}")
    protected String unrecoverableExceptionBindingKey;

    /**
     * exception.exchangeから回復可能例外キューへと配信するBindキー
     * 
     * デフォルト：#.recoverable.exception.#
     */
    @Value("${rabbitmq.recoverable.exception.binding.key:" + DEFAULT_RECOVERABLE_BINDING_KEY + "}")
    protected String recoverableExceptionBindingKey;

    /**
     * 予期しない例外発生時に利用するデフォルトのルーティングキー
     * 
     * デフォルト：unknown.unrecoverable.exception.key
     */
    @Value("${rabbitmq.unknown.unrecoverable.exception.routing.key:" + DEFAULT_EXCEPTION_ROUTING_KEY + "}")
    protected String unknownExceptionRoutingKey;

    /**
     * デフォルトコンストラクタです。
     */
    public ExceptionQueueContextConfig() {}

    /**
     * RabbitMQの管理操作を実行する{@link AmqpAdmin}のインスタンスを生成し、DIコンテナに登録します。
     * この{@link AmqpAdmin}を利用することにより、Exchange/Queueの自動生成を行うことが可能となります。
     * 自動生成する場合はSpring のBeanProfileのスコープ指定を<strong>development</strong>に指定してください。
     * @return {@link RabbitAdmin}のインスタンス
     */
    @Bean
    @Profile("development")
    public AmqpAdmin amqpAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(factory());
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 例外用の{@link Exchange}のインスタンスを生成し、DIコンテナに登録します。
     * @return {@link TopicExchange}のインスタンス
     */
    @Bean
    public Exchange errorExchange() {
        return new TopicExchange(errorExchangeKey);
    }

    /**
     * 例外のルーティングキーに応じて配信するキューを決定する{@link Exchange}のインスタンスを生成し、DIコンテナに登録します。
     * @return {@link TopicExchange}のインスタンス
     */
    @Bean
    public Exchange exceptionExchange() {
        return new TopicExchange(exceptionExchangeKey);
    }

    /**
     * 回復可能例外を指定回数リトライしても失敗する場合にメッセージが配信される{@link Queue}のインスタンスを生成し、DIコンテナに登録します。
     * @return {@link Queue}のインスタンス
     */
    @Bean
    public Queue recoverableExceptionQueue() {
        return new Queue(recoverableExceptionQueue);
    }

    /**
     * 回復不能例外が発生した場合にメッセージが配信される{@link Queue}のインスタンスを生成し、DIコンテナに登録します。
     * @return {@link Queue}のインスタンス
     */
    @Bean
    public Queue unrecoverableExceptionQueue() {
        return new Queue(unrecoverableExceptionQueue);
    }

    /**
     * error.exchangeからexception.exchangeへと配信する{@link Binding}のインスタンスを生成し、DIコンテナに登録します。
     * @return {@link Binding}のインスタンス
     */
    @Bean
    public Binding exceptionBinding() {
        return BindingBuilder.bind(exceptionExchange())
            .to(errorExchange())
            .with(exceptionBindingKey)
            .noargs();
    }

    /**
     * exception.exchangeからrecoverable.exception.messages.queueへと配信する{@link Binding}のインスタンスを生成し、DIコンテナに登録します。
     * @return {@link Binding}のインスタンス
     */
    @Bean
    public Binding recoverableExceptionQueueBinding() {
        return BindingBuilder.bind(recoverableExceptionQueue())
            .to(exceptionExchange())
            .with(getRecoverableExceptionBindingKey())
            .noargs();
    }

    /**
     * exception.exchangeからunrecoverable.exception.messages.queueへと配信する{@link Binding}のインスタンスを生成し、DIコンテナに登録します。
     * @return {@link Binding}のインスタンス
     */
    @Bean
    public Binding unrecoverableExceptionQueueBinding() {
        return BindingBuilder.bind(unrecoverableExceptionQueue())
            .to(exceptionExchange())
            .with(getUnrecoverableExceptionBindingKey())
            .noargs();
    }

    /**
     * {@link org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer}やリトライ失敗時などの
     * 例外が発生した際にDeadLetterとして扱うための引数のインスタンスを生成します。
     * このメソッドは各キューに対してDeadLetterの引数を設定する際に参照されることを期待しています。
     * 例えば、キュー：test.queueのインスタンスを生成するときに
     * <pre class="brush:java">
     * new Queue("test.queue", true, false, false, deadLetterArguments());
     * </pre>
     * のように指定することでリトライ指定回数を超えた場合や予期しない例外が発生した際に
     * 例外用のExchangeへとメッセージを転送することが可能になります。
     * @return DeadLetterとして扱うための引数
     */
    public Map<String, Object> deadLetterArguments() {
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put(X_DEAD_LETTER_EXCHANGE, getErrorExchangeKey());
        arguments.put(X_DEAD_LETTER_ROUTING_KEY, getUnknownExceptionRoutingKey());
        arguments.put("x-ha-policy", "all");
        return arguments;
    }

    /**
     * リトライのポリシー（間隔、回数）を設定し、
     * {@link RetryOperations}のインスタンスをDIコンテナに登録します。
     * @return {@link RetryTemplate}のインスタンス
     */
    @Bean
    public RetryOperations retryOperations() {
        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(retryPolicy());
        template.setBackOffPolicy(backOffPolicy());
        return template;
    }

    /**
     * メッセージ受信側で処理が継続できなかった場合に
     * 例外用のキューへ転送するための{@link MessageRecoverer}の
     * インスタンスをDIコンテナに登録します。
     * @return {@link ExceptionMessageExchanger}のインスタンス
     */
    @Bean
    public MessageRecoverer exchanger() {
        ExceptionMessageExchanger exchanger = new ExceptionMessageExchanger();
        exchanger.setAmqpTemplate(rabbitTemplate());
        exchanger.setExchange(getErrorExchangeKey());
        exchanger.setDefaultRoutingKey(getUnknownExceptionRoutingKey());
        return exchanger;
    }

    /**
     * メッセージ受信側で例外が発生した場合に
     * リトライ対象例外は警告ログ、リトライ対象外例外はエラーログを出力する{@link ErrorHandler}のインスタンスを生成し、
     * インスタンスをDIコンテナに登録します。
     * @return {@link LoggingErrorHandler}のインスタンス
     */
    @Bean
    public ErrorHandler errorHandler() {
        LoggingErrorHandler handler = new LoggingErrorHandler();
        handler.setRetryableExceptions(exceptionMapping());
        return handler;
    }

    /**
     * リトライインタセプタ実行のアドバイスを指定し、DIコンテナに登録します。
     * @return {@link #statefulRetryOperationsInterceptor()}
     */
    @Bean
    public Advice[] advice() {
        return new Advice[] {
            statefulRetryOperationsInterceptor()
        };
    }

    /**
     * リトライポリシ(間隔・例外・回数)と指定回数を超えた場合の例外処理が設定された{@link MethodInterceptor}のインスタンスを生成し、
     * インスタンスをDIコンテナに登録します。
     * @return {@link org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor}のインスタンス
     */
    @Bean
    public MethodInterceptor statefulRetryOperationsInterceptor() {
        StatefulRetryOperationsInterceptorFactoryBean factory = new StatefulRetryOperationsInterceptorFactoryBean();
        factory.setRetryOperations(retryOperations());
        factory.setMessageRecoverer(exchanger());
        return factory.getObject();
    }

    /**
     * リトライの対象となる例外とリトライ回数を指定し、
     * {@link RetryPolicy}のインスタンスを生成します。
     * @return {@link SimpleRetryPolicy}のインスタンス
     */
    protected RetryPolicy retryPolicy() {
        return new SimpleRetryPolicy(retryCount, exceptionMapping(), true);
    }

    /**
     * リトライ対象の例外を設定します。
     * デフォルトでは{@link AmqpApplicationRecoverableException}をリトライ対象の例外として登録します。
     * リトライ対象の例外を追加したい場合はこのクラスをオーバライドし、
     * 追加することが可能です。
     * @return リトライ対象の例外マップ
     */
    protected Map<Class<? extends Throwable>, Boolean> exceptionMapping() {
        Map<Class<? extends Throwable>, Boolean> map = new HashMap<Class<? extends Throwable>, Boolean>();
        map.put(AmqpApplicationRecoverableException.class, true);
        return map;
    }

    /**
     * 例外用のExchangeキーを取得します。
     * @return 例外用のExchangeキー
     */
    public String getErrorExchangeKey() {
        return errorExchangeKey;
    }

    /**
     * exception.exchangeから回復不可能例外キューへと配信するBindキーを取得します。
     * @return exception.exchangeから回復不可能例外キューへと配信するBindキー
     */
    public String getUnrecoverableExceptionBindingKey() {
        return unrecoverableExceptionBindingKey;
    }

    /**
     * exception.exchangeから回復可能例外キューへと配信するBindキーを取得します。
     * @return exception.exchangeから回復可能例外キューへと配信するBindキー
     */
    public String getRecoverableExceptionBindingKey() {
        return recoverableExceptionBindingKey;
    }

    /**
     * 予期しない例外発生時に利用するデフォルトのルーティングキーを取得します。
     * @return 予期しない例外発生時に利用するデフォルトのルーティングキー
     */
    public String getUnknownExceptionRoutingKey() {
        return unknownExceptionRoutingKey;
    }

    /**
     * {@link SimpleMessageListenerContainer}のインスタンスを生成します。
     * このインスタンスを生成後にRabbitMQへのコネクション{@link #factory()}の設定や
     * メッセージ受信側での例外ハンドラ{@link #errorHandler()}の設定、
     * リトライ処理{@link #advice()}の設定を行います。
     * @return {@link SimpleMessageListenerContainer}のインスタンス
     */
    protected SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(factory());
        container.setErrorHandler(errorHandler());
        container.setAdviceChain(advice());
        return container;
    }

}
