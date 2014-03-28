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

import java.util.Locale;
import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Strings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * <p>
 * このクラスは、J-Framework管理下のリソースを横断的に検索する機能を提供します。
 * </p>
 *
 * <h4>検索対象リソース</h4>
 * <p>
 * このクラスは {@link org.springframework.context.support.ResourceBundleMessageSource } 登録された順番にリソースを検索します。
 * </p>
 *
 * <h4>ローカライゼーション</h4>
 * <p>
 * 任意のロケールにローカライズされリソースを検索対象としたい場合、 メソッドの引数にロケールを渡してください。 ロケールを省略した場合、
 * {@link Locale}に従います。 つまり、Webアプリケーションとして動作している場合にはブラウザが送信してきたロケールを利用し、
 * そうでない場合や、ブラウザがロケールを送信しない場合にはサーバロケールを利用します。
 * </p>
 *
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Rs {

    private static final Log L = LogFactory.getLog(Rs.class);
    private static final ResourceBundle R = InternalMessages.getBundle(Rs.class);

    private Rs() {
    }

    /**
     * 指定されたキーに対応する値をリソースから検索します。
     * もし対応するキー値が存在ない場合は、<strong>キーが値として返却されます。</strong>
     *
     * @param key
     *            検索対象となるリソースキー
     * @return キーに対応する値
     */
    public static String find(String key) {
        return find(key, (Locale) null);
    }

    /**
     * 指定されたロケールのリソースから、指定されたキーに対応する値を検索します。
     * もし対応するキーが存在ない場合は、defaultValueが値として返却されます。</strong>
     *
     * @param key
     *            検索対象となるリソースキー
     * @param defaultValue
     *            デフォルトの値
     * @return キーに対応する値
     */
    public static String find(String key, String defaultValue) {
        try {
            MessageSource source = MessageSourceLocator.get();
            if (Strings.isEmpty(key)) return "";
            Locale userLocale = LocaleContextHolder.getLocale();
            Locale currentLocale = userLocale != null ? userLocale : Locale.getDefault();
            String value = source.getMessage(key, null, currentLocale);
            if(Strings.isEmpty(value)) {
                return key;
            } else {
                return value;
            }
        } catch (NoSuchMessageException e) {
            return defaultValue;
        }
    }

    /**
     * 指定されたロケールのリソースから、指定されたキーに対応する値を検索します。
     * もし対応するキー値が存在ない場合は、<strong>キーが値として返却されます。</strong>
     *
     * @param key
     *            検索対象となるリソースキー
     * @param locale
     *            検索対象となるロケール
     * @return キーに対応する値
     */
    public static String find(String key, Locale locale) {
        try {
            MessageSource source = MessageSourceLocator.get();

            if (Strings.isEmpty(key)) return "";

            Locale currentLocale = locale;
            if (currentLocale == null) {
                Locale userLocale = LocaleContextHolder.getLocale();
                currentLocale = userLocale != null ? userLocale : Locale.getDefault();
            }
            String value = source.getMessage(key, null, currentLocale);
            if(Strings.isEmpty(value)) {
                return key;
            } else {
                return value;
            }
        } catch (NoSuchMessageException e) {
            if (L.isDebugEnabled()) {
                String message = Strings.substitute(R.getString("I-RESOURCE#0001"), Maps.hash("key", key));
                L.debug(message);
            }
            return key;
        }
    }

    /**
     * 複数のメッセージキーより該当する値を検索します。
     * もし対応するキー値が存在しない場合は、デフォルトメッセージが返却されます。
     * @param context {@link MessageSourceResolvable}
     * @return キーに対応する値
     */
    public static String find(MessageSourceResolvable context) {
        return find(context, (Locale)null);
    }

    /**
     * 指定されたロケールから、複数のメッセージキーより該当する値を検索します。
     * もし対応するキー値が存在しない場合は、デフォルトメッセージが返却されます。
     * @param context {@link MessageSourceResolvable}
     * @param locale ロケール
     * @return キーに対応する値
     */
    public static String find(MessageSourceResolvable context, Locale locale) {
        try {
            MessageSource source = MessageSourceLocator.get();
            Locale currentLocale = locale;
            if (currentLocale == null) {
                Locale userLocale = LocaleContextHolder.getLocale();
                currentLocale = userLocale != null ? userLocale : Locale.getDefault();
            }
            return source.getMessage(context, currentLocale);
        } catch (NoSuchMessageException e) {
            return context.getDefaultMessage();
        }
    }

}
