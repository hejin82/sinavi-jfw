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

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * <p>
 * このクラスはRESTクライアントとしてHTTP通信を行う際のリクエストデータやヘッダ情報を設定するユーティリティです。
 * </p>
 * <p>
 * SpringMVCのデフォルトでは{@link HttpEntity}のインスタンスを生成して、リクエストデータやヘッダ情報を個別に設定しなければいけません。
 * この処理を簡潔にするためにユーティリティを提供しています。
 * </p>
 * <p>
 * 単にリクエストデータだけを設定するのであれば以下のように設定します。
 * <pre>
 * public void setting(Hoge hoge) {
 *     Entity.entity(hoge);
 * }
 * </pre>
 * <p>
 * この設定の場合のコンテントタイプはデフォルトでtext/plainになります。
 * コンテントタイプを指定したい場合は二通りのやり方があります。
 * まず一つ目はjsonやxmlといった代表的なものは
 * </p>
 * <pre>
 * public void setting(Hoge hoge) {
 *     Entity.json(hoge);
 *     Entity.xml(hoge);
 * }
 * </pre>
 * のようにメディアタイプの指定を省略することができます。
 * これを省略せずに実施する方法が二つ目のやり方です。
 * <pre>
 * public void setting(Hoge hoge) {
 *     Entity.entity(hoge, MediaType.APPLICATION_JSON);
 *     Entity.entity(hoge, MediaType.APPLICATION_XML);
 * }
 * </pre>
 *
 * リクエストデータのヘッダ情報としてコンテントタイプや許可されているコンテントタイプ、文字コードなどを設定する場合は
 * 次のように設定することが可能です。
 * <pre>
 * public void setting(Hoge hoge) {
 *     Entity.json(hoge).accept(MediaType.APPLICATION_JSON).acceptCharset(Charset.defaultCharset());
 * }
 * </pre>
 * また、任意のヘッダ情報を設定することも可能です。
 * <pre>
 * public void setting(Hoge hoge) {
 *     Entity.json(hoge).add("x-auth", "aaaaa");
 * }
 * </pre>
 * </p>
 * @param <T> リクエストデータの型
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Entity<T> {

    private final T entity;
    private final MediaType mediaType;
    private final HttpHeaders headers = new HttpHeaders();

    /**
     * ContentTypeがtext/planのヘッダとリクエストデータを設定します。
     * @param entity リクエストデータ
     * @param <T> リクエストデータの型
     * @return このユーティリティを返します。
     */
    public static <T> Entity<T> entity(final T entity) {
        return new Entity<T>(entity, MediaType.TEXT_PLAIN);
    }

    /**
     * ContentTypeを指定して、リクエストデータを設定します。
     * @param entity リクエストデータ
     * @param mediaType メディアタイプ
     * @param <T> リクエストデータの型
     * @return このユーティリティを返します。
     */
    public static <T> Entity<T> entity(final T entity, final MediaType mediaType) {
        return new Entity<T>(entity, mediaType);
    }

    /**
     * ContentTypeがapplication/jsonのヘッダとリクエストデータを設定します。
     * @param entity リクエストデータ
     * @param <T> リクエストデータの型
     * @return このユーティリティを返します。
     */
    public static <T> Entity<T> json(final T entity) {
        return new Entity<T>(entity, MediaType.APPLICATION_JSON);
    }

    /**
     * ContentTypeがapplication/xmlのヘッダとリクエストデータを設定します。
     * @param entity リクエストデータ
     * @param <T> リクエストデータの型
     * @return このユーティリティを返します。
     */
    public static <T> Entity<T> xml(final T entity) {
        return new Entity<T>(entity, MediaType.APPLICATION_XML);
    }

    /**
     * ContentTypeがtext/htmlのヘッダとリクエストデータを設定します。
     * @param entity リクエストデータ
     * @param <T> リクエストデータの型
     * @return このユーティリティを返します。
     */
    public static <T> Entity<T> html(final T entity) {
        return new Entity<T>(entity, MediaType.TEXT_HTML);
    }

    /**
     * ContentTypeがtext/planのヘッダとリクエストデータを設定します。
     * @param entity リクエストデータ
     * @param <T> リクエストデータの型
     * @return このユーティリティを返します。
     */
    public static <T> Entity<T> text(final T entity) {
        return new Entity<T>(entity, MediaType.TEXT_PLAIN);
    }

    /**
     * ContentTypeがapplication/octet-streamのヘッダとリクエストデータを設定します。
     * @param entity リクエストデータ
     * @param <T> リクエストデータの型
     * @return このユーティリティを返します。
     */
    public static <T> Entity<T> octet(final T entity) {
        return new Entity<T>(entity, MediaType.APPLICATION_OCTET_STREAM);
    }

    private Entity(final T entity, final MediaType mediaType) {
        this.entity = entity;
        if (mediaType != null) {
            this.mediaType = mediaType;
            this.headers.setContentType(mediaType);
        } else {
            this.mediaType = null;
        }
    }

    /**
     * HTTPヘッダのacceptを設定します。デフォルトではこのインスタンスを生成したときに指定したMediaTypeです。
     * @return このユーティリティを返します。
     */
    public Entity<T> accept() {
        Assert.notNull(mediaType, "メディアタイプは必須です。");
        this.headers.setAccept(Arrays.asList(new MediaType[]{mediaType}));
        return this;
    }

    /**
     * 指定されたMediaTypeでHTTPヘッダのacceptを設定します。
     * @param  accept 任意のMediaType
     * @return このユーティリティを返します。
     */
    public Entity<T> accept(final MediaType accept) {
        this.headers.setAccept(Arrays.asList(new MediaType[]{accept}));
        return this;
    }

    /**
     * 指定されたMediaTypeでHTTPヘッダのacceptを設定します。
     * @param  accepts 任意のMediaType
     * @return このユーティリティを返します。
     */
    public Entity<T> accept(final MediaType... accepts) {
        this.headers.setAccept(Arrays.asList(accepts));
        return this;
    }

    /**
     * HTTPヘッダのAccept-Charsetを設定します。デフォルトではUTF-8です。
     * @return このユーティリティを返します。
     */
    public Entity<T> acceptCharset() {
        this.headers.setAcceptCharset(Arrays.asList(Charset.defaultCharset()));
        return this;
    }

    /**
     * 指定されたCharsetでHTTPヘッダのAccept-Charsetを設定します。デフォルトではUTF-8です。
     * @param charset 文字コード
     * @return このユーティリティを返します。
     */
    public Entity<T> acceptCharset(final Charset charset) {
        this.headers.setAcceptCharset(Arrays.asList(new Charset[]{charset}));
        return this;
    }

    /**
     * HTTPヘッダのAccept-Charsetを設定します。デフォルトではUTF-8です。
     * @param charsets 文字コード
     * @return このユーティリティを返します。
     */
    public Entity<T> acceptCharset(final Charset... charsets) {
        this.headers.setAcceptCharset(Arrays.asList(charsets));
        return this;
    }

    /**
     * 任意のHTTPヘッダを設定します。
     * @param headerName HTTPヘッダのキー
     * @param headerValue HTTPヘッダの値
     * @return このユーティリティを返します。
     */
    public Entity<T> add(final String headerName, final String headerValue) {
        this.headers.add(headerName, headerValue);
        return this;
    }

    /**
     * {@link HttpEntity}のインスタンスを生成します。
     * 生成時にはHttpHeaderの設定も行います。
     * @return HttpEntity
     */
    public HttpEntity<T> get() {
        return new HttpEntity<T>(entity, headers);
    }

}
