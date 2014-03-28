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

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * このクラスは、エラーメッセージのプロパティファイルのキャッシュをコントロールするクラスです。
 * </p>
 * <p>
 * エラーメッセージのプロパティファイルを1時間キャッシュします。
 * キャッシュはJDK6より追加されたControlクラスを利用し、キャッシュします。
 * </p>
 * @see Control
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class ErrorMessageCacheControl extends Control {
    
    private static final Logger L = LoggerFactory.getLogger(ErrorMessageCacheControl.class);
    private static final ResourceBundle R = InternalMessages.getBundle(ErrorMessageCacheControl.class);
    
    private final List<String> formats;

    /**
     * コンストラクタです。
     * @param formats プロパティの形式
     */
    protected ErrorMessageCacheControl(List<String> formats) {
        this.formats = formats;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTimeToLive(String baseName, Locale locale) {
        if (baseName == null || locale == null) {
            if (L.isDebugEnabled()) L.debug(R.getString("D-REST-JERSEY-UTIL#0002"));
            throw new NullPointerException(R.getString("E-REST-JERSEY-UTIL#0002"));
        }
        return 60 * 60 * 1000;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFormats(String baseName) {
        if (baseName == null) {
            if (L.isDebugEnabled()) L.debug(R.getString("D-REST-JERSEY-UTIL#0003"));
            throw new NullPointerException(R.getString("E-REST-JERSEY-UTIL#0002"));
        }
        return formats;
    }

}
