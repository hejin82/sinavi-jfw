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

import java.util.List;
import java.util.regex.Matcher;

/**
 * <p>
 * プリペアードクエリ、コーラブルクエリのプレースホルダー置換処理の実装を提供する{@link QueryBuilder}の抽象クラスです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public abstract class PlaceholderQueryBuilder extends NormalizationQueryBuilder implements QueryBuilder {

    protected static final String PLACEHOLDER_STRING = "\\?";

    private static final LiteralConvertorRegistory LITERAL_CONVERTOR_REGISTORY = LiteralConvertorRegistory.getInstance();

    protected abstract List<Object> createParameterList(QueryInformation queryLoggingSource);

    /**
     * デフォルトコンストラクタです。
     */
    public PlaceholderQueryBuilder() {}

    /**
     * コンストラクタです。
     * @param normalize 正規化するかどうか
     */
    public PlaceholderQueryBuilder(Boolean normalize) {
        super(normalize);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected String replacePlaceholder(String query, List<Object> parameterList) {
        for (Object value : parameterList) {
            String literalString;
            if (value != null) {
                LiteralConvertor lc = LITERAL_CONVERTOR_REGISTORY.getConverter(value.getClass());
                literalString = lc.convert(value);
            } else {
                literalString = LITERAL_CONVERTOR_REGISTORY.getConverter(null).convert(value);
            }
            query = query.replaceFirst(PLACEHOLDER_STRING, Matcher.quoteReplacement(literalString));
        }
        return query;
    }
}
