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

package jp.co.ctc_g.jse.core.message;

import java.io.Serializable;
import java.util.Map;

import jp.co.ctc_g.jfw.core.resource.Rs;
import jp.co.ctc_g.jfw.core.util.Strings;

/**
 * このクラスはクライアントに返却されるメッセージを表現するクラスです。
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    private String property;

    private String constraintName;

    private String modelName;

    private Map<String, ? extends Object> replacementValues;

    /**
     * デフォルトコンストラクタです。
     */
    public Message() {}

    /**
     * コンストラクタです。
     * @param key メッセージ・キー
     */
    public Message(String key) {
        this(key, null, null, null, null);
    }

    /**
     * コンストラクタです。
     * @param key メッセージ・キー
     * @param replacementValues replace メッセージのプレースホルダを置換するキーとバリューの{@link Map}
     */
    public Message(String key, Map<String, ? extends Object> replacementValues) {
        this(key, replacementValues, null, null, null);
    }

    /**
     * コンストラクタです。
     * @param key メッセージ・キー
     * @param replacementValues replace メッセージのプレースホルダを置換するキーとバリューの{@link Map}
     * @param property プロパティ名
     * @param constraintName 入力チェック名
     * @param modelName モデル名
     */
    public Message(String key,
            Map<String, ? extends Object> replacementValues,
            String property,
            String constraintName,
            String modelName) {
        this.key = key;
        this.replacementValues = replacementValues;
        this.property = property;
        this.constraintName = constraintName;
        this.modelName = modelName;
    }

    /**
     * メッセージをビルドし文字列表現に変換します。
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return replacementValues == null ?
                Rs.find(key) :
                    Strings.substitute(Rs.find(key), replacementValues);
    }

    /**
     * プロパティ名を返却します。
     * @return プロパティ名
     */
    public String getProperty() {
        return property;
    }

    /**
     * 入力チェック名を返却します。
     * @return 入力チェック名
     * @return
     */
    public String getConstraintName() {
        return constraintName;
    }

    /**
     * モデル名を返却します。
     * @return モデル名
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * メッセージキーを返却します。
     * @return メッセージキー
     */
    public String getKey() {
        return key;
    }
}
