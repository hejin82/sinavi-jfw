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
 * このクラスは、{@link java.sql.Statement}を解析して、ログ出力するSQLを生成する{@link QueryBuilder}の実装です。<br>
 * {@link NormalizationQueryBuilder}の実装を呼出し、SQLの正規化のみを行います。
 * </p>
 */
public class SimpleQueryBuilder extends NormalizationQueryBuilder implements QueryBuilder {

    /**
     * デフォルトコンストラクタです。
     */
    public SimpleQueryBuilder() {}

    /**
     * コンストラクタです。
     * @param normalize 正規化するかどうか
     */
    public SimpleQueryBuilder(Boolean normalize) {
        super(normalize);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ctc_g.jfw.core.jdbc.mybatis.QueryBuilder#build(jp.co.ctc_g.jfw.core.jdbc.mybatis.QueryInformation)
     */
    @Override
    public String build(QueryInformation queryInformation) {
        return normalized(queryInformation.getQuery());
    }
}
