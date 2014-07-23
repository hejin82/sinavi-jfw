/*
 *  Copyright (c) 2013 ITOCHU Techno-Solutions Corporation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package jp.co.ctc_g.jse.core.message;

import org.springframework.validation.DefaultMessageCodesResolver.Format;
import org.springframework.validation.MessageCodeFormatter;

/**
 * <p>
 * このクラスはバリデーションエラー発生時にフレームワーク内部でラベルとして利用するメッセージキーを生成します。
 * </p>
 * <p>
 * Springデフォルトのフォーマッターでは
 * <pre class="brush:java">
 * エラーコード.オブジェクト名.フィールド名
 * エラーコード.フィールド名
 * エラーコード.フィールド型のFQCN
 * </pre>
 * となります。
 * 例えば、以下のJavaBeanに対してバリデーションエラーが発生した際は
 * <pre class="brush:java">
 * public class Hoge {
 *   &#64;Required
 *   private String id;
 *   // getter/setter省略
 * }
 * 
 * Required.hoge.id
 * Required.id
 * Required.java.lang.String
 * 
 * </pre>
 * の複数のコードが生成されます。
 * </p>
 * <p>
 * J-Frameworkにおいてバリデーションエラーメッセージのラベルを自動解決するためにこの機構を利用します。
 * ラベルとは <strong>名前 は必須入力です。</strong>のようなメッセージを表示する際の
 * 名詞となる項目のことを指します。
 * J-Frameworkのラベルのキーの生成はエラーコードを省略し、
 * オブジェクト名とフィールド名を単純に連結します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
@Deprecated
public class MessageCodesFormat implements MessageCodeFormatter {

    /**
     * デフォルトコンストラクタです。
     */
    public MessageCodesFormat() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(String errorCode, String objectName, String field) {
        return Format.toDelimitedString(objectName, field);
    }
}