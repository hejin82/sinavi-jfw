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

package jp.co.ctc_g.jse.core.rest.springmvc.server.handler;

import jp.co.ctc_g.jse.core.rest.entity.ErrorMessage;
import jp.co.ctc_g.jse.core.rest.springmvc.server.util.ErrorCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * <p>
 * このクラスは、J-Frameworkで拡張した{@link AbstractRestExceptionHandler}の例外ハンドラに
 * デフォルトで警告・エラーログを出力する例外ハンドラです。
 * </p>
 * <p>
 * 警告・エラーログのフォーマットを変更したい場合は{@link AbstractRestExceptionHandler}を継承し、
 * 独自の例外ハンドラを実装してください。
 * </p>
 * @see AbstractRestExceptionHandler
 * @author ITOCHU Techno-Solutions Corporation.
 */
@ControllerAdvice
public class RestDefaultExceptionHandler extends AbstractRestExceptionHandler {

    private static final Logger L  = LoggerFactory.getLogger(RestDefaultExceptionHandler.class);

    /**
     * デフォルトコンストラクタです。
     */
    public RestDefaultExceptionHandler() {}

    /**
     * {@inheritDoc}
     */
    @Override
     public void warn(ErrorMessage entity, Throwable e) {
        L.warn(entity.log(), e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(ErrorMessage entity, Throwable e) {
        L.error(entity.log(), e);
    }

    @Override
    protected String getClientErrorCode(HttpStatus status) {
        return ErrorCode.get(status).code();
    }

    @Override
    protected String getServerErrorCode(HttpStatus status) {
        return ErrorCode.get(status).code();
    }

}
