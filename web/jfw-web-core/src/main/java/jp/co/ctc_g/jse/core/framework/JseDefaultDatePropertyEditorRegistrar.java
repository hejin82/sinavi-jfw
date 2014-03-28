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

package jp.co.ctc_g.jse.core.framework;

import java.util.Date;

import jp.co.ctc_g.jse.core.util.web.beans.DateEditor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * <p>
 * このクラスは、リクエスト・パラメータの値をビーンの日付型のプロパティにバインドするためのプロパティ・エディタをレジストする機能を提供します。
 * {@code Spring MVC} のコンテキストXMLに設定を行うことによって{@link ModelAttribute}によって注釈されたビーンの日付型の
 * プロパティ全てに対して、プロパティ・エディタ{@link DateEditor}を動作させることができます。<br/>
 * ただし、コントローラの{@link org.springframework.web.bind.annotation.InitBinder}によって注釈されたメソッドにおいて個別のプロパティ・エディタが設定された場合は、コントローラで
 * 設定されたプロパティ・エディタが優先されます。
 * </p>
 * <h4>設定方法</h4>
 * <p>
 * 設定は{@code Spring MVC} のコンテキストXMLに対して行います。<br/>
 * {@code JseDefaultDatePropertyEditorRegistrar}のBean定義を行います。<br/>
 * 日付型の文字列に対してバインドする際の文字列のフォーマットを表現する{@code pattern}と
 * 日付型の該当のプロパティがnullを許可するかどうか(入力が必須であるかどうか)を表現する{@code allowEmpty}の2つのプロパティを定義することができます。
 * なお、この2つのプロパティ定義を省略した場合は、{@link DateEditor} のデフォルトの動作によりバインドが行われます。
 * {@link ConfigurableWebBindingInitializer}のプロパティ{@code propertyEditorRegistrars}に対して{@code JseDefaultDatePropertyEditorRegistrar}を設定します。
 *   <pre>
 *     &lt;bean id="configurableWebBindingInitializer" class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer" &gt;
 *       &lt;property name="validator" ref="validator" /&gt;
 *       ・・・
 *       &lt;property name="propertyEditorRegistrars"&gt;
 *         &lt;list&gt;
 *           &lt;ref bean="defaultDatePropertyEditorRegistrar"/&gt;
 *            &lt;ref bean="defaultNumberPropertyEditorRegistrar"/&gt;
 *         &lt;/list&gt;
 *       &lt;/property&gt;
 *     &lt;/bean&gt;
 *     
 *     &lt;bean id="defaultDatePropertyEditorRegistrar" class="jp.co.ctc_g.jse.core.framework.JseDefaultDatePropertyEditorRegistrar"&gt;
 *       &lt;property name="pattern" value="yyyy/MM/dd" /&gt;
 *       &lt;property name="allowEmpty" value="true" /&gt;
 *     &lt;/bean&gt;
 *   </pre>
 * </p>
 * <h4>サポートするプロパティの型</h4>
 * <p>
 * 対象とするプロパティの型は{@link Date}のみです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see PropertyEditorRegistrar
 * @see DateEditor
 */
public class JseDefaultDatePropertyEditorRegistrar implements PropertyEditorRegistrar {

    private String pattern;

    private boolean allowEmpty;

    private String message;
    
    /**
     * デフォルトコンストラクタです。
     */
    public JseDefaultDatePropertyEditorRegistrar() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(Date.class, new DateEditor(allowEmpty, true, pattern, message));
    }

    /**
     * 日付のフォーマットチェックを行う文字列を返却します。
     * @return 日付のフォーマットチェックを行う文字列
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * 日付のフォーマットチェックを行う文字列を設定します。
     * 文字列は{@link SimpleDateFormat}で利用可能な日付・時刻パターンに従う必要があります。
     * @param pattern 日付のフォーマットチェックを行う文字列
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * 操作対象のプロパティが{@code null}を許可するかどうかを返却します。
     * @return {@code null}の可否 {@code true}の場合{@code null}を許可
     */
    public boolean isAllowEmpty() {
        return allowEmpty;
    }

    /**
     * 操作対象のプロパティが{@code null}を許可するかどうかを設定します。
     * @param allowEmpty {@code null}の可否
     */
    public void setAllowEmpty(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    /**
     * バインド・エラー発生時のメッセージを返却します。
     * @return バインド・エラー発生時のメッセージ
     */
    public String getMessage() {
        return message;
    }

    /**
     * バインド・エラー発生時のメッセージを設定します。
     * {@code null}を指定した場合、プロパティの型にマッピングされたプロパティ・エディタのデフォルト・メッセージが利用されます。
     * @param message バインド・エラー発生時のメッセージ
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
