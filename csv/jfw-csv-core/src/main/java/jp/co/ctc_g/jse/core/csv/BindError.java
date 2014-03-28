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

/**
 * <p>
 * このクラスは、CSVファイルの読み込み時のデータのバリデーションチェックのエラー情報を保持します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class BindError {

    private String className;
    private String fieldName;
    private String path;
    private String message;

    /**
     * デフォルトコンストラクタです。
     */
    public BindError() {}

    /**
     * コンストラクタです。
     * @param className クラス名
     * @param fieldName エラーが発生したフィールド名
     * @param message エラーメッセージ
     */
    public BindError(String className, String fieldName, String message) {
        setClassName(className);
        setFieldName(fieldName);
        setMessage(message);
    }

    /**
     * コンストラクタです。
     * @param path エラーが発生したパス
     * @param message エラーメッセージ
     */
    public BindError(String path, String message) {
        setPath(path);
        setMessage(message);
    }

    /**
     * 例外が発生したクラス名を取得します。
     * @return 例外が発生したクラス名
     */
    public String getClassName() {
        return className;
    }

    /**
     * 例外が発生したクラス名を設定します。
     * @param className 例外が発生したクラス名
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 例外が発生したフィールド名を取得します。
     * @return 例外が発生したフィールド名
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 例外が発生したフィールド名を設定します。
     * @param fieldName 例外が発生したフィールド名
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * エラーメッセージを取得します。
     * @return エラーメッセージ
     */
    public String getMessage() {
        return message;
    }

    /**
     * エラーメッセージを設定します。
     * @param message エラーメッセージ
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * エラーが発生したパスを取得します。
     * @return エラーが発生したパス
     */
    public String getPath() {
        return path;
    }

    /**
     * エラーが発生したパスを設定します。
     * @param path エラーが発生したパス
     */
    public void setPath(String path) {
        this.path = path;
    }

}
