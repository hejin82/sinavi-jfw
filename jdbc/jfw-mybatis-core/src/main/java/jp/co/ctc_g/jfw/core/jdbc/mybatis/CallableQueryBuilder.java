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
 * {@link java.sql.CallableStatement}を解析して、ログ出力するSQLを生成する{@link QueryBuilder}の実装です。<br>
 * このクラスは主にプレースホルダの置換処理を受け持ちます。また、{@link NormalizationQueryBuilder}の実装を呼出し、SQLの正規化を行います。
 * </p>
 * <h3>プレースホルダの置換処理</h3>
 * <p>
 * プレースホルダの置換処理については、{@link PreparedQueryBuilder}のAPIマニュアルで確認して下さい。<br>
 * ただし、{@link java.sql.CallableStatement}特有のアウト・パラメータの置換についてはSqlMapファイルに定義される
 * プロパティ名によって置換されます。
 * </p>
 */
public class CallableQueryBuilder extends PlaceholderQueryBuilder implements QueryBuilder {

    /**
     * コンストラクタです。
     * @param normalize 正規化するかどうか
     */
    public CallableQueryBuilder(Boolean normalize) {
        super(normalize);
    }

    @Override
    public String build(QueryInformation queryInformation) {
        List<Object> parameterList = createParameterList(queryInformation) ;
        String query = replacePlaceholder(queryInformation.getCallableQuery(), parameterList);
        return normalized(query);
    }

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
            } else {
                parameterList.add(propertyName);
            }
        }

        return parameterList;
    }
}
