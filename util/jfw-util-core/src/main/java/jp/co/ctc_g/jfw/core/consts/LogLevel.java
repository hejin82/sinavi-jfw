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

package jp.co.ctc_g.jfw.core.consts;

/**
 * <p>
 * この列挙はログレベルを表現します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public enum LogLevel {

    /**
     * トレースレベル
     */
    TRACE("TRACE"),

    /**
     * デバッグレベル
     */
    DEBUG("DEBUG"),

    /**
     * 情報レベル
     */
    INFO("INFO"),

    /**
     * 警告レベル
     */
    WARN("WARN"),

    /**
     * エラーレベル
     */
    ERROR("ERROR"),

    /**
     * 致命的レベル
     */
    FATAL("FATAL"),

    /**
     * 非サポートレベル
     */
    UNSUPPORTED("UNSUPPORTED");

    private String logLevelString;

    private LogLevel(String logLevelString) {
        this.logLevelString = logLevelString;
    }

    /**
     * ログレベルに応じた文字列を取得します。
     * @return ログレベルの文字列
     */
    public String getLogLevelString() {
        return this.logLevelString;
    }

    /**
     * 文字列から列挙型へ変換します。
     * @param logLevelString ログレベルの文字列
     * @return ログレベル
     */
    public static LogLevel getLogLevel(String logLevelString) {
        if ("TRACE".equals(logLevelString.toUpperCase())) {
            return TRACE;
        } else if ("DEBUG".equals(logLevelString.toUpperCase())) {
            return DEBUG;
        } else if ("INFO".equals(logLevelString.toUpperCase())) {
            return INFO;
        } else if ("WARN".equals(logLevelString.toUpperCase())) {
            return WARN;
        } else if ("ERROR".equals(logLevelString.toUpperCase())) {
            return ERROR;
        } else if ("FATAL".equals(logLevelString.toUpperCase())) {
            return FATAL;
        } else {
            return UNSUPPORTED;
        }
    }
}
