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

package jp.co.ctc_g.jse.core.rest.springmvc.server.util;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * この列挙型は、HTTPステータスごとのエラーコードとメッセージキーを表現します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public enum ErrorCode {

    /**
     * ステータスコード：400
     * メッセージコード：W-REST-CLIENT#400
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "W-REST-CLIENT#400"),

    /**
     * ステータスコード：404
     * メッセージコード：W-REST-CLIENT#404
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "W-REST-CLIENT#404"),

    /**
     * ステータスコード：405
     * メッセージコード：W-REST-CLIENT#405
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "W-REST-CLIENT#405"),

    /**
     * ステータスコード：406
     * メッセージコード：W-REST-CLIENT#406
     */
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "W-REST-CLIENT#406"),

    /**
     * ステータスコード：407
     * メッセージコード：W-REST-CLIENT#407
     */
    PROXY_AUTHENTICATION_REQUIRED(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, "W-REST-CLIENT#407"),

    /**
     * ステータスコード：408
     * メッセージコード：W-REST-CLIENT#408
     */
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "W-REST-CLIENT#408"),

    /**
     * ステータスコード：415
     * メッセージコード：W-REST-CLIENT#415
     */
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "W-REST-CLIENT#415"),

    /**
     * ステータスコード：422
     * メッセージコード：W-REST-CLIENT#422
     */
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "W-REST-CLIENT#422"),

    /**
     * ステータスコード：500
     * メッセージコード：E-REST-SERVER#500
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E-REST-SERVER#500"),

    /**
     * ステータスコード：500
     * メッセージコード：E-REST-SERVER#999
     */
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E-REST-SERVER#999");

    private HttpStatus status;

    private String code;

    private ErrorCode(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    private static final Map<HttpStatus, ErrorCode> LOOKUP = new HashMap<HttpStatus, ErrorCode>();
    static {
        for (ErrorCode e : ErrorCode.values()) {
            LOOKUP.put(e.status, e);
        }
    }

    /**
     * ステータスコードに応じたエラーコードを返却します。
     * @param status HTTPステータス
     * @return エラーコード
     */
    public static ErrorCode get(HttpStatus status) {
        return LOOKUP.get(status);
    }

    /**
     * エラーコードを返却します。
     * @return エラーコード
     */
    public String code() {
        return code;
    }

}
