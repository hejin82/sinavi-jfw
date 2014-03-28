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

import java.util.List;

import jp.co.ctc_g.jfw.core.exception.ApplicationRecoverableException;

/**
 * <p>
 * この例外は、CSVファイルの読み込み時にエラーが発生したことを表明します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class BindException extends ApplicationRecoverableException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 9062908428307321256L;

    private List<BindError> errors;

    /**
     * コンストラクタです。
     * @param code エラーコード
     */
    public BindException(String code) {
        super(code);
    }

    /**
     * コンストラクタです。
     * @param code エラーコード
     * @param errors {@link BindError}
     */
    public BindException(String code, List<BindError> errors) {
        super(code);
        this.errors = errors;
    }

    /**
     * 詳細なエラー情報を取得します。
     * @return {@link BindError}
     */
    public List<BindError> getErrors() {
        return errors;
    }

}
