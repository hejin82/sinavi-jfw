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

package jp.co.ctc_g.jse.core.rest.springmvc.client.exception;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

/**
 * <p>
 * このクラスは、HTTPステータスコード 404が返されたときにスローされる例外です。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
@SuppressWarnings("serial")
public class NotFoundException extends HttpClientErrorException {

    /**
     * コンストラクタです。
     * @param responseHeaders レスポンスヘッダー情報
     * @param responseBody レスポンスボディ情報
     * @param responseCharset レスポンスキャラセット
     */
    public NotFoundException(HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.name(), responseHeaders, responseBody, responseCharset);
    }
}
