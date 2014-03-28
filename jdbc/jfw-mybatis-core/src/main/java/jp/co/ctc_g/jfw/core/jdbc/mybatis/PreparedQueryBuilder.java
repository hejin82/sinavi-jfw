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

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;

/**
 * <p>
 * {@link java.sql.PreparedStatement}を解析して、ログ出力するSQLを生成する{@link QueryBuilder}の実装です。<br>
 * このクラスは主にプレースホルダの置換処理を受け持ちます。また、{@link NormalizationQueryBuilder}の実装を呼出し、SQLの正規化を行います。
 * </p>
 * <h3>プレースホルダの置換処理</h3>
 * <p>
 * プリペアードクエリのプレースホルダの置換は、パラメータを{@link LiteralConvertor}により変換されたリテラル文字列
 * に置換する処理として実行されます。<br>
 * リテラル文字列への具体的な変換ロジックは、Javaのデータ型ごとに用意された{@link LiteralConvertor}の実装によって提供されます。<br>
 * デフォルトでは、以下のリテラル文字列コンバータを提供しています。
 * </p>
 * <ul>
 *   <li>{@link DefaultLiteralConvertor}</li>
 *   <li>{@link StringLiteralConvertor}</li>
 *   <li>{@link DateLiteralConvertor}</li>
 *   <li>{@link SqlDateLiteralConvertor}</li>
 *   <li>{@link TimeLiteralConvertor}</li>
 *   <li>{@link TimestampLiteralConvertor}</li>
 * </ul>
 */
public class PreparedQueryBuilder extends PlaceholderQueryBuilder implements QueryBuilder {

    /**
     * デフォルトコンストラクタです。
     */
    public PreparedQueryBuilder() {}

    /**
     * コンストラクタです。
     * @param normalize 正規化するかどうか
     */
    public PreparedQueryBuilder(Boolean normalize) {
        super(normalize);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ctc_g.jfw.core.jdbc.mybatis.QueryBuilder#build(jp.co.ctc_g.jfw.core.jdbc.mybatis.QueryInformation)
     */
    @Override
    public String build(QueryInformation queryInformation) {
        List<Object> parameterList = createParameterList(queryInformation);
        String query = replacePlaceholder(queryInformation.getPreparedQuery(), parameterList);
        return normalized(query);
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ctc_g.jfw.core.jdbc.mybatis.PlaceholderQueryBuilder#createParameterList(jp.co.ctc_g.jfw.core.jdbc.mybatis.QueryInformation)
     */
    @Override
    protected List<Object> createParameterList(QueryInformation queryLoggingSource) {

        List<Object> parameterList = new ArrayList<Object>();

        for (ParameterMapping p : queryLoggingSource.getParameterMappingList()) {
            String propertyName = p.getProperty();
            PropertyTokenizer prop = new PropertyTokenizer(propertyName);
            if (p.getMode() != ParameterMode.OUT) {
                Object parameterObject = queryLoggingSource.getParameterObject();
                if (parameterObject == null) {
                    parameterList.add(null);
                } else if (queryLoggingSource.getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
                    parameterList.add(parameterObject);
                } else if (queryLoggingSource.getBoundSql().hasAdditionalParameter(propertyName)) {
                    parameterList.add(queryLoggingSource.getBoundSql().getAdditionalParameter(propertyName));
                } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
                        && queryLoggingSource.getBoundSql().hasAdditionalParameter(prop.getName())) {
                    String value = queryLoggingSource.getBoundSql().getAdditionalParameter(prop.getName()).toString();
                    if (value != null) {
                        Object obj = queryLoggingSource.getMetaObject().getValue(propertyName.substring(prop.getName().length()));
                        parameterList.add(obj == null ? null : obj);
                    } else {
                        parameterList.add(null);
                    }
                } else {
                    Object obj = queryLoggingSource.getMetaObject() == null ? null : queryLoggingSource.getMetaObject().getValue(propertyName);
                    parameterList.add(obj == null ? null : obj);
                }
            }
        }

        return parameterList;
    }
}
