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

package jp.co.ctc_g.jse.core.rest.jersey.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jse.core.rest.jersey.i18n.LocaleContextKeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * このクラスは、エラーメッセージをプロパティファイルより取得するユーティリティです。
 * </p>
 * <p>
 * ロケールが指定されなかった場合はデフォルトのロケールを利用します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class ErrorResources {

    private static final Logger L = LoggerFactory.getLogger(ErrorResources.class);
    private static final ResourceBundle R = InternalMessages.getBundle(ErrorResources.class);
    private static final String RESOURCE_NAME = "ErrorMessages";

    /**
     * コンストラクタです。
     * インスタンスの生成を抑止します。
     */
    private ErrorResources() {}

    /**
     * リソースファイルよりメッセージを取得します。
     * もし、指定されたキーでメッセージが解決できない場合はキーを返します。
     * @param key エラーメッセージのキー
     * @return エラーメッセージ
     */
    public static String find(String key) {
        return find(key, LocaleContextKeeper.getLocale());
    }

    /**
     * リソースファイルよりメッセージを取得します。
     * もし、指定されたキーでメッセージが解決できない場合はキーを返します。
     * @param key エラーメッセージのキー
     * @param locale ロケール
     * @return エラーメッセージ
     */
    public static String find(String key, Locale locale) {
        if (Strings.isEmpty(key)) return key;
        String msg = key;
        try {
            msg = ResourceBundle.getBundle(RESOURCE_NAME, locale != null ? locale : Locale.getDefault(),
                new ErrorMessageCacheControl(Control.FORMAT_PROPERTIES)).getString(key);
        } catch (MissingResourceException e) {
            if (L.isDebugEnabled()) {
                L.debug(Strings.substitute(R.getString("D-REST-JERSEY-UTIL#0001"), Maps.hash("key", key)));
            }
        }
        return msg;
    }

}
