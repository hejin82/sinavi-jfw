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

package jp.co.ctc_g.jfw.core.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * <p>
 * このクラスは、各種リソースを管理する機能を提供します。
 * </p>
 * <h4>概要</h4>
 * <p>
 * このクラスは、J-Frameworkのリソースファイルやアプリケーションで定義するリソースファイルを管理します。
 * この管理機構を利用することにより、以下の機能を実現しています。
 * <ol>
 * <li>一括読み込み：リソース機能のロード時に管理リソースを一括で読み込みキャッシュします。</li>
 * <li>国際化対応：ユーザーのロケールに対応するリソースを取得することが可能です。</li>
 * <li>分割リソースのマージ：物理的に分割されたリソースを1つのリソースとしてマージできます。</li>
 * <li>リロード：アプリケーションをロードし直すことなく更新されたリソースの情報を取得することが可能です。</li>
 * </ol>
 * リソースファイルを読み込む場合はSpringの設定ファイルに以下のような定義を追加して、リソースファイルを読み込んでください。
 * </p>
 * <pre>
 * &lt;bean id="messageSourceLocator" class="jp.co.ctc_g.jfw.core.resource.MessageSourceLocator" /&gt;
 * &lt;bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource"&gt;
 *   &lt;property name="basenames"&gt;
 *     &lt;list&gt;
 *       &lt;value&gt;classpath:ApplicationResources&lt;/value&gt;
 *     &lt;/list&gt;
 *   &lt;/property&gt;
 *   &lt;property name="defaultEncoding" value="UTF-8" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * <p>
 * リソースファイルから値を取得する場合は以下のようにJ-Frameworkのリソースアクセスを利用してください。
 * </p>
 * <pre>
 * Rs.find("hoge.foo"); // ロケールを省略するとデフォルトのロケールでリソースを検索します。
 * </pre>
 * <h4>一括読み込み</h4>
 * <p>
 * リソース機能が初回起動する際に管理リソースを一括で読み込んでキャッシュします。
 * 起動するときにはSpringの設定ファイルで指定されたリソース以外にもJ-Framework内部のリソースファイルを読み込みキャッシュします。
 * </p>
 * <h4>国際化対応</h4>
 * <p>
 * ロケール毎に取得するリソースを切り替えることで国際化に対応します。
 * リソースファイルは(1) ロケールなし、(2) ja、(3) ja_JP の順でリソースの検索をしてから、
 * 1つのリソースとしてオーバライドした内容を返します。
 * つまり、3つのリソースが存在する場合は、(3) ja_JPのリソースが有効になります。
 * 例えば、以下の3つのリソースファイルが定義されているときは
 * <code>ja_JP</code>が有効になります。
 * <dl>
 * <dt>I18N.properties</dt>
 * <dd>lang=default</dd>
 * </dl>
 * <dl>
 * <dt>I18N_ja.properties</dt>
 * <dd>lang=ja</dd>
 * </dl>
 * <dl>
 * <dt>I18N_ja_JP.properties</dt>
 * <dd>lang=ja_JP</dd>
 * </dl>
 * </p>
 * <h4>分割リソースのマージ</h4>
 * <p>
 * 複数のリソースファイルに分割されたリソースファイルをマージすることも可能です。
 * マージする場合はSpringの設定ファイルに以下のように分割されたリソースファイルを指定してください。
 * </p>
 * <pre>
 * &lt;bean id="messageSourceLocator" class="jp.co.ctc_g.jfw.core.resource.MessageSourceLocator" /&gt;
 * &lt;bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource"&gt;
 *   &lt;property name="basenames"&gt;
 *     &lt;list&gt;
 *       &lt;value&gt;classpath:ApplicationResources1&lt;/value&gt;
 *       &lt;value&gt;classpath:ApplicationResources2&lt;/value&gt;
 *       &lt;value&gt;classpath:ApplicationResources3&lt;/value&gt;
 *     &lt;/list&gt;
 *   &lt;/property&gt;
 *   &lt;property name="defaultEncoding" value="UTF-8" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * <h4>リロード</h4>
 * <p>
 * アプリケーションをロードし直すことなく更新されたリソースの情報を取得することが可能です。
 * リロードする場合はSpringの設定ファイルに以下のように更新の読込間隔を指定してください。
 * 詳細は{@link ReloadableResourceBundleMessageSource#setCacheSeconds(int)}}を参照してください。
 * </p>
 * <pre>
 * &lt;bean id="messageSourceLocator" class="jp.co.ctc_g.jfw.core.resource.MessageSourceLocator" /&gt;
 * &lt;bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource"&gt;
 *   &lt;property name="basenames"&gt;
 *     &lt;list&gt;
 *       &lt;value&gt;file:/home/user/SystemConfig&lt;/value&gt;
 *     &lt;/list&gt;
 *   &lt;/property&gt;
 *   <strong>&lt;property name="cacheSeconds" value="60" /&gt;</strong>
 *   &lt;property name="defaultEncoding" value="UTF-8" /&gt;
 * &lt;/bean&gt;
 * </pre>
 */
public final class MessageSourceLocator {

    /**
     * J-Framework内部のリソースファイルです。
     */
    private static final String[] JFRAMEWORK_PROPERTIES = {
        "jp.co.ctc_g.jfw.core.resource.ErrorResources"
    };

    /**
     * ユーザ指定のリソースファイルを管理するReloadableResourceBundleMessageSourceです。
     */
    private static ReloadableResourceBundleMessageSource messageSource;

    /**
     * J-Frameworkのリソースファイルを管理するReloadableResourceBundleMessageSourceです。
     */
    private static final ResourceBundleMessageSource PARENT_MESSAGE_SOURCE;

    static {
        PARENT_MESSAGE_SOURCE = new ResourceBundleMessageSource();
        PARENT_MESSAGE_SOURCE.setBasenames(JFRAMEWORK_PROPERTIES);
    }

    /**
     * MessageSourceを取得します。
     * @return MessageSource
     */
    public static MessageSource get() {

        if (messageSource == null) return PARENT_MESSAGE_SOURCE;
        else return messageSource;
    }

    /**
     * MessageSourceを設定します。
     * アプリケーションのリソースが指定されている場合は、ParentにJ-FrameworkのMessageSourceを設定します。
     * @param source メッセージソース
     */
    public static void set(ReloadableResourceBundleMessageSource source) {

        source.setParentMessageSource(PARENT_MESSAGE_SOURCE);
        messageSource = source;
    }

    /**
     * MessageSourceを設定します。
     * @param messageSource メッセージソース
     */
    @Autowired(required = true)
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {

        MessageSourceLocator.set(messageSource);
    }
}
