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

/*
 * Created on 2004/02/27
 * Copyright (c) 2006 ITOCHU Techno-Solutions Corporation. All Rights Reserved.
 */

package jp.co.ctc_g.jfw.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * データフォーマットを規定するクラス.
 * </p>
 * @author ITOCHU Techno-Solutions Corporation
 */
public class DateFormatType {

    /**
     * フォーマット名.
     */
    protected String name;

    /**
     * フォーマット.
     */
    protected SimpleDateFormat formatter;

    /**
     * デフォルトコンストラクタです。
     */
    public DateFormatType() {}

    /**
     * フォーマットを元にインスタンスを生成します.
     * @param name フォーマット文字列
     */
    protected DateFormatType(String name) {

        this.name = name;
        this.formatter = new SimpleDateFormat(name);
    }

    /**
     * フォーマット文字列を返します.
     * @return フォーマット文字列
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return this.name;
    }

    /**
     * フォーマット文字列を返します.
     * @param date 日付
     * @return フォーマット文字列
     * @see java.lang.Object#toString()
     */
    protected String format(Date date) {

        return this.formatter.format(date);
    }

}
