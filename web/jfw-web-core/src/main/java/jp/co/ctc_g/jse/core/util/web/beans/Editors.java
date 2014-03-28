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

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jse.core.internal.WebCoreInternals;

/**
 * <p>
 * カスタムプロパティエディタ用のユーティリティです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Editors {

    /**
     * 数値フォーマットエラーのメッセージコード
     */
    public static final String NUMERIC_FORMAT_MESSAGE;
    
    /**
     * 必須エラーのメッセージコード
     */
    public static final String REQUIRED_MESSAGE;
    
    static {
        Config config = WebCoreInternals.getConfig(Editors.class);
        NUMERIC_FORMAT_MESSAGE = config.find("numericformat_error");
        REQUIRED_MESSAGE = config.find("required_error");
    }

    /**
     * コンストラクタです。
     * インスタンスの生成を抑止します。
     */
    private Editors(){}
    
    /**
     * NumberFormatExceptionが原因かどうかをチェックするメソッドです。
     * CustomEditor内で文字列→数値変換時にJ-FrameworkのTypeConvertersを利用してコンバートする場合に利用します。
     * また、このメソッドは原因となっている例外を再起的に検査します。
     * 利用の際はこの点に注意してください。
     * @param e 例外情報
     * @return NumberFormatExceptionが発生していればtrue、それ以外であればfalseを返します。
     */
    public static boolean isHandlingTargetException(Throwable e) {
        if (e instanceof NumberFormatException) {
            return true;
        }
        Throwable nested = e.getCause();
        if (nested != null) {
            return isHandlingTargetException(nested);
        }
        return false;
    }
    
}
