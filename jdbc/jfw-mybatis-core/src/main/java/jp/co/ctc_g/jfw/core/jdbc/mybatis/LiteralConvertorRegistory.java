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

import static jp.co.ctc_g.jfw.core.util.Args.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * {@link LiteralConvertor} を管理するレジストリです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class LiteralConvertorRegistory {

    private static LiteralConvertorRegistory instance = new LiteralConvertorRegistory();

    @SuppressWarnings("rawtypes")
    private Map<Class<?>, LiteralConvertor> registory = new HashMap<Class<?>, LiteralConvertor>();

    private LiteralConvertorRegistory() {
        initializeConvertor();
    }

    /**
     * <p>
     * {@code LiteralConvertorRegistory} インスタンスを返却します。
     * </p>
     * @return {@code LiteralConvertorRegistory} インスタンス
     */
    public static LiteralConvertorRegistory getInstance() {
        return instance;
    }

    /**
     * <p>
     * {@link LiteralConvertor} をレジストリに一括登録します。
     * </p>
     * @param convertors リテラル文字列コンバータの{@link List}
     */
    @SuppressWarnings("rawtypes")
    public void regist(List<LiteralConvertor> convertors) {
        for (LiteralConvertor convertor : convertors) {
            regist(convertor);
        }
    }

    /**
     * <p>
     * {@link LiteralConvertor} をレジストリに登録します。
     * </p>
     * @param convertor リテラル文字列コンバータ
     */
    @SuppressWarnings("rawtypes")
    public void regist(LiteralConvertor convertor) {
        checkNotNull(convertor);
        registory.put(convertor.getJavaType(), convertor);
    }

    /**
     * <p>
     * 指定したJavaのデータ型に対応するリテラル文字列コンバータが存在するかチェックします。
     * </p>
     * @param type Javaのデータ型
     * @return 指定したJavaのデータ型に対応するリテラル文字列コンバータが存在する場合 true
     */
    public boolean hasConvertor(Class<?> type) {
        return registory.containsKey(type);
    }

    /**
     * <p>
     * 指定したJavaのデータ型に対応する{@link LiteralConvertor}を返却します。
     * </p>
     * @param type Javaのデータ型
     * @return 指定したJavaのデータ型に対応する{@link LiteralConvertor}
     */
    @SuppressWarnings("rawtypes")
    public LiteralConvertor getConverter(Class<?> type) {
        if (hasConvertor(type)) {
            return registory.get(type);
        } else {
            return registory.get(null);
        }
    }

    /*
     * <p>
     * レジストリの初期化を行います。
     * {@link LiteralConvertor} を初期化しレジストリに登録します。
     * </p>
     */
    private void initializeConvertor() {
        regist(new StringLiteralConvertor());
        regist(new DateLiteralConvertor());
        regist(new SqlDateLiteralConvertor());
        regist(new TimeLiteralConvertor());
        regist(new TimestampLiteralConvertor());
        regist(new DefaultLiteralConvertor());
    }
}
