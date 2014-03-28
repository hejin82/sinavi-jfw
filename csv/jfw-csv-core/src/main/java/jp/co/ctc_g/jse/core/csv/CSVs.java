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

package jp.co.ctc_g.jse.core.csv;

import java.io.File;

import jp.co.ctc_g.jse.core.csv.CSVConfigs.CSVConfig;

/**
 * <p>
 * このクラスはCSV出力とCSV読み込みのファイルアクセスを共通化したユーティリティです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class CSVs {

    /**
     * コンストラクタです。
     * インスタンスの生成は抑止します。
     */
    private CSVs() {}

    /**
     * CSVファイルの書き込みユーティリティを取得します。
     * @return CSV書き込みユーティリティ
     */
    public static CSVWriters writers() {
        return new CSVWriters();
    }
    
    /**
     * CSVファイルの書き込みユーティリティを取得します。
     * @param config コンフィグ
     * @return CSV書き込みユーティリティ
     */
    public static CSVWriters writers(CSVConfig config) {
        return new CSVWriters(config);
    }

    /**
     * CSVファイルの読み込みユーティリティを取得します。
     * @param file ファイル名(パスを含む)
     * @return CSVファイル読み込みユーティリティ
     */
    public static CSVReaders readers(File file) {
        return new CSVReaders(file);
    }

    /**
     * CSVファイルの読み込みユーティリティを取得します。
     * @param file ファイル名(パスを含む)
     * @return CSVファイル読み込みユーティリティ
     */
    public static CSVReaders readers(String file) {
        return readers(new File(file));
    }

    /**
     * CSVファイルの読み込みユーティリティを取得します。
     * @param file ファイル名(パスを含む)
     * @param config 読込の設定
     * @return CSVファイル読み込みユーティリティ
     */
    public static CSVReaders readers(File file, CSVConfig config) {
        return new CSVReaders(file, config);
    }

    /**
     * CSVファイルの読み込みユーティリティを取得します。
     * @param file ファイル名(パスを含む)
     * @param config 読込の設定
     * @return CSVファイル読み込みユーティリティ
     */
    public static CSVReaders readers(String file, CSVConfig config) {
        return readers(new File(file), config);
    }

    /**
     * CSVの1レコードとJavaBeansをマッピングするCSV読込ユーティリティのインスタンスを生成します。
     * このメソッドはシンプルなデータ構造のみをサポートしており、
     * 複雑(1ファイルに複数のデータ構造を保持)なデータ構造はサポートしていません。
     * @param file ファイル名(パスを含む)
     * @param type 読込対象の型
     * @param <T> 読込対象の型
     * @return CSVファイル読込ユーティリティ(JavaBeansマッピング)
     */
    public static <T> BeanMappingCSVReaders<T> readers(File file, Class<T> type) {
        return new BeanMappingCSVReaders<T>(file).type(type).resolve();
    }

    /**
     * CSVの1レコードとJavaBeansをマッピングするCSV読込ユーティリティのインスタンスを生成します。
     * このメソッドはシンプルなデータ構造のみをサポートしており、
     * 複雑(1ファイルに複数のデータ構造を保持)なデータ構造はサポートしていません。
     * @param file ファイル名(パスを含む)
     * @param config CSVの設定情報
     * @param type 読込対象の型
     * @param <T> 読込対象の型
     * @return CSVファイル読込ユーティリティ(JavaBeansマッピング)
     */
    public static <T> BeanMappingCSVReaders<T> readers(File file, CSVConfig config, Class<T> type) {
        return new BeanMappingCSVReaders<T>(file, config).type(type).resolve();
    }

    /**
     * CSVの1レコードとJavaBeansをマッピングするCSV読込ユーティリティのインスタンスを生成します。
     * このメソッドはシンプルなデータ構造のみをサポートしており、
     * 複雑(1ファイルに複数のデータ構造を保持)なデータ構造はサポートしていません。
     * @param file ファイル名(ポスを含む)
     * @param type 読込対象の型
     * @param <T> 読込対象の型
     * @return CSVファイル読込ユーティリティ(JavaBeansマッピング)
     */
    public static <T> BeanMappingCSVReaders<T> readers(String file, Class<T> type) {
        return readers(new File(file), type);
    }

    /**
     * CSVの1レコードとJavaBeansをマッピングするCSV読込ユーティリティのインスタンスを生成します。
     * このメソッドはシンプルなデータ構造のみをサポートしており、
     * 複雑(1ファイルに複数のデータ構造を保持)なデータ構造はサポートしていません。
     * @param file ファイル名(ポスを含む)
     * @param config CSVの設定情報
     * @param type 読込対象の型
     * @param <T> 読込対象の型
     * @return CSVファイル読込ユーティリティ(JavaBeansマッピング)
     */
    public static <T> BeanMappingCSVReaders<T> readers(String file, CSVConfig config, Class<T> type) {
        return readers(new File(file), config, type);
    }

}
