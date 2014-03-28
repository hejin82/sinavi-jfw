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

package jp.co.ctc_g.jse.core.rest.jersey.i18n;

import java.util.Locale;

/**
 * <p>
 * このクラスは、{@link Locale}を保持します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class LocaleContextKeeper {

    private static final ThreadLocal<Locale> LOCALE_KEEPER = new ThreadLocal<Locale>();

    /**
     * コンストラクタです。
     * インスタンスの生成を抑止します。
     */
    private LocaleContextKeeper(){}

    /**
     * ロケールをスレッドローカルに設定します。
     * @param locale ロケール
     */
    public static void setLocale(Locale locale) {
        LOCALE_KEEPER.set(locale);
    }

    /**
     * ロケールをスレッドローカルより取得します。
     * @return ロケール
     */
    public static Locale getLocale() {
        return LOCALE_KEEPER.get();
    }

    /**
     * ロケールをスレッドローカルより削除します。
     */
    public static void remove() {
        LOCALE_KEEPER.remove();
    }

}
