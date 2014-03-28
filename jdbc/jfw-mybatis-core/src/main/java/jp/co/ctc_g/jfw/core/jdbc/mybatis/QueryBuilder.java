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
 * ステートメントを解析してログ出力するSQL文を生成インタフェースを定義します。
 * </p>
 */
public interface QueryBuilder {

    /**
     * ステートメントを解析してログ出力するSQL文を返却します。
     * @param queryInformation ログ出力用のSQL生成に使用する情報
     * @return ログ出力するSQL文
     */
    String build(QueryInformation queryInformation);
}
