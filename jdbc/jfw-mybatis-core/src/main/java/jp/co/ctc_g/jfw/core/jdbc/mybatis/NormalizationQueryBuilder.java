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
 * ログ出力するSQLの正規化処理の実装を提供する{@link QueryBuilder}の抽象クラスです。
 * </p>
 * <h3>SQL文の正規化</h3>
 * <p>
 * SQL文の正規化とは以下を意味します。
 * <ul>
 *   <li>改行コードの除外</li>
 *   <li>連続したスペースを1個のスペースに置換</li>
 *   <li>連続したタブを1個のスペースに置換</li>
 * </ul>
 * </p>
 */
public abstract class NormalizationQueryBuilder implements QueryBuilder {

    private boolean normalize = true;

    /**
     * デフォルトコンストラクタです。
     */
    public NormalizationQueryBuilder() {}

    /**
     * コンストラクタです。
     * @param normalize 正規化するかどうか
     */
    public NormalizationQueryBuilder(Boolean normalize) {
        this.normalize = normalize;
    }

    /**
     *
     * @param targetSql 正規化対象のSQL文
     * @return 正規化されたSQL文
     */
    protected String normalized(String targetSql) {
        if (!normalize)
            return targetSql;

        return targetSql.replaceAll("\r\n", "")
                        .replaceAll("[\n\r]", "")
                        .replaceAll("[\\s]+", " ")
                        .replaceAll("[\\t]+", " ");
    }
}
