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

package jp.co.ctc_g.jse.core.rest.springmvc.server.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * <p>
 * このクラスは、エラーリソースを扱うユーテリティです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class ErrorResources {

    /**
     * コンストラクタです。
     * インスタンスの生成を抑止します。
     */
    private ErrorResources() {}

    private static final String[] ERROR_RESOURCES = {
        ErrorResources.class.getPackage().getName() + "." + "ErrorResources"
    };

    private static ReloadableResourceBundleMessageSource messageSource;
    private static final ResourceBundleMessageSource PARENT_MESSAGE_SOURCE;
    static {
        PARENT_MESSAGE_SOURCE = new ResourceBundleMessageSource();
        PARENT_MESSAGE_SOURCE.setBasenames(ERROR_RESOURCES);
    }

    /**
     * メッセージソースを取得します。
     * @return メッセージソース
     */
    public static MessageSource get() {
        if (messageSource == null) return PARENT_MESSAGE_SOURCE;
        else
            return messageSource;
    }

    /**
     * メッセージソースを設定します。
     * @param message メッセージソース
     */
    public static void set(ReloadableResourceBundleMessageSource message) {
        message.setParentMessageSource(PARENT_MESSAGE_SOURCE);
    }

    /**
     * メッセージソースを設定します。
     * @param messageSource メッセージソース
     */
    @Autowired(required = true)
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
        set(messageSource);
    }

    /**
     * 指定されたキーに対応したメッセ―ジをリソースより取得します。
     * @param key 検索対象のキー
     * @return キーに対応したメッセージ
     *         該当するメッセージがないときは、検索対象のキー
     */
    public static String find(String key) {
        try {
            Locale userLocale = LocaleContextHolder.getLocale();
            Locale currentLocale = userLocale != null ? userLocale : Locale.getDefault();
            String msg = get().getMessage(key, null, currentLocale);
            return msg != null ? msg : key;
        } catch (NoSuchMessageException e) {
            return key;
        }
    }
}
