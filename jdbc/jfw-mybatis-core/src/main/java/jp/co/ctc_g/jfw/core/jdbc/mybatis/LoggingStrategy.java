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

package jp.co.ctc_g.jfw.core.jdbc.mybatis;

/**
 * <p>
 * {@link QueryLogger}に対して具体的なSQLのログ出力処理を提供するためのインタフェースを定義します。
 * </p>
 * <p>
 * </p>
 * <h2>SQLのログ出力処理のカスタマイズ</h2>
 * <p>
 * {@link QueryLogger} の具象クラスを実装することにより独自のログ出力処理を実行させることができます。
 * 、独自のログ出力処理を有効にするには以下の設定を行いDIコンテナに登録する必要があります。 
 * </p>
 * <pre>
 * &lt;bean id=&quot;loggingStrategy&quot; class=&quot;{@link LoggingStrategy}の具象クラスFQCN&quot /&gt;
 * </pre>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public interface LoggingStrategy {

    /**
     * SQLのログ出力処理を実行します。
     * @param sql ログ出力するSQL文
     */
    void log(String sql);
}
