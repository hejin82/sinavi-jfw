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

package jp.co.ctc_g.jse.core.csv.mybatis;

import jp.co.ctc_g.jse.core.csv.CSVConfigs.CSVConfig;
import jp.co.ctc_g.jse.core.csv.DownloadFile;

import org.apache.ibatis.session.ResultHandler;

/**
 * <p>
 * このインタフェースは、{@link ResultHandler}を拡張し、
 * RDBMSの1レコードずつCSVファイルに書き込む処理を共通化します。
 * </p>
 * @see AbstractCSVResultHandlerImpl
 * @author ITOCHU Techno-Solutions Corporation.
 */
public interface CSVResultHandler extends ResultHandler {

    /**
     * ファイルオープンするメソッドです。
     */
    void open();

    /**
     * ファイルをオープンするメソッドです。
     * @param config CSV出力の設定
     */
    void open(CSVConfig config);

    /**
     * CSVのヘッダーを出力するメソッドです。
     */
    void header();

    /**
     * ファイルをクローズするメソッドです。
     */
    void close();

    /**
     * 一時ファイルを格納したドメインを取得します。
     * @return 一時ファイルドメイン
     */
    DownloadFile get();

}
