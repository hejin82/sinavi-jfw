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

package jp.co.ctc_g.jse.core.rest.jersey.config;

import java.util.LinkedHashSet;
import java.util.Set;

import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.ApplicationRecoverableExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.ApplicationUnrecoverableExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.BadRequestExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.ClientErrorExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.InternalServerErrorExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.NotAcceptableExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.NotAllowedExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.NotAuthorizedExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.NotSupportedExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.ServerErrorExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.ServiceUnavailableExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.SystemExceptionMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.ThrowableMapper;
import jp.co.ctc_g.jse.core.rest.jersey.exception.mapper.ValidationExceptionMapper;

/**
 * <p>
 * このクラスは、フレームワークが提供する例外ハンドラのクラス情報を取得するAPIを提供します。
 * </p>
 * <p>
 * Jerseyの設定時に例外ハンドラの設定を簡単にするためにAPIを提供し、
 * カスタマイズした例外ハンドラを追加で登録する場合は以下のようなコードで実現可能です。
 * <pre class="brush:java">
 * public AppConfig extends ResourceConfig {
 *   public AppConfig() {
 *     Set&lt;Class&lt;?&gt;&gt; handlers = JerseyConfigs.getExcpetionMappers();
 *     handlers.add(HogeExceptionMapper.class);
 *     registerClasses(handlers);
 *   }
 * }
 * </pre>
 * </p>
 * <p>
 * フレームワークが提供する例外ハンドラは以下の種類があります。
 * <ol>
 *  <li>{@link ApplicationRecoverableExceptionMapper}</li>
 *  <li>{@link ApplicationUnrecoverableExceptionMapper}</li>
 *  <li>{@link SystemExceptionMapper}</li>
 *  <li>{@link BadRequestExceptionMapper}</li>
 *  <li>{@link ClientErrorExceptionMapper}</li>
 *  <li>{@link InternalServerErrorExceptionMapper}</li>
 *  <li>{@link NotAcceptableExceptionMapper}</li>
 *  <li>{@link NotAllowedExceptionMapper}</li>
 *  <li>{@link NotAuthorizedExceptionMapper}</li>
 *  <li>{@link NotSupportedExceptionMapper}</li>
 *  <li>{@link ServerErrorExceptionMapper}</li>
 *  <li>{@link ServiceUnavailableExceptionMapper}</li>
 *  <li>{@link ThrowableMapper}</li>
 *  <li>{@link ValidationExceptionMapper}</li>
 * </ol>
 * 詳細はそれぞれの例外ハンドラを参照してください。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class JerseyConfigs {
    
    /**
     * コンストラクタです。
     * インスタンスの生成を抑止します。
     */
    private JerseyConfigs() {}
    
    /**
     * フレームワークが提供する以下の例外ハンドラのクラス情報を取得します。
     * @return ExceptionMapper
     */
    public static Set<Class<?>> getExcpetionMappers() {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        classes.add(ApplicationRecoverableExceptionMapper.class);
        classes.add(ApplicationUnrecoverableExceptionMapper.class);
        classes.add(SystemExceptionMapper.class);
        classes.add(BadRequestExceptionMapper.class);
        classes.add(ClientErrorExceptionMapper.class);
        classes.add(InternalServerErrorExceptionMapper.class);
        classes.add(NotAcceptableExceptionMapper.class);
        classes.add(NotAllowedExceptionMapper.class);
        classes.add(NotAuthorizedExceptionMapper.class);
        classes.add(NotSupportedExceptionMapper.class);
        classes.add(ServerErrorExceptionMapper.class);
        classes.add(ServiceUnavailableExceptionMapper.class);
        classes.add(ThrowableMapper.class);
        classes.add(ValidationExceptionMapper.class);
        return classes;
    }

}
