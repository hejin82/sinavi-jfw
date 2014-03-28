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

import java.util.HashMap;
import java.util.Map;

import jp.co.ctc_g.jfw.core.util.Args;

import org.apache.ibatis.mapping.StatementType;

/**
 * <p>
 * QueryBuilderのファクトリです。
 * </p>
 */
public final class QueryBuilderFactory {

    private final Map<StatementType, QueryBuilder> registory;

    /**
     * コンストラクタです。
     * @param normalize 正規化するかどうか
     */
    public QueryBuilderFactory(boolean normalize) {
        registory = new HashMap<StatementType, QueryBuilder>();
        registory.put(StatementType.STATEMENT, new SimpleQueryBuilder(normalize));
        registory.put(StatementType.PREPARED, new PreparedQueryBuilder(normalize));
        registory.put(StatementType.CALLABLE, new CallableQueryBuilder(normalize));
    }

    /**
     * ロギング対象のステートメントのタイプにあったクエリービルダを返却します。
     * @param statementType ステートメントのタイプ
     * @return クエリービルダ
     */
    public QueryBuilder getBuilder(StatementType statementType) {
        Args.checkNotNull(statementType);
        return registory.get(statementType);
    }
}
