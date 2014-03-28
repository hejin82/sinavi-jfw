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

import java.math.BigDecimal;
import java.math.BigInteger;

import jp.co.ctc_g.jse.core.util.web.beans.HalfwidthDecimalEditor;
import jp.co.ctc_g.jse.core.util.web.beans.HalfwidthNumberEditor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * <p>
 * このクラスは、リクエスト・パラメータの値をビーンの数値型のプロパティにバインドするためのプロパティ・エディタをレジストする機能を提供します。
 * {@code Spring MVC} のコンテキストXMLに設定を行うことによって{@link ModelAttribute}によって注釈されたビーンの数値型の
 * プロパティ全てに対して、プロパティ・エディタ{@link jp.co.ctc_g.jse.core.util.web.beans.NumberEditor}を動作させることができます。<br/>
 * ただし、コントローラの{@link org.springframework.web.bind.annotation.InitBinder}によって注釈されたメソッドにおいて個別のプロパティ・エディタが設定された場合は、コントローラで
 * 設定されたプロパティ・エディタが優先されます。
 * </p>
 * <h4>設定方法</h4>
 * <p>
 * 設定は{@code Spring MVC} のコンテキストXMLに対して行います。<br/>
 * {@code JseDefaultNumberPropertyEditorRegistrar}のBean定義を行います。<br/>
 * {@link Date}型の文字列に対してバインドする際の文字列のフォーマットを表現する{@code pattern}と
 * {@link Date}型の該当のプロパティがnullを許可するかどうかを表現する{@code allowEmpty}の2つのプロパティを定義することができます。
 * なお、この2つのプロパティ定義を省略した場合は、{@link jp.co.ctc_g.jse.core.util.web.beans.DateEditor} のデフォルトの動作によりバインドが行われます。
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
 *     &lt;bean id="defaultNumberPropertyEditorRegistrar" class="jp.co.ctc_g.jse.core.framework.JseDefaultNumberPropertyEditorRegistrar"&gt;
 *       &lt;property name="pattern" value="yyyy/MM/dd" /&gt;
 *       &lt;property name="allowEmpty" value="true" /&gt;
 *     &lt;/bean&gt;
 *   </pre>
 * </p>
 * <h4>サポートするプロパティの型</h4>
 * <p>
 * 対象とするプロパティの型は下記の通りです。
 *   <ul>
 *     <li>{@link Byte}</li>
 *     <li>{@link Short}</li>
 *     <li>{@link Integer}</li>
 *     <li>{@link Long}</li>
 *     <li>{@link BigInteger}</li>
 *     <li>{@link Float}</li>
 *     <li>{@link Double}</li>
 *     <li>{@link BigDecimal}</li>
 *   </ul>
 * </p>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see PropertyEditorRegistrar
 * @see HalfwidthNumberEditor
 * @see HalfwidthDecimalEditor
 */
public class JseDefaultNumberPropertyEditorRegistrar implements PropertyEditorRegistrar {
    
    private boolean allowEmpty;
    
    private String message;
    
    /**
     * デフォルトコンストラクタです。
     */
    public JseDefaultNumberPropertyEditorRegistrar() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(Byte.class, new HalfwidthNumberEditor(Byte.class, allowEmpty, message));
        registry.registerCustomEditor(Short.class, new HalfwidthNumberEditor(Short.class, allowEmpty, message));
        registry.registerCustomEditor(Integer.class, new HalfwidthNumberEditor(Integer.class, allowEmpty, message));
        registry.registerCustomEditor(Long.class, new HalfwidthNumberEditor(Long.class, allowEmpty, message));
        registry.registerCustomEditor(BigInteger.class, new HalfwidthNumberEditor(BigInteger.class, allowEmpty, message));
        registry.registerCustomEditor(BigDecimal.class, new HalfwidthDecimalEditor(BigDecimal.class, allowEmpty, message));
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
