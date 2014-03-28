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

package jp.co.ctc_g.jse.core.rest.springmvc.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

/**
 * <p>
 * このクラスは、J-Frameworkにより拡張された {@link RestOperations} です。
 * RestOperationsのラッパーとして動作します。
 * J-Frameworkによる拡張は主としてRestClient時の操作を簡素化するためです。
 * </p>
 * @see RestOperations
 * @see RestClientTemplate
 * @author ITOCHU Techno-Solutions Corporation.
 */
public interface RestClientOperations extends RestOperations {

    /**
     * GETで対象URLにアクセスします。
     * 処理結果のBodyのみ必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URL
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return 処理結果
     * @throws RestClientException 通信時に発生する例外
     */
    <T> T get(Target target, Class<T> responseType) throws RestClientException;

    /**
     * GETで対象URLにアクセスします。
     * 処理結果のステータスコードやヘッダなどが必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URL
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return 処理結果
     * @throws RestClientException 通信時に発生する例外
     */
    <T> ResponseEntity<T> getForEntity(Target target, Class<T> responseType) throws RestClientException;

    /**
     * DELETEで対象URLにアクセスします。
     * データがない場合かつ処理結果のBodyのみ必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URLや処理データなどを格納したオブジェクト
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return 処理結果
     * @throws RestClientException 通信時に発生する例外
     */
    <T> T delete(Target target, Class<T> responseType) throws RestClientException;

    /**
     * DELETEで対象URLにアクセスします。
     * 処理結果のBodyのみ必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URL
     * @param entity 処理データなどを格納したオブジェクト
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return 処理結果
     * @throws RestClientException 通信時に発生する例外
     */
    <T> T delete(Target target, Entity<?> entity, Class<T> responseType) throws RestClientException;

    /**
     * DELETEで対象URLにアクセスします。
     * データがない場合かつ処理結果のステータスコードやヘッダなどが必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URL
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return 処理結果
     * @throws RestClientException 通信時に発生する例外
     */
    <T> ResponseEntity<T> deleteForEntity(Target target, Class<T> responseType) throws RestClientException;

    /**
     * DELETEで対象URLにアクセスします。
     * 処理結果のステータスコードやヘッダなどが必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URL
     * @param entity 処理データなどを格納したオブジェクト
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return 処理結果
     * @throws RestClientException 通信時に発生する例外
     */
    <T> ResponseEntity<T> deleteForEntity(Target target, Entity<?> entity, Class<T> responseType) throws RestClientException;

    /**
     * POSTで対象URLにアクセスします。
     * 処理結果のBodyのみ必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URL
     * @param entity 処理データなどを格納したオブジェクト
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return 処理結果
     * @throws RestClientException 通信時に発生する例外
     */
    <T> T post(Target target, Entity<?> entity, Class<T> responseType) throws RestClientException;

    /**
     * POSTで対象URLにアクセスします。
     * 処理結果のBodyのみ必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URL
     * @param entity 処理データなどを格納したオブジェクト
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return 処理結果
     * @throws RestClientException 通信時に発生する例外
     */
    <T> ResponseEntity<T> postForEntity(Target target, Entity<?> entity, Class<T> responseType) throws RestClientException;

    /**
     * PUTで対象URLにアクセスします。
     * 処理結果のBodyのみ必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URL
     * @param entity 処理データなどを格納したオブジェクト
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return 処理結果
     * @throws RestClientException 通信中に発生する例外
     */
    <T> T put(Target target, Entity<?> entity, Class<T> responseType) throws RestClientException;

    /**
     * PUTで対象URLにアクセスします。
     * 処理結果のステータスコードやヘッダなどが必要な場合はこのメソッドを利用して下さい。
     * @param target 対象URL
     * @param entity 処理データなどを格納したオブジェクト
     * @param responseType レスポンスのタイプ
     * @param <T> レスポンスのタイプ
     * @return レスポンスエンティティ
     * @throws RestClientException 通信中に発生する例外
     */
    <T> ResponseEntity<T> putForEntity(Target target, Entity<?> entity, Class<T> responseType) throws  RestClientException;

}
