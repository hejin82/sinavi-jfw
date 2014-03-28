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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jse.core.internal.WebCoreInternals;

import org.apache.commons.validator.GenericValidator;

/**
 * <p>
 * このクラスは日付値を扱うプロパティ・エディタです。
 * ビーンの日付型のプロパティを編集する機能を提供します。<br/>
 * 日付型とは以下の型を意味します。<br/>
 * <ul>
 *     <li>{@link java.util.Date}</li>
 * </ul>
 * {@code Spring MVC} はこのプロパティ・エディタを用いてリクエスト・パラメータのBeanへのバインディング、および、
 * Beanのプロパティ値の文字列への変換を行います。バインディングを行う際は文字列として送られてくるリクエスト・パラメータを
 * 該当のBeanのプロパティの型に型変換する必要があり、型変換可能であるかどうかのチェックは {@code yyyy/MM/dd}
 * に従っているかどうかで判断されます。この日付の書式に関するパターンは{@link SimpleDateFormat} で指定可能な日付、および、時間の
 * パターンに従います。
 * なお、正規表現に違反するリクエスト・パラメータが送信された場合は{@code J-Framework} によって入力値検証違反と同様に処理されます。
 * </p>
 * <h4>設定方法</h4>
 * <p>
 * {@link J-Framework} ではこのプロパティ・エディタを {@link jp.co.ctc_g.jse.core.framework.JseDefaultDatePropertyEditorRegistrar} によって共通の設定として
 * アプリケーション起動時に初期化します。これは {@link java.util.Date} 型プロパティに対してリクエスト・パラメータをバインドする際のデフォルトの
 * プロパティ・エディタとして設定されることを意味します。
 * デフォルトの日付の書式は、前述の通り {@code yyyy/MM/dd} です。この書式を変更するには  {@link jp.co.ctc_g.jse.core.framework.JseDefaultDatePropertyEditorRegistrar#pattern}
 * に書式を定義する必要があります。<br/>
 * また、プロパティによって受付可能な日付書式が異なる場合は、{@code Controller} の {@link org.springframework.web.bind.annotation.InitBinder} で注釈されたメソッドで
 * プロパティごとにプロパティ・エディタを適切に設定する必要があります。<br/>
 * 設定例は以下の通りです。<br/>
 * {@link java.util.Date}型のプロパティ{@code bar}に対して、 書式 {@code yyyy/MM/dd HH:mm:ss} に従った
 * リクエスト・パラメータのバインドを許容する際の設定です。
 * <pre>
 * &#64;Controller
 * public class FooController {
 * 
 *   &#64;InitBinder
 *   public void setupPropertyEditor(WebDataBinder binder) {
 *       DateEditor dateEditor = new DateEditor(true, "yyyy/MM/dd HH:mm:ss");
 *       binder.registerCustomEditor(Date.class, "bar", dateEditor);
 *   }
 * </pre>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see PropertyEditorSupport
 * @see java.util.Date
 * @see SimpleDateFormat
 */
public class DateEditor extends PropertyEditorSupport {

    private static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd";
    
    private static final String CODE = "date.typeMissmatch";
    private static final String DEFAULT_MESSAGE;
    static {
        DEFAULT_MESSAGE = WebCoreInternals.getConfig(DateEditor.class).find("error");
    }

    private static final boolean DEFAULT_STRICT = true;

    private static final boolean DEFAULT_ALLOW_EMPTY = true;

    private final String pattern;

    private final boolean strict;

    private final boolean allowEmpty;

    private final SimpleDateFormat dateFormat;

    private final String message;

    /**
     * <p>
     * コンストラクタです。
     * </p>
     */
    public DateEditor() {
        this(DEFAULT_ALLOW_EMPTY, DEFAULT_STRICT, null, null);
    }

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param allowEmpty trueの場合、空を許可
     */
    public DateEditor(boolean allowEmpty) {
        this(allowEmpty, DEFAULT_STRICT, null, null);
    }

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param allowEmpty trueの場合、空を許可
     * @param pattern 日付のフォーマット
     */
    public DateEditor(boolean allowEmpty, String pattern) {
        this(allowEmpty, DEFAULT_STRICT, pattern, null);
    }

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param allowEmpty trueの場合、空を許可
     * @param strict trueの場合、日付形式のチェックを厳密に行う
     * @param pattern 日付のフォーマット
     */
    public DateEditor(boolean allowEmpty, boolean strict, String pattern) {
        this(allowEmpty, strict, pattern, null);
    }

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param allowEmpty trueの場合、空を許可
     * @param strict trueの場合、日付形式のチェックを厳密に行う
     * @param pattern 日付のフォーマット
     * @param message メッセージのキー
     */
    public DateEditor(boolean allowEmpty, boolean strict, String pattern, String message) {
        this.allowEmpty = allowEmpty;
        this.pattern = !Strings.isEmpty(pattern) ? pattern : DEFAULT_DATE_PATTERN;
        this.strict = strict;
        this.dateFormat = new SimpleDateFormat(this.pattern);
        this.message = !Strings.isEmpty(message) ? message : DEFAULT_MESSAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) {

        if (allowEmpty && Strings.isEmpty(text)) {
            setValue(null);
        } else if (GenericValidator.isDate(text, pattern, strict)) {
            try {
                setValue(this.dateFormat.parse(text));
            } catch (ParseException e) {
                throw new PropertyEditingException(CODE, Strings.substitute(message, Maps.hash("pattern", pattern)));
            }
        } else {
            throw new PropertyEditingException(CODE, Strings.substitute(message, Maps.hash("pattern", pattern)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? this.dateFormat.format(value) : "");
    }
}
