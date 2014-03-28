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

package jp.co.ctc_g.jse.core.util.web.beans;

import java.beans.PropertyEditorSupport;

import jp.co.ctc_g.jfw.core.util.Args;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jse.core.internal.WebCoreInternals;

/**
 * <p>
 * このクラスは列挙型のプロパティを扱うプロパティ・エディタです。
 * ビーンの列挙型のプロパティを編集する機能を提供します。<br/>
 * {@code Spring MVC} はこのプロパティ・エディタを用いてリクエスト・パラメータのBeanへのバインディング、および、
 * Beanのプロパティ値の文字列への変換を行います。バインディングを行う際は文字列として送られてくるリクエスト・パラメータを
 * 該当のBeanのプロパティの型に型変換する必要があり、型変換可能であるかどうかのチェックはプロパティエディタに指定した
 * 列挙型の {@code valueOf(Class<T>, String)}メソッドにより判断されます。
 * <p>
 * <h4>設定方法</h4>
 * <p>
 * {@code Spring MVC} の {@code Application Context}に一括する設定する方法と、
 * {@code Spring MVC}の{@code Controller}コンポーネントに設定する方法の２種類あります。<br/>
 * 列挙型のプロパティ・エディタはBeanの個々のプロパティ合わせて変更する必要がないため、一括設定する方法を推奨します。<br/>
 * 以下に一括する設定する方法を記載します。
 * まず、{@link jp.co.ctc_g.jse.core.framework.JseDefaultDatePropertyEditorRegistrar}、
 * {@link jp.co.ctc_g.jse.core.framework.JseDefaultNumberPropertyEditorRegistrar}のような
 * 列挙型用の{@code PropertyEditorRegistrar}インタフェース実装を用意します。
 * <pre>
 * public class EnumPropertyEditorRegistrar implements PropertyEditorRegistrar {
 *   
 *   @Override
 *   public void registerCustomEditors(PropertyEditorRegistry registry) {
 *       registry.registerCustomEditor(FooType.class, new EnumEditor(FooType.class));
 *       registry.registerCustomEditor(BarType.class, new EnumEditor(BarType.class));
 *       ・・・
 *   }
 * </pre>
 * 次に{@code EnumPropertyEditorRegistrar}を{@code Application Context}に設定します。<br/>
 * {@code J-Frameowrk}では、日付型、および、数値型に対するプロパティ・エディタがデフォルトで設定されているため
 * {@code ConfigurableWebBindingInitializer}の{@code List}型のプロパティ{@code propertyEditorRegistrars}に
 * 追加設定を行う必要があります。日付型、および、数値型に対するプロパティ・エディタの設定を上書きしてしまうとリクエスト・パラメータ値を日付型や数値型のプロパティに
 * バインドする際にエラーが発生するので注意して下さい。
 * <pre>
 * &lt;bean id="configurableWebBindingInitializer" class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer" &gt;
 *   &lt;property name="validator" ref="validator" /&gt;
 *   ・・・
 *   &lt;property name="propertyEditorRegistrars"&gt;
 *     &lt;list&gt;
 *       &lt;ref bean="defaultDatePropertyEditorRegistrar"/&gt;
 *       &lt;ref bean="defaultNumberPropertyEditorRegistrar"/&gt;
 *       &lt;ref bean="enumPropertyEditorRegistrar"/&gt;
 *     &lt;/list&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * 
 * &lt;bean id="enumPropertyEditorRegistrar" class="jp.co.ctc_g.foo.bar.EnumPropertyEditorRegistrar"&gt;
 * </pre>
 * 以下に{@code Controller}コンポーネントに設定する方法を記載します。
 * <pre>
 * &#64;Controller
 * public class FooController {
 * 
 *   &#64;InitBinder
 *   public void setupPropertyEditor(WebDataBinder binder) {
 *       EnumEditor enumEditor = new EnumEditor(BarType.class);
 *       binder.registerCustomEditor(BarType.class, enumEditor);
 *   }
 * </pre>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see PropertyEditorSupport
 * @see java.lang.Enum
 * @see jp.co.ctc_g.jse.core.framework.JseDefaultDatePropertyEditorRegistrar
 * @see jp.co.ctc_g.jse.core.framework.JseDefaultNumberPropertyEditorRegistrar
 */
@SuppressWarnings("rawtypes")
public class EnumEditor extends PropertyEditorSupport {

    private static final String CODE = "enum.typeMissmatch";
    private static final String DEFAULT_MESSAGE;
    static {
        DEFAULT_MESSAGE = WebCoreInternals.getConfig(EnumEditor.class).find("error");
    }

    private static final boolean DEFAULT_ALLOW_EMPTY = true;

    private boolean allowEmpty;

    private final String message;

    private Class<? extends Enum> enumType;

    /**
     * コンストラクタです。
     * @param enumType 列挙型のタイプ
     * @param <T> 列挙型のクラス
     */
    public <T extends Enum> EnumEditor(Class<T> enumType) {
        this(DEFAULT_ALLOW_EMPTY, enumType, null);
    }

    /**
     * コンストラクタです。
     * @param allowEmpty 空設定を許可するかどうか
     * @param enumType 列挙型のタイプ
     * @param <T> 列挙型のクラス
     */
    public <T extends Enum> EnumEditor(boolean allowEmpty, Class<T> enumType) {
        this(allowEmpty, enumType, null);
    }

    /**
     * コンストラクタです。
     * @param enumType 列挙型のタイプ
     * @param message エラーメッセージ
     * @param <T> 列挙型のクラス
     */
    public <T extends Enum> EnumEditor(Class<T> enumType, String message) {
        this(DEFAULT_ALLOW_EMPTY, enumType, message);
    }

    /**
     * コンストラクタです。
     * @param allowEmpty 空設定を許可するかどうか
     * @param enumType 列挙型のタイプ
     * @param message エラーメッセージ
     * @param <T> 列挙型のクラス
     */
    public <T extends Enum> EnumEditor(boolean allowEmpty, Class<T> enumType, String message) {
        this.allowEmpty = allowEmpty;
        this.enumType = enumType;
        this.message = !Strings.isEmpty(message) ? message : DEFAULT_MESSAGE;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setAsText(String text) {
        if (allowEmpty && Strings.isEmpty(text)) {
            setValue(null);
        } else if (!allowEmpty && Strings.isEmpty(text)) {
            throw new PropertyEditingException(CODE, Editors.REQUIRED_MESSAGE);
        } else {
            try {
                setValue(Enum.valueOf(this.enumType, text.trim()));
            } catch (IllegalArgumentException e) {
                throw new PropertyEditingException(CODE, Strings.substitute(message, Maps.hash("pattern", generatePattern())));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        Object value = getValue();
        if (value == null) {
            return "";
        } else {
            return ((Enum)value).name();
        }
    }

    private String generatePattern() {
        Enum[] es = enumType.getEnumConstants();
        return join(",", es);
    }

    private <T extends Enum> String join(String separator, T... joinees) {

        if (joinees == null || joinees.length == 0)
            return "";
        separator = Args.proper(separator, "");
        if (joinees.length == 1) {
            return joinees[0].name();
        } else {
            StringBuilder joined = new StringBuilder();
            joined.append(joinees[0]);
            for (int i = 1; i < joinees.length; i++) {
                joined.append("".equals(joinees[i - 1]) ? "" : separator).append(joinees[i]);
            }
            return joined.toString();
        }
    }
}
